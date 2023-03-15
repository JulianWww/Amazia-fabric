from json import load, dump
from deep_translator import GoogleTranslator
import os, sys
from pathlib import Path
from time import time, sleep
from threading import Thread

abspath = os.path.abspath(__file__)
dname = os.path.dirname(abspath)
os.chdir(dname)
sys.path.insert(1, str(Path(abspath).parent))

langDir = "../../src/main/resources/assets/amazia/lang/"
origFile = langDir + "en_us.json"
compFile = langDir + "en_gb.json"

SOCKS_PORT = 9050
TOR_PATH = os.path.normpath(os.getcwd()+"/Tor/tor/tor.exe")


langTimeTable = []

PROXIES = {
    'http': 'socks5://127.0.0.1:9050',
    'https': 'socks5://127.0.0.1:9050'
}

log = open("out.log", "w")

def file_print(*args, **kwargs):
  print(*args, file=log, flush=True, **kwargs)

def remove_last_line():
  global log
  log.close()
  with open("out.log", "r") as file:
    lines = file.readlines()
  
  log = open("out.log", "w")
  log.writelines(lines[:-1])

def printProgress(done, todo, time):
  if (done > 0):
    remove_last_line()
  file_print(f"{done} / {todo} \t\t {time} s")


def translate(orig, dest, filename):
  with open(filename, "r") as file:
    last = load(file)

  custom = {}
  customFile = f"./customs/{dest}.json"
  print(customFile)
  if (os.path.exists(customFile)):
    with open(customFile, "r") as file:
      custom = load(file)

  translator = GoogleTranslator(source='en', target=dest, proxies=PROXIES)
  s = time()
  
  out = {}
  for idx, (x, y) in enumerate(orig.items()):
    printProgress(idx, len(orig), time() - s)
    s = time()
    while True:
      try:
        if x in custom:
          out[x] = custom[x]
        elif (dest == "en"):
          out[x] = y
        elif x in comp and comp[x] == y:
          out[x] = last[x]
        else:
          out[x] = translator.translate(y)
        break
      except Exception as e:
        sleep(0.2)
        file_print(f"TRANSLATION FAILED {e}\n")

  return out

def makeTranslation(transKey, filenames):
  s = time()

  file_print(f"Translating to {transKey}")

  if isinstance(filenames, str):
    filenames = [filenames]

  start = time()
  data = translate(en_us, transKey, f"{langDir}{filenames[0]}.json")
  for filename in filenames:
    with open(f"{langDir}{filename}.json", "w") as file:
      dump(data, file, indent=4, sort_keys=True)

  file_print(f"Translated to {transKey} {time() - start} s")
  
  langTimeTable.append([transKey, f"{int(time() - start)} s"])

  
if __name__ == "__main__":
  with open(origFile, "r") as file:
    en_us = load(file)

  with open(compFile, "r") as file:
    comp = load(file)
  

  with open("langs.json") as file:
    langs = load(file)

  with open("langs.json", "w") as file:
    dump(langs, file, indent=4, sort_keys=True)


  for idx, args in enumerate(langs.items()):
    file_print(f"{idx} / {len(langs)} ", end="")
    makeTranslation(*args)

  print(langTimeTable)

  log.close()

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


def translate(orig, dest):
  #runTor()
  translator = GoogleTranslator(source='en', target=dest, proxies=PROXIES)
  s = time()

  try:
    out = {}
    for idx, (x, y) in enumerate(orig.items()):
      printProgress(idx, len(orig), time() - s)
      s = time()
      out[x] = translator.translate(y)
  except Exception as e:
    sleep(0.2)
    file_print(f"TRANSLATION FAILED {e}")
    return translate(orig, dest)

  return out

def makeTranslation(transKey, filenames):
  s = time()

  file_print(f"Translating to {transKey}")

  if isinstance(filenames, str):
    filenames = [filenames]

  start = time()
  data = translate(en_us, transKey)
  for filename in filenames:
    with open(f"{langDir}{filename}.json", "w") as file:
      dump(data, file, indent=4, sort_keys=True)

  file_print(f"Translated to {transKey} {time() - start} s")
  
  langTimeTable.append([transKey, f"{int(time() - start)} s"])

  
if __name__ == "__main__":
  with open(origFile, "r") as file:
    en_us = load(file)

  with open("langs.json") as file:
    langs = load(file)

  with open("langs.json", "w") as file:
    dump(langs, file, indent=4, sort_keys=True)


  for idx, args in enumerate(langs.items()):
    file_print(idx, end="")
    makeTranslation(*args)

  print(langTimeTable)

  log.close()

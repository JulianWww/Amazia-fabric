from json import load, dump
from deep_translator import GoogleTranslator
import os, sys
from pathlib import Path
from time import time
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


def translate(orig, dest):
  #runTor()
  translator = GoogleTranslator(source='en', target=dest, proxies=PROXIES)

  out = {x: translator.translate(y) for x, y in orig.items()}

  return out

def printToGit(*args, **kwargs):
  with open(os.environ['GITHUB_OUTPUT'], 'a') as fh:
    print(*args, file=fh, **kwargs)

def makeTranslation(transKey, filenames):
  if isinstance(filenames, str):
    filenames = [filenames]

  start = time()
  data = translate(en_us, transKey)
  for filename in filenames:
    with open(f"{langDir}{filename}.json", "w") as file:
      dump(data, file, indent=4, sort_keys=True)


  printToGit(f"Translated to {transKey}")
  
  langTimeTable.append([transKey, f"{int(time() - start)} s"])

  
if __name__ == "__main__":
  with open(origFile, "r") as file:
    en_us = load(file)

  with open("langs.json") as file:
    langs = load(file)

  with open("langs.json", "w") as file:
    dump(langs, file, indent=4, sort_keys=True)


  for args in langs.items():
    makeTranslation(*args)

  print(langTimeTable)
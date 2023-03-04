from json import load, dump
from deep_translator import GoogleTranslator
import os
from threading import Thread
from time import time
from wget import download

abspath = os.path.abspath(__file__)
dname = os.path.dirname(abspath)
os.chdir(dname)

langDir = "../../src/main/resources/assets/amazia/lang/"
origFile = langDir + "en_us.json"

if (not os.path.exists("TableIt.py")):
  download("https://raw.githubusercontent.com/SuperMaZingCoder/TableIt/master/TableIt.py")
  print("downloaded Tableit")

from TableIt import printTable

langTimeTable = []


def translate(orig, dest):
  translator = GoogleTranslator(source='en', target=dest)

  out = {}
  for key, txt in orig.items():
    translated = translator.translate(txt)
    out[key] = translated

  return out

def makeTranslation(transKey, filenames):
  if isinstance(filenames, str):
    filenames = [filenames]

  start = time()
  data = translate(en_us, transKey)
  for filename in filenames:
    with open(f"{langDir}{filename}.json", "w") as file:
      dump(data, file, indent=4, sort_keys=True)
  
  langTimeTable.append([transKey, f"{int(time() - start)} s"])

with open(origFile, "r") as file:
  en_us = load(file)

with open("langs.json") as file:
  langs = load(file)

with open("langs.json", "w") as file:
  dump(langs, file, indent=4, sort_keys=True)

threads = []

for args in langs.items():
  translator = Thread(target=makeTranslation, args=args)
  translator.start()
  threads.append(translator)

for thread in threads:
  thread.join()

printTable(langTimeTable)
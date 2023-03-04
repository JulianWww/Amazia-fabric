from json import load, dump
from deep_translator import GoogleTranslator
import os, sys
from pathlib import Path
from time import time, sleep

abspath = os.path.abspath(__file__)
dname = os.path.dirname(abspath)
os.chdir(dname)
sys.path.insert(1, str(Path(abspath).parent))

langDir = "../../src/main/resources/assets/amazia/lang/"
origFile = langDir + "en_us.json"


langTimeTable = []


def translate(orig, dest):
  translator = GoogleTranslator(source='en', target=dest)

  translated = translator.translate_batch(orig.values())

  out = {x: y for x, y in zip(orig.keys(), translated)}

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

  
if __name__ == "__main__":
  with open(origFile, "r") as file:
    en_us = load(file)

  with open("langs.json") as file:
    langs = load(file)

  with open("langs.json", "w") as file:
    dump(langs, file, indent=4, sort_keys=True)



  for args in langs.items():
    makeTranslation(*args)
    sleep(1)

  print(langTimeTable)
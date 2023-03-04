from os import walk, chdir, remove
from os.path import join, abspath, dirname

abspath = abspath(__file__)
dname = dirname(abspath)
chdir(dname)

from generate import langDir

for root, _, files in walk(langDir):
  for file in files:
    if (file != "en_us.json"):
      path = join(root, file)
      remove(path)
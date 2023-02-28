import os
import json

abspath = os.path.abspath(__file__)
dname = os.path.dirname(abspath)
os.chdir(dname)


path = "../src/main/resources/"

config = path + "fabric.mod.json"

with open(config, "r") as file:
	data = json.load(file)

with open("modmenuText.txt", "r") as file:
	txt = file.read()
data["description"] = txt
	
with open(config, "w") as file:
	json.dump(data, file, indent=6)

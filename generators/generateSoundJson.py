import json
import os
from pathlib import Path

path = "../src/main/resources/assets/amazia/"
directory = path + "sounds/"


out = {}

for key in os.listdir(directory):
	key = str(Path(key).with_suffix(""))
	data = {
		"subtitle": "subtitles.amazia." + key,
		"sounds": [ 
			"amazia:" + key
		]
	}
	out[key] = data
	

with open(path + "sounds.json", "w") as file:
	json.dump(out, file, indent = 6)


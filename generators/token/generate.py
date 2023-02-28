import wget
from os import remove, path, chdir, system
import cv2
import numpy as np
import json

abspath = path.abspath(__file__)
dname = path.dirname(abspath)
chdir(dname)

mc_version = "1.19.3"
path = f"https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/1.19.3/assets/minecraft/textures/item/"
outPath = "../../src/main/resources/assets/amazia/textures/item/tokens/"

from villagers import images

system(f"rm {outPath}*")
system(f"mkdir {outPath}")

base = cv2.imread("base_token.png", cv2.IMREAD_UNCHANGED)

for entity, img in images.items():
  file = img[1:] + ".png" if img[0] == "/" else wget.download(path + img + ".png")

  mask = cv2.imread(file, cv2.IMREAD_UNCHANGED)
  mask = np.pad(mask, ((3,3), (3, 3), (0, 0)), "constant", constant_values = 0)

  pos = np.where(mask[:,:,3] != 0)
  new_base = np.copy(base)
  new_base[pos] = mask[pos]

  cv2.imwrite(outPath + entity + ".png", new_base)


  if (img[0] != "/"): remove(file)
  
  print (f"generated {entity} conversion token")
 
root_acivement = "../../src/main/resources/data/amazia/advancements/village/root.json"
with open(root_acivement, "r") as file:
	data = json.load(file)
	
tokens = [x + "_transformation_token" for x in images]
tokens.append("base_transformation_token")
	
data["rewards"] = {
	"recipes": tokens,
	"experience": 10000
}
	
with open(root_acivement, "w") as file:
	json.dump(data, file, indent=4)

system(f"cp base_token.png {outPath}base.png")

print(f"Generating token based advancements")

outPath = "../../src/main/resources/data/amazia/advancements/village/"

villagerTypes = {
  "miner": ("root", 100),
  "blacksmith": ("miner", 100),
  
  "lumberjack": ("root", 100),
  
  "farmer": ("root", 100),
  "druid": ("farmer", 100),
  "rancher": ("farmer", 100),
  "chef": ("rancher", 100),
  
  "bard": ("root", 100),
  
  "guard": ("root", 100),
  "cleric": ("guard", 100),
  
  "teacher": ("root", 100),
  "enchanter": ("teacher", 100)
}

def genAchivement(villager, parent, reward):
	return {
	  "parent": f"amazia:village/{parent}",
	  "criteria": {
	    "make_bard": {
	      "conditions": {
		"item": {
			"items": [f"amazia:{villager}_transformation_token"]
		}
	      },
	      "trigger": "minecraft:using_item"
	    }
	  },
	  "display": {
	    "announce_to_chat": True,
	    "description": {
	      "translate": f"advancements.village.{villager}.description"
	    },
	    "frame": "task",
	    "icon": {
	      "item": f"amazia:{villager}_transformation_token"
	    },
	    "show_toast": True,
	    "title": {
	      "translate": f"advancements.village.{villager}.title"
	    }
	  },
	  "rewards": {
	    "experience": reward
	  }
	}

for villager, data in villagerTypes.items():
	with open(outPath + villager + ".json", "w") as file:
		json.dump(genAchivement(villager, *data), file, indent=4)


print(f"Generated token based advancements")

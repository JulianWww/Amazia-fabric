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

images = {
  "miner": "iron_pickaxe",
  "bard": "music_disc_chirp",
  "chef": "cooked_beef",
  "druid": "bone_meal",
  "cleric": "white_candle",
  "farmer": "iron_hoe",
  "rancher": "lead",
  "teacher": "writable_book",
  "enchanter": "enchanted_book",
  "lumberjack": "iron_axe",
  "blacksmith": "/hammer"
}

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

system(f"cp base_token.png {outPath}base.png")

print(f"Generating token based advancements")

outPath = "../../src/main/resources/data/amazia/advancements/village/"

villagerTypes = {
  "miner": 100,
  "bard": 100,
  "chef": 100,
  "druid": 100,
  "cleric": 100,
  "farmer": 100,
  "rancher": 100,
  "teacher": 100,
  "enchanter": 100,
  "lumberjack": 100,
  "blacksmith": 100
}

def genAchivement(villager):
	return {
	  "parent": "amazia:village/root",
	  "criteria": {
	    "make_bard": {
	      "conditions": {
		"item": f"amazia:{villager}_transformation_token"
	      },
	      "trigger": "minecraft:using_item"
	    }
	  },
	  "display": {
	    "announce_to_chat": True,
	    "description": {
	      "translate": f"advancements.village.{villager}.description"
	    },
	    "frame": "goal",
	    "icon": {
	      "item": f"amazia:{villager}_transformation_token"
	    },
	    "show_toast": True,
	    "title": {
	      "translate": f"advancements.village.{villager}.title"
	    }
	  },
	  "rewards": {
	    "experience": 85
	  }
	}

for villager, reward in villagerTypes.items():
	with open(outPath + villager + ".json", "w") as file:
		json.dump(genAchivement(villager), file, indent=4)

import wget
from os import remove, path, chdir, system, mkdir
import cv2
import numpy as np
import json
import shutil

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
 
system(f"cp base_token.png {outPath}base.png")

print(f"Generating token based advancements")

outPath = "../../src/main/resources/data/amazia/advancements/village/"

try:
	shutil.rmtree(outPath)
except FileNotFoundError as e:
	print(e)
mkdir(outPath)




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
	    f"make_{villager}": {
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

print(f"Generation level advancement Acivements")

def genLevelAcivement(villager, parent, reward, type, frame):
	return {
	  "parent": f"amazia:village/{parent}",
	  "criteria": {
	    f"amazia.gain_{type}_rank_for_amazia:{villager}": {
	      "conditions": {
		"tier": type,
		"profession": f"amazia:{villager}"
	      },
	      "trigger": "amazia:gain_advancement"
	    }
	  },
	  "display": {
	    "announce_to_chat": True,
	    "description": {
	      "translate": f"advancements.village.{villager}.{type}.description"
	    },
	    "frame": frame,
	    "icon": {
	      "item": f"amazia:{villager}_transformation_token"
	    },
	    "show_toast": True,
	    "title": {
	      "translate": f"advancements.village.{villager}.{type}.title"
	    }
	  },
	  "rewards": {
	    "experience": reward
	  }
	}

acivementLevels = [
	("novice", 				"task"),
	("beginner", 			"task"),
	("aprentice", 		"task"),
	("journeyman", 		"task"),
	("student", 			"goal"),
	("grad_student", 	"goal"),
	("expert", 				"goal"),
	("leading_expert","challenge"),
	("master",				"challenge"),
	("grand_master",  "challenge")
]

for idx, (lvl, frame) in enumerate(acivementLevels):
	for villager in villagerTypes:
		with open(outPath + villager + "_" + lvl + ".json", "w") as file:
			json.dump(genLevelAcivement(villager, villager if lvl == "novice" else villager + "_" + acivementLevels[idx - 1][0], 1024, lvl, frame), file, indent=4)
			
print("Generated level advanement Acivements")

print("Generate titles")

def getTitleAcivements(title, name, icon, xp, parent):
	return {
	    "parent": f"amazia:village/{parent}",
	    "criteria": {
		"doctorate": {
		    "conditions": {
		        "title": title
		    },
		    "trigger": "amazia:gain_title"
		}
	    },
	    "display": {
		"announce_to_chat": True,
		"description": {
		    "translate": f"advancements.village.{name}.description"
		},
		"frame": "challenge",
		"icon": {
		    "item": icon
		},
		"show_toast": True,
		"title": {
		    "translate": f"advancements.village.{name}.title"
		}
	    },
	    "rewards": {
		"experience": xp
	    }
	}

titles = [
	("phd", 	"doctorate", "minecraft:writable_book", 1024, "teacher"),
	("professor", 	"professor", "minecraft:lectern",	2048, "doctorate")
]

for data in titles:
	with open(outPath + data[1] + ".json", "w") as file:
		json.dump(getTitleAcivements(*data), file, indent=4)

print("Generated titles")


with open(outPath + "root.json", "w") as file:
	json.dump(
		{
		    "criteria": {
			"first_village": {
			    "conditions": {
				"block": "amazia:village_core"
			    },
			    "trigger": "minecraft:placed_block"
			}
		    },
		    "display": {
			"background": "minecraft:textures/block/oak_log.png",
			"description": {
			    "translate": "advancements.village.root.description"
			},
			"frame": "task",
			"hidden": False,
			"icon": {
			    "item": "amazia:village_core"
			},
			"title": {
			    "translate": "advancements.village.root.title"
			}
		    }
		},
		file, indent=4)

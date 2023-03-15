import wget
from os import remove, path, chdir, system, mkdir
import cv2
import numpy as np
import json
import shutil
from tiers import getImage, updateTokeTierOverrides

langFile = "../../src/main/resources/assets/amazia/lang/en_us.json"

abspath = path.abspath(__file__)
dname = path.dirname(abspath)
chdir(dname)

mc_version = "1.19.3"
path = f"https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/1.19.3/assets/minecraft/textures/item/"
outPath = "../../src/main/resources/assets/amazia/textures/item/tokens/"

with open(langFile, "r") as file:
	lang = json.load(file)

from villagers import images, acivementLevels, toast

system(f"rm {outPath}*")
system(f"mkdir {outPath}")
system(f"mkdir ./tmp")


base = cv2.imread("base_token.png", cv2.IMREAD_UNCHANGED)

for entity, img in images.items():
	file = img[1:] + ".png" if img[0] == "/" else wget.download(path + img + ".png", f"./tmp/{img}.png")

	mask = cv2.imread(file, cv2.IMREAD_UNCHANGED)
	mask = np.pad(mask, ((3,3), (3, 3), (0, 0)), "constant", constant_values = 0)

	pos = np.where(mask[:,:,3] != 0)
	new_base = np.copy(base)
	new_base[pos] = mask[pos]

	cv2.imwrite(outPath + entity + ".png", new_base)

	if entity != "child" and entity != "nitwit":
		for acivement_data in acivementLevels:
			lvl = acivement_data[0]
			x, y = acivement_data[2]

			cv2.imwrite(outPath + entity + "_" + lvl + ".png", getImage(x, y, mask))
		updateTokeTierOverrides(entity)
  
	print (f"generated {entity} conversion token")

	lang[f"entity.amazia.{entity}"] = entity.title()
 
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
	  "parent": f"amazia:village/root",
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
	    "show_toast": toast,
	    "title": {
	      "translate": f"advancements.village.{villager}.title"
	    }
	  },
	  "rewards": {
	    "experience": reward
	  }
	}

for villager, data in villagerTypes.items():
	if (villager != "child" and villager != "nitwit"):
		with open(outPath + villager + ".json", "w") as file:
			json.dump(genAchivement(villager, *data), file, indent=4)


print(f"Generated token based advancements")

print(f"Generation level advancement Acivements")

def genLevelAcivement(villager, parent, reward, type, frame, idx):
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
	      "item": f"amazia:{villager}_transformation_token",
	      "nbt": "{CustomModelData:" + str(idx + 1) + "}"
	    },
	    "show_toast": toast,
	    "hidden": True,
	    "title": {
	      "translate": f"advancements.village.{villager}.{type}.title"
	    }
	  },
	  "rewards": {
	    "experience": reward
	  }
	}

for idx, (lvl, frame, _) in enumerate(acivementLevels):
	for villager in villagerTypes:
		with open(outPath + villager + "_" + lvl + ".json", "w") as file:
			json.dump(genLevelAcivement(villager, villager if lvl == "novice" else villager + "_" + acivementLevels[idx - 1][0], 2**(idx+1), lvl, frame, idx), file, indent=4)
		
		level = lvl.replace("_", " ").title()
		lang[f"advancements.village.{villager}.{lvl}.description"] = f"Have a {villager.title()} reach the rank of {level} in your village"
		lang[f"advancements.village.{villager}.{lvl}.title"] = f"{level} {villager.title()}"
			
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
		"show_toast": toast,
		"title": {
		    "translate": f"advancements.village.{name}.title"
		}
	    },
	    "rewards": {
		"experience": xp
	    }
	}

titles = [
	("phd", 	"doctorate", "minecraft:writable_book", 1024, "root"),
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

with open("../../src/main/resources/assets/amazia/lang/en_us.json", "w") as file:
	json.dump(lang, file, indent=4, sort_keys=True)

system(f"rm -r ./tmp")

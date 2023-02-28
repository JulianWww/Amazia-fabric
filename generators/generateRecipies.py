import os
import json
import sys
from pathlib import Path
import shutil

abspath = os.path.abspath(__file__)
dname = os.path.dirname(abspath)
os.chdir(dname)
sys.path.insert(1, str(Path(abspath).parent) + "/token")

path = "../src/main/resources/data/amazia/recipes/"
types = [
  "oak",
  "spruce",
  "birch",
  "jungle",
  "acacia",
  "dark_oak",
  "crimson",
  "warped",
  "mangrove",
  "stripped_oak",
  "stripped_spruce",
  "stripped_birch",
  "stripped_jungle",
  "stripped_acacia",
  "stripped_dark_oak",
  "stripped_crimson",
  "stripped_warped",
  "stripped_mangrove",
]

def getShapedBase():
  return {
    "type": "minecraft:crafting_shaped"
  }

def getLogs(type):
  if ("warped" in type or "crimson" in type):
    return f"minecraft:{type}_stem"
  return f"minecraft:{type}_log"

def getPlanks(type):
  return f"minecraft:{type}_planks"

def addChairIn(data, type, alt):
  data["pattern"] = [
    "  #" if alt else "#  ",
    "###",
    "/ /"
  ]
  data["key"] = {
    "#": {
      "item": getLogs(type)
      },
    "/": {
      "item": "minecraft:stick"
    }
  }
  return data

def addDeskIn(data, type):
  data["pattern"] = [
    "###",
    "/ /"
  ]
  data["key"] = {
    "#": {
      "item": getLogs(type)
      },
    "/": {
      "item": "minecraft:stick"
    }
  }
  return data

def addDeskCabinetIn(data, type, alt):
  data["pattern"] = [
    "###",
    "/ C" if alt else "C /"
  ]
  data["key"] = {
    "#": {
      "item": getLogs(type)
      },
    "/": {
      "item": "minecraft:stick"
    },
    "C": {
      "item": "minecraft:chest"
    }
  }
  return data

def addTroughIn(data, type):
  data["pattern"] = [
    "# #",
    "# #",
    "###"
  ]
  data["key"] = {
    "#": {
      "item": getPlanks(type)
      }
  }
  return data

def removeOf(type):
  os.system(f"rm {path}*{type}.json")

def addOut(data, item, count, group) :
  data["result"] = {
    "item": item,
    "count": count
  }
  if not group is None:
  	data["group"] = "amazia:" + group
  return data

def getChair(type, alt):
  data = getShapedBase()
  data = addChairIn(data, type, alt)
  data = addOut(data, f"amazia:{type}_chair", 1, "chair")

  save(f"{type}_chair" + ("_right" if alt else "_left"), data)

def getDesk(type):
  data = getShapedBase()
  data = addDeskIn(data, type)
  data = addOut(data, f"amazia:{type}_desk", 1, "desk")

  save(f"{type}_desk", data)

def getDeskCabinet(type, alt):
  data = getShapedBase()
  data = addDeskCabinetIn(data, type, alt)
  data = addOut(data, f"amazia:{type}_desk_cabinet", 1, "desk_cabinet")

  save(f"{type}_desk_cabinet" + ("" if alt else "_alt"), data)
  
  items = [getLogs(type)]
  saveUnlocker(f"{type}_desk_cabinet" + ("" if alt else "_alt"), "decorations", items)

def getChairs(types) :
  removeOf("chair_left")
  removeOf("chair_right")
  for type in types:
    getChair(type, True)
    getChair(type, False)
    
    items = [getLogs(type)]
    saveUnlocker(f"{type}_chair_left",  "decorations", items)
    saveUnlocker(f"{type}_chair_right", "decorations", items)
  
def getDesks(types) :
  removeOf("desk")
  for type in types:
    getDesk(type)
    
    items = [getLogs(type)]
    saveUnlocker(f"{type}_desk", "decorations", items)

def getDeskCabinets(types) :
  removeOf("desk_cabinet")
  removeOf("desk_cabinet_alt")
  for type in types:
    getDeskCabinet(type, True)
    getDeskCabinet(type, False)
   

def getTrough(type, name):
  data = getShapedBase()
  data = addTroughIn(data, type)
  data = addOut(data, f"amazia:{name}_trough", 1, None)

  save(f"{name}_trough", data)
  
  items = [getPlanks(type)]
  saveUnlocker(f"{name}_trough", "decorations", items)


def save(name, data):
  with open(f"{path}{name}.json", "w") as file:
    json.dump(data, file, indent=4)


def genUnlocker(identifier, items=None, tags=None):
  data = {
	  "parent": "minecraft:recipes/root",
	  "criteria": {
	    f"has_items_{identifier}": {
	      "conditions": {
          "items": []
	      },
	      "trigger": "minecraft:inventory_changed"
	    },
	    "has_the_recipe": {
	      "conditions": {
		      "recipe": identifier
	      },
	      "trigger": "minecraft:recipe_unlocked"
	    }
	  },
	  "requirements": [
	    [
	      f"has_items_{identifier}",
	      "has_the_recipe"
	    ]
	  ],
	  "rewards": {
	    "recipes": [
	      identifier
	    ]
	  }
	}
  
  if not tags is None:
    for tag in tags:
      data["criteria"][f"has_items_{identifier}"]["conditions"]["items"].append({"tag": tag})

  if not items is None:
    for item in items:
      data["criteria"][f"has_items_{identifier}"]["conditions"]["items"].append({"items": [item]})

  return data


def deleteUnlocksers():
	path = "../src/main/resources/data/amazia/advancements/recipes"
	try:
		shutil.rmtree(path)
	except:
	 	pass

def saveUnlocker(ID, group, *args):
	path = f"../src/main/resources/data/amazia/advancements/recipes/{group}/{ID}.json"
	Path(path).parent.mkdir(parents=True, exist_ok=True)
	with open(path, "w") as file:
		json.dump(genUnlocker("amazia:" + ID, *args), file, indent=2)

def registerOtherItemUnlockers():
  items = {
		"village_core":                 ("decorations", ["minecraft:diamond", "minecraft:iron_ingot", "amazia:ruby_block"]),
		"ruby": 	                      ("misc", ["amazia:ruby_block"]),
		"ruby_block":	                  ("building_blocks", ["amazia:ruby"]),
    "tree_school":                  ("decorations", None, ["minecraft:planks"]),
    "mine_marker":                  ("decorations", ["minecraft:iron_pickaxe"]),
    "flute":                        ("misc", None, ["minecraft:planks"]),
    "base_conversion_token":        ("misc", ["minecraft:diamond", "minecraft:glass"]),
    "ruby_ore_smelting":            ("misc", ["amazia:ruby_ore"]),
    "ruby_ore_blasting":            ("misc", ["amazia:ruby_ore"]),
    "deepslate_ruby_ore_smelting":  ("misc", ["amazia:deepslate_ruby_ore"]),
    "deepslate_ruby_ore_blasting":  ("misc", ["amazia:deepslate_ruby_ore"]),
  }
    
  for ID, args in items.items():
    saveUnlocker(ID, *args)
  
  from villagers import images
  for villager in images:
     saveUnlocker(f"{villager}_conversion_token", "misc", ["amazia:base_transformation_token"])



if __name__ == "__main__":
  deleteUnlocksers()
  getChairs(types)
  getDesks(types)
  getDeskCabinets(types)

  removeOf("trough")

  getTrough("birch", "chicken")
  getTrough("oak", "cow")
  getTrough("spruce", "pig")
  getTrough("dark_oak", "sheep")
  
  registerOtherItemUnlockers()

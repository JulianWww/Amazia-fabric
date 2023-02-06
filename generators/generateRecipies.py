import os
import json

abspath = os.path.abspath(__file__)
dname = os.path.dirname(abspath)
os.chdir(dname)


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

def getPlanks(type):
  if ("warped" in type):
    return f"minecraft:{type}_stem"
  return f"minecraft:{type}_log"

def addChairIn(data, type, alt):
  data["pattern"] = [
    "  #" if alt else "#  ",
    "###",
    "/ /"
  ]
  data["key"] = {
    "#": {
      "item": getPlanks(type)
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
      "item": getPlanks(type)
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
      "item": getPlanks(type)
      },
    "/": {
      "item": "minecraft:stick"
    },
    "C": {
      "item": "minecraft:chest"
    }
  }
  return data

def removeOf(type):
  os.system(f"rm {path}*{type}.json")

def addOut(data, item, count) :
  data["result"] = {
    "item": item,
    "count": count
  }
  return data

def getChair(type, alt):
  data = getShapedBase()
  data = addChairIn(data, type, alt)
  data = addOut(data, f"amazia:{type}_chair", 1)

  save(f"{type}_chair" + ("_right" if alt else "_left"), data)

def getDesk(type):
  data = getShapedBase()
  data = addDeskIn(data, type)
  data = addOut(data, f"amazia:{type}_desk", 1)

  save(f"{type}_desk", data)

def getDeskCabinet(type, alt):
  data = getShapedBase()
  data = addDeskCabinetIn(data, type, alt)
  data = addOut(data, f"amazia:{type}_desk_cabinet", 1)

  save(f"{type}_desk_cabinet" + ("" if alt else "_alt"), data)

def getChairs(types) :
  removeOf("chair_left")
  removeOf("chair_right")
  for type in types:
    getChair(type, True)
    getChair(type, False)
  
def getDesks(types) :
  removeOf("desk")
  for type in types:
    getDesk(type)

def getDeskCabinets(types) :
  removeOf("desk_cabinet")
  removeOf("desk_cabinet_alt")
  for type in types:
    getDeskCabinet(type, True)
    getDeskCabinet(type, False)


def save(name, data):
  with open(f"{path}{name}.json", "w") as file:
    json.dump(data, file, indent=4)


if __name__ == "__main__":
  getChairs(types)
  getDesks(types)
  getDeskCabinets(types)
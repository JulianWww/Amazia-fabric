from os import chdir, mkdir
from os.path import abspath, dirname
from sys import path
from json import dump
from pathlib import Path
from shutil import rmtree
from copy import copy

abspath = abspath(__file__)
dname = dirname(abspath)
chdir(dname)
path.insert(1, str(Path(abspath).parent.parent) + "/particles")
from utils import putSegments

dir = "../../src/main/resources/data/amazia/worldgen/"
feature_dir = dir + "configured_feature/"
placed_dir = dir + "placed_feature/"
javaFile = "../../src/main/java/net/denanu/amazia/compat/mc/worldgen/Worldgen.java"

staticJava = []
dynamicJava = []

def reset(file):
  try:
    rmtree(file)
  except: pass
  mkdir(file)

reset(feature_dir)
reset(placed_dir)

def genJava(config):
  name = config["name"]
  staticJava.append(f"""public static final RegistryKey<PlacedFeature> {name.upper()} = RegistryKey.of(Registry.PLACED_FEATURE_KEY, Identifier.of(Amazia.MOD_ID, "{name}"));""")
  dynamicJava.append(f"""\tBiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, Worldgen.{name.upper()});""")


def createPlaced(data) :
  with open(placed_dir + data["name"] + ".json", "w") as file:
    dump({
      "feature": f"amazia:{data['name']}",
      "placement": [
        {
          "type": "minecraft:count",
          "count": data["rarity"]
        },
        {
          "type": "minecraft:in_square"
        },
        {
          "type": "minecraft:height_range",
          "height": {
            "type": "minecraft:trapezoid",
            "max_inclusive": {
              "absolute": 50
            },
            "min_inclusive": {
              "absolute": -24
            }
          }
        },
        {
          "type": "minecraft:biome"
        }
      ]
    },
    file,
    sort_keys=True,
    indent="\t")

def createConfig(data):
  with open(feature_dir + data["name"] + ".json", "w") as file:
    dump(    {
      "type": "minecraft:ore",
      "config": {
        "discard_chance_on_air_exposure": 0.2,
        "size": data["size"],
        "targets": [
          {
            "state": {
              "Name": "amazia:ruby_ore"
            },
            "target": {
              "predicate_type": "minecraft:tag_match",
              "tag": "minecraft:stone_ore_replaceables"
            }
          },
          {
            "state": {
              "Name": "amazia:deepslate_ruby_ore"
            },
            "target": {
              "predicate_type": "minecraft:tag_match",
              "tag": "minecraft:deepslate_ore_replaceables"
            }
          }
        ]
      }
    }

,
    file,
    sort_keys=True,
    indent="\t")

ore_configs = [
  {"name": "giant_ruby_ore", "size": 10, "rarity": 1},
  {"name": "large_ruby_ore", "size": 6, "rarity": 5},
  {"name": "medium_ruby_ore", "size": 5, "rarity": 5},
  {"name": "small_ruby_ore", "size": 4, "rarity": 5},
  {"name": "tiny_ruby_ore", "size": 3, "rarity": 5}
]

for config in ore_configs:
  createPlaced(config)
  createConfig(config)
  genJava(config)

putSegments(staticJava + ["", "", "public static void setup() {"] + dynamicJava + ["}"], javaFile)
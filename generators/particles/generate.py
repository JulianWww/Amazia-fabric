from json import load, dump
import os, sys
from shutil import rmtree
from wget import download
from pathlib import Path
from utils import putSegments

abspath = os.path.abspath(__file__)
dname = os.path.dirname(abspath)
os.chdir(dname)
sys.path.insert(1, str(Path(abspath).parent))

assetsPath = "../../src/main/resources/assets/amazia/"
particle_dir = assetsPath + "particles/"
texturePath = assetsPath + "/textures/particle/"
item_path = "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/1.19.3/assets/minecraft/textures/item/"
block_path = "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/1.19.3/assets/minecraft/textures/block/"
javaPath = "../../src/main/java/net/denanu/amazia/particles/"
downloadFolder = "./tmp/"

def reset(file):
  try:
    rmtree(file)
  except: pass
  os.mkdir(file)

reset(particle_dir)
reset(texturePath)
reset(downloadFolder)

def generateJavaRegister(particles):
  filename = javaPath + "AmaziaParticles.java"
  gen = [f"""public static final DefaultParticleType {particle.upper()} = AmaziaParticles.register(FabricParticleTypes.simple(), "{particle}");""" for particle in particles]
  putSegments(gen, filename)

def generateJavaClientRegister(particles):
  filename = javaPath + "AmaziaClientParticles.java"
  gen = [f"""ParticleFactoryRegistry.getInstance().register(AmaziaParticles.{particle.upper()}, SuspendParticle.Factory::new);""" for particle in particles]
  putSegments(gen, filename)
  
def defGenerateItemParticleMap(particles):
  filename = javaPath + "AmaziaItemParticleMap.java"

  gen = []
  for key, data in particles.items():
    items = [key]
    if "items" in data:
      items = data["items"]
    elif "type" in data:
      group = data["type"]
      if group == "tool":
        items = [f"{type}_{key}" for type in ["wooden", "stone", "iron", "golden", "diamond", "netherite"]]
      
      elif group == "tree":
        items = [f"{type}_{key}" for type in ["oak", "spruce", "birch", "jungle", "acacia", "dark_oak"]]
    
    gen.extend([f"""AmaziaItemParticleMap.itemParticleMap.put(Items.{item.upper()}, AmaziaParticles.{key.upper()});""" for item in items])

  putSegments(gen, filename)




def getItem(source, item, key, to=downloadFolder):
  download(f"{source}{item}.png", out=to + key + ".png")

def generateConfigFile(particle):
  with open(particle_dir + particle + ".json", "w") as file:
    dump(
      {
        "textures": [
          f"amazia:{particle}"
        ]
      },
      file,
      sort_keys=True,
      indent="\t"
    )

def generateTexture(data, key):
  if "item" in data:
    getItem(item_path, data["item"], key, texturePath)
  
  if "block" in data:
    getItem(block_path, data["block"], key, texturePath)

with open("particles.json", "r") as file:
  particles = load(file)

with open("particles.json", "w") as file:
  dump(particles, file, sort_keys=True, indent="\t")



for particle, data in particles.items():
  generateConfigFile(particle)
  generateTexture(data, particle)

generateJavaRegister(particles.keys())
generateJavaClientRegister(particles.keys())
defGenerateItemParticleMap(particles)
from json import load, dump
import os, sys
from shutil import rmtree
from wget import download
from pathlib import Path

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

def getSegments(filename):
  with open(filename, "r") as file:
    lines = file.readlines()

  headLines = []
  tailLines = []

  for line in lines:
    headLines.append(line)
    if "PYTHON GENERATOR BEGIN" in line:
      indent = len(line) - len("// PYTHON GENERATOR BEGIN ")
      break
  
  for line in reversed(lines):
    tailLines.insert(0, line)
    if "PYTHON GENERATOR END" in line:
      break
  
  return (headLines, tailLines, indent)

def putSegments(genLines, filename):
  head, tail, indent = getSegments(filename)
  genLines = ["\t" * indent + line + "\n" for line in genLines]
  with open(filename, "w") as file:
    file.writelines(head + genLines + tail)

def generateJavaRegister(particles):
  filename = javaPath + "AmaziaParticles.java"
  gen = [f"""public static final DefaultParticleType {particle.upper()} = AmaziaParticles.register(FabricParticleTypes.simple(), "{particle}");""" for particle in particles]
  putSegments(gen, filename)

def generateJavaClientRegister(particles):
  filename = javaPath + "AmaziaClientParticles.java"
  gen = [f"""ParticleFactoryRegistry.getInstance().register(AmaziaParticles.{particle.upper()}, SuspendParticle.Factory::new);""" for particle in particles]
  putSegments(gen, filename)
  





def getItem(source, item, to=downloadFolder):
  download(f"{source}{item}.png", out=to + item + ".png")

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

def generateTexture(data):
  if "item" in data:
    getItem(item_path, data["item"], texturePath)
  
  if "block" in data:
    getItem(block_path, data["block"], texturePath)

with open("particles.json", "r") as file:
  particles = load(file)

with open("particles.json", "w") as file:
  dump(particles, file, sort_keys=True, indent="\t")



for particle, data in particles.items():
  generateConfigFile(particle)
  generateTexture(data)

generateJavaRegister(particles.keys())
generateJavaClientRegister(particles.keys())
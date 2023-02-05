import wget
from os import remove
import cv2
import numpy as np

mc_version = "1.19.3"
path = f"https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/1.19.3/assets/minecraft/textures/item/"
outPath = "../../src/main/resources/assets/amazia/textures/item/tokens/"

images = {
  "miner": "diamond_pickaxe",
  "bard": "music_disc_chirp",
  "chef": "cooked_beef",
  "druid": "kelp",
  "clerid": "white_candle",
  "farmer": "iron_hoe",
  "rancher": "lead",
  "teacher": "writable_book",
  "enchanter": "enchanted_book",
  "lumberjack": "iron_axe",
  "blacksmith": "/hammer"
}

base = cv2.imread("base_token.png", cv2.IMREAD_UNCHANGED)

for entity, img in images.items():
  file = img[1:] + ".png" if img[0] == "/" else wget.download(path + img + ".png")

  mask = cv2.imread(file, cv2.IMREAD_UNCHANGED)
  mask = np.pad(mask, ((3,3), (3, 3), (0, 0)), "constant", constant_values = 0)
  
  print(mask.shape)

  pos = np.where(mask[:,:,3] != 0)
  new_base = np.copy(base)
  new_base[pos] = mask[pos]

  cv2.imwrite(outPath + entity + ".png", new_base)


  if (img[0] != "/"): remove(file)
  
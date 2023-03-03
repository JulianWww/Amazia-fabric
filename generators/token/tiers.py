import cv2
import numpy as np
from os import path, chdir, mkdir
from villagers import acivementLevels
from pathes import item_models, tier_models
import json
import shutil
import matplotlib.pyplot as plt

abspath = path.abspath(__file__)
dname = path.dirname(abspath)
chdir(dname)

base = cv2.imread("tiers.png", cv2.IMREAD_UNCHANGED).astype(np.float64)
try: shutil.rmtree(tier_models)
except: pass
mkdir(tier_models)

if (base.shape[2] == 3):
    base = np.pad(base, ((0,0), (0,0), (0,1)), "constant", constant_values=255)

xs = [4, 97, 187]
ys = [11, 100, 191, 289]

dx = 20
dy = 8

icon_size = 44

size = 82

def getImage(x, y, mask):
    global xs, ys

    mask = cv2.resize(mask, dsize=(icon_size, icon_size), interpolation=cv2.INTER_NEAREST)

    mask = padMask(mask)

    x = xs[x]
    y = ys[y]
    new_base = np.copy(base[y:y + size, x:x + size])

    shape = (size, size, 1)
    alpha_base = new_base[:, :, 3].reshape(shape) / 255
    alpha_mask = mask[:, :, 3].reshape(shape) / 255

    new_base = mask * alpha_mask + new_base * alpha_base * (1 - alpha_mask)
    new_base[:,:,3] = ((1 - (1 - alpha_mask) * (1 - alpha_base)) * 255).reshape(shape[:-1])
    
    return new_base

def padMask(mask):
    return np.pad(mask, ((dy, size - dy - mask.shape[1]), (dx, size - dx - mask.shape[0]), (0, 0)), "constant", constant_values = 0)

def updateTokeTierOverrides(villager):
    fileName = item_models + villager + "_transformation_token.json"
    with open(fileName, "r") as file:
        data = json.load(file)
    
    data["overrides"] = [
        {
            "predicate": {
                "custom_model_data": idx + 1
            },
            "model": f"amazia:item/tier/{villager}_{tier}"
        }
        for idx, (tier, _, _) in enumerate(acivementLevels)
    ]

    with open(fileName, "w") as file:
        json.dump(data, file, indent=4)
    
    for (tier, *args) in acivementLevels:
        data = {
            "parent": "minecraft:item/generated",
            "textures": {
                "layer0": f"amazia:item/tokens/{villager}_{tier}"
            }
        }
        with open(tier_models + villager + "_" + tier + ".json", "w") as file:
            json.dump(data, file, indent=4)

if __name__ == "__main__":
    import generate
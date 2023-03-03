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
  "blacksmith": "/hammer",
  "guard": "iron_sword"
}

acivementLevels = [
	("novice", 				"task", (0, 0)),
	("beginner", 			"task", (0, 1)),
	("aprentice", 		"task", (0, 2)),
	("journeyman", 		"task", (0, 3)),
	("student", 			"goal", (1, 0)),
	("grad_student", 	"goal", (1, 1)),
	("expert", 				"goal", (1, 2)),
	("leading_expert","challenge", (1, 3)),
	("master",				"challenge", (2, 0)),
	("grand_master",  "challenge", (2, 1))
]


if __name__ == "__main__":
    import generate
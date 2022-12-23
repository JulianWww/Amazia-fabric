import json

def roman(number):
	num = [1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000]
	sym = ["I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"]
	i = 12

	out = ""
	while number:
		div = number // num[i]
		number %= num[i]

		while div:
			out = out + sym[i]
			div -= 1
		i-=1
	return out

out = {}
for i in range(10, 101):
	out["enchantment.level." + str(i)] = roman(i)

with open("../src/main/resources/assets/amazia/lang/_all.json", "w") as file:
	json.dump(out, file, indent=6)

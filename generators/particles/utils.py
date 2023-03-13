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
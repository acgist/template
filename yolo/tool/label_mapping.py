import os

mapping = {
  "0": "14",
  "1": "15",
  "2": "16",
  "3": "17",
  "4": "18",
  "5": "19",
  "6": "0",
  "7": "1"
}

folder = "D:/download/1_images"
for filename in os.listdir(folder):
    if filename == "classes.txt":
        continue
    if filename.endswith(".txt"):
        lines = []
        with open(os.path.join(folder, filename), 'r') as f:
            lines = f.readlines()
        with open(os.path.join(folder, filename), 'w') as f:
            for line in lines:
                line  = line.strip()
                index = line.find(" ")
                cls   = line[:index]
                if cls not in mapping:
                    print(f"警告: 未知类型{cls}跳过")
                    continue
                f.write(f"{mapping[cls]}{line[index:]}\n")

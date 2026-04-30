import os

delete_label = [ "1" ]

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
                if cls in delete_label:
                    print(f"删除类型{cls}")
                    continue
                f.write(f"{line}\n")

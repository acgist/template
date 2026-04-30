import os

for folder in os.listdir("./"):
    if os.path.isdir(folder):
        for file in os.listdir(folder):
            if file.endswith(".torrent"):
                if file[0] == ".":
                    os.rename(os.path.join(folder, file), os.path.join(folder, file[1:]))
import os
import uuid

from utils import get_label_path

folder = "D:/download/1_images"
for filename in os.listdir(folder):
    label_suffix = ".txt"
    if not filename.endswith(label_suffix):
        path = os.path.join(folder, filename)
        label_path = get_label_path(path, label_suffix)
        id = str(uuid.uuid4())
        if not os.path.exists(label_path):
            print(f"{filename}没有标签文件{label_path}")
            continue
        os.rename(path, os.path.join(folder, f"{id}" + path[path.rfind("."):]))
        os.rename(label_path, os.path.join(folder, f"{id}{label_suffix}"))

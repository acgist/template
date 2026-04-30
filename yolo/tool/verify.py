import os

from utils import get_label_path, get_image_path

folder = "D:/download/1_images"
for filename in os.listdir(folder):
    if filename == "classes.txt":
        continue
    if filename.endswith(".txt"):
        image_path = get_image_path(os.path.join(folder, filename), ".txt")
        if not os.path.exists(image_path):
            print(f"图片文件不存在: {image_path}")
            continue
    else:
        label_path = get_label_path(os.path.join(folder, filename), ".txt")
        if not os.path.exists(label_path):
            print(f"标签文件不存在: {label_path}")
            continue

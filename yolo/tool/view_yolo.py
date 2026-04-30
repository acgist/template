import os
import cv2

from utils import get_label_path

folder = "D:/download/3_images"
for filename in os.listdir(folder):
    if not filename.endswith(".txt"):
        print(filename)
        image = cv2.imread(os.path.join(folder, filename))
        label_path = get_label_path(os.path.join(folder, filename), ".txt")
        with open(label_path, 'r') as f:
            lines = f.readlines()
            for line in lines:
                cls, x, y, w, h = line.strip().split(" ")
                x = float(x)
                y = float(y)
                w = float(w)
                h = float(h)
                x1 = int((x - w / 2) * image.shape[1])
                y1 = int((y - h / 2) * image.shape[0])
                x2 = int(x1 + w * image.shape[1])
                y2 = int(y1 + h * image.shape[0])
                cv2.rectangle(image, (x1, y1), (x2, y2), (0, 255, 0),  2)
        width, height, _ = image.shape
        max_width, max_height = 1280, 1280
        if width > max_width or height > max_height:
            scale = min(max_width / width, max_height / height)
            new_w, new_h = int(width * scale), int(height * scale)
            image = cv2.resize(image, (new_w, new_h))
        cv2.imshow("image", image)
        cv2.waitKey(0)

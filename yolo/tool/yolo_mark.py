import os
import cv2

from ultralytics import YOLO
from utils import get_label_path

def mark_yolo(path: str, model: YOLO, label_size: int):
    results = model(path, conf = 0.60, iou = 0.50)
    txt_file = get_label_path(path, ".txt")
    with open(txt_file, 'a') as f:
        for result in results:
            clss  = result.boxes.cls.cpu().numpy()
            boxes = result.boxes.xyxy.cpu().numpy()
            confs = result.boxes.conf.cpu().numpy()
            if len(boxes) == 0:
                print("未检测到目标")
                return
            image = cv2.imread(path)
            height, width, _ = image.shape
            for cls, box, conf in zip(clss, boxes, confs):
                # 重新添加标记类型
                cls = int(cls)
                if not(cls == 0 or cls == 1):
                    continue
                x1, y1, x2, y2 = box.astype(int)
                f.write(f"{cls + label_size} {(x1 + x2) / 2 / width:.6f} {(y1 + y2) / 2 / height:.6f} {(x2 - x1) / width:.6f} {(y2 - y1) / height:.6f}\n")
                # 标记
                # label = f"{result.names[cls]}: {conf:.2f}"
                # cv2.rectangle(image, (x1, y1     ), (x2,       y2), (0, 255, 0),  2)
                # cv2.rectangle(image, (x1, y1 - 20), (x1 + 220, y1), (0, 255, 0), -1)
                # cv2.putText(image, label, (x1, y1 - 5), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 0, 0), 2)
            # max_width, max_height = 1280, 1280
            # if width > max_width or height > max_height:
            #     scale = min(max_width / width, max_height / height)
            #     new_w, new_h = int(width * scale), int(height * scale)
            #     image = cv2.resize(image, (new_w, new_h))
            # cv2.imshow("image", image)
            # cv2.waitKey(0)

model  = YOLO("D:/download/model.pt")
folder = "D:/download/1_images"
for filename in os.listdir(folder):
    if not filename.endswith(".txt"):
        image_path = os.path.join(folder, filename)
        # 注意：必须修改label_size参数
        mark_yolo(image_path, model, 6)

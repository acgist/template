import os
import cv2
import shutil

from ultralytics import YOLO

def mark_txt(path: str, output: str):
    """
    根据标记抠图
    """
    image = cv2.imread(path)
    height, width, _ = image.shape
    with open(path.replace(path[path.rfind("."):], ".txt"), 'r') as f:
        lines = f.readlines()
        for index, line in enumerate(lines):
            line = line.strip()
            if line.startswith("0"):
                x, y, w, h = map(float, line.split(" ")[1:])
                x = int((x - w / 2) * width)
                y = int((y - h / 2) * height)
                w = int(w * width)
                h = int(h * height)
                copy = image[y:y + h, x:x + w]
                # cv2.imshow("copy", copy)
                # cv2.waitKey(0)
                cv2.imwrite(os.path.join(output, os.path.basename(path) + "_" + str(index) + ".jpg"), copy)
                # cv2.rectangle(image, (x, y), (x + w, y + h), (0, 255, 0), 2)
    # cv2.imshow("image", image)
    # cv2.waitKey(0)

def mark_yolo(path: str, output: str, model: YOLO):
    """
    根据标记抠图
    """
    results = model(path)
    for result in results:
        # 置信度 = 0.25 IOU = 0.70
        # result.show()
        # 过滤低置信度
        clss  = result.boxes.cls.cpu().numpy()
        boxes = result.boxes.xyxy.cpu().numpy()
        confs = result.boxes.conf.cpu().numpy()
        conf_threshold = 0.4
        iou_threshold  = 0.2
        conf_indices = confs > conf_threshold
        clss  = clss[conf_indices]
        boxes = boxes[conf_indices]
        confs = confs[conf_indices]
        if len(boxes) == 0:
            print("未检测到目标")
            return
        # NMS CV2
        boxes_nms = []
        for box in boxes:
            x1, y1, x2, y2 = box
            w = x2 - x1
            h = y2 - y1
            boxes_nms.append([x1, y1, w, h])
#       nms_indices = cv2.dnn.NMSBoxes(boxes_nms, confs, conf_threshold, iou_threshold)
        nms_indices = cv2.dnn.NMSBoxesBatched(boxes_nms, confs, clss.astype(int), conf_threshold, iou_threshold)
        final_clss  = clss[nms_indices]
        final_boxes = boxes[nms_indices]
        final_confs = confs[nms_indices]
        # NMS Torch
        # boxes_tensor  = torch.from_numpy(boxes)
        # scores_tensor = torch.from_numpy(confs)
        # nms_indices = torchvision.ops.nms(boxes_tensor, scores_tensor, iou_threshold)
        # final_clss  = clss[nms_indices.cpu().numpy()]
        # final_boxes = boxes[nms_indices.cpu().numpy()]
        # final_confs = confs[nms_indices.cpu().numpy()]
        # 绘制结果
        if len(final_boxes) == 0:
            print("未检测到目标")
            return
        image = cv2.imread(path)
        height, width, _ = image.shape
        damage_boxes = []
        with open(path.replace(path[path.rfind("."):], ".txt"), 'r') as f:
            lines = f.readlines()
            for line in lines:
                line = line.strip()
                if line.startswith("0"):
                    x, y, w, h = map(float, line.split(" ")[1:])
                    x = int((x - w / 2) * width)
                    y = int((y - h / 2) * height)
                    w = int(w * width)
                    h = int(h * height)
                    damage_boxes.append([x, y, w, h])
        # 复制图片
        shutil.copy(path, os.path.join(output, os.path.basename(path)))
        with open(os.path.join(output, os.path.basename(path).replace(path[path.rfind("."):], ".txt")), 'w') as f:
            for cls, box, conf in zip(final_clss, final_boxes, final_confs):
                if is_inside(box, damage_boxes):
                    x1, y1, x2, y2 = box.astype(int)
                    # label = f"F {result.names[int(cls)]}: {conf:.2f}"
                    # cv2.rectangle(image, (x1, y1     ), (x2,       y2), (0, 255, 0),  2)
                    # cv2.rectangle(image, (x1, y1 - 20), (x1 + 160, y1), (0, 255, 0), -1)
                    # cv2.putText(image, label, (x1, y1 - 5), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 0, 0), 2)
                    f.write(f"1 {(x1 + x2) / 2 / width:.6f} {(y1 + y2) / 2 / height:.6f} {(x2 - x1) / width:.6f} {(y2 - y1) / height:.6f}\n")
                else:
                    x1, y1, x2, y2 = box.astype(int)
                    # label = f"T {result.names[int(cls)]}: {conf:.2f}"
                    # cv2.rectangle(image, (x1, y1     ), (x2,       y2), (0, 255, 0),  2)
                    # cv2.rectangle(image, (x1, y1 - 20), (x1 + 160, y1), (0, 255, 0), -1)
                    # cv2.putText(image, label, (x1, y1 - 5), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 0, 0), 2)
                    f.write(f"0 {(x1 + x2) / 2 / width:.6f} {(y1 + y2) / 2 / height:.6f} {(x2 - x1) / width:.6f} {(y2 - y1) / height:.6f}\n")
        # cv2.imshow("image", image)
        # cv2.waitKey(0)

def is_inside(box: list, damage_boxes: list):
    x1, y1, x2, y2 = box
    for damage_box in damage_boxes:
        x, y, w, h = damage_box
        if (
            x1 < x and x < x2 and
            y1 < y and y < y2 and
            x1 < x + w and x + w < x2 and
            y1 < y + h and y + h < y2
        ):
            return True
    return False

def mark_text(path: str, output: str):
    shutil.copy(path, os.path.join(output, os.path.basename(path)))
    with (
        open(path.replace(path[path.rfind("."):], ".txt"), 'r') as r,
        open(os.path.join(output, os.path.basename(path).replace(path[path.rfind("."):], ".txt")), 'w') as w
    ):
        lines = r.readlines()
        for line in lines:
            line = line.strip()
            line = "1" + line[1:]
            w.write(line + "\n")

model  = YOLO("./best.pt")
folder = "./dataset/damage"
# folder = "./dataset/normal"
# folder = "./dataset/damage_yolo"
# output = "./dataset/normal_cls"
# output = "./dataset/damage_cls"
# output = "./dataset/damage_yolo"
output = "./dataset/damage_1"
for filename in os.listdir(folder):
    if not filename.endswith(".txt"):
        image_path = os.path.join(folder, filename)
        # mark_txt(image_path, output)
        # mark_yolo(image_path, output, model)
        mark_text(image_path, output)

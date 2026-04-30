import os
import cv2

from ultralytics import YOLO

def mark_yolo(path: str, model: YOLO, writer: cv2.VideoWriter):
    # 默认置信度 = 0.25 IOU = 0.70
    # results = model(path, conf = 0.25, iou = 0.70)
    results = model(path, conf = 0.40, iou = 0.50)
    for result in results:
        # result.show()
        clss  = result.boxes.cls.cpu().numpy()
        boxes = result.boxes.xyxy.cpu().numpy()
        confs = result.boxes.conf.cpu().numpy()
        if len(boxes) == 0:
            print("未检测到目标")
            return
        # 如果需要topK使用下面数据进行计算
        # result.boxes.data
        # 配置
        # conf_threshold = 0.4
        # iou_threshold  = 0.2
        # 过滤低置信度
        # conf_indices = confs > conf_threshold
        # clss  = clss[conf_indices]
        # boxes = boxes[conf_indices]
        # confs = confs[conf_indices]
        # if len(boxes) == 0:
        #     print("未检测到目标")
        #     return
        # NMS CV2
        # boxes_nms = []
        # for box in boxes:
        #     x1, y1, x2, y2 = box
        #     w = x2 - x1
        #     h = y2 - y1
        #     boxes_nms.append([x1, y1, w, h])
        # nms_indices = cv2.dnn.NMSBoxes(boxes_nms, confs, conf_threshold, iou_threshold)
        # nms_indices = cv2.dnn.NMSBoxesBatched(boxes_nms, confs, clss.astype(int), conf_threshold, iou_threshold, top_k = 2)
        # final_clss  = clss[nms_indices]
        # final_boxes = boxes[nms_indices]
        # final_confs = confs[nms_indices]
        # NMS Torch
        # boxes_tensor  = torch.from_numpy(boxes)
        # scores_tensor = torch.from_numpy(confs)
        # nms_indices = torchvision.ops.nms(boxes_tensor, scores_tensor, iou_threshold)
        # final_clss  = clss[nms_indices.cpu().numpy()]
        # final_boxes = boxes[nms_indices.cpu().numpy()]
        # final_confs = confs[nms_indices.cpu().numpy()]
        # NMS YOLO
        final_clss  = clss[:]
        final_boxes = boxes[:]
        final_confs = confs[:]
        # 绘制结果
        if len(final_boxes) == 0:
            print("未检测到目标")
            return
        image = cv2.imread(path)
        for cls, box, conf in zip(final_clss, final_boxes, final_confs):
            x1, y1, x2, y2 = box.astype(int)
            label = f"{result.names[int(cls)]}: {conf:.2f}"
            cv2.rectangle(image, (x1, y1     ), (x2,       y2), (0, 255, 0),  2)
            cv2.rectangle(image, (x1, y1 - 20), (x1 + 220, y1), (0, 255, 0), -1)
            cv2.putText(image, label, (x1, y1 - 5), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 0, 0), 2)
        cv2.imshow("image", image)
        cv2.waitKey(0)
        # writer.write(cv2.resize(image, (640, 640)))

model  = YOLO("./best_box.pt")
folder = "./dataset/val_box"
# writer = cv2.VideoWriter("./val_box.mp4", cv2.VideoWriter_fourcc(*"mp4v"), 1.0, (640, 640))
for filename in os.listdir(folder):
    if not filename.endswith(".txt"):
        image_path = os.path.join(folder, filename)
        mark_yolo(image_path, model, None)

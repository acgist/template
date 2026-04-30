import os
import cv2
import json
import pandas as pd

from utils import get_label_path

csv_file_path = 'D:/download/1train_rname.csv'
images_dir    = 'D:/download'
output_dir    = 'D:/download'

os.makedirs(output_dir, exist_ok = True)

# safebelt：佩戴安全带
# ground：着地状态的人
# offground：离地状态的人
# 监护袖章(红only)：监护袖章（只识别红色袖章）

# badge：监护袖章（只识别红色修章）
# person：图中出现的所有在场人员
# glove：绝缘手套（橡胶材质）
# wrongglove：未佩戴绝缘手套（其他手套或裸露手掌）
# operatingbar：操作杆
# powerchecker：验电笔

class_mapping = {
    # 'safebelt'       : 0, # 安全带
    # 'ground'         : 1, # 着地人员
    # 'offground'      : 2, # 离地人员
    # '监护袖章(红only)': 3, # 监护袖章（只识别红色袖章）

    'badge'       : 0, # 监护袖章（只识别红色修章）
    'person'      : 1, # 图中出现的所有在场人员
    'glove'       : 2, # 绝缘手套（橡胶材质）
    'wrongglove'  : 3, # 未佩戴绝缘手套（其他手套或裸露手掌）
    'operatingbar': 4, # 操作杆
    'powerchecker': 5, # 验电笔
}

df = pd.read_csv(csv_file_path, header = None)

def convert_to_yolo(x_min, y_min, w, h, img_width, img_height):
    x_center = (x_min + w / 2) / img_width
    y_center = (y_min + h / 2) / img_height
    norm_width  = w / img_width
    norm_height = h / img_height
    return x_center, y_center, norm_width, norm_height

# cv2.namedWindow("image", cv2.WINDOW_AUTOSIZE)

for index, row in df.iterrows():
    image_name = row[4]
    image_path = os.path.join(images_dir, image_name)
    img = cv2.imread(image_path)
    if img is None:
        print(f"无法读取图片: {image_path}")
        continue
    data = row[5]
    data = json.loads(data)
    txt_filename = get_label_path(image_name, ".txt")
    txt_path = os.path.join(output_dir, txt_filename)
    with open(txt_path, 'w') as f:
        img_height, img_width, _ = img.shape
        for item in data["items"]:
            geometry = item["meta"]["geometry"]
            category_name = item["labels"]["标签"]
            x_min, y_min, x_max, y_max = geometry
            x_min, y_min, x_max, y_max = int(x_min), int(y_min), int(x_max), int(y_max)
            # cv2.rectangle(img, (x_min, y_min     ), (x_max,       y_max), (0, 255, 0),  2)
            # cv2.rectangle(img, (x_min, y_min - 20), (x_min + 220, y_min), (0, 255, 0), -1)
            # cv2.putText(img, category_name, (x_min, y_min - 5), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 0, 0), 2)
            width  = x_max - x_min
            height = y_max - y_min
            x_center, y_center, w_norm, h_norm = convert_to_yolo(x_min, y_min, width, height, img_width, img_height)
            class_id = class_mapping.get(category_name)
            if class_id is None:
                print(f"未知类别: {category_name}")
                continue
            yolo_line = f"{class_id} {x_center:.6f} {y_center:.6f} {w_norm:.6f} {h_norm:.6f}\n"
            f.write(yolo_line)
        # max_width, max_height = 1280, 1280
        # if img_width > max_width or img_height > max_height:
        #     scale = min(max_width / img_width, max_height / img_height)
        #     new_w, new_h = int(img_width * scale), int(img_height * scale)
        #     img = cv2.resize(img, (new_w, new_h))
        # cv2.imshow("image", img)
        # cv2.waitKey(0)
print("转换完成")

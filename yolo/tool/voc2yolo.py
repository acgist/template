import os
import cv2
import xml.etree.ElementTree as ET

from utils import get_image_path

input_dir  = 'D:/download/aqd2000/images'
output_dir = 'D:/download/aqd2000/yolo'
classes    = [
    'aqd_wpd',   # 未佩戴
    'aqd_gfsy',  # 规范使用
    'aqd_dggy',  # 低挂高用
    'aqd_wzqpd', # 未正确佩戴
]

def voc_to_yolo(xml_file, txt_file, classes):
    tree = ET.parse(xml_file)
    root = tree.getroot()
    size = root.find('size')
    img_width  = int(size.find('width').text)
    img_height = int(size.find('height').text)
    if img_width == 0 or img_height == 0:
        try:
            img_path = get_image_path(xml_file, ".xml")
            image = cv2.imread(img_path)
            img_width, img_height, _ = image.shape
        except:
            pass
    if img_width == 0 or img_height == 0:
        print(f"警告: 图片尺寸错误{xml_file}")
        return
    with open(txt_file, 'w') as f:
        for obj in root.iter('object'):
            cls_name = obj.find('name').text
            if cls_name not in classes:
                print(f"警告: 类未知{cls_name}跳过")
                continue
            cls_id = classes.index(cls_name)
            bndbox = obj.find('bndbox')
            xmin = float(bndbox.find('xmin').text)
            ymin = float(bndbox.find('ymin').text)
            xmax = float(bndbox.find('xmax').text)
            ymax = float(bndbox.find('ymax').text)
            x_center = (xmin + xmax) / 2.0 / img_width
            y_center = (ymin + ymax) / 2.0 / img_height
            w = (xmax - xmin) / img_width
            h = (ymax - ymin) / img_height
            f.write(f"{cls_id} {x_center:.6f} {y_center:.6f} {w:.6f} {h:.6f}\n")

if not os.path.exists(output_dir):
    os.makedirs(output_dir)

for filename in os.listdir(input_dir):
    if filename.endswith('.xml'):
        xml_path = os.path.join(input_dir, filename)
        txt_filename = filename.replace('.xml', '.txt')
        txt_path = os.path.join(output_dir, txt_filename)
        # print(f"转换：{filename}")
        voc_to_yolo(xml_path, txt_path, classes)
print("批量转换完成")

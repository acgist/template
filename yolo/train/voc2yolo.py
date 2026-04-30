import os
import xml.etree.ElementTree as ET

input_dir  = './damage'
output_dir = './yolo'
classes    = ['insulator']

def convert_voc_to_yolo(xml_file, txt_file, classes):
    tree = ET.parse(xml_file)
    root = tree.getroot()
    size = root.find('size')
    img_width  = int(size.find('width').text)
    img_height = int(size.find('height').text)
    with open(txt_file, 'w') as f:
        for obj in root.iter('object'):
            cls_name = obj.find('name').text
            if cls_name not in classes:
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
        convert_voc_to_yolo(xml_path, txt_path, classes)
        print(f"转换：{filename}")
print("批量转换完成！")

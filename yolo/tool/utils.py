import os

def get_image_path(label_file: str, label_suffix: str):
    image_path = None
    if os.path.exists(label_file.replace(label_suffix, '.jpg')):
        image_path = label_file.replace(label_suffix, '.jpg')
    elif os.path.exists(label_file.replace(label_suffix, '.png')):
        image_path = label_file.replace(label_suffix, '.png')
    elif os.path.exists(label_file.replace(label_suffix, '.jpeg')):
        image_path = label_file.replace(label_suffix, '.jpeg')
    elif os.path.exists(label_file.replace(label_suffix, '.JPG')):
        image_path = label_file.replace(label_suffix, '.JPG')
    elif os.path.exists(label_file.replace(label_suffix, '.PNG')):
        image_path = label_file.replace(label_suffix, '.PNG')
    elif os.path.exists(label_file.replace(label_suffix, '.JPEG')):
        image_path = label_file.replace(label_suffix, '.JPEG')
    return image_path

def get_label_path(image_path: str, label_suffix: str):
    return os.path.splitext(image_path)[0] + label_suffix

# print(get_label_path("D:/download/1_images/1.jpg", ".txt"))

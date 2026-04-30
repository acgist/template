import os

from PIL import Image, ImageOps

input_dir = "D:/download/1_images"

count = 0
total = 0

for filename in os.listdir(input_dir):
    if not filename.endswith('.txt') and not filename.endswith('.zip'):
        total += 1
        try:
            with Image.open(os.path.join(input_dir, filename)) as img:
                exif = img.getexif()
                if exif is None or len(exif) == 0:
                    continue
                ori_width, ori_height = img.size
                img = ImageOps.exif_transpose(img)
                img_width, img_height = img.size
                if ori_width != img_width or ori_height != img_height:
                    count += 1
                    print(f"{count} / {total}", filename, ori_width, ori_height, img_width, img_height)
                    img.save(os.path.join(input_dir, filename), exif=b"")
        except:
            print(f"处理{filename}失败")
print(f"共{total}张图片，{count}张尺寸不一致")

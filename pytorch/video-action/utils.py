import cv2
import torch
import numpy as np

# 采样帧数
frame_size = 16
# 通道数量
frame_channel = 3
# 图片高宽
image_h = 224
image_w = 224
# image_h = 256
# image_w = 256

def scale(frame, mat, zoom):
    nh, nw = mat.shape[:2]
    oh, ow = frame.shape[:2]
    new_w = int(ow * zoom)
    new_h = int(oh * zoom)
    x = (nw - new_w) // 2
    y = (nh - new_h) // 2
    interpolation = cv2.INTER_AREA if zoom < 1.0 else cv2.INTER_LINEAR
    resized_frame = cv2.resize(frame, (new_w, new_h), interpolation=interpolation)
    mat_y1, mat_y2 = max(0, y), min(nh, y + new_h)
    mat_x1, mat_x2 = max(0, x), min(nw, x + new_w)
    frame_y1, frame_y2 = max(0, -y), min(new_h, nh - y)
    frame_x1, frame_x2 = max(0, -x), min(new_w, nw - x)
    mat[mat_y1:mat_y2, mat_x1:mat_x2] = resized_frame[frame_y1:frame_y2, frame_x1:frame_x2]
    return mat

def cutout(image, num_holes = 8, max_h_size = 64, max_w_size = 64, fill_value = 0):
    h, w, c  = image.shape
    img_copy = image.copy()
    for _ in range(num_holes):
        hole_h = np.random.randint(1, max_h_size + 1)
        hole_w = np.random.randint(1, max_w_size + 1)
        y = np.random.randint(0, h - hole_h)
        x = np.random.randint(0, w - hole_w)
        if fill_value == -1:
            img_copy[y:y+hole_h, x:x+hole_w] = np.random.randint(0, 256, (hole_h, hole_w, c), dtype=np.uint8)
        else:
            img_copy[y:y+hole_h, x:x+hole_w] = fill_value
    return img_copy

"""
frame_pos  偏移位置
frame_skip 间隔帧数
"""
def loadVideo(file, frame_pos = 0, frame_skip = 16, flip = False, zoom = 1.0, rotate = 0, holes = 0):
    index    = 0
    capture  = cv2.VideoCapture(file)
    frames   = int(capture.get(cv2.CAP_PROP_FRAME_COUNT))
    features = []
    if frames <= 0:
        return features
    while True:
        index += 1
        ret, frame = capture.read()
        if not ret:
            break
        if frame_pos == 0:
            frame_pos = 0
        else:
            frame_pos = frames - (frame_size + 1) * frame_skip
        if index < frame_pos:
            continue
        if index % frame_skip != 0:
            continue
        h = frame.shape[0]
        w = frame.shape[1]
        m = max(h, w)
        mat = np.zeros((m, m, 3), dtype = np.uint8)
        # 图片增广
        # 翻转
        if flip:
            frame = cv2.flip(frame, 1)
        # 旋转
        if rotate != 0:
            center = (w // 2, h // 2)
            M      = cv2.getRotationMatrix2D(center, angle = rotate, scale = 1.0)
            frame  = cv2.warpAffine(frame, M, (w, h))
        # 缩放
        mat = scale(frame, mat, zoom)
        # 挖空
        mat = cutout(mat, holes)
        # 重置大小
        mat = cv2.resize(mat, (image_h, image_w))
        # cv2.imshow("mat",   mat  )
        # cv2.imshow("frame", frame)
        # cv2.waitKey()
        rgb = cv2.cvtColor(mat, cv2.COLOR_BGR2RGB)
        # 处理数据
        feature = torch.tensor(rgb).float() / 255.0 * 2.0 - 1.0
        feature = feature.permute(2, 0, 1)
        if len(features) < frame_size:
            features.append(feature)
        if len(features) >= frame_size:
            break
    capture.release()
    if len(features) < frame_size:
        for _ in range(frame_size - len(features)):
            features.append(features[-1].clone())
    return features

# print(len(loadVideo("D:/tmp/video.mp4", 0, 8, True, 1.0, 0, 8)))
# print(len(loadVideo("D:/download/aliang.mp4")))

import cv2
import json
import torch

import numpy as np

from player import VideoPlayerWithTimeline

# JIT
model = torch.jit.load("model.pt")
if torch.cuda.is_available():
    model.cuda()
else:
    model.cpu()
model.eval()

# 原始模型
# model = torch.load("model.pth", weights_only = False)
# if torch.cuda.is_available():
#     model.cuda()
# else:
#     model.cpu()
# model.eval()

mapping = [ "eat", "sit", "situp", "smile", "smoke" ]

index = 0
last_ret = None
json_ret = {}
features = []
cap = cv2.VideoCapture("video.mp4")
fps = int(cap.get(cv2.CAP_PROP_FPS))
with torch.no_grad():
    while True:
        ret, frame = cap.read()
        if not ret:
            break
        index += 1
        if index % 8 != 0:
            continue
        h = frame.shape[0]
        w = frame.shape[1]
        m = max(h, w)
        mat = np.zeros((m, m, 3), dtype = np.uint8)
        mat[0:h, 0:w] = frame
        mat = cv2.resize(mat, (224, 224))
        # cv2.imshow("mat", mat)
        # cv2.waitKey(200)
        rgb = cv2.cvtColor(mat, cv2.COLOR_BGR2RGB)
        feature = torch.tensor(rgb).float() / 255.0 * 2.0 - 1.0
        feature = feature.permute(2, 0, 1)
        features.append(feature)
        if len(features) == 16:
            input = torch.stack(features, dim = 0).unsqueeze(0)
            if torch.cuda.is_available():
                input = input.cuda()
            ret = model(input)
            # print(torch.argmax(ret, 1))
            # print(torch.softmax(ret, 1))
            probs = torch.softmax(ret, 1)
            top2_probs, top2_indices = torch.topk(probs, 2)
            features = features[1:]
            ret_int = torch.argmax(ret, 1).item()
            print(f"index: {index} ret: {probs.tolist()}")
            if last_ret == ret_int:
                continue
            json_ret[round((index - 8 * 16) * 1.0 / fps, 2)] = (
                *top2_probs.tolist(),
                list(map(lambda x: mapping[x], *top2_indices.tolist())),
            )
            last_ret = ret_int
print(json.dumps(json_ret, ensure_ascii = False))

# 视频处理

video_file = "video.mp4" 
player = VideoPlayerWithTimeline(video_file, json_ret, False)
player.run()
print("完成")

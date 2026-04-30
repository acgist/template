import torch

from utils import loadVideo

# features = loadVideo("/data/video-action/video.mp4")
features = torch.cat(loadVideo("D:/download/k400/drinking_beer/OmMYqI_YjBo_000236_000246.mp4"))

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

ret = model(features.unsqueeze(0))

print(ret)
print(torch.argmax(ret, 1))
print(torch.softmax(ret, 1))

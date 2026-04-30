"""
https://docs.pytorch.org/docs/stable/onnx_torchscript.html#torch.onnx.export
"""

import torch

from utils import image_h, image_w, frame_size, frame_channel

model = torch.load("model.pth", weights_only = False)
model.cpu()
model.eval()

torch.onnx.export(
    model,
    (torch.rand(1, frame_size, frame_channel, image_h, image_w)),
    "model.onnx",
    dynamo         = True,
    opset_version  = 18,
    input_names  = [ "vid" ],
    output_names = [ "cls" ],
)

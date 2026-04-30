from ultralytics import YOLO

model = YOLO("yolo11n-cls.pt")

results = model.train(
    data    = "./dataset",
    imgsz   = 224,
    epochs  = 128,
    batch   = 64,
    workers = 16,
    scale   = 0.2,
    shear   = 5.0,
    flipud  = 0.5,
    fliplr  = 0.5,
    mosaic  = 0.0,
    erasing = 0.0,
    degrees = 90.0,
)
model.export(format="onnx")

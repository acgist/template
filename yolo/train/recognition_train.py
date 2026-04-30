from ultralytics import YOLO

model = YOLO('yolo11n.pt')
model.train(
    single_cls = True,
    data       = './dataset.yaml',
    imgsz      = 640,
    epochs     = 128,
    batch      = 64,
    workers    = 16,
    scale      = 0.5,
    shear      = 5.0,
    flipud     = 0.5,
    fliplr     = 0.5,
    mosaic     = 1.0,
    erasing    = 0.2,
    degrees    = 90.0,
)
model.export(format = "onnx")

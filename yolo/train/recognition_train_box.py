from ultralytics import YOLO

model = YOLO('yolo11n.pt')
# model = YOLO('yolo11s.pt')
model.train(
    single_cls = False,
    data       = './dataset_box.yaml',
    imgsz      = 640,
    epochs     = 256,
    batch      = 64,
    workers    = 16,
    scale      = 0.2,
    shear      = 5.0,
    flipud     = 0.5,
    fliplr     = 0.5,
    mosaic     = 0.5,
    erasing    = 0.0, # 不要擦除：避免擦除损坏位置
    degrees    = 90.0,
)
model.export(format = "onnx")

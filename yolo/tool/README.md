# YOLO

## 处理顺序

**注意：每步操作前尽量备份数据避免数据丢失**

1. 删除旋转`rm_exif.py`
2. 标签转换`csv2yolo.py`或者`voc2yolo.py`
3. 自动标记`yolo_mark.py`
4. 标签映射`label_mapping.py`（提前准备标签映射）
5. 重新命名`rename.py`
6. 上传数据

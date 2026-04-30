import os
import sys

filename = sys.argv[1]
if not os.path.exists(filename):
    print(f"文件无效{filename}")
    sys.exit(0)
print(f"开始排序{filename}")
# 读取
with open(filename, "r", encoding="utf-8") as f:
    lines = f.readlines()
# 清洗
lines = [line.strip() for line in lines]
# 分片
blocks = []
current_block = []
for line in lines:
    if line == "":
        if current_block:
            blocks += sorted(list(dict.fromkeys(current_block)))
            blocks.append("")
            current_block = []
    else:
        current_block.append(line)
if current_block:
    blocks += sorted(list(dict.fromkeys(current_block)))
    current_block = []
# 保存
with open(f"sorted.{filename}", "w", encoding="utf-8") as f:
    f.write("\n".join(blocks))
print(f"排序完成 {len(lines)} -> {len(blocks)}")

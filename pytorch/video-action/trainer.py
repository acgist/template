import os
import torch
import random
import torch.nn as nn

from tqdm  import tqdm
from utils import loadVideo, image_h, image_w, frame_size, frame_channel
# from model_van import VAN
# from model_vit import VAN
from model_resnet import VAN
# from model_slowfast import VAN

print("""

众鸟高飞尽，孤云独去闲。
相看两不厌，只有敬亭山。

""")

num_epochs  = 128 # 训练总的轮次
batch_size  =  32 # 训练批次数量
train_ratio = .80 # 训练集总占比

label_mapping = {
    # "smoking"    : 0,
    # "tickling"   : 1,
    # "driving_car": 2,
    # "pushing_car": 3,

    # "smoking"      : 0,
    # "tickling"     : 1,
    # "eating_cake"  : 2,
    # "eating_burger": 3,
    # "eating_hotdog": 4,
    # "drinking_beer": 5,

    "eat"  : 0,
    "sit"  : 1,
    "situp": 2,
    "smile": 3,
    "smoke": 4,
}

label_size = len(label_mapping)

class VANDataset(torch.utils.data.Dataset):
    def __init__(self, features, labels):
        self.features = features
        self.labels   = labels

    def __len__(self):
        return len(self.features)

    def __getitem__(self, index):
        feature = self.features[index]
        label   = self.labels  [index]
        return feature, label

def loadDataset(path):
    files = []
    for type_name in os.listdir(path):
        if label_mapping.get(type_name) == None:
            continue
        type_file = os.path.join(path, type_name)
        if os.path.isfile(type_file):
            continue
        for video_name in os.listdir(type_file):
            video_file = os.path.join(path, type_name, video_name)
            if not os.path.isfile(video_file):
                continue
            files.append((video_file, video_name, type_name))
    random.shuffle(files)
    index   = 0
    length  = len(files)
    process = tqdm(files)
    features_train = []
    labels_train   = []
    features_val   = []
    labels_val     = []
    for video_file, video_name, type_name in process:
        process.set_postfix(file = "{}".format(video_name[-16:]))
        index += 1
        for i in range(2):
            frames = None
            if i == 0:
                frames = loadVideo(video_file, frame_pos = 0, flip = random.choice((True, False)), zoom = 1.0, rotate = 0, holes = 0)
            else:
                frames = loadVideo(video_file, frame_pos = 1, flip = random.choice((True, False)), zoom = random.choice((0.6, 1.2)), rotate = random.choice((15, -15)), holes = 8)
            if len(frames) != frame_size:
                continue
            feature = torch.stack(frames)
            label   = label_mapping[type_name]
            if index >= int(train_ratio * length):
                features_val.append(feature)
                labels_val  .append(label)
            else:
                features_train.append(feature)
                labels_train  .append(label)
    return VANDataset(features_train, labels_train), VANDataset(features_val, labels_val)

# train_dataset, val_dataset = loadDataset("D:/tmp/video-action")
# train_dataset, val_dataset = loadDataset("/data/video-action/k400")
train_dataset, val_dataset = loadDataset("/data/video-action/hmdb51")

train_loader = torch.utils.data.DataLoader(train_dataset, batch_size = batch_size, shuffle = True, num_workers = 0)
val_loader   = torch.utils.data.DataLoader(val_dataset,   batch_size = batch_size, shuffle = True, num_workers = 0)

print(f"train_dataset: {len(train_dataset)}")
print(f"val_dataset:   {len(val_dataset)}")

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

model = VAN(label_size)
model.to(device)

criterion = nn.CrossEntropyLoss()
optimizer = torch.optim.AdamW(model.parameters(), lr = 0.0003) # eps = 1e-8, weight_decay = 1e-2
scheduler = torch.optim.lr_scheduler.StepLR(optimizer, step_size = 10, gamma = 0.99)

for epoch in range(num_epochs):
    loss_sum   = 0.0
    loss_count = 0
    accu_sum   = 0
    accu_count = 0
    # 训练集
    model.train()
    process = tqdm(train_loader, total = len(train_loader), desc = f"Epoch [{epoch + 1} / { num_epochs }]")
    for features, labels in process:
        features = features.to(device)
        labels   = labels  .to(device)
        optimizer.zero_grad()
        pred = model(features)
        loss = criterion(pred, labels)
        loss.backward()
        optimizer.step()
        with torch.no_grad():
            loss_sum   += loss.item()
            loss_count += 1
            true_idx = labels
            pred_idx = torch.argmax(pred, 1)
            accu_sum   += (pred_idx == true_idx).sum().item()
            accu_count += labels.size(0)
            process.set_postfix(loss = "{:.2f}".format(loss_sum / loss_count), accu = "{:.2f}%".format(100 * accu_sum / accu_count))
    scheduler.step()
    # 验证集
    model.eval()
    with torch.no_grad():
        count      = 0
        accu_sum   = 0
        accu_count = 0
        # 混淆矩阵
        confusion_matrix = torch.zeros(label_size, label_size)
        for features, labels in val_loader:
            features = features.to(device)
            labels   = labels  .to(device)
            pred   = model(features)
            count += labels.size(0)
            # 忽略概率
            true_idx = labels
            pred_idx = torch.argmax(pred, 1)
            accu_sum   += (pred_idx == true_idx).sum().item()
            accu_count += true_idx.size(0)
            for i, j in zip(pred_idx, true_idx):
                confusion_matrix[i, j] += 1
            # 计算概率
            # true_idx = labels
            # pred_prob, pred_idx = torch.max(torch.softmax(pred, dim = 1), dim = 1)
            # pred_prob_mask = pred_prob > 0.8
            # filtered_true_idx = true_idx[pred_prob_mask]
            # filtered_pred_idx = pred_idx[pred_prob_mask]
            # if len(filtered_pred_idx) > 0:
            #     accu_sum   += (filtered_pred_idx == filtered_true_idx).sum().item()
            #     accu_count += filtered_pred_idx.size(0)
            #     for i, j in zip(filtered_pred_idx, filtered_true_idx):
            #         confusion_matrix[i, j] += 1
        print(
            "正确率 = {} / {} = {:.2f}% | 识别率 = {} / {} = {:.2f}%".format(
                accu_sum, accu_count, 0 if accu_count == 0 else 100 * accu_sum / accu_count,
                accu_count, count, 100 * accu_count / count
            )
        )
        print(confusion_matrix)

model.eval()
model = model.cpu()
torch.save(model, "model.pth")
model = torch.jit.trace(model, torch.rand(1, frame_size, frame_channel, image_h, image_w))
model.save("model.pt")

print("""

贵逼人来不自由，龙骧凤翥势难收。
满堂花醉三千客，一剑霜寒十四州。
鼓角揭天嘉气冷，风涛动地海山秋。
东南永作金天柱，谁羡当时万户侯。

""")

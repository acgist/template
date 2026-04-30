import torch
import torch.nn as nn
from torchvision import models

from utils import image_h, image_w, frame_size, frame_channel

class FFN(nn.Module):
    def __init__(self, embed_dim: int):
        super().__init__()
        self.norm = nn.LayerNorm(embed_dim)
        self.ffn  = nn.Sequential(
            nn.Linear(embed_dim, embed_dim * 2),
            nn.SiLU(),
            nn.Linear(embed_dim * 2, embed_dim),
        )
    def forward(self, input):
        return self.norm(input + self.ffn(input))

class MHA(nn.Module):
    def __init__(self, q_dim, k_dim, v_dim, o_dim, h_dim = 256, num_heads = 4):
        super().__init__()
        self.q    = nn.Linear(q_dim, h_dim, bias = False)
        self.k    = nn.Linear(k_dim, h_dim, bias = False)
        self.v    = nn.Linear(v_dim, h_dim, bias = False)
        self.attn = nn.MultiheadAttention(h_dim, num_heads, bias = False, batch_first = False, dropout = 0.1)
        self.proj = nn.Linear(h_dim, o_dim, bias = False)
        self.norm = nn.LayerNorm(o_dim)
        self.ffn  = FFN(o_dim)
    def forward(self, query, key, value):
        q = self.q(query).transpose(0, 1)
        k = self.k(key  ).transpose(0, 1)
        v = self.v(value).transpose(0, 1)
        o, _ = self.attn(q, k, v)
        o = query + self.proj(o).transpose(0, 1)
        o = self.norm(o)
        return self.ffn(o)

class VAN(nn.Module):
    def __init__(self, out: int):
        super().__init__()
        vit_feature_dim = 768
        embed_dim = 512
        
        self.vit = models.vit_b_16(weights = models.ViT_B_16_Weights.IMAGENET1K_V1)
        for param in self.vit.parameters():
            param.requires_grad = False
        self.backbone = self.vit.encoder
        self.patch_embed_conv = self.vit.conv_proj 
        self.class_token = self.vit.class_token

        self.dim_reduction = nn.Linear(vit_feature_dim, embed_dim)
        self.mha = MHA(embed_dim, embed_dim, embed_dim, embed_dim, embed_dim, 8)
        self.embed_dim = embed_dim
        self.frame_embed = nn.Parameter(torch.zeros(1, frame_size, embed_dim))
        self.norm = nn.LayerNorm(embed_dim)
        self.pool = nn.AdaptiveAvgPool2d((1, 1))
        self.out = nn.Sequential(
            nn.Conv1d(embed_dim, 16, kernel_size = 3, padding = 1),
            nn.SiLU(),
            nn.Flatten(1),
            nn.Linear(frame_size * 16, 256),
            nn.SiLU(),
            nn.Dropout(0.1),
            nn.Linear(256, out),
        )

    def forward(self, input: torch.Tensor) -> torch.Tensor:
        model = torch.hub.load('facebookresearch/pytorchvideo', 'slowfast_r50', pretrained = True)
        B, T, C, H, W = input.shape
        video_frames = input.view(-1, C, H, W)
        with torch.no_grad():
            x = self.patch_embed_conv(video_frames)
            x = x.flatten(2).transpose(1, 2)
            cls_tokens = self.class_token.expand(x.shape[0], -1, -1)
            x = torch.cat((cls_tokens, x), dim = 1)
            features = self.backbone(x)
        features = self.dim_reduction(features)
        features = features.mean(dim = 1)
        features = features.view(B, T, self.embed_dim)
        features = features + self.frame_embed
        features = self.norm(features)
        out = self.mha(features, features, features)
        # (B, T, D) -> (B, D, T)
        out = out.transpose(1, 2)
        out = self.out(out)
        return out
    
model = VAN(2)
model.eval()
model = model.cpu()
torch.save(model, "model.pth")
model = torch.jit.trace(model, torch.rand(10, frame_size, frame_channel, image_h, image_w))
model.save("model.pt")

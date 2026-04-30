import torch
import torch.nn as nn

from typing import List

from utils import image_h, image_w, frame_size, frame_channel

class MHA(nn.Module):
    def __init__(
        self,
        q_dim    : int,
        k_dim    : int,
        v_dim    : int,
        o_dim    : int,
        h_dim    : int = 256,
        num_heads: int = 4,
    ):
        super().__init__()
        self.q    = nn.Linear(q_dim, h_dim, bias = False)
        self.k    = nn.Linear(k_dim, h_dim, bias = False)
        self.v    = nn.Linear(v_dim, h_dim, bias = False)
        self.attn = nn.MultiheadAttention(h_dim, num_heads, bias = False, batch_first = False, dropout = 0.1)
        self.proj = nn.Linear(h_dim, o_dim, bias = False)
        self.norm = nn.LayerNorm(o_dim)
        self.ffn  = FFN(o_dim)

    def forward(
        self,
        query: torch.Tensor,
        key  : torch.Tensor,
        value: torch.Tensor,
    ) -> torch.Tensor:
        q = self.q(query).transpose(0, 1)
        k = self.k(key  ).transpose(0, 1)
        v = self.v(value).transpose(0, 1)
        o, _ = self.attn(q, k, v)
        o = query + self.proj(o).transpose(0, 1)
        o = self.norm(o)
        return self.ffn(o)

class ViT(nn.Module):
    def __init__(
        self,
        i_channels: int,
        o_channels: int,
        kernel    : List[int],
    ):
        super().__init__()
        self.patch = nn.Sequential(
            nn.Conv2d(i_channels, o_channels, kernel, stride = kernel, bias = False),
            nn.BatchNorm2d(o_channels),
        )
        self.conv1 = nn.Sequential(
            nn.Conv2d(o_channels, o_channels, [3, 3], padding = [1, 1], bias = False),
            nn.BatchNorm2d(o_channels),
            nn.SiLU(),
            nn.Conv2d(o_channels, o_channels, [3, 3], padding = [1, 1], bias = False),
            nn.BatchNorm2d(o_channels),
        )
        self.conv_2l0 = nn.Sequential(
            nn.Conv2d(o_channels, o_channels * 2, [3, 3], padding = [1, 1], bias = False),
            nn.BatchNorm2d(o_channels * 2),
        )
        self.conv_2r0 = nn.Sequential(
            nn.Conv2d(o_channels, o_channels * 2, [3, 3], padding = [1, 1], bias = False),
            nn.BatchNorm2d(o_channels * 2),
            nn.SiLU(),
            nn.Conv2d(o_channels * 2, o_channels * 2, [3, 3], padding = [1, 1], bias = False),
            nn.BatchNorm2d(o_channels * 2),
        )
        self.conv_2r1 = nn.Sequential(
            nn.Conv2d(o_channels * 2, o_channels * 2, [3, 3], padding = [1, 1], bias = False),
            nn.BatchNorm2d(o_channels * 2),
            nn.SiLU(),
            nn.Conv2d(o_channels * 2, o_channels * 2, [3, 3], padding = [1, 1], bias = False),
            nn.BatchNorm2d(o_channels * 2),
        )
        self.conv_3l0 = nn.Sequential(
            nn.Conv2d(o_channels * 2, o_channels * 4, [3, 3], padding = [1, 1], bias = False),
            nn.BatchNorm2d(o_channels * 4),
        )
        self.conv_3r0 = nn.Sequential(
            nn.Conv2d(o_channels * 2, o_channels * 4, [3, 3], padding = [1, 1], bias = False),
            nn.BatchNorm2d(o_channels * 4),
            nn.SiLU(),
            nn.Conv2d(o_channels * 4, o_channels * 4, [3, 3], padding = [1, 1], bias = False),
            nn.BatchNorm2d(o_channels * 4),
        )
        self.conv_3r1 = nn.Sequential(
            nn.Conv2d(o_channels * 4, o_channels * 4, [3, 3], padding = [1, 1], bias = False),
            nn.BatchNorm2d(o_channels * 4),
            nn.SiLU(),
            nn.Conv2d(o_channels * 4, o_channels * 4, [3, 3], padding = [1, 1], bias = False),
            nn.BatchNorm2d(o_channels * 4),
        )
        self.pool = nn.AdaptiveAvgPool2d(2)

    def forward(
        self,
        input: torch.Tensor,
    ) -> torch.Tensor:
        out = self.patch(input)
        out = torch.relu(self.conv1(out) + out)
        out = torch.relu(self.conv_2l0(out) + self.conv_2r0(out))
        out = torch.relu(self.conv_2r1(out) + out)
        out = torch.relu(self.conv_3l0(out) + self.conv_3r0(out))
        out = torch.relu(self.conv_3r1(out) + out)
        return self.pool(out)
    
class FFN(nn.Module):
    def __init__(
        self,
        embed_dim: int,
    ):
        super().__init__()
        self.norm = nn.LayerNorm(embed_dim)
        self.ffn  = nn.Sequential(
            nn.Linear(embed_dim, embed_dim * 2),
            nn.SiLU(),
            nn.Linear(embed_dim * 2, embed_dim),
        )

    def forward(
        self,
        input: torch.Tensor,
    ) -> torch.Tensor:
        return self.norm(input + self.ffn(input))

class VAN(nn.Module):
    def __init__(
        self,
        out: int,
    ):
        super().__init__()
        kernel   = [ 16, 16 ]
        self.vit = ViT(frame_channel, 128, kernel)
        self.linear = nn.Sequential(
            nn.Linear(512 * 4, 512),
            nn.SiLU(),
        )
        self.embed = nn.Parameter(torch.zeros(1, frame_size, 512))
        self.norm  = nn.LayerNorm(512)
        self.mha = MHA(512, 512, 512, 512, 512, 8)
        self.out = nn.Sequential(
            nn.Conv1d(512, 128, kernel_size = 3, padding = 1),
            nn.SiLU(),
            nn.Flatten(1),
            nn.Linear(128 * frame_size, 256),
            nn.SiLU(),
            nn.Dropout(0.1),
            nn.Linear(256, out),
        )

    def forward(
        self,
        input: torch.Tensor,
    ) -> torch.Tensor:
        B, T, C, H, W = input.shape
        video = self.vit(input.view(-1, C, H, W))
        # video = video.mean([2, 3])
        video = video.flatten(1)
        video = self.linear(video)
        video = video.view(B, T, -1)
        video = video + self.embed
        video = self.norm(video)
        out   = self.mha(video, video, video)
        out   = out.transpose(1, 2)
        out   = self.out(out)
        return out

# model = ViT(image_h, image_w, 3, 128, [16, 16])
# output = model(torch.rand(1, 3, image_h, image_w))
# print(output.shape)
# model = torch.jit.trace(model, torch.rand(1, 3, image_h, image_w))
# model.save("model.pt")

# model = VAN(2)
# model.eval()
# model = model.cpu()
# torch.save(model, "model.pth")
# model = torch.jit.trace(model, torch.rand(10, frame_size, frame_channel, image_h, image_w))
# model.save("model.pt")
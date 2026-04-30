/**
 * Copyright(c) 2024-present acgist. All Rights Reserved.
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * gitee : https://gitee.com/acgist/chobits
 * github: https://github.com/acgist/chobits
 * 
 * 神经网络
 * 
 * @author acgist
 * 
 * @version 1.0.0
 */
#ifndef CHOBITS_NN_HPP
#define CHOBITS_NN_HPP

#include "model.hpp"
#include "chobits.hpp"

#include "torch/nn.h"

using shape_t = std::vector<int64_t>;

namespace chobits::nn {

static void init_weight(std::shared_ptr<torch::nn::Module> layer);

class ActivationImpl : public torch::nn::Module {

public:
    torch::Tensor forward(
        const torch::Tensor& input
    ) {
//      return torch::relu(input);
        return torch::silu(input);
//      return torch::leaky_relu(input);
    }

};

TORCH_MODULE(Activation);

class PatchImpl : public torch::nn::Module {

private:
    torch::Tensor         embed{ nullptr };
    torch::nn::LayerNorm  norm { nullptr };
    torch::nn::Sequential conv { nullptr };

public:
    PatchImpl(
        const int64_t h,
        const int64_t w,
        const int64_t i_channels,
        const int64_t o_channels,
        const shape_t kernel_p,
        const shape_t stride_p,
        const shape_t padding_p  = std::vector<int64_t>{ 0, 0 },
        const shape_t dilation_p = std::vector<int64_t>{ 1, 1 },
        const shape_t kernel_h   = std::vector<int64_t>{ 3, 3 },
        const shape_t stride_h   = std::vector<int64_t>{ 1, 1 },
        const shape_t padding_h  = std::vector<int64_t>{ 1, 1 },
        const shape_t dilation_h = std::vector<int64_t>{ 1, 1 }
    ) {
        const int64_t seq_len = (h / stride_p[0]) * (w / stride_p[1]);
        this->embed = this->register_parameter("embed", torch::zeros({ 1, seq_len, o_channels }));
        this->norm  = this->register_module("norm",     torch::nn::LayerNorm(torch::nn::LayerNormOptions(std::vector<int64_t>{ o_channels })));
        this->conv  = this->register_module("conv",     torch::nn::Sequential(
            torch::nn::Conv2d(torch::nn::Conv2dOptions(i_channels, o_channels, kernel_p).padding(padding_p).dilation(dilation_p).stride(stride_p)),
            chobits::nn::Activation(),
            torch::nn::Conv2d(torch::nn::Conv2dOptions(o_channels, o_channels, kernel_h).padding(padding_h).dilation(dilation_h).stride(stride_h))
        ));
    }

public:
    torch::Tensor forward(
        const torch::Tensor& input
    ) {
        auto out = this->conv->forward(input).flatten(2).transpose(1, 2);
             out = out + this->embed;
        return this->norm->forward(out);
    }

    void init() {
        init_weight(this->conv.ptr());
        init_weight(this->norm.ptr());
    }

};

TORCH_MODULE(Patch);

class Patch1dImpl : public torch::nn::Module {

private:
    torch::Tensor         embed{ nullptr };
    torch::nn::LayerNorm  norm { nullptr };
    torch::nn::Sequential conv { nullptr };

public:
    Patch1dImpl(
        const int64_t l,
        const int64_t i_channels,
        const int64_t o_channels,
        const int64_t kernel_p,
        const int64_t stride_p,
        const int64_t padding_p  = 0,
        const int64_t dilation_p = 1,
        const int64_t kernel_h   = 3,
        const int64_t stride_h   = 1,
        const int64_t padding_h  = 1,
        const int64_t dilation_h = 1
    ) {
        const int64_t seq_len = l / stride_p;
        this->embed = this->register_parameter("embed", torch::zeros({ 1, seq_len, o_channels }));
        this->norm  = this->register_module("norm",     torch::nn::LayerNorm(torch::nn::LayerNormOptions(std::vector<int64_t>{ o_channels })));
        this->conv  = this->register_module("conv",     torch::nn::Sequential(
            torch::nn::Conv1d(torch::nn::Conv1dOptions(i_channels, o_channels, kernel_p).padding(padding_p).dilation(dilation_p).stride(stride_p)),
            chobits::nn::Activation(),
            torch::nn::Conv1d(torch::nn::Conv1dOptions(o_channels, o_channels, kernel_h).padding(padding_h).dilation(dilation_h).stride(stride_h))
        ));
    }

public:
    torch::Tensor forward(
        const torch::Tensor& input
    ) {
        auto out = this->conv->forward(input).transpose(1, 2);
             out = out + this->embed;
        return this->norm->forward(out);
    }

    void init() {
        init_weight(this->conv.ptr());
        init_weight(this->norm.ptr());
    }

};

TORCH_MODULE(Patch1d);

class QueryImpl : public torch::nn::Module {

private:
    torch::nn::Sequential conv{ nullptr };

public:
    QueryImpl(
        const int64_t i_channels,
        const int64_t o_channels,
        const int64_t dim,
        const int64_t kernel  = 3,
        const int64_t padding = 1
    ) {
        this->conv = this->register_module("conv", torch::nn::Sequential(
            torch::nn::Conv1d(torch::nn::Conv1dOptions(i_channels, o_channels, kernel).padding(padding)),
            chobits::nn::Activation(),
            torch::nn::Conv1d(torch::nn::Conv1dOptions(o_channels, o_channels, kernel).padding(padding)),
            torch::nn::LayerNorm(torch::nn::LayerNormOptions(std::vector<int64_t>{ dim }))
        ));
    }

public:
    torch::Tensor forward(
        const torch::Tensor& input
    ) {
        return this->conv->forward(input);
    }

    void init() {
        init_weight(this->conv.ptr());
    }

};

TORCH_MODULE(Query);

class ExpertImpl : public torch::nn::Module {

private:
    torch::nn::Sequential fc{ nullptr };

public:
    ExpertImpl(
        const int64_t embed_dim,
        const int64_t scale = 2
    ) {
        this->fc = this->register_module("fc", torch::nn::Sequential(
            torch::nn::Linear(torch::nn::LinearOptions(embed_dim, embed_dim * scale)),
            chobits::nn::Activation(),
            torch::nn::Linear(torch::nn::LinearOptions(embed_dim * scale, embed_dim))
        ));
    }

public:
    torch::Tensor forward(
        const torch::Tensor& input
    ) {
        return this->fc->forward(input);
    }

    void init() {
        init_weight(this->fc.ptr());
    }

};

TORCH_MODULE(Expert);

class MoEImpl : public torch::nn::Module {

private:
    torch::nn::Linear     gate   { nullptr };
    torch::nn::LayerNorm  norm   { nullptr };
    torch::nn::ModuleList experts{         };

public:
    MoEImpl(
        const int64_t embed_dim,
        const int64_t num_experts = 2
    ) {
        torch::nn::ModuleList experts;
        for (int i = 0; i < num_experts; ++i) {
            experts->push_back(chobits::nn::Expert(embed_dim));
        }
        this->gate    = this->register_module("gate",    torch::nn::Linear(embed_dim, num_experts));
        this->norm    = this->register_module("norm",    torch::nn::LayerNorm(torch::nn::LayerNormOptions(std::vector<int64_t>{ embed_dim })));
        this->experts = this->register_module("experts", experts);
    }

public:
    torch::Tensor forward(
        const torch::Tensor& input
    ) {
        auto flat_input   = input.view({ -1, input.size(2) });
        auto gate_logits  = this->gate->forward(flat_input);
        auto gate_weights = torch::softmax(gate_logits, -1);
        std::vector<torch::Tensor> expert_outs;
        for (auto iter = this->experts->begin(); iter != this->experts->end(); ++iter) {
            auto expert_out = (*iter)->as<chobits::nn::Expert>()->forward(flat_input);
            expert_outs.push_back(expert_out);
        }
        auto stacked_expert_outs  = torch::stack(expert_outs, 1);
        auto weighted_expert_outs = stacked_expert_outs * gate_weights.unsqueeze(-1);
        auto final_output = weighted_expert_outs.sum(1);
        return this->norm->forward(final_output.view(input.sizes()) + input);
    }

    void init() {
        init_weight(this->gate.ptr());
        init_weight(this->norm.ptr());
        init_weight(this->experts.ptr());
    }

};

TORCH_MODULE(MoE);

class MHAImpl : public torch::nn::Module {

private:
    torch::nn::Linear             q   { nullptr };
    torch::nn::Linear             k   { nullptr };
    torch::nn::Linear             v   { nullptr };
    torch::nn::MultiheadAttention attn{ nullptr };
    torch::nn::Linear             proj{ nullptr };
    chobits::nn::MoE              ffn { nullptr };
//  chobits::nn::Expert           ffn { nullptr };
    torch::nn::LayerNorm          norm{ nullptr };

public:
    MHAImpl(
        const int64_t q_dim,
        const int64_t k_dim,
        const int64_t v_dim,
        const int64_t o_dim,
        const int64_t h_dim     = 1024,
        const int64_t num_heads = 8
    ) {
        this->q    = this->register_module("q",    torch::nn::Linear(torch::nn::LinearOptions(q_dim, h_dim).bias(false)));
        this->k    = this->register_module("k",    torch::nn::Linear(torch::nn::LinearOptions(k_dim, h_dim).bias(false)));
        this->v    = this->register_module("v",    torch::nn::Linear(torch::nn::LinearOptions(v_dim, h_dim).bias(false)));
        this->attn = this->register_module("attn", torch::nn::MultiheadAttention(torch::nn::MultiheadAttentionOptions(h_dim, num_heads).bias(false)));
        this->proj = this->register_module("proj", torch::nn::Linear(torch::nn::LinearOptions(h_dim, o_dim).bias(false)));
        this->ffn  = this->register_module("ffn",  chobits::nn::MoE(o_dim));
//      this->ffn  = this->register_module("ffn",  chobits::nn::Expert(o_dim));
        this->norm = this->register_module("norm", torch::nn::LayerNorm(torch::nn::LayerNormOptions(std::vector<int64_t>{ o_dim })));
    }

public:
    torch::Tensor forward(
        const torch::Tensor& query,
        const torch::Tensor& key,
        const torch::Tensor& value
    ) {
        auto q = this->q->forward(query).transpose(0, 1);
        auto k = this->k->forward(key  ).transpose(0, 1);
        auto v = this->v->forward(value).transpose(0, 1);
        auto [ o, _ ] = this->attn->forward(q, k, v);
             o = query + this->proj->forward(o).transpose(0, 1);
             o = this->norm->forward(o);
        return this->ffn->forward(o);
    }

    void init() {
        init_weight(this->q.ptr());
        init_weight(this->k.ptr());
        init_weight(this->v.ptr());
        init_weight(this->attn.ptr());
        init_weight(this->proj.ptr());
        init_weight(this->ffn.ptr());
        init_weight(this->norm.ptr());
    }

};

TORCH_MODULE(MHA);

class AsTImpl : public torch::nn::Module {

private:
    chobits::nn::Patch1d   patch_s{ nullptr };
    chobits::nn::Patch1d   patch_l{ nullptr };
    chobits::nn::Query     query  { nullptr };
    chobits::nn::MHA       mha    { nullptr };
    chobits::nn::MHA       out    { nullptr };

public:
    AsTImpl(
        const int64_t l,
        const int64_t channels,
        const int64_t i_channels,
        const int64_t o_channels,
        const int64_t kernel_s,
        const int64_t kernel_l,
        const int64_t num_heads = 8
    ) {
        const int64_t o_dim   = o_channels;
        const int64_t h_dim   = o_dim * 2;
        const int64_t seq_len = l / kernel_s;
        this->patch_s = this->register_module("patch_s", chobits::nn::Patch1d(l, i_channels, o_channels, kernel_s, kernel_s));
        this->patch_l = this->register_module("patch_l", chobits::nn::Patch1d(l, i_channels, o_channels, kernel_l, kernel_l));
        this->query = this->register_module("query", chobits::nn::Query(seq_len, channels, o_dim));
        this->mha   = this->register_module("mha",   chobits::nn::MHA(o_dim, o_dim, o_dim, o_dim, h_dim, num_heads));
        this->out   = this->register_module("out",   chobits::nn::MHA(o_dim, o_dim, o_dim, o_dim, h_dim, num_heads));
    }
    
public:
    torch::Tensor forward(
        const torch::Tensor& input
    ) {
        auto input_s = this->patch_s->forward(input);
        auto input_l = this->patch_l->forward(input);
        auto out     = this->mha->forward(input_s, input_l, input_l);
        auto query   = this->query->forward(out);
        return this->out->forward(query, out, out);
    }

    void init() {
        init_weight(this->patch_s.ptr());
        init_weight(this->patch_l.ptr());
        init_weight(this->query.ptr());
        init_weight(this->mha.ptr());
        init_weight(this->out.ptr());
    }

};

TORCH_MODULE(AsT);

class ViTImpl : public torch::nn::Module {

private:
    chobits::nn::Patch   patch_s{ nullptr };
    chobits::nn::Patch   patch_l{ nullptr };
    chobits::nn::Query   query  { nullptr };
    chobits::nn::MHA     mha    { nullptr };
    chobits::nn::MHA     out    { nullptr };

public:
    ViTImpl(
        const int64_t h,
        const int64_t w,
        const int64_t channels,
        const int64_t i_channels,
        const int64_t o_channels,
        const shape_t kernel_s,
        const shape_t kernel_l,
        const int64_t num_heads = 8
    ) {
        const int64_t o_dim   = o_channels;
        const int64_t h_dim   = o_dim * 2;
        const int64_t seq_len = (h / kernel_s[0]) * (w / kernel_s[1]);
        this->patch_s = this->register_module("patch_s", chobits::nn::Patch(h, w, i_channels, o_channels, kernel_s, kernel_s));
        this->patch_l = this->register_module("patch_l", chobits::nn::Patch(h, w, i_channels, o_channels, kernel_l, kernel_l));
        this->query = this->register_module("query", chobits::nn::Query(seq_len, channels, o_dim));
        this->mha   = this->register_module("mha",   chobits::nn::MHA(o_dim, o_dim, o_dim, o_dim, h_dim, num_heads));
        this->out   = this->register_module("out",   chobits::nn::MHA(o_dim, o_dim, o_dim, o_dim, h_dim, num_heads));
    }
    
public:
    torch::Tensor forward(
        const torch::Tensor& input
    ) {
        auto input_s = this->patch_s->forward(input);
        auto input_l = this->patch_l->forward(input);
        auto out     = this->mha->forward(input_s, input_l, input_l);
        auto query   = this->query->forward(out);
        return this->out->forward(query, out, out);
    }

    void init() {
        init_weight(this->patch_s.ptr());
        init_weight(this->patch_l.ptr());
        init_weight(this->query.ptr());
        init_weight(this->mha.ptr());
        init_weight(this->out.ptr());
    }

};

TORCH_MODULE(ViT);

class MixerImpl : public torch::nn::Module {

private:
    chobits::nn::MHA audio_mha{ nullptr };
    chobits::nn::MHA video_mha{ nullptr };

public:
    MixerImpl(
        const int64_t audio_dim = 256,
        const int64_t video_dim = 512,
        const int64_t h_dim     = 1024,
        const int64_t num_heads = 8
    ) {
        this->audio_mha = this->register_module("audio_mha", chobits::nn::MHA(audio_dim, video_dim, video_dim, audio_dim, h_dim, num_heads));
        this->video_mha = this->register_module("video_mha", chobits::nn::MHA(video_dim, audio_dim, audio_dim, video_dim, h_dim, num_heads));
    }
    
public:
    std::tuple<torch::Tensor, torch::Tensor> forward(
        const torch::Tensor& audio,
        const torch::Tensor& video
    ) {
        auto audio_o = this->audio_mha->forward(audio, video, video);
        auto video_o = this->video_mha->forward(video, audio, audio);
        return { audio_o, video_o };
    }

    void init() {
        init_weight(this->audio_mha.ptr());
        init_weight(this->video_mha.ptr());
    }

};

TORCH_MODULE(Mixer);

class TalkImpl : public torch::nn::Module {

private:
    chobits::nn::Query    query{ nullptr };
    chobits::nn::MHA      mha  { nullptr };
    torch::nn::Sequential out  { nullptr };

public:
    TalkImpl(
        const int64_t channels   = 512,
        const int64_t i_features = 256,
        const int64_t o_features = 800,
        const int64_t num_heads  = 8
    ) {
        this->query = this->register_module("query", chobits::nn::Query(channels, 4, i_features));
        this->mha   = this->register_module("mha",   chobits::nn::MHA(i_features, i_features, i_features, i_features, i_features * 2, num_heads));
        this->out   = this->register_module("out",   torch::nn::Sequential(
            torch::nn::Linear(i_features * 4, o_features * 2),
            torch::nn::Tanh(),
            torch::nn::Linear(o_features * 2, o_features)
        ));
    }

public:
    torch::Tensor forward(
        const torch::Tensor& audio
    ) {
        auto query = this->query->forward(audio);
        auto out   = this->mha->forward(query, audio, audio);
             out   = out.flatten(1);
        return torch::tanh(this->out->forward(out));
    }

    void init() {
        init_weight(this->query.ptr());
        init_weight(this->mha.ptr());
        init_weight(this->out.ptr());
    }

};

TORCH_MODULE(Talk);

class ChobitsImpl : public torch::nn::Module {

friend chobits::model::Trainer;

private:
    chobits::nn::AsT      audio_ast{ nullptr };
    chobits::nn::ViT      video_vit{ nullptr };
    chobits::nn::ViT      image_vit{ nullptr };
    chobits::nn::MHA      image_mha{ nullptr };
    chobits::nn::MHA      mixer_mha{ nullptr };
    torch::nn::ModuleList mixers   {         };
    chobits::nn::Talk     talk     { nullptr };

public:
    ChobitsImpl() {
        this->audio_ast = this->register_module("audio_ast", chobits::nn::AsT(
            32 * 800, 512, 1, 256,
            100, 200
        ));
        this->video_vit = this->register_module("video_vit", chobits::nn::ViT(
            360, 640, 512, 32, 512,
            std::vector<int64_t>{ 20, 20 }, std::vector<int64_t>{ 40, 40 }
        ));
        this->image_vit = this->register_module("image_vit", chobits::nn::ViT(
            360, 640, 512, 3, 512,
            std::vector<int64_t>{ 20, 20 }, std::vector<int64_t>{ 40, 40 }
        ));
        torch::nn::ModuleList mixers;
        for (int i = 0; i < 3; ++i) {
            mixers->push_back(chobits::nn::Mixer(256, 512, 1024, 8));
        }
        this->image_mha = this->register_module("image_mha", chobits::nn::MHA(512, 512, 512, 512, 1024, 8));
        this->mixer_mha = this->register_module("mixer_mha", chobits::nn::MHA(256, 512, 512, 256, 1024, 8));
        this->mixers    = this->register_module("mixers",    mixers);
        this->talk      = this->register_module("talk",      chobits::nn::Talk());
    }

public:
    torch::Tensor forward(
        const torch::Tensor& audio
    ) {
        auto audio_o = this->audio_ast->forward(audio.view({ audio.size(0), 1, -1 }));
        return this->talk->forward(audio_o);
    }
    
    torch::Tensor forward(
        const torch::Tensor& audio,
        const torch::Tensor& video
    ) {
        auto audio_o = this->audio_ast->forward(audio.view({ audio.size(0), 1, -1 }));
        auto video_i = video.select(2, -1);
        auto image_i = video.select(1, -1);
        auto video_o = this->video_vit->forward(video_i);
        auto image_o = this->image_vit->forward(image_i);
             video_o = this->image_mha->forward(video_o, image_o, image_o);
        for (auto iter = this->mixers->begin(); iter != this->mixers->end(); ++iter) {
            auto [ audio_x, video_x ] = (*iter)->as<chobits::nn::Mixer>()->forward(audio_o, video_o);
            audio_o = audio_x;
            video_o = video_x;
        }
        audio_o = this->mixer_mha->forward(audio_o, video_o, video_o);
        return this->talk->forward(audio_o);
    }

    void init() {
        init_weight(this->audio_ast.ptr());
        init_weight(this->video_vit.ptr());
        init_weight(this->image_vit.ptr());
        init_weight(this->image_mha.ptr());
        init_weight(this->mixer_mha.ptr());
        init_weight(this->mixers.ptr());
        init_weight(this->talk.ptr());
    }

};

TORCH_MODULE(Chobits);

static void init_weight(std::shared_ptr<torch::nn::Module> module) {
    if(auto* layer = module->as<torch::nn::Conv2d>()) {
        layer->reset_parameters();
    } else if(auto* layer = module->as<torch::nn::Conv1d>()) {
        layer->reset_parameters();
    } else if(auto* layer = module->as<torch::nn::Linear>()) {
        layer->reset_parameters();
    } else if(auto* layer = module->as<torch::nn::LayerNorm>()) {
        layer->reset_parameters();
    } else if(auto* layer = module->as<torch::nn::MultiheadAttention>()) {
        layer->_reset_parameters();
    } else if(auto* layer = module->as<chobits::nn::MoE>()) {
        layer->init();
    } else if(auto* layer = module->as<chobits::nn::MHA>()) {
        layer->init();
    } else if(auto* layer = module->as<chobits::nn::AsT>()) {
        layer->init();
    } else if(auto* layer = module->as<chobits::nn::ViT>()) {
        layer->init();
    } else if(auto* layer = module->as<chobits::nn::Talk>()) {
        layer->init();
    } else if(auto* layer = module->as<chobits::nn::Patch>()) {
        layer->init();
    } else if(auto* layer = module->as<chobits::nn::Query>()) {
        layer->init();
    } else if(auto* layer = module->as<chobits::nn::Mixer>()) {
        layer->init();
    } else if(auto* layer = module->as<chobits::nn::Expert>()) {
        layer->init();
    } else if(auto* layer = module->as<torch::nn::Sequential>()) {
        for(auto value : layer->children()) {
            init_weight(value);
        }
    } else if(auto* layer = module->as<torch::nn::ModuleList>()) {
        for(auto value : layer->children()) {
            init_weight(value);
        }
    }
}

} // END OF chobits::nn

#endif // CHOBITS_NN_HPP

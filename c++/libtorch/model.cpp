#include "chobits/nn.hpp"
#include "chobits/media.hpp"
#include "chobits/model.hpp"
#include "chobits/chobits.hpp"

#include <thread>
#include <cinttypes>
#include <filesystem>

#include "torch/torch.h"

static struct TrainerState {
    float learning_rate  = 0.0001;
    float clip_grad_norm = 1.0;
    torch::DeviceType    device = torch::cuda::is_available() ? torch::DeviceType::CUDA : torch::DeviceType::CPU;
    chobits::nn::Chobits model  = nullptr;
} trainer_state;

bool chobits::model::Trainer::save(const std::string& path, bool train) {
    if(!trainer_state.model) {
        return false;
    }
    trainer_state.model->eval();
    trainer_state.model->to(torch::DeviceType::CPU);
    std::printf("保存模型：%s\n", path.c_str());
    const std::string save_path = "chobits.ckpt";
    torch::save(trainer_state.model, save_path);
    std::filesystem::rename(save_path, path);
    trainer_state.model->to(trainer_state.device);
    if(train) {
        trainer_state.model->train();
    }
    return true;
}

bool chobits::model::Trainer::load(const std::string& path, bool train) {
    trainer_state.model = chobits::nn::Chobits();
    if(std::filesystem::exists(path)) {
        try {
            std::printf("加载模型：%s\n", path.c_str());
            torch::load(trainer_state.model, path, torch::DeviceType::CPU);
        } catch(const std::exception& e) {
            std::printf("加载模型失败：%s\n", e.what());
        }
    } else {
        std::printf("初始化模型的参数");
        trainer_state.model->init();
    }
    trainer_state.model->to(trainer_state.device);
    if(train) {
        trainer_state.model->train();
    } else {
        trainer_state.model->eval();
    }
    this->info();
    return true;
}

void chobits::model::Trainer::train() {
    try {
        trainer_state.model->train();
        auto optimizer  = torch::optim::AdamW(trainer_state.model->parameters(), trainer_state.learning_rate);
        auto scheduler  = torch::optim::StepLR(optimizer, 10, 0.999);
        auto loss_val   = 0.0F;
        auto time_point = std::chrono::system_clock::now();
        static const int per_op_epoch = 10;
        static const int per_ck_epoch = 10000;
        for (size_t epoch = 1; epoch <= 31'536'000'000ULL && chobits::running; ++epoch) {
            this->train(loss_val);
            if(epoch % per_op_epoch == 0) {
                torch::nn::utils::clip_grad_norm_(trainer_state.model->parameters(), trainer_state.clip_grad_norm);
                optimizer.step();
                optimizer.zero_grad();
                auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(std::chrono::system_clock::now() - time_point).count();
                std::printf("轮次：%" PRIu64 " 损失：%.6f 耗时：%" PRId64 "\n", epoch, loss_val / per_op_epoch, duration);
                loss_val   = 0.0;
                time_point = std::chrono::system_clock::now();
            }
            if(epoch % per_ck_epoch == 0) {
                scheduler.step();
                this->save("chobits." + std::to_string(epoch / per_ck_epoch % 10) + ".ckpt", true);
            }
        }
    } catch(const std::exception& e) {
        std::printf("训练异常：%s\n", e.what());
    } catch(...) {
        std::printf("训练异常\n");
    }
}

void chobits::model::Trainer::train(float& loss_val) {
    auto [success, audio, video, label] = chobits::media::get_data();
    if(!success) {
        std::this_thread::sleep_for(std::chrono::milliseconds(100));
        return;
    }
    if(chobits::train_media == "video") {
        video = video.to(trainer_state.device);
    }
    audio = audio.to(trainer_state.device);
    label = label.to(trainer_state.device);
    torch::Tensor pred{};
    if(chobits::train_media == "video") {
        pred = trainer_state.model->forward(audio, video);
    } else {
        pred = trainer_state.model->forward(audio);
    }
    auto loss = torch::l1_loss(pred, label, torch::Reduction::Mean);
    loss.backward();
    loss_val += loss.template item<float>();
    if(chobits::mode_play || chobits::mode_save) {
        torch::NoGradGuard no_grad_guard;
        chobits::media::set_data(pred[0], video);
    }
}

void chobits::model::Trainer::eval() {
    try {
        trainer_state.model->eval();
        torch::NoGradGuard no_grad_guard;
        while(chobits::running) {
            auto [success, audio, video, label] = chobits::media::get_data();
            if(!success) {
                std::this_thread::sleep_for(std::chrono::milliseconds(100));
                continue;
            }
            if(chobits::train_media == "video") {
                video = video.to(trainer_state.device);
            }
            audio = audio.to(trainer_state.device);
//          label = label.to(trainer_state.device);
            torch::Tensor pred{};
            if(chobits::train_media == "video") {
                pred = trainer_state.model->forward(audio, video);
            } else {
                pred = trainer_state.model->forward(audio);
            }
            if(chobits::mode_play || chobits::mode_save) {
                chobits::media::set_data(pred[0], video);
            }
        }
    } catch(const std::exception& e) {
        std::printf("预测异常：%s\n", e.what());
    } catch(...) {
        std::printf("预测异常\n");
    }
}

void chobits::model::Trainer::close() {
    if(trainer_state.model) {
        trainer_state.model = nullptr;
    }
}

void chobits::model::Trainer::info() {
    int     layer_size  = 0;
    int64_t total_numel = 0;
    for(const auto& parameter : trainer_state.model->named_parameters()) {
        ++layer_size;
        total_numel += parameter.value().numel();
        std::printf("模型参数数量：%64s = %" PRId64 "\n", parameter.key().c_str(), parameter.value().numel());
    }
    std::printf("模型层数总量：%d\n", layer_size);
    std::printf("模型参数总量：%" PRId64 "\n", total_numel);
}

bool chobits::model::open_model() {
    chobits::model::Trainer trainer;
    trainer.load();
    if(chobits::mode_eval) {
        trainer.eval();
    } else {
        trainer.train();
        trainer.save();
    }
    trainer.close();
    return true;
}

void chobits::model::stop_all() {
    std::printf("关闭模型\n");
}

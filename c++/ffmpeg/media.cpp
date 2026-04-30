#include "chobits/media.hpp"
#include "chobits/player.hpp"
#include "chobits/chobits.hpp"

#include <mutex>
#include <random>
#include <thread>
#include <numbers>
#include <fstream>
#include <algorithm>
#include <filesystem>
#include <condition_variable>

#include "torch/types.h"

extern "C" {

#include "libavutil/opt.h"
#include "libavutil/imgutils.h"
#include "libavcodec/avcodec.h"
#include "libswscale/swscale.h"
#include "libavdevice/avdevice.h"
#include "libavformat/avformat.h"
#include "libswresample/swresample.h"

}

const static float audio_normalization = 32768.0;
const static float video_normalization = 128.0;

const static AVPixelFormat video_pix_format = AV_PIX_FMT_RGB24;

const static AVSampleFormat  audio_format           = AV_SAMPLE_FMT_S16;
const static AVChannelLayout audio_layout           = AV_CHANNEL_LAYOUT_MONO;
const static int             audio_bytes_per_sample = av_get_bytes_per_sample(audio_format);

static std::ofstream stream;

struct Dataset {
    size_t cache_size = 150; // 150 * 1000 / per_wind_second = 15000ms = 15s
    size_t audio_size = 2ULL * chobits::audio_sample_rate / chobits::per_wind_second; // 2ULL = 16bits
    std::mutex mutex;
    std::condition_variable condition;
    std::vector<std::vector<torch::Tensor>> audio;
    std::vector<std::vector<torch::Tensor>> video;
};

static Dataset dataset = {};

thread_local static int dataset_index = 0;

static void init_context();

static SwrContext* init_audio_swr(AVCodecContext* ctx, AVFrame* frame);
static SwsContext* init_video_sws(AVCodecContext* ctx, AVFrame* frame);

static std::string device_name(AVMediaType type, const char* format_name);

static void sws_free(SwsContext** sws);

static bool audio_to_tensor(std::vector<torch::Tensor>& audio, std::vector<torch::Tensor>& video, SwrContext* swr, AVFrame* frame);
static bool video_to_tensor(std::vector<torch::Tensor>& audio, std::vector<torch::Tensor>& video, SwsContext* sws, AVFrame* frame);

static bool open_audio_file(const std::string& file);
static bool open_video_file(const std::string& file);

static std::vector<std::string> list_train_dataset();

bool chobits::media::open_media() {
    if(chobits::mode_file) {
        std::mt19937 rand(std::random_device{}());
        std::vector<std::thread> threads;
        std::vector<std::string> files = list_train_dataset();
        std::printf("加载文件数量：%" PRIu64 "\n", files.size());
        for(int index = 0; index < chobits::batch_thread && chobits::running; ++index) {
            threads.push_back(std::thread([&rand, index, files]() mutable {
                dataset_index = index;
                std::shuffle(files.begin(), files.end(), rand);
                std::printf("开始加载文件：%d\n", dataset_index);
                for(int epoch = 0; epoch < chobits::train_epoch && chobits::running; ++epoch) {
                    for(const auto& file : files) {
                        if(!chobits::running) {
                            break;
                        }
                        std::printf("开始加载文件：%d = %d = %s\n", dataset_index, epoch, file.c_str());
                        if(chobits::media::open_file(file)) {
                            std::printf("文件加载完成：%d = %d = %s\n", dataset_index, epoch, file.c_str());
                        } else {
                            std::printf("文件加载失败：%d = %d = %s\n", dataset_index, epoch, file.c_str());
                        }
                    }
                }
                std::printf("文件加载完成：%d\n", dataset_index);
            }));
        }
        for(auto& thread : threads) {
            thread.join();
        }
        chobits::stop_all();
    } else {
        std::thread player_thread([]() {
            chobits::player::open_player();
        });
        chobits::media::open_device();
        chobits::stop_all();
        player_thread.join();
    }
    return true;
}

bool chobits::media::open_file(const std::string& file) {
    if(chobits::train_media == "audio") {
        return open_audio_file(file);
    } else if(chobits::train_media == "video") {
        return open_video_file(file);
    } else {
        // -
        return false;
    }
}

static bool open_audio_file(const std::string& file) {
    int ret = 0;
    AVFormatContext* format_ctx = avformat_alloc_context();
    ret = avformat_open_input(&format_ctx, file.c_str(), nullptr, nullptr);
    if(ret != 0) {
        avformat_close_input(&format_ctx);
        std::printf("打开输入文件失败：%d - %s\n", ret, file.c_str());
        return false;
    }
    // av_dump_format(format_ctx, 0, format_ctx->url, 0);
    int audio_index = -1;
    for(uint32_t i = 0; i < format_ctx->nb_streams; ++i) {
        auto stream = format_ctx->streams[i];
        if(stream->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            audio_index = stream->index;
        } else {
            // -
        }
    }
    if(audio_index < 0) {
        avformat_close_input(&format_ctx);
        std::printf("查找媒体轨道失败：%d - %s\n", audio_index, file.c_str());
        return false;
    }
    std::printf("打开输入文件成功：%d - %s\n", audio_index, file.c_str());
    const AVStream* audio_stream    = format_ctx->streams[audio_index];
    const AVCodec * audio_codec     = avcodec_find_decoder(audio_stream->codecpar->codec_id);
    AVCodecContext* audio_codec_ctx = avcodec_alloc_context3(audio_codec);
    ret = avcodec_parameters_to_context(audio_codec_ctx, audio_stream->codecpar);
    ret = avcodec_open2(audio_codec_ctx, audio_codec, nullptr);
    if(ret != 0) {
        avcodec_free_context(&audio_codec_ctx);
        avformat_close_input(&format_ctx);
        std::printf("打开音频解码器失败：%d\n", ret);
        return false;
    }
    double audio_time  = 0;
    double audio_base  = av_q2d(audio_stream->time_base);
    uint64_t audio_pos = 0;
    uint64_t audio_frame_count = 0;
    AVFrame * frame  = av_frame_alloc();
    AVPacket* packet = av_packet_alloc();
    SwrContext* audio_swr = nullptr;
    init_context();
    std::vector<torch::Tensor>& audio = dataset.audio[dataset_index];
    std::vector<torch::Tensor>& video = dataset.video[dataset_index];
    while(chobits::running && av_read_frame(format_ctx, packet) == 0) {
        if(packet->stream_index == audio_index) {
            if(avcodec_send_packet(audio_codec_ctx, packet) == 0) {
                while(chobits::running && avcodec_receive_frame(audio_codec_ctx, frame) == 0) {
                    ++audio_frame_count;
                    if(audio_pos == 0) {
                        audio_pos = frame->pts;
                    }
                    audio_time = (frame->pts - audio_pos) * audio_base;
                    if(audio_swr == nullptr) {
                        audio_swr = init_audio_swr(audio_codec_ctx, frame);
                    }
                    if(audio_swr != nullptr) {
                        audio_to_tensor(audio, video, audio_swr, frame);
                    }
                    av_frame_unref(frame);
                }
            }
        } else {
            // -
        }
        av_packet_unref(packet);
    }
    swr_free(&audio_swr);
    av_frame_free(&frame);
    av_packet_free(&packet);
    avcodec_free_context(&audio_codec_ctx);
    avformat_close_input(&format_ctx);
    std::printf("文件处理完成：%" PRIu64 " - %.2f - %s\n", audio_frame_count, audio_time, file.c_str());
    return true;
}

static bool open_video_file(const std::string& file) {
    int ret = 0;
    AVFormatContext* format_ctx = avformat_alloc_context();
    ret = avformat_open_input(&format_ctx, file.c_str(), nullptr, nullptr);
    if(ret != 0) {
        avformat_close_input(&format_ctx);
        std::printf("打开输入文件失败：%d - %s\n", ret, file.c_str());
        return false;
    }
    // av_dump_format(format_ctx, 0, format_ctx->url, 0);
    int audio_index = -1;
    int video_index = -1;
    for(uint32_t i = 0; i < format_ctx->nb_streams; ++i) {
        auto stream = format_ctx->streams[i];
        if(stream->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
            audio_index = stream->index;
        } else if(stream->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            video_index = stream->index;
        } else {
            // -
        }
    }
    if(audio_index < 0 || video_index < 0) {
        avformat_close_input(&format_ctx);
        std::printf("查找媒体轨道失败：%d - %d - %s\n", audio_index, video_index, file.c_str());
        return false;
    }
    std::printf("打开输入文件成功：%d - %d - %s\n", audio_index, video_index, file.c_str());
    const AVStream* audio_stream    = format_ctx->streams[audio_index];
    const AVCodec * audio_codec     = avcodec_find_decoder(audio_stream->codecpar->codec_id);
    AVCodecContext* audio_codec_ctx = avcodec_alloc_context3(audio_codec);
    const AVStream* video_stream    = format_ctx->streams[video_index];
    const AVCodec * video_codec     = avcodec_find_decoder(video_stream->codecpar->codec_id);
    AVCodecContext* video_codec_ctx = avcodec_alloc_context3(video_codec);
    ret = avcodec_parameters_to_context(audio_codec_ctx, audio_stream->codecpar);
    ret = avcodec_open2(audio_codec_ctx, audio_codec, nullptr);
    if(ret != 0) {
        avcodec_free_context(&audio_codec_ctx);
        avcodec_free_context(&video_codec_ctx);
        avformat_close_input(&format_ctx);
        std::printf("打开音频解码器失败：%d\n", ret);
        return false;
    }
    ret = avcodec_parameters_to_context(video_codec_ctx, video_stream->codecpar);
    ret = avcodec_open2(video_codec_ctx, video_codec, nullptr);
    if(ret != 0) {
        avcodec_free_context(&audio_codec_ctx);
        avcodec_free_context(&video_codec_ctx);
        avformat_close_input(&format_ctx);
        std::printf("打开视频解码器失败：%d\n", ret);
        return false;
    }
    double audio_time  = 0;
    double video_time  = 0;
    double audio_base  = av_q2d(audio_stream->time_base);
    double video_base  = av_q2d(video_stream->time_base);
    uint64_t audio_pos = 0;
    uint64_t video_pos = 0;
    uint64_t audio_frame_count = 0;
    uint64_t video_frame_count = 0;
    AVFrame * frame  = av_frame_alloc();
    AVPacket* packet = av_packet_alloc();
    SwrContext* audio_swr = nullptr;
    SwsContext* video_sws = nullptr;
    init_context();
    std::vector<torch::Tensor>& audio = dataset.audio[dataset_index];
    std::vector<torch::Tensor>& video = dataset.video[dataset_index];
    while(chobits::running && av_read_frame(format_ctx, packet) == 0) {
        if(packet->stream_index == audio_index) {
            if(avcodec_send_packet(audio_codec_ctx, packet) == 0) {
                while(chobits::running && avcodec_receive_frame(audio_codec_ctx, frame) == 0) {
                    ++audio_frame_count;
                    if(audio_pos == 0) {
                        audio_pos = frame->pts;
                    }
                    audio_time = (frame->pts - audio_pos) * audio_base;
                    if(audio_swr == nullptr) {
                        audio_swr = init_audio_swr(audio_codec_ctx, frame);
                    }
                    if(audio_swr != nullptr) {
                        audio_to_tensor(audio, video, audio_swr, frame);
                    }
                    av_frame_unref(frame);
                }
            }
        } else if(packet->stream_index == video_index) {
            if(avcodec_send_packet(video_codec_ctx, packet) == 0) {
                while(chobits::running && avcodec_receive_frame(video_codec_ctx, frame) == 0) {
                    ++video_frame_count;
                    if(video_pos == 0) {
                        video_pos = frame->pts;
                    }
                    video_time = (frame->pts - video_pos) * video_base;
                    if(video_sws == nullptr) {
                        video_sws = init_video_sws(video_codec_ctx, frame);
                    }
                    if(video_sws != nullptr) {
                        video_to_tensor(audio, video, video_sws, frame);
                    }
                    av_frame_unref(frame);
                }
            }
        } else {
            // -
        }
        av_packet_unref(packet);
    }
    swr_free(&audio_swr);
    sws_free(&video_sws);
    av_frame_free(&frame);
    av_packet_free(&packet);
    avcodec_free_context(&audio_codec_ctx);
    avcodec_free_context(&video_codec_ctx);
    avformat_close_input(&format_ctx);
    std::printf("文件处理完成：%" PRIu64 " - %" PRIu64 " - %.2f - %.2f - %s\n", audio_frame_count, video_frame_count, audio_time, video_time, file.c_str());
    while(audio.size() != video.size()) {
        if(audio.size() > video.size()) {
            video.push_back(torch::zeros({ 3, chobits::video_height, chobits::video_width }));
        } else {
            auto pcm_size = int(dataset.audio_size / sizeof(short));
            audio.push_back(torch::zeros({ pcm_size }));
        }
    }
    return true;
}

bool chobits::media::open_device() {
    // ffmpeg -devices
    int ret = 0;
    avdevice_register_all();
    #if _WIN32
    const char* audio_format_name = "dshow";
    const char* video_format_name = "dshow";
    #else
    const char* audio_format_name = "alsa";
    const char* video_format_name = "v4l2";
    #endif
    std::string audio_device_name = device_name(AVMEDIA_TYPE_AUDIO, audio_format_name);
    std::string video_device_name = device_name(AVMEDIA_TYPE_VIDEO, video_format_name);
    std::printf("打开音频输入设备：%s\n", audio_device_name.c_str());
    std::printf("打开视频输入设备：%s\n", video_device_name.c_str());
    #if _WIN32
    audio_device_name = "audio=" + audio_device_name;
    video_device_name = "video=" + video_device_name;
    #endif
    AVFormatContext    * audio_format_ctx = avformat_alloc_context();
    AVDictionary       * audio_options    = nullptr;
    const AVInputFormat* audio_format     = av_find_input_format(audio_format_name);
    AVFormatContext    * video_format_ctx = avformat_alloc_context();
    AVDictionary       * video_options    = nullptr;
    const AVInputFormat* video_format     = av_find_input_format(video_format_name);
    // 音频参数
    av_dict_set(&audio_options, "channels",          "2",         0);
    av_dict_set(&audio_options, "sample_rate",       "48000",     0);
    av_dict_set(&audio_options, "sample_format",     "pcm_s16le", 0);
    av_dict_set(&audio_options, "audio_buffer_size", "100",       0); // 毫秒
    // 视频参数
    av_dict_set(&video_options, "framerate",    "30",      0);
    av_dict_set(&video_options, "video_size",   "640*360", 0);
    av_dict_set(&video_options, "pixel_format", "yuyv422", 0);
    ret = avformat_open_input(&audio_format_ctx, audio_device_name.c_str(), audio_format, &audio_options);
    av_dict_free(&audio_options);
    if(ret != 0) {
        avformat_close_input(&audio_format_ctx);
        avformat_close_input(&video_format_ctx);
        std::printf("打开音频硬件失败：%d - %s\n", ret, audio_device_name.c_str());
        return false;
    }
    ret = avformat_open_input(&video_format_ctx, video_device_name.c_str(), video_format, &video_options);
    av_dict_free(&video_options);
    if(ret != 0) {
        avformat_close_input(&audio_format_ctx);
        avformat_close_input(&video_format_ctx);
        std::printf("打开视频硬件失败：%d - %s\n", ret, video_device_name.c_str());
        return false;
    }
    // av_dump_format(audio_format_ctx, 0, audio_format_ctx->url, 0);
    // av_dump_format(video_format_ctx, 0, video_format_ctx->url, 0);
    uint64_t audio_frame_count = 0;
    uint64_t video_frame_count = 0;
    init_context();
    std::vector<torch::Tensor>& audio = dataset.audio[dataset_index];
    std::vector<torch::Tensor>& video = dataset.video[dataset_index];
    std::thread audio_thread([&audio, &video, audio_format_ctx, &audio_frame_count]() {
        int ret = 0;
        const AVStream* audio_stream    = audio_format_ctx->streams[0];
        const AVCodec * audio_codec     = avcodec_find_decoder(audio_stream->codecpar->codec_id);
        AVCodecContext* audio_codec_ctx = avcodec_alloc_context3(audio_codec);
        ret = avcodec_parameters_to_context(audio_codec_ctx, audio_stream->codecpar);
        ret = avcodec_open2(audio_codec_ctx, audio_codec, nullptr);
        if(ret != 0) {
            avcodec_free_context(&audio_codec_ctx);
            std::printf("打开音频解码器失败：%d\n", ret);
            return;
        }
        AVFrame   * frame     = av_frame_alloc();
        AVPacket  * packet    = av_packet_alloc();
        SwrContext* audio_swr = nullptr;
        while(chobits::running) {
            if(av_read_frame(audio_format_ctx, packet) == 0) {
                if(avcodec_send_packet(audio_codec_ctx, packet) == 0) {
                    while(chobits::running && avcodec_receive_frame(audio_codec_ctx, frame) == 0) {
                        ++audio_frame_count;
                        if(audio_swr == nullptr) {
                            audio_swr = init_audio_swr(audio_codec_ctx, frame);
                        }
                        if(audio_swr != nullptr) {
                            audio_to_tensor(audio, video, audio_swr, frame);
                        }
                        av_frame_unref(frame);
                    }
                }
            }
            av_packet_unref(packet);
        }
        swr_free(&audio_swr);
        av_frame_free(&frame);
        av_packet_free(&packet);
        avcodec_free_context(&audio_codec_ctx);
    });
    std::thread video_thread([&audio, &video, video_format_ctx, &video_frame_count]() {
        int ret = 0;
        const AVStream* video_stream    = video_format_ctx->streams[0];
        const AVCodec * video_codec     = avcodec_find_decoder(video_stream->codecpar->codec_id);
        AVCodecContext* video_codec_ctx = avcodec_alloc_context3(video_codec);
        ret = avcodec_parameters_to_context(video_codec_ctx, video_stream->codecpar);
        ret = avcodec_open2(video_codec_ctx, video_codec, nullptr);
        if(ret != 0) {
            avcodec_free_context(&video_codec_ctx);
            std::printf("打开视频解码器失败：%d\n", ret);
            return;
        }
        AVFrame   * frame     = av_frame_alloc();
        AVPacket  * packet    = av_packet_alloc();
        SwsContext* video_sws = nullptr;
        while(chobits::running) {
            if(av_read_frame(video_format_ctx, packet) == 0) {
                if(avcodec_send_packet(video_codec_ctx, packet) == 0) {
                    while(chobits::running && avcodec_receive_frame(video_codec_ctx, frame) == 0) {
                        ++video_frame_count;
                        if(video_sws == nullptr) {
                            video_sws = init_video_sws(video_codec_ctx, frame);
                        }
                        if(video_sws != nullptr) {
                            video_to_tensor(audio, video, video_sws, frame);
                        }
                        av_frame_unref(frame);
                    }
                }
            }
            av_packet_unref(packet);
        }
        sws_free(&video_sws);
        av_frame_free(&frame);
        av_packet_free(&packet);
        avcodec_free_context(&video_codec_ctx);
    });
    audio_thread.join();
    video_thread.join();
    avformat_close_input(&audio_format_ctx);
    avformat_close_input(&video_format_ctx);
    std::printf("媒体处理完成：%" PRIu64 " - %" PRIu64 "\n", audio_frame_count, video_frame_count);
    return true;
}

void chobits::media::stop_all() {
    std::printf("关闭媒体\n");
    {
        std::unique_lock<std::mutex> lock(dataset.mutex);
        dataset.audio.clear();
        dataset.video.clear();
        if(stream.is_open()) {
            stream.close();
        }
    }
    dataset.condition.notify_all();
}

std::tuple<bool, at::Tensor, at::Tensor, at::Tensor> chobits::media::get_data() {
    std::vector<torch::Tensor> audio;
    std::vector<torch::Tensor> video;
    std::vector<torch::Tensor> label;
    const int epoch = chobits::batch_size <= chobits::batch_thread ? 1 : (chobits::batch_size / chobits::batch_thread);
    {
        std::unique_lock<std::mutex> lock(dataset.mutex);
        dataset.condition.wait(lock, [epoch]() {
            const size_t batch_thread = chobits::batch_thread;
            const size_t batch_length = chobits::batch_length + epoch;
            return
                !(
                    chobits::running &&
                    (
                        (
                            chobits::train_media == "audio" &&
                            (
                                dataset.audio.size() < batch_thread ||
                                std::any_of(dataset.audio.begin(), dataset.audio.end(), [batch_length](const auto& audio) { return audio.size() < batch_length; })
                            )
                        ) ||
                        (
                            chobits::train_media == "video" &&
                            (
                                dataset.audio.size() < batch_thread ||
                                dataset.video.size() < batch_thread ||
                                std::any_of(dataset.audio.begin(), dataset.audio.end(), [batch_length](const auto& audio) { return audio.size() < batch_length; }) ||
                                std::any_of(dataset.video.begin(), dataset.video.end(), [batch_length](const auto& video) { return video.size() < batch_length; })
                            )
                        )
                    )
                );
        });
        if(!chobits::running) {
            return { false, {}, {}, {} };
        }
        for(int jndex = 0; jndex < epoch; ++ jndex) {
            for(int index = 0; index < chobits::batch_thread; ++index) {
                auto& dataset_audio = dataset.audio[index];
                auto& dataset_video = dataset.video[index];
                audio.push_back(torch::stack(std::vector<torch::Tensor>(dataset_audio.begin(), dataset_audio.begin() + chobits::batch_length)));
                if(chobits::train_media == "video") {
                    video.push_back(torch::stack(std::vector<torch::Tensor>(dataset_video.begin(), dataset_video.begin() + chobits::batch_length)));
                }
                label.push_back(dataset_audio[chobits::batch_length]);
                dataset_audio.erase(dataset_audio.begin());
                if(chobits::train_media == "video") {
                    dataset_video.erase(dataset_video.begin());
                }
            }
        }
    }
    dataset.condition.notify_all();
    if(chobits::train_media == "video") {
        return {
            true,
            torch::stack(audio),
            torch::stack(video),
            torch::stack(label)
        };
    } else {
        return {
            true,
            torch::stack(audio),
            {},
            torch::stack(label)
        };
    }
}

void chobits::media::set_data(const torch::Tensor& audio, const torch::Tensor& video) {
    // audio
    auto audio_tensor = audio.mul(audio_normalization).to(torch::kShort).cpu();
    auto audio_data   = reinterpret_cast<short*>(audio_tensor.data_ptr());
    auto audio_length = audio.size(-1);
    if(chobits::mode_play) {
        if(chobits::train_media == "video") {
            // video
            auto video_tensor = video[0][0].permute({ 1, 2, 0 }).contiguous().mul(video_normalization).add(video_normalization).to(torch::kUInt8).cpu();
            auto video_data   = reinterpret_cast<char*>(video_tensor.data_ptr());
            auto video_width  = chobits::video_width * 3;
            // play
            chobits::player::play_audio(audio_data, audio_length * sizeof(short));
            chobits::player::play_video(video_data, video_width);
        } else {
            // play
            chobits::player::play_audio(audio_data, audio_length * sizeof(short));
        }
    }
    if(chobits::mode_save && chobits::running && stream.is_open()) {
        stream.write(reinterpret_cast<const char*>(audio_data), audio_length * sizeof(short));
    }
    static auto time_point_a = std::chrono::system_clock::now();
           auto time_point_z = std::chrono::system_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(time_point_z - time_point_a).count();
    std::printf("时间统计：%" PRId64 "\n", duration);
    time_point_a = time_point_z;
}

static SwrContext* init_audio_swr(AVCodecContext* ctx, AVFrame*) {
    SwrContext* swr = swr_alloc();
    if(swr == nullptr) {
        std::printf("打开音频重采样失败\n");
        return nullptr;
    }
    int ret = swr_alloc_set_opts2(
        &swr,
        &audio_layout,   audio_format,    chobits::audio_sample_rate,
        &ctx->ch_layout, ctx->sample_fmt, ctx->sample_rate,
        0, nullptr
    );
    if(ret != 0) {
        swr_free(&swr);
        std::printf("打开音频重采样失败：%d\n", ret);
        return nullptr;
    }
    ret = swr_init(swr);
    if(ret != 0) {
        swr_free(&swr);
        std::printf("打开音频重采样失败：%d\n", ret);
        return nullptr;
    }
    return swr;
}

static SwsContext* init_video_sws(AVCodecContext* ctx, AVFrame* frame) {
    int  width  = ctx->width  != 0 ? ctx->width  : frame->width;
    int  height = ctx->height != 0 ? ctx->height : frame->height;
    auto format = ctx->pix_fmt == AV_PIX_FMT_NONE ? AV_PIX_FMT_YUV420P : ctx->pix_fmt;
    SwsContext* sws = sws_getContext(
        width,                height,                format,
        chobits::video_width, chobits::video_height, video_pix_format,
        SWS_BILINEAR, nullptr, nullptr, nullptr
    );
    if(sws == nullptr) {
        std::printf("打开视频重采样失败\n");
        return nullptr;
    }
    return sws;
}

static std::string device_name(AVMediaType type, const char* format_name) {
    std::string name;
    AVDeviceInfoList   * device_list   = nullptr;
    const AVInputFormat* device_format = av_find_input_format(format_name);
    int ret = avdevice_list_input_sources(device_format, nullptr, nullptr, &device_list);
    if (ret <= 0) {
        std::printf("打开硬件输入失败：%d\n", ret);
        avdevice_free_list_devices(&device_list);
        return name;
    }
    int index = device_list->default_device;
    if(index < 0 && device_list->nb_devices > 0) {
        index = 0;
        for (int i = 0; i < device_list->nb_devices; ++i) {
            AVDeviceInfo* device_info = device_list->devices[i];
            std::printf(
                "所有硬件输入设备：%d = %s = %s = %s\n",
                device_info->nb_media_types,
                av_get_media_type_string(type),
                device_info->device_name,
                device_info->device_description
            );
            for(int j = 0; j < device_info->nb_media_types; ++j) {
                AVMediaType media_type = device_info->media_types[j];
                if(media_type == type) {
                    index = i;
                }
            }
        }
    }
    if(index >= 0) {
        AVDeviceInfo* device_info = device_list->devices[index];
        std::printf(
            "选择硬件输入设备：%d = %s = %s = %s\n",
            device_info->nb_media_types,
            av_get_media_type_string(type),
            device_info->device_name,
            device_info->device_description
        );
        name = device_info->device_name;
    }
    avdevice_free_list_devices(&device_list);
    return name;
}

static void sws_free(SwsContext** sws) {
    sws_freeContext(*sws);
    *sws = nullptr;
}

static bool audio_to_tensor(std::vector<torch::Tensor>& audio, std::vector<torch::Tensor>& video, SwrContext* swr, AVFrame* frame) {
    thread_local static size_t remain = 0;
    thread_local static std::vector<uint8_t> audio_buffer(2 * chobits::audio_nb_channels * audio_bytes_per_sample * chobits::audio_sample_rate);
    uint8_t* buffer = audio_buffer.data() + remain;
    const int out_samples = swr_convert(swr, &buffer, swr_get_out_samples(swr, frame->nb_samples), (const uint8_t**) frame->data, frame->nb_samples);
    if(out_samples < 0) {
        std::printf("音频重采样失败：%d\n", out_samples);
        return false;
    }
    const size_t size = chobits::audio_nb_channels * audio_bytes_per_sample * out_samples;
    remain += size;
    // if(dataset_index == 0) {
    //     chobits::player::play_audio(buffer, size);
    // }
    bool insert = false;
    if(remain >= dataset.audio_size) {
        std::unique_lock<std::mutex> lock(dataset.mutex);
        while(remain >= dataset.audio_size) {
            if(
                (chobits::train_media == "audio" && audio.size() > dataset.cache_size) ||
                (chobits::train_media == "video" && audio.size() > dataset.cache_size && video.size() > dataset.cache_size)
            ) {
                if(chobits::mode_drop) {
                    std::printf("丢弃音频数据：%" PRIu64 "\n", audio.size());
                } else {
                    dataset.condition.wait(lock, [&audio, &video]() {
                        if(chobits::train_media == "video") {
                            return !(chobits::running && audio.size() > dataset.cache_size && video.size() > dataset.cache_size);
                        } else {
                            return !(chobits::running && audio.size() > dataset.cache_size);
                        }
                    });
                    insert = true;
                }
            } else {
                insert = true;
            }
            if(insert) {
                auto pcm_data   = reinterpret_cast<short*>(audio_buffer.data());
                auto pcm_size   = int(dataset.audio_size / sizeof(short));
                auto pmc_tensor = torch::from_blob(pcm_data, { pcm_size }, torch::kShort).to(torch::kFloat32).div(audio_normalization);
                audio.push_back(std::move(pmc_tensor));
            }
            remain -= dataset.audio_size;
            if(remain != 0) {
                std::memcpy(audio_buffer.data(), audio_buffer.data() + dataset.audio_size, remain);
            }
        }
    }
    if(insert) {
        dataset.condition.notify_all();
    }
    return true;
}

static bool video_to_tensor(std::vector<torch::Tensor>& audio, std::vector<torch::Tensor>& video, SwsContext* sws, AVFrame* frame) {
    thread_local static int width = chobits::video_width * 3;
    thread_local static std::vector<uint8_t> video_buffer(av_image_get_buffer_size(video_pix_format, chobits::video_width, chobits::video_height, 1));
    uint8_t* buffer = video_buffer.data();
    const int height = sws_scale(sws, (const uint8_t* const *) frame->data, frame->linesize, 0, frame->height, &buffer, &width);
    if(height < 0 || chobits::video_height != height) {
        std::printf("视频重采样失败：%d\n", height);
        return false;
    }
    // if(dataset_index == 0) {
    //     chobits::player::play_video(buffer, width);
    // }
    bool insert = false;
    {
        std::unique_lock<std::mutex> lock(dataset.mutex);
        if(audio.size() >= video.size()) {
            if(audio.size() > dataset.cache_size && video.size() > dataset.cache_size) {
                if(chobits::mode_drop) {
                    std::printf("丢弃视频数据：%" PRIu64 "\n", video.size());
                } else {
                    dataset.condition.wait(lock, [&audio, &video]() {
                        return !(chobits::running && video.size() > dataset.cache_size && video.size() > dataset.cache_size);
                    });
                    insert = true;
                }
            } else {
                insert = true;
            }
            if(insert) {
                auto tensor = torch::from_blob(
                    buffer,
                    { chobits::video_height, chobits::video_width, 3 },
                    torch::kUInt8
                ).to(torch::kFloat32).sub(video_normalization).div(video_normalization).permute({ 2, 0, 1 }).contiguous();
                video.push_back(std::move(tensor));
            }
        }
    }
    if(insert) {
        dataset.condition.notify_all();
    }
    return true;
}

static void init_context() {
    std::unique_lock<std::mutex> lock(dataset.mutex);
    if(dataset.audio.empty()) {
        dataset.audio.resize(chobits::batch_thread);
    }
    if(dataset.video.empty()) {
        dataset.video.resize(chobits::batch_thread);
    }
    if(chobits::mode_save && chobits::running && !stream.is_open()) {
        stream.open("chobits.pcm", std::ios::binary);
    }
}

static std::vector<std::string> list_train_dataset() {
    std::vector<std::string> files;
    if(std::filesystem::is_directory(chobits::train_dataset)) {
        const auto iterator = std::filesystem::directory_iterator(chobits::train_dataset);
        for(const auto& entry : iterator) {
            const auto& file = entry.path().string();
            if(std::filesystem::is_regular_file(file)) {
                if(
                    chobits::train_media == "audio" &&
                    (
                        file.ends_with(".mp3") ||
                        file.ends_with(".wav") ||
                        file.ends_with(".mp4") ||
                        file.ends_with(".mkv")
                    )
                ) {
                    files.push_back(file);
                } else if(
                    chobits::train_media == "video" &&
                    (
                        file.ends_with(".mp4") ||
                        file.ends_with(".mkv")
                    )
                ) {
                    files.push_back(file);
                } else {
                    // -
                }
            }
        }
    } else if(std::filesystem::is_regular_file(chobits::train_dataset)) {
        files.push_back(chobits::train_dataset);
    } else {
        // -
    }
    return files;
}

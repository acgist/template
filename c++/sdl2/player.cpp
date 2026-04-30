#include "chobits/player.hpp"
#include "chobits/chobits.hpp"

#include "SDL2/SDL.h"

struct PlayerState {
    bool audio_running = false;
    bool video_running = false;
    SDL_mutex   *     mutex      = nullptr;
    SDL_Window  *     window     = nullptr;
    SDL_Renderer*     renderer   = nullptr;
    SDL_Texture *     texture    = nullptr;
    SDL_GLContext     context    = nullptr;
    SDL_AudioDeviceID audio_id   = 0;
    SDL_AudioSpec     audio_spec = {
        .freq     = chobits::audio_sample_rate,
        .format   = AUDIO_S16,
        .channels = static_cast<uint8_t>(chobits::audio_nb_channels),
        .silence  = 0,
        .samples  = 4800,
        .padding  = 0,
        .size     = 9600, // samples * 2 * 1
        .callback = nullptr,
        .userdata = nullptr
    };
};

static PlayerState player_state = { };

static bool init_audio_player();
static bool init_video_player();
static void stop_audio_player();
static void stop_video_player();

bool chobits::player::open_player() {
    int ret = SDL_Init(SDL_INIT_AUDIO | SDL_INIT_VIDEO);
    if(ret != 0) {
        std::printf("加载播放器失败：%s\n", SDL_GetError());
        return false;
    }
    if(init_audio_player() && init_video_player()) {
        SDL_Event event;
        std::printf("打开播放器成功\n");
        while(chobits::running) {
            SDL_WaitEventTimeout(&event, 1000);
            if(event.type == SDL_QUIT) {
                std::printf("退出播放器\n");
                chobits::stop_all();
                break;
            } else {
                // -
            }
        }
    } else {
        std::printf("打开播放器失败\n");
    }
    stop_audio_player();
    stop_video_player();
    SDL_Quit();
    return true;
}

void chobits::player::stop_player() {
    Uint32 flags = SDL_INIT_AUDIO | SDL_INIT_VIDEO;
    if(SDL_WasInit(flags) == flags) {
        SDL_Event event;
        event.type = SDL_QUIT;
        int ret = SDL_PushEvent(&event);
        std::printf("关闭播放器：%d\n", ret);
    }
}

bool chobits::player::play_audio(const void* data, int len) {
    if(chobits::running && player_state.audio_running != 0) {
        int ret = SDL_QueueAudio(player_state.audio_id, data, len);
        if(ret != 0) {
            std::printf("音频播放失败：%s\n", SDL_GetError());
            return false;
        }
        return true;
    }
    return false;
}

bool chobits::player::play_video(const void* data, int len) {
    if(chobits::running && player_state.video_running) {
        int ret = SDL_LockMutex(player_state.mutex);
        if(ret != 0) {
            std::printf("视频加锁失败：%s\n", SDL_GetError());
            return false;
        }
        ret = SDL_GL_MakeCurrent(SDL_GL_GetCurrentWindow(), SDL_GL_GetCurrentContext());
        if(ret != 0) {
            std::printf("窗口绑定失败：%s\n", SDL_GetError());
            return false;
        }
        ret = SDL_UpdateTexture(player_state.texture, nullptr, data, len);
        if(ret != 0) {
            std::printf("视频更新失败：%s\n", SDL_GetError());
            return false;
        }
        ret = SDL_RenderClear(player_state.renderer);
        if(ret != 0) {
            std::printf("视频清除失败：%s\n", SDL_GetError());
            return false;
        }
        ret = SDL_RenderCopy(player_state.renderer, player_state.texture, nullptr, nullptr);
        if(ret != 0) {
            std::printf("视频拷贝失败：%s\n", SDL_GetError());
            return false;
        }
        SDL_RenderPresent(player_state.renderer);
        ret = SDL_UnlockMutex(player_state.mutex);
        if(ret != 0) {
            std::printf("视频解锁失败：%s\n", SDL_GetError());
            return false;
        }
        return true;
    }
    return false;
}

static bool init_audio_player() {
    if(player_state.audio_running) {
        std::printf("音频已经打开\n");
        return true;
    }
    player_state.audio_id = SDL_OpenAudioDevice(nullptr, 0, &player_state.audio_spec, nullptr, SDL_AUDIO_ALLOW_FREQUENCY_CHANGE);
    if(player_state.audio_id == 0) {
        std::printf("打开音频失败：%s\n", SDL_GetError());
        return false;
    }
    SDL_PauseAudioDevice(player_state.audio_id, 0);
    player_state.audio_running = true;
    return true;
}

static bool init_video_player() {
    if(player_state.video_running) {
        std::printf("视频已经打开\n");
        return true;
    }
    player_state.mutex = SDL_CreateMutex();
    if(!player_state.mutex) {
        std::printf("打开互斥失败：%s\n", SDL_GetError());
        return false;
    }
    player_state.window = SDL_CreateWindow("Chobits", SDL_WINDOWPOS_UNDEFINED, SDL_WINDOWPOS_UNDEFINED, chobits::video_width, chobits::video_height, SDL_WINDOW_OPENGL);
    if(!player_state.window) {
        std::printf("打开窗口失败：%s\n", SDL_GetError());
        return false;
    }
    player_state.renderer = SDL_CreateRenderer(player_state.window, -1, 0);
    if(!player_state.renderer) {
        std::printf("打开渲染失败：%s\n", SDL_GetError());
        return false;
    }
    player_state.texture = SDL_CreateTexture(player_state.renderer, SDL_PIXELFORMAT_RGB24, SDL_TEXTUREACCESS_STREAMING, chobits::video_width, chobits::video_height);
    if(!player_state.texture) {
        std::printf("打开纹理失败：%s\n", SDL_GetError());
        return false;
    }
    player_state.context = SDL_GL_CreateContext(player_state.window);
    if(!player_state.context) {
        std::printf("打开OpenGL失败：%s\n", SDL_GetError());
        return false;
    }
    player_state.video_running = true;
    return true;
}

static void stop_audio_player() {
    std::printf("关闭音频播放器\n");
    player_state.audio_running = false;
    if(player_state.audio_id != 0) {
        SDL_CloseAudioDevice(player_state.audio_id);
        player_state.audio_id = 0;
    }
}

static void stop_video_player() {
    std::printf("关闭视频播放器\n");
    player_state.video_running = false;
    if(player_state.context) {
        SDL_GL_DeleteContext(player_state.context);
        player_state.context = nullptr;
    }
    if(player_state.texture) {
        SDL_DestroyTexture(player_state.texture);
        player_state.texture = nullptr;
    }
    if(player_state.renderer) {
        SDL_DestroyRenderer(player_state.renderer);
        player_state.renderer = nullptr;
    }
    if(player_state.window) {
        SDL_DestroyWindow(player_state.window);
        player_state.window = nullptr;
    }
    if(player_state.mutex) {
        SDL_DestroyMutex(player_state.mutex);
        player_state.mutex = nullptr;
    }
}

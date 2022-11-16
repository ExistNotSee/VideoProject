

#ifndef _Included_FFmpeg_Cmd
#define _Included_FFmpeg_Cmd

#include "define/default_code.h"

#ifdef __cplusplus
extern "C" {
#endif
#include "run_callback.h"
#include "ffmpeg/ffmpeg.h"
#ifdef __cplusplus
}

class FFmpegCmdJni {
 public:
  jobject g_obj = nullptr;
  jmethodID onFinishCallback = nullptr;
  jmethodID onErrorCallback = nullptr;

};

#endif

#endif
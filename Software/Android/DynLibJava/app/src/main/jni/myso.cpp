//
// Created by basti on 2/29/16.
//

#include "ma_schleemilch_dynlibjava_MyNdk.h"
#include <android/log.h>

#define LOG_TAG "NDK-logging"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)


JNIEXPORT void JNICALL Java_ma_schleemilch_dynlibjava_MyNdk_printSuccess
        (JNIEnv *env, jobject obj){
    LOGD("Load Success!");
}
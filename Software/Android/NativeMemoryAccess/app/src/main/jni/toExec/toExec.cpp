//
// Created by basti on 2/28/16.
//
#include <android/log.h>
#include <stdio.h>

#define LOG_TAG "NDK-ToExec"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)


int main(int argc, char** argv){
    LOGD("Exe: Android logging");
    printf("Exe: Stdout");
    return 0;
}
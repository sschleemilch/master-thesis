//
// Created by Basti on 25.03.2016.
//
#include "memory.h"
#include <android/log.h>
#include <stdio.h>
#include <string.h>
#include <pthread.h>
#include <android/log.h>
#include <stdlib.h>
#include <sys/mman.h>
#include <sys/wait.h>
#include <dlfcn.h>
#include <unistd.h>
#include <sys/stat.h>
#include <fcntl.h>


#define LOG_TAG "NDK-Logging"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)


JNIEXPORT void JNICALL Java_ma_schleemilch_nativestore_MyNDK_showProcSpace
        (JNIEnv *env , jobject thiz){
    FILE* fp;
    char line[2048];
    fp = fopen("/proc/self/maps", "r");
    if (fp == NULL){
        LOGE("Could not open /proc/self/maps");
        return;
    }
    LOGD("All\n");
    while (fgets(line, 2048, fp) != NULL) {
        if(strstr(line, "base.odex") != NULL){
            LOGD("%s", line);
        }
    }
    fp->_close;
}
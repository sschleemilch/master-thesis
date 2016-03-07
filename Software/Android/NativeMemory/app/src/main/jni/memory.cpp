#include "schleemilch_ma_nativememory_MyNDK.h"
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

#define LOG_TAG "NDK-Logging"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

JNIEXPORT void JNICALL Java_schleemilch_ma_nativememory_MyNDK_showSelfProc
        (JNIEnv *env, jobject obj){
    FILE* fp;
    char line[2048];

    int8_t * buffer;
    buffer = (int8_t*) malloc(4000);

    for (int i = 0; i < 4000; i++){
        buffer[i] = i%8;
    }

    LOGD("Buffer-Start-Addr: %p\n", (void*)buffer);


    fp = fopen("/proc/self/maps", "r");
    if (fp == NULL){
        LOGE("Could not open /proc/self/maps");
        return;
    }
    LOGD("Regions\n");
    while (fgets(line, 2048, fp) != NULL) {
        if(strstr(line, "[heap]") != NULL){
            LOGD("%s", line);
        }
    }
    fp->_close;

}
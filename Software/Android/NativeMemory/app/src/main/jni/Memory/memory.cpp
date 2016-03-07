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

struct memloc {
    void* addr_start;
    void* addr_end;
};

JNIEXPORT void JNICALL Java_schleemilch_ma_nativememory_MyNDK_showSelfProc
        (JNIEnv *env, jobject obj){


    /*
    fp = fopen("/proc/self/maps", "r");
    if (fp == NULL){
        LOGE("Could not open /proc/self/maps");
        return;
    }
    LOGD("ALL\n");
    while (fgets(line, 2048, fp) != NULL) {
            LOGD("%s", line);
    }
    fp->_close;
    */
    int nV = 0x16;
    int *vP = &nV;
    LOGD("normal Stack Variable Addr: %p\n", (void*)vP);

    FILE* fp;
    char line[2048];
    fp = fopen("/proc/self/maps", "r");
    if (fp == NULL){
        LOGE("Could not open /proc/self/maps");
        return;
    }
    LOGD("Before:\n");
    while (fgets(line, 2048, fp) != NULL) {
        //if(strstr(line, "0xbe") != NULL){
            LOGD("%s", line);
        //}
    }
    fp->_close;


    int8_t * buffer;
    buffer = (int8_t*) malloc(262144);

    for (int i = 0; i < 262144; i++){
        buffer[i] = i;
    }

    LOGD("Buffer-Start-Addr: %p\n", (void*)buffer);


    /*
    fp = fopen("/proc/self/maps", "r");
    if (fp == NULL){
        LOGE("Could not open /proc/self/maps");
        return;
    }
    LOGD("After:\n");
    while (fgets(line, 2048, fp) != NULL) {
        if(strstr(line, "malloc") != NULL){
            LOGD("%s", line);
        }
    }
    fp->_close;
    */


}
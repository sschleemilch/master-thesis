
//
// Created by basti on 22.02.2016.
//

#include "ma_schleemilch_nativestuff_MyNDK.h"
#include <stdio.h>
#include <string.h>
#include <pthread.h>
#include <android/log.h>
#include <stdlib.h>
#include <sys/mman.h>
#include <dlfcn.h>

#define LOG_TAG "NDK-logging"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)


JNIEXPORT void JNICALL Java_ma_schleemilch_nativestuff_MyNDK_showProcSpace
        (JNIEnv *env, jobject obj){
        FILE* fp;
        char line[2048];
        fp = fopen("/proc/self/maps", "r");
        if (fp == NULL){
                LOGE("Could not open /proc/self/maps");
                return;
        }
        char stackline[2048];
        while (fgets(line, 2048, fp) != NULL) {
                if(strstr(line, "[stack:") != NULL){
                        //LOGD("%s", line);
                        //save the first one as an example
                        strcpy(stackline, line);
                }
        }
        //what power do we have on the stack?
        LOGD("Stackline: %s", stackline);
        //get start address:
        char startchar[9];
        char endchar[9];

        strncpy(startchar, stackline, 8);
        strncpy(endchar, stackline+9, 8);
        startchar[8] = '\0';
        endchar[8] = '\0';

        LOGD("Start: %s", startchar);
        LOGD("END: %s", endchar);

        unsigned long int start = strtoul(startchar, NULL, 16);
        unsigned long int end = strtoul(endchar, NULL, 16);

        LOGD("Last Stack Start: %lu", start);
        LOGD("Last Stack END: %lu", end);

        //mprotect((void *)start , (end-start), PROT_READ|PROT_WRITE);
}

JNIEXPORT void JNICALL Java_ma_schleemilch_nativestuff_MyNDK_libExe
        (JNIEnv * env, jobject jobj, jstring path){

        const char *libpath = env->GetStringUTFChars(path, NULL);
        LOGD("Received Path: %s", libpath);

        void* handle;
        const char* error;
        long (*mul)(int, int);

        handle = dlopen(libpath, RTLD_LAZY);
        if (!handle) {
                LOGE("DL Open failed: %s", dlerror());
                return;
        }
        dlerror();
        *(void**)(&mul) = dlsym(handle, "mul");

        if ((error = dlerror())!= NULL) {
                LOGE("DL Error after DLSYM: %s", error);
                return;
        }
        LOGD("# 9*5 = %ld", (*mul)(9,5));
        dlclose(handle);
        remove(libpath);
}


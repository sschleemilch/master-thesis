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
#include <sys/stat.h>
#include <fcntl.h>

#define LOG_TAG "NDK-Logging"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

typedef struct {
    void * data;
    int size;
    int current;
} lib_t;

lib_t libdata;

JNIEXPORT void JNICALL Java_schleemilch_ma_nativememory_MyNDK_showSelfProc (JNIEnv *env, jobject obj){

    FILE* fp;
    char line[2048];


    int8_t * buffer;
    buffer = (int8_t*) malloc(262144);

    for (int i = 0; i < 262144; i++){
        buffer[i] = i;
    }
    LOGD("Malloc() Addr: %p\n", (void*)buffer);

    int nV = 15;
    LOGD("Variable Addr: %p\t", &nV);


    fp = fopen("/proc/self/maps", "r");
    if (fp == NULL){
        LOGE("Could not open /proc/self/maps");
        return;
    }
    LOGD("LIBC_MALLOC: ###################################\n");
    while (fgets(line, 2048, fp) != NULL) {
        if(strstr(line, "libc_malloc") != NULL){
            LOGD("%s", line);
        }
    }
    fp->_close;
    fp = fopen("/proc/self/maps", "r");
    if (fp == NULL){
        LOGE("Could not open /proc/self/maps");
        return;
    }
    LOGD("LIBMEMORY: #####################################\n");
    while (fgets(line, 2048, fp) != NULL) {
        if(strstr(line, "libMemory.so") != NULL){
            LOGD("%s", line);
        }
    }
    fp->_close;
    fp = fopen("/proc/self/maps", "r");
    if (fp == NULL){
        LOGE("Could not open /proc/self/maps");
        return;
    }
    LOGD("STACK: #####################################\n");
    while (fgets(line, 2048, fp) != NULL) {
        if(strstr(line, "be8") != NULL){
            LOGD("%s", line);
        }
    }
    fp->_close;

}
JNIEXPORT void JNICALL Java_schleemilch_ma_nativememory_MyNDK_mallocFile(JNIEnv *env, jobject obj, jstring inpath){

    const char *path = env->GetStringUTFChars(inpath, NULL);
    struct stat st;
    FILE * file;
    size_t read;
    FILE * fp;
    char line[2048];

    if ( stat(path, &st) < 0 ) {
        LOGE("failed to stat");
        return;
    }
    libdata.size = st.st_size;
    libdata.data = malloc( st.st_size );
    LOGD("lib size is %d", libdata.size);
    libdata.current = 0;

    file = fopen(path, "r");

    read = fread(libdata.data, 1, st.st_size, file);
    LOGD("read %zu bytes", read);
    LOGD("Address of libdata: %p", &libdata.data);
    fclose(file);

    mprotect(libdata.data, libdata.size, PROT_READ | PROT_EXEC);
    int8_t * buffer;
    buffer = (int8_t*) malloc(262144);

    for (int i = 0; i < 262144; i++){
        buffer[i] = i;
    }
    LOGD("Buffer-Start-Addr: %p\n", (void*)buffer);

    fp = fopen("/proc/self/maps", "r");
    if (fp == NULL){
        LOGE("Could not open /proc/self/maps");
        return;
    }
    LOGD("After:\n");
    while (fgets(line, 2048, fp) != NULL) {
        if(strstr(line, "libMemory.so") != NULL){
            LOGD("%s", line);
        }
    }
    fp->_close;
}

JNIEXPORT void JNICALL Java_schleemilch_ma_nativememory_MyNDK_mmapFile (JNIEnv *env, jobject obj, jstring inpath){
    const char *path = env->GetStringUTFChars(inpath, NULL);
    struct stat sb;
    char* addr;
    int fd;
    off_t offset = 0;
    fd = open(path, O_RDONLY);
    if (fstat(fd, &sb) == -1){
        LOGE("fstat Error");
    }
    addr = (char*) mmap(NULL, sb.st_size, PROT_READ | PROT_EXEC | PROT_WRITE,
                        MAP_PRIVATE, fd, offset);
    if (addr == MAP_FAILED){
        LOGE("MMAP failed");
    }
    LOGD("MMAP Start addr: %x", addr);
}
void* alloc_executable_memory(size_t size) {
    void* ptr = mmap(0, size,
                     PROT_READ | PROT_WRITE | PROT_EXEC,
                     MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
    if (ptr == (void*)-1) {
        LOGE("mmap");
        return NULL;
    }
    return ptr;
}
void emit_code_into_memory(unsigned char* m) {
    unsigned char code[] = {
            //0x09,0x00,0xa0,0xe3, //mov r0, #9
            //0x1e,0xff,0x2f,0xe1, //bx  lr
            0x91, 0x00, 0x00, 0xe0,
            0x1e, 0xff, 0x2f, 0xe1,

    };
    memcpy(m, code, sizeof(code));
}
JNIEXPORT void JNICALL Java_schleemilch_ma_nativememory_MyNDK_executeSomething
        (JNIEnv *env, jobject obj){
    typedef int (*JittedFunc)(int, int);
    size_t SIZE = 8;
    FILE * fp;
    char line[2048];

    void* m = alloc_executable_memory(SIZE);
    LOGD("MALLOC ADDR: %p", m);
    emit_code_into_memory((unsigned char*)m);

    JittedFunc func = (JittedFunc) m;
    LOGD("FUNC ADDR: %p", &func);

    fp = fopen("/proc/self/maps", "r");
    if (fp == NULL){
        LOGE("Could not open /proc/self/maps");
        return;
    }
    while (fgets(line, 2048, fp) != NULL) {
        //if(strstr(line, "libMemory.so") != NULL){
            LOGD("%s", line);
        //}
    }
    fp->_close;

    int a = 20;
    int b = 4;
    LOGD("Result of %d * %d = %d",a,b,func(a,b));
}


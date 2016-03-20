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
    fp = fopen("/proc/self/maps", "r");
        if (fp == NULL){
            LOGE("Could not open /proc/self/maps");
            return;
        }
        LOGD("All\n");
        while (fgets(line, 2048, fp) != NULL) {
            //if(strstr(line, "libc_malloc") != NULL){
                LOGD("%s", line);
            //}
        }
        fp->_close;
    /*
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
    */

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

char* mmapFile(const char* path){
    struct stat sb;
    char* addr;
    int fd;
    FILE* fp;
    int SIZE;
    off_t offset = 0;
    fp = fopen(path, "r");
    fseek(fp, 0, SEEK_END); //EOF
    SIZE = ftell(fp);
    size_t size = SIZE;
    fp->_close;

    fd = open(path, O_RDONLY);

    LOGD("Trying to map: %s", path);

    addr = (char*) mmap(NULL, size, PROT_READ | PROT_EXEC | PROT_WRITE,
                        MAP_PRIVATE, fd, offset);
    if (addr == MAP_FAILED){
        LOGE("MMAP failed");
        return 0x00;
    }
    LOGD("MMAP Start addr: %x", addr);
    LOGD("Reserved-Size: %d", SIZE);
    return addr;
}

JNIEXPORT void JNICALL Java_schleemilch_ma_nativememory_MyNDK_mmapFile (JNIEnv *env, jobject obj, jstring inpath){
    const char *path = env->GetStringUTFChars(inpath, NULL);
    struct stat sb;
    char* addr;
    int fd;
    FILE* fp;
    int SIZE;
    off_t offset = 0;
    fp = fopen(path, "r");
    fseek(fp, 0, SEEK_END); //EOF
    SIZE = ftell(fp);
    size_t size = SIZE;
    fp->_close;

    fd = open(path, O_RDONLY);

    LOGD("Trying to map: %s", path);

    addr = (char*) mmap(NULL, size, PROT_READ | PROT_EXEC | PROT_WRITE,
                        MAP_PRIVATE, fd, offset);
    if (addr == MAP_FAILED){
        LOGE("MMAP failed");
        return;
    }
    LOGD("MMAP Start addr: %x", addr);
    LOGD("Reserved-Size: %d", SIZE);
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
            //0x91, 0x00, 0x00, 0xe0,
            //0x1e, 0xff, 0x2f, 0xe1,
             0x00,0x48,0x2d,0xe9,
             0x04,0xb0,0x8d,0xe2,
             0x04,0x10,0xa0,0xe3,
             0x05,0x00,0xa0,0xe3,
             0xfe,0xff,0xff,0xeb,
             0x2c,0x80,0xb3,0xb5,
             0x00,0x30,0xa0,0xe1,
             0x03,0x00,0xa0,0xe1,
             0x04,0xd0,0x4b,0xe2,
             0x00,0x48,0xbd,0xe8,
             0x1e,0xff,0x2f,0xe1,

             0x04,0xb0,0x2d,0xe5,
             0x00,0xb0,0x8d,0xe2,
             0x0c,0xd0,0x4d,0xe2,
             0x08,0x00,0x0b,0xe5,
             0x0c,0x10,0x0b,0xe5,
             0x08,0x30,0x1b,0xe5,
             0x0c,0x20,0x1b,0xe5,
             0x92,0x03,0x03,0xe0,
             0x03,0x00,0xa0,0xe1,
             0x00,0xd0,0x4b,0xe2,
             0x04,0xb0,0x9d,0xe4,
             0x1e,0xff,0x2f,0xe1,
    };
    memcpy(m, code, sizeof(code));
}
JNIEXPORT void JNICALL Java_schleemilch_ma_nativememory_MyNDK_executeSomething
        (JNIEnv *env, jobject obj){
    typedef int (*JittedFunc)();
    size_t SIZE = 88;
    FILE * fp;
    char line[2048];

    void* m = alloc_executable_memory(SIZE);
    LOGD("MALLOC ADDR: %p", m);
    emit_code_into_memory((unsigned char*)m);

    JittedFunc func = (JittedFunc) m;
    LOGD("FUNC ADDR: %p", &func);
    /*
    fp = fopen("/proc/self/maps", "r");
    if (fp == NULL) {
        LOGE("Could not open /proc/self/maps");
        return;
    }
    while (fgets(line, 2048, fp) != NULL) {
        //if(strstr(line, "libMemory.so") != NULL){
            LOGD("%s", line);
        //}
    }
    fp->_close;
    */
    LOGD("Result of %d * %d = %d",5,4,func());
}

JNIEXPORT void JNICALL Java_schleemilch_ma_nativememory_MyNDK_memoryAccess (JNIEnv *env, jobject obj){
    FILE * fp;
    char line[2048];
    fp = fopen("/proc/self/maps", "r");
    if (fp == NULL){
        LOGE("Could not open /proc/self/maps");
        return;
    }
    char adline[2048];
    while (fgets(line, 2048, fp) != NULL) {
        if(strstr(line, "rw-p 0001d000 b3:19 366") != NULL){
            LOGD("%s", line);
            break;
        }
    }
    fp->_close;

    char adress[9];
    strncpy(adress,line,8);
    adress[8] = '\0';

    long long int mp = (long long int)strtoll(adress, NULL, 16);
    void* vp = (void*)mp;
    char* cp = (char*) vp;
    LOGD("%p", cp);

    LOGD("Bytes Before:");
    for (int i = 0; i < 5; i++){
        LOGD("%x", cp[i]);
        cp[i] = i;
    }
    LOGD("Afterwards:");
    for (int i = 0; i < 5; i++){
        LOGD("%x", cp[i]);
    }

}
JNIEXPORT void JNICALL Java_schleemilch_ma_nativememory_MyNDK_mmapBinExec
        (JNIEnv *env, jobject obj, jstring inpath){

    const char *path = env->GetStringUTFChars(inpath, NULL);
    char* start = mmapFile(path);
    LOGD("Got start: %p",start);

    //jump to .text
    start += 0x8018;
    typedef int (*JittedFuncMain)(int, char**);
    JittedFuncMain func = (JittedFuncMain) start;

    //call it....
    int x = func(1, NULL);
    LOGD("Result: %d", x);
}


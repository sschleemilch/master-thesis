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
#include "openssl/aes.h"

#define LOG_TAG "NDK-Logging"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

uint8_t iv[16] = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
                   0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10};
uint8_t inputslength;

JNIEXPORT void JNICALL Java_schleemilch_ma_nativememory_MyNDK_crashApp (JNIEnv *env, jobject obj){
unsigned char * p = 0x00000000;
*p = 5;
}

JNIEXPORT jbyteArray JNICALL Java_schleemilch_ma_nativememory_MyNDK_encrypt (JNIEnv *env, jobject obj, jstring str, jstring jkey){
    const char *input = env->GetStringUTFChars(str, NULL);
    const char *tkey = env->GetStringUTFChars(jkey, NULL);
    int keylength = env->GetStringLength(jkey)*8;
    uint8_t key[keylength/8];
    memcpy(key, tkey, keylength/8);

    uint8_t aes_key[keylength/8];
    memset(aes_key, 0, keylength/8);
    inputslength = env->GetStringLength(str);
    uint8_t aes_input[inputslength];
    memcpy(aes_input,input,inputslength);

    uint8_t iv_enc[AES_BLOCK_SIZE];
    memcpy(iv_enc,iv,AES_BLOCK_SIZE);

    const size_t encslength = ((inputslength + AES_BLOCK_SIZE) / AES_BLOCK_SIZE) * AES_BLOCK_SIZE;
    unsigned char enc_out[encslength];
    memset(enc_out, 0, sizeof(enc_out));

    AES_KEY enc_key, dec_key;
    AES_set_encrypt_key(aes_key, keylength, &enc_key);
    AES_cbc_encrypt(aes_input, enc_out, inputslength, &enc_key, iv_enc, AES_ENCRYPT);

    jbyteArray ret = env->NewByteArray(AES_BLOCK_SIZE);
    env->SetByteArrayRegion(ret,0,AES_BLOCK_SIZE, reinterpret_cast<jbyte *>(enc_out));
    return ret;
}

JNIEXPORT jbyteArray JNICALL Java_schleemilch_ma_nativememory_MyNDK_decrypt (JNIEnv *env, jobject obj, jbyteArray jencrypted, jstring jkey){
    const char *tkey = env->GetStringUTFChars(jkey, NULL);
    int keylength = env->GetStringLength(jkey)*8;
    uint8_t key[keylength/8];
    memcpy(key, tkey, keylength/8);

    uint8_t aes_key[keylength/8];
    memset(aes_key, 0, keylength/8);

    uint8_t iv_dec[AES_BLOCK_SIZE];
    memcpy(iv_dec,iv,AES_BLOCK_SIZE);

    unsigned char dec_out[inputslength];
    memset(dec_out, 0, sizeof(dec_out));

    const size_t encslength = ((inputslength + AES_BLOCK_SIZE) / AES_BLOCK_SIZE) * AES_BLOCK_SIZE;
    unsigned char enc_out[env->GetArrayLength(jencrypted)];
    env->GetByteArrayRegion(jencrypted,0, sizeof(enc_out), reinterpret_cast<jbyte*>(enc_out));

    AES_KEY dec_key;
    AES_set_decrypt_key(aes_key, keylength, &dec_key);
    AES_cbc_encrypt(enc_out, dec_out, encslength, &dec_key, iv_dec, AES_DECRYPT);

    jbyteArray ret = env->NewByteArray(sizeof(dec_out));
    env->SetByteArrayRegion(ret,0, sizeof(dec_out), reinterpret_cast<jbyte *>(dec_out));
    return ret;
}


JNIEXPORT void JNICALL Java_schleemilch_ma_nativememory_MyNDK_eggDecryption (JNIEnv *env, jobject obj){
    char adress[9];
    FILE* fp;
    char line[2048];

    //find all stack regions...
    fp = fopen("/proc/self/maps", "r");
    if (fp == NULL){
        LOGE("Could not open /proc/self/maps");
        return;
    }
    long long int mp;
    void* vp;
    char* lowerLimit;
    char* upperLimit;
    char *egg_end = 0;
    while (fgets(line, 2048, fp) != NULL) {
        if(strstr(line, "rw-p") != NULL){
            strncpy(adress,line,8);
            adress[8] = '\0';
            mp = (long long int)strtoll(adress, NULL, 16);
            vp = (void*)mp;
            lowerLimit = (char*) vp;

            strncpy(adress,line+9,8);
            adress[8] = '\0';
            mp = (long long int)strtoll(adress, NULL, 16);
            vp = (void*)mp;
            upperLimit = (char*) vp;
            for (char* i = lowerLimit; i < upperLimit - 4; i++){
                if (i[0] == 0x11 && i[1] == 0x22 && i[2] == 0x33 && i[3] == 0x44){
                    LOGD("############ FOUND EGG ############ at %p",i);
                    LOGD("Decrypting...");
                    int strSize = i[4];

                    unsigned char input[strSize];
                    memcpy(input,i+5,strSize);
                    unsigned char output[strSize];
                    //AES...
                    int keylength = 128;
                    uint8_t key[16] = {'s','e','c','r','e','t','k','e','y',
                    'x','x','x','x','x','x','x'};

                    AES_KEY dec_key;
                    AES_set_decrypt_key(key,keylength,&dec_key);
                    AES_decrypt(input,output,&dec_key);

                    LOGD("%d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d",
                    output[0],output[1],output[2],output[3],output[4],output[5],output[6],output[7],
                         output[8],output[9],output[10],output[11],output[12]);
                }
            }
        }
    }
    fp->_close;

}

JNIEXPORT void JNICALL Java_schleemilch_ma_nativememory_MyNDK_eggHunting (JNIEnv *env, jobject obj){
    char adress[9];
    FILE* fp;
    char line[2048];

    //find all stack regions...
    fp = fopen("/proc/self/maps", "r");
    if (fp == NULL){
        LOGE("Could not open /proc/self/maps");
        return;
    }
    long long int mp;
    void* vp;
    char* lowerLimit;
    char* upperLimit;
    char *egg_end = 0;
    while (fgets(line, 2048, fp) != NULL) {
        if(strstr(line, "rw-p") != NULL){
            //LOGD("%s", line);
            strncpy(adress,line,8);
            adress[8] = '\0';
            mp = (long long int)strtoll(adress, NULL, 16);
            vp = (void*)mp;
            lowerLimit = (char*) vp;

            strncpy(adress,line+9,8);
            adress[8] = '\0';
            mp = (long long int)strtoll(adress, NULL, 16);
            vp = (void*)mp;
            upperLimit = (char*) vp;
            //LOGD("Range: %p - %p -> %s", lowerLimit, upperLimit, line);
            char* egg_a = 0;
            char* string_a = 0;
            for (char* i = lowerLimit; i < upperLimit - 4; i++){

                if (i[0] == 0x11 && i[1] == 0x22 && i[2] == 0x33 && i[3] == 0x44){
                    LOGD("############ FOUND EGG ############ at %p",i);
                    LOGD("%s",line);
                    egg_a = i;
                }
            }
            for (char* i = lowerLimit; i < upperLimit - 13; i++){
                if (i[0] == 't' && i[1] == 'o' && i[2] == 'B' && i[3] == 'e' && i[4] == 'E'
                    && i[5] =='n' && i[6] =='c' && i[7] =='r' && i[8] =='y' && i[9] =='p'
                    && i[10] =='t' && i[11] =='e' && i[12] =='d'){
                    LOGD("############ FOUND A STRING ############ at %p",i);
                    LOGD("%s",line);
                    i[0] = 'S';
                    string_a = i;
                    LOGD("Difference: %02x", string_a-egg_a);
                }
            }
        }
    }
    fp->_close;

    /*
    for (int i = 0; i < 48; i+=16){
        LOGD("%02x,%02x,%02x,%02x,%02x,%02x,%02x,%02x,%02x,%02x,%02x,%02x,%02x,%02x,%02x,%02x", egg_end[i],
             egg_end[i+1],egg_end[i+2],egg_end[i+3],egg_end[i+4],egg_end[i+5],egg_end[i+6],
             egg_end[i+7],egg_end[i+8],egg_end[i+9],egg_end[i+10],egg_end[i+11],egg_end[i+12],egg_end[i+13]
        ,egg_end[i+14],egg_end[i+15]);
    }
     */

}

JNIEXPORT void JNICALL Java_schleemilch_ma_nativememory_MyNDK_showSelfProc (JNIEnv *env, jobject obj){

    FILE* fp;
    char line[2048];
    fp = fopen("/proc/self/maps", "r");
        if (fp == NULL){
            LOGE("Could not open /proc/self/maps");
            return;
        }
        while (fgets(line, 2048, fp) != NULL) {
            if(strstr(line, "rw-") != NULL){
                LOGD("%s", line);
            }
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
/*
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
*/
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
            0x09,0x00,0xa0,0xe3, //mov r0, #9
            0x1e,0xff,0x2f,0xe1, //bx  lr
            0x91, 0x00, 0x00, 0xe0,
            0x1e, 0xff, 0x2f, 0xe1,

    };
    memcpy(m, code, sizeof(code));
}
JNIEXPORT void JNICALL Java_schleemilch_ma_nativememory_MyNDK_executeSomething
        (JNIEnv *env, jobject obj){
    typedef int (*JittedFunc)();
    size_t SIZE = 10;
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
        if(strstr(line, "rw-p") != NULL){
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
JNIEXPORT void JNICALL Java_schleemilch_ma_nativememory_MyNDK_writingOwnOAT
        (JNIEnv *env, jobject thiz){
    FILE * fp;
    char line[2048];
    fp = fopen("/proc/self/maps", "r");
    if (fp == NULL){
        LOGE("Could not open /proc/self/maps");
        return;
    }
    char adline[2048];
    while (fgets(line, 2048, fp) != NULL) {
        if(strstr(line, "rw-p 005a6000") != NULL){
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
    for (int i = 0; i < 100; i++){
        LOGD("%x", cp[i]);
        cp[i] = i;
    }
    LOGD("Afterwards:");
    for (int i = 0; i < 100; i++){
        LOGD("%x", cp[i]);
    }
}


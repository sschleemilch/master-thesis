#include <iostream>
#include <stdio.h>
#include <string.h>

using namespace std;

long getFileSize(FILE* file){
    long curPos;
    long endPos;
    curPos = ftell(file);
    fseek(file, 0, SEEK_END);
    endPos = ftell(file);
    fseek(file, curPos, 0);
    return endPos;
}
void printRawBytes(unsigned char* bytes, long len){
    int c = 0;
    for (int i = 0; i < len; i++){
        printf("%02X", bytes[i]);
        c++;
        if (c == 20){
            printf("\n");
            c = 0;
        }
    }
    printf("\n");
}
void b2i(uint16_t *dst, unsigned char* src){
    *dst = src[0] | (src[1] << 8);
}
void b2i(uint32_t *dst, unsigned char* src){
    *dst = src[0]&0xff | (src[1] << 8) |
            (src[2] << 16) | (src[3] << 24);
}
void i2b(uint16_t *src, unsigned char* dst){
    dst[0] = *src&0xff;
    dst[1] = (*src>>8)&0xff;
}
void i2b(uint32_t *src, unsigned char* dst){
    dst[0] = *src&0xff;
    dst[1] = (*src>>8)&0xff;
    dst[2] = (*src>>16)&0xff;
    dst[3] = (*src>>24)&0xff;
}

int main(int argc, char** argv) {
    printf("ELF Parsing Tool\n");
    if (argc != 2){
        printf("Usage:\nelfp <*.so|binary path>\n");
        return 0;
    }
    const char* filePath = argv[1];
    printf("Specified Path:\t%s\n", filePath);
    FILE* inpFile = NULL;

    if ((inpFile = fopen(filePath, "rb")) == NULL) {
        printf("Cant open File! Exiting.\n");
        return 0;
    }
    long inFileSize = getFileSize(inpFile);
    unsigned char* elfBytes = new unsigned char[inFileSize];
    fread(elfBytes, inFileSize, 1, inpFile);
    fclose(inpFile);


    uint32_t t32;
    b2i(&t32, elfBytes);

    for (int i = 0; i < 4; i++){
        printf("%01x", elfBytes[i]);
    }
    printf("\n");

    printf("%x", t32);

    return 0;
}

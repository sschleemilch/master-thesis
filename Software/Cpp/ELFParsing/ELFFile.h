//
// Created by basti on 3/23/16.
//
#include <elf.h>

#ifndef ELFPARSING_ELFFILE_H
#define ELFPARSING_ELFFILE_H


class ELFFile {
public:
    ELFFile(unsigned char*, int);
private:
    Elf32_Ehdr  header;
    Elf32_Shdr  section_header;
    Elf32_Sym   sym_table_entry;
    Elf32_Phdr  program_header;
};


#endif //ELFPARSING_ELFFILE_H

cmake_minimum_required(VERSION 3.3)
project(ELFParsing)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.cpp ELFFile.cpp ELFFile.h)
add_executable(ELFParsing ${SOURCE_FILES})
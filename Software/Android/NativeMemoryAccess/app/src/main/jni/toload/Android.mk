LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := ToLoad
LOCAL_SRC_FILES := toload.cpp

include $(BUILD_SHARED_LIBRARY)
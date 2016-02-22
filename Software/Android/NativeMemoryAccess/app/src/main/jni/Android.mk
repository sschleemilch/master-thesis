LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := MyLib
LOCAL_SRC_FILES := MyLib.cpp
LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)


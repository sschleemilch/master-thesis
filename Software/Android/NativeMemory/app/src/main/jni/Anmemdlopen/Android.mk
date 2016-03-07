LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := Anmemdlopen
LOCAL_SRC_FILES := anmemdlopen.cpp
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := Memory
LOCAL_SRC_FILES := memory.cpp
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)
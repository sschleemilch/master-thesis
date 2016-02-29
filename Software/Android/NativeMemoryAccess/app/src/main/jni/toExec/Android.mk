LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := toExec
LOCAL_SRC_FILES := toExec.cpp
LOCAL_LDLIBS := -llog
include $(BUILD_EXECUTABLE)
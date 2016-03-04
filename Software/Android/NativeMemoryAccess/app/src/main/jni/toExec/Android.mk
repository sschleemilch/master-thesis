LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := toExec
LOCAL_SRC_FILES := toExec.cpp
LOCAL_LDLIBS := -llog
LOCAL_CFLAGS += -fPIE
LOCAL_LDFLAGS += -fPIE -pie
include $(BUILD_SHARED_LIBRARY)
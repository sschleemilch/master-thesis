LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := opencrypto_static
LOCAL_SRC_FILES := $(TARGET_ARCH_ABI)/lib/libcrypto.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := Memory
LOCAL_SRC_FILES := memory.cpp
LOCAL_LDLIBS := -llog
LOCAL_C_INCLUDES:= openssl
LOCAL_SHARED_LIBRARIES := opencrypto_static
include $(BUILD_SHARED_LIBRARY)
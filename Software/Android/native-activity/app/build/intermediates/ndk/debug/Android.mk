LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := native-activity
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	/home/basti/master-thesis/Software/Android/native-activity/app/src/main/jni/Android.mk \
	/home/basti/master-thesis/Software/Android/native-activity/app/src/main/jni/main.c \
	/home/basti/master-thesis/Software/Android/native-activity/app/src/main/jni/Application.mk \

LOCAL_C_INCLUDES += /home/basti/master-thesis/Software/Android/native-activity/app/src/main/jni
LOCAL_C_INCLUDES += /home/basti/master-thesis/Software/Android/native-activity/app/src/debug/jni

include $(BUILD_SHARED_LIBRARY)

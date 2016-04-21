LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := GsudoStr
LOCAL_LDLIBS := \
	-llog \
	-lm \
	-lz

LOCAL_SRC_FILES := \
	Generate.cpp \

include $(BUILD_SHARED_LIBRARY)
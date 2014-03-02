LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := CameraPreview
LOCAL_SRC_FILES := CameraPreview.cpp

include $(BUILD_SHARED_LIBRARY)
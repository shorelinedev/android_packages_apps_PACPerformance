LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_STATIC_JAVA_LIBRARIES := android-support-v13
LOCAL_SRC_FILES := $(call all-subdir-java-files)
LOCAL_PACKAGE_NAME := PACPerformance
LOCAL_CERTIFICATE := platform
LOCAL_PRIVILEGED_MODULE := true
LOCAL_MODULE_TAGS := optional
include $(BUILD_PACKAGE)
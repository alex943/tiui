LOCAL_PATH := $(call my-dir)
JAVA_HOME :=
include $(CLEAR_VARS)

# for libopenjdkjvmti_headers
LOCAL_C_INCLUDES := $(LOCAL_PATH)/include

LOCAL_MODULE    := libmethodtrace
LOCAL_SRC_FILES := ti_string.cpp
LOCAL_SRC_FILES += ti_o.cpp
LOCAL_SRC_FILES += ti_jni_helper.cpp
LOCAL_SRC_FILES += ti_event.cpp
LOCAL_SRC_FILES += method_trace.cpp


# for android/log.h
LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog

include $(BUILD_SHARED_LIBRARY)
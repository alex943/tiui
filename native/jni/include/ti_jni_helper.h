#include "jvmti.h"

namespace method_trace {

    void DeleteLocalRef(JNIEnv* env,
                        jobject obj);

    int isMainThread(jvmtiEnv* jvmti,
                    JNIEnv* jni,
                    jthread thread);

    int getIntMethodReturn(JNIEnv* jni,
                                jclass clazz,
                                jobject obj,
                                char* methodName);

    jobject getThisObject(jvmtiEnv* jvmti,
                                jthread thread,
                                jmethodID method);

    bool getSupperClassName(jvmtiEnv* jvmti,
                            JNIEnv* jni,
                            jclass clazz,
                            std::string spec);
}
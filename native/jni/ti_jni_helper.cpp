#include <string.h>
#include <stdio.h>
#include <string>

#include "jvmti.h"
#include "ti_jni_helper.h"
#include "ti_logger.h"

namespace method_trace {

    #define CHECK_EQ(x, y) x == y;
    #define CHECK_JVMTI(x) CHECK_EQ((x), JVMTI_ERROR_NONE)

    void method_trace::DeleteLocalRef(JNIEnv* env, jobject obj) {
      if (obj != nullptr && env != nullptr) {
        env->DeleteLocalRef(obj);
      }
    }
    // std::vector<std::string> spec
    bool getSupperClassName(jvmtiEnv* jvmti, JNIEnv* jni, jclass clazz, std::string spec) {
        char* superClassName;
        jclass superclazz = jni->GetSuperclass(clazz);
        jvmti->GetClassSignature(superclazz, &superClassName, 0);
        while(superclazz != nullptr) {
            if (strcmp("Ljava/lang/Object;", superClassName) == 0) {
                break;
            } else {
                if (strcmp(spec.c_str(), superClassName)) {
                   DeleteLocalRef(jni, superclazz);
                   return true;
                }
            }
            superclazz = jni->GetSuperclass(superclazz);
            jvmti->GetClassSignature(superclazz, &superClassName, 0);
        }
        DeleteLocalRef(jni, superclazz);
        return false;
    }

    int method_trace::isMainThread(jvmtiEnv* jvmti, JNIEnv* jni, jthread thread) {
        jvmtiThreadInfo info_{};
        CHECK_JVMTI(jvmti->GetThreadInfo(thread, &info_));
        int value = strcmp(info_.name, "main");
        std::ostringstream log2;

        jvmti->Deallocate(reinterpret_cast<unsigned char*>(info_.name));
        DeleteLocalRef(jni, info_.thread_group);
        DeleteLocalRef(jni, info_.context_class_loader);
        return value;
    }

    int method_trace::getIntMethodReturn(JNIEnv* jni,
                                jclass clazz,
                                jobject obj,
                                char* methodName) {
        jmethodID mid = jni->GetMethodID(clazz, methodName, "()I");
        if (NULL == mid) {
            return -1;
        }
        jint value = jni->CallIntMethod(obj, mid);
        return int(value);
    }

    jobject method_trace::getThisObject(jvmtiEnv* jvmti,
                                jthread thread,
                                jmethodID method) {
        jobject thisObj = 0;
        jint count;
        jvmtiLocalVariableEntry* localTable;
        jvmti->GetLocalVariableTable(method, &count, &localTable);
        for (int i = 0; i < count; i++) {
            jvmtiLocalVariableEntry *entry = &localTable[i];
            if (entry == nullptr) {
                continue;
            }
            if (entry->name == NULL) {
                continue;
            }
            if(strcmp(entry->name, "this") == 0) {
                jvmti->GetLocalObject(thread, 0, entry->slot, &thisObj);
                return thisObj;
            }
        }
        return nullptr;
    }
}// namespace
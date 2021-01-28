#include <string.h>
#include <stdio.h>
#include <string>
#include <vector>
#include <cstdarg>

#include "jvmti.h"
#include "ti_event.h"
#include "ti_logger.h"
#include "ti_string.h"
#include "ti_jni_helper.h"
#include "ti_o.h"

namespace method_trace {

static ArgsDefinition args;
static EventDefinition event;
#define DEBUG false

// return 0 , is out app
static bool OurAppCmp(std::string className) {
    return className == args.package;
}

void TiEvent::Args(char* options) {
    ParseArgs(options, &args);
}

void TiEvent::Build(jvmtiEnv* jvmti,
        JNIEnv* jni,
        jthread thread,
        jmethodID method,
        int type) {

    jclass clazz;
    char* className;
    char* methodName;
    int id = 0;
    int hashCode = 0;

    int isUIThread = isMainThread(jvmti, jni, thread);
    if (isUIThread != 0) {
        return;
    }

    jvmti->GetMethodDeclaringClass(method, &clazz);
    jvmti->GetClassSignature(clazz, &className, 0);
    jvmti->GetMethodName(method, &methodName, NULL, NULL);

    if (ExcludeSpecifyClasses(className)) {
        return ;
    }

    if (ExcludeSpecifyMethods(methodName)) {
        return ;
    }

    std::ostringstream log;
    int find = LookupArgsPackage(className, &args);
    if (DEBUG) {
        log << "LookupArgsPackage " << methodName << " "
                                    << className << " "
                                    << find << " end" << std::endl;
        LOGT(&log);
    }


    if (find == 1) { // not found
        find = LookupArgsMethod(methodName, &args);
        if (DEBUG) {
            log << "Lookup " << methodName << " "
                             << className << " ";
         }
        if(find != 0) {
            if (DEBUG) {
                log << " not find in LookupArgsMethod " << std::endl;
                LOGT(&log);
            }
            DeleteLocalRef(jni, clazz); // DeleteLocalRef only for jobject
            return;
        }

        // 1 .
        find = IncludeSpecifyClasses(className);
        if (find) {
            if (DEBUG) {
                log << " find in IncludeSpecifyClasses(ViewRootImpl) " << std::endl;
                LOGT(&log);
            }
            ToJSON1(type, 0, 1, 0, className, methodName);
            DeleteLocalRef(jni, clazz);
            return ;
        }

        find = getSupperClassName(jvmti, jni, clazz, "Landroid/app/Activity;");
        if (find) {
            if (DEBUG) {
                log << " find in getSupperClassName(Activity) " << std::endl;
                LOGT(&log);
            }
            DeleteLocalRef(jni, clazz);
            ToJSON1(type, 0, 2, 0, className, methodName);
            return ;
        }

        find = getSupperClassName(jvmti, jni, clazz, "Landroid/view/View;");
        if (!find) {
            if (DEBUG) {
                log << " not find in getSupperClassName(View) " << std::endl;
                LOGT(&log);
            }
            DeleteLocalRef(jni, clazz);
            return ;
        }
        find = 1; // mark
        if (DEBUG) {
            log << " is View" << std::endl;
            LOGT(&log);
        }
    }

    log << "Started Print " << methodName << " "
                            << className << " end" << std::endl;
    LOGT(&log);

    jobject obj = getThisObject(jvmti, thread, method);
    if(obj == nullptr) {
        DeleteLocalRef(jni, clazz); // DeleteLocalRef only for jobject
        return;
    }

    if (find == 1) { // the mark is View
        id = getIntMethodReturn(jni, clazz, obj, "getId");
    }
    hashCode = getIntMethodReturn(jni, clazz, obj, "hashCode");

    DeleteLocalRef(jni, clazz);
    DeleteLocalRef(jni, obj);

    ToJSON1(type, 0, hashCode, id, className, methodName);
}

} // namespace

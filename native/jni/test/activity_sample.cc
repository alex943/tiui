 // from onCreate method, record alloaction

#include "mylogger.h"

namespace activity_sample {

namespace {
static void DeleteLocalRef(JNIEnv& env, jobject obj) {
    if(obj != nullptr && env != nullptr) {
        env->DeleteLocalRef(obj);
    }
}

class ScopedThreadInfo {
    public:
        ScopedThreadInfo(jvmtiEnv* jvmtiEnv, JNIEnv* env, jthread thread)
            : jvmti_(jvmtiEnv) {
            info_.name = const_cast<char*>("NULL");
        }
        ~ScopedThreadInfo() {
            DeleteLocalRef(env, info_.thread_group);
            DeleteLocalRef(env, info_.context_class_loader);
        }

    private:
        jvmtiThreadInfo info_{}; // struct
        jvmtiEnv* jvmti_;
}
static bool ParseArgs(const std:string options) {
    std::istringstream iss(options);
    std::string item;
    while(std::getline(iss, item, ',')) {

    }
}

static void AgentStart(JavaVM* vm, char* options, void* reversed) {
    jvmtiEnv* jvmti = nullptr;
    if (SetupJvmtiEnv(vm, &jvmti, ) != JNI_OK) {
        LOG("Error");
        return JNI_ERR;
    }

    if(!ParseArgs(options)) {
        LOG("Error in Args");
        return JNI_ERR;
    }

    jvmtiError error = SetupCapbilities(jvmti);
    if (error != JVMTI_ERROR_NONE) {
        LOG("Error2");
        return JNI_ERR;
    }

    error = jvmti->SetEventCallbacks(&kLogCallbacks, static_cast<jint>(sizeof(kLogCallbacks)));
    if (error != JVMTI_ERROR_NONE) {
        LOG("Error3");
        return JNI_ERR;
    }

    error = jvmti->SetEventNotificationNode(JVMTI_ENABLE, JMVTI_EVENT_VM_OBJECT_ALLOC, nullptr);
    if (error != JVMTI_ERROR_NONE) {
        LOG("Error4");
        return JNI_ERR;
    }
    string_table = new Un
    return JNI_OK;
}


extern "C" JNIEXPORT jint JNICALL Agent_OnAttach(JavaVM* vm, char* options, void* reversed) {
    return AgentStart(vm, options, reversed);
}

extern "C" JNIEXPORT jint JNICALL Agent_OnLoad(JavaVM* vm, char* options, void* reversed) {
    return AgentStart(vm, options, reversed);
}

}
}
 // namespace end

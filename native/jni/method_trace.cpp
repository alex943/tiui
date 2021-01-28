#include <string.h>
#include <string>
#include <vector>

#include "jvmti.h"
#include "ti_event.h"
#include "ti_logger.h"
#include "ti_string.h"

namespace method_trace {

#define CHECK_EQ(x, y) x == y;
#define CHECK_JVMTI(x) CHECK_EQ((x), JVMTI_ERROR_NONE)

// Special art ti-version number. We will use this as a fallback if we cannot get a regular JVMTI
// env.
static constexpr jint kArtTiVersion = JVMTI_VERSION_1_2 | 0x40000000;
static JavaVM* java_vm;

static void
HandleMethodEntry(jvmtiEnv* jvmti,
            JNIEnv* jni,
            jthread thread,
            jmethodID method) {
    TiEvent::Build(jvmti, jni, thread, method, 1);
}

static void
HandleMethodExit(jvmtiEnv *jvmti,
        JNIEnv* jni,
        jthread thread,
        jmethodID method,
        jboolean was_popped_by_exception,
        jvalue return_value) {
    TiEvent::Build(jvmti, jni, thread, method, 2);
}

static jint
SetupJvmtiEnv(JavaVM* vm, jvmtiEnv** jvmti) {
  jint res = 0;
  res = vm->GetEnv(reinterpret_cast<void**>(jvmti), JVMTI_VERSION_1_1);

  if (res != JNI_OK || *jvmti == nullptr) {
    LOG("Unable to access JVMTI, error code ");
    return vm->GetEnv(reinterpret_cast<void**>(jvmti), kArtTiVersion);
  }
  return res;
}

static jint
Init(JavaVM *vm, char* options) {
    if (java_vm != nullptr) {
        return JNI_OK;
    }
    // init jvm
    LOG("{\"stage\":\"Attached\"}");
    java_vm = vm;
    jvmtiEnv* jvmti = nullptr;
    if (SetupJvmtiEnv(vm, &jvmti) != JNI_OK) {
       LOG("Could not get JVMTI env or ArtTiEnv!");
       return JNI_ERR;
    }

    // options
    TiEvent::Args(options);

    // add capabilities
    jvmtiCapabilities caps {
        .can_generate_method_entry_events = 1,
        .can_generate_method_exit_events = 1,
        .can_access_local_variables = 1,
    };
    CHECK_JVMTI(jvmti->AddCapabilities(&caps));

    // add callbacks
    jvmtiEventCallbacks cb {
        .MethodEntry = HandleMethodEntry,
        .MethodExit = HandleMethodExit,
    };
    CHECK_JVMTI(jvmti->SetEventCallbacks(&cb, sizeof(cb)));

    // set event TODO: On Main Thread.
    CHECK_JVMTI(jvmti->SetEventNotificationMode(JVMTI_ENABLE,
                JVMTI_EVENT_METHOD_ENTRY, (jthread) NULL));
    CHECK_JVMTI(jvmti->SetEventNotificationMode(JVMTI_ENABLE,
                JVMTI_EVENT_METHOD_EXIT, (jthread) NULL));
    return JNI_OK;
}

extern
"C" JNIEXPORT jint JNICALL
Agent_OnAttach(JavaVM *vm,
               char* options,
               void* reserved) {
  return Init(vm, options);
}

extern
"C" JNIEXPORT jint JNICALL
Agent_OnLoad(JavaVM* vm,
             char* options,
             void* reserved) {
  return Init(vm, options);
}

// Note: This is not called for normal Android apps,
// use "kill -SIGQUIT" instead.
// kill -SIGQUIT $(pid com.littleinc.orm_benchmark)
extern
"C" JNIEXPORT void JNICALL
Agent_OnUnload(JavaVM* vm) {
  LOG("{\"stage\":\"DeAttached\"");
}

} // namespace
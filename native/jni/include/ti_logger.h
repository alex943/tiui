#include <android/log.h>
#include <sstream>

#define TAG "UI_JVMTI"
#define TAG_FOR_LOG "UI_FOR_LOG"

namespace {
    static void
    LOG(const char* msg) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "%s", msg);
    }
    static void
    LOGI(const int msg) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "%d", msg);
    }
    static void
    LOGS(const std::string msg) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "%s", &msg);
    }

    static void
    LOGT(std::ostringstream* log) {
        __android_log_print(ANDROID_LOG_ERROR, TAG_FOR_LOG, "%s", log->str().c_str());
    }
}

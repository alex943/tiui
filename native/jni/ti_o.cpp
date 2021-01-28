#include <iostream>
#include <sstream>
#include <ostream>
#include <string>

#include <android/log.h>

#include "ti_event.h"

namespace method_trace {

    #define TAG "UI_JVMTI"

    std::ostream& operator<<(std::ostream& os, const EventDefinition& dd) {
      return os << "{"
                << "\"type\":" << dd.type << ","
                << "\"className\":\"" << dd.className << "\"" << ","
                << "\"methodName\":\"" << dd.methodName << "\"" << ","
                << "\"threadId\":" << dd.threadId << ","
                << "\"hashCode\":" << dd.hashCode << ","
                << "\"id\":" << dd.id
                << "}";
    }

    void ToJSON2(const EventDefinition& dd) {
        std::ostringstream json;
        json << dd ;
        __android_log_print(ANDROID_LOG_ERROR, TAG, "%s", json.str().c_str());
    }

    void ToJSON1(int type, int threadId, int hashCode, int id, char* className, char* methodName) {
        std::ostringstream json;
        json << "{" ;
        json << "\"type\":" << type << ",";
        json << "\"className\":\"" << className << "\"" << ",";
        json << "\"methodName\":\"" << methodName << "\"" << ",";
        json << "\"threadId\":" << threadId << ",";
        json << "\"hashCode\":" << hashCode << ",";
        json << "\"id\":" << id;
        json << "}";
        __android_log_print(ANDROID_LOG_ERROR, TAG, "%s", json.str().c_str());
    }
}

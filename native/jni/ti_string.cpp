#include <iostream>
#include <sstream>
#include <string>
#include <unordered_set>
#include <string.h>
#include <mutex>

//#include "ti_logger.h"
#include "ti_string.h"

using namespace std;

namespace method_trace {

    static std::mutex mutex;

    // Converts a class name to a type descriptor
    // (ex. "java.lang.String" to "Ljava/lang/String")
    std::string method_trace::classNameToDescriptor(const char* className) {
      std::stringstream ss;
      ss << "L";
      for (auto p = className; *p != '\0'; ++p) {
        ss << (*p == '.' ? '/' : *p);
      }
      //ss << ";";
      return ss.str();
    }

    std::string method_trace::substrOf(const std::string& s, size_t start, size_t end) {
      if (end == start) {
        return "";
      } else if (end == std::string::npos) {
        end = s.size();
      }
      return s.substr(start, end - start);
    }

    bool method_trace::ParseArgs(const std::string& options, ArgsDefinition* args) {
        std::istringstream iss(options);
        std::string item;
        if(options.find(";") == std::string::npos) {
            return false;
        }
        while(std::getline(iss, item, ';')) {
            std::string key = substrOf(item, 0, item.find(':'));
            std::string value = substrOf(item, item.find(':') + 1, item.size());
            if (key == "package") {
                args->package = method_trace::classNameToDescriptor(value.c_str());
            } else if (key == "methods") {
                std::istringstream ms(value);
                std::string method;
                while(std::getline(ms, method, ',')) {
                    args->methods.insert(method);
                }
            }
        }
        return true;
    }

    bool method_trace::ExcludeSpecifyMethods(std::string className) {
        if (className == "<init>") {
            return true;
        }
        return false;
    }


    bool method_trace::ExcludeSpecifyClasses(std::string className) {
        if (className == "Ljava/lang/ThreadLocal$ThreadLocalMap;") {
            return true;
        } else if (className == "Lhuawei/hiview/HiTraceImpl;") {
            return true;
        } else if (className == "Landroid/os/SystemProperties;") {
            return true;
        } else if (className == "Landroid/util/HwStylusUtils;") {
            return true;
        } else if (className == "Landroid/view/HwViewRootImpl;") {
            return true;
        }  else if (className == "Landroid/view/HwViewRootImpl;") {
           return true;
       }
        return false;
    }

    bool method_trace::IncludeSpecifyClasses(std::string className) {
        if (className == "Landroid/view/ViewRootImpl;") {
            return true;
        }
        return false;
    }


    bool method_trace::LookupArgsPackage(std::string className, ArgsDefinition* args) {
        if (args->package.length() < className.length()) {
            return args->package.compare(className.substr(0, args->package.length()));
        }
        return args->package.compare(className);
    }

    // 0 found , 1 not found
    int method_trace::LookupArgsMethod(std::string methodName, ArgsDefinition* args) {
        if (args->methods.size() == 0) {
            return 1;
        }
        int result = 1;
        for (auto it : args->methods) {
            if (it.length() == methodName.length() && it == methodName) {
                return 0;
            }
        }
        return 1;
    }

}

method_trace::ArgsDefinition testOptionParse() {
    cout << "   testOptionParse" << endl;
    method_trace::ArgsDefinition args;
    char* options = "package:com.example.animations30;methods:performMeasure,onMeasure,onResume,onCreate;";
    bool v1 = method_trace::ParseArgs(options, &args);
    cout << "package = " << args.package << endl;
    for(auto method : args.methods) {
        cout << "method = " << method << endl;
    }
    return args;
}

void testToolMethods(method_trace::ArgsDefinition* mock) {
    cout << "   testToolMethods" << endl;
    std::string packageDef = method_trace::classNameToDescriptor("com.example.animations30");
    cout << "D " << packageDef << endl;

    int re = method_trace::LookupArgsMethod("onCreate", mock);
    cout << "M  "<< re << " size : " << (mock->methods).size() << endl;

    re = method_trace::LookupArgsMethod("set", mock);
    cout << "M2  "<< re << " " << "set" << " size : " << (mock->methods).size() << endl;

    re = method_trace::LookupArgsPackage("Lcom/example/animations30/Activity;", mock);
    cout << "C " << re << " " << mock->package << endl;

    re = method_trace::IncludeSpecifyClasses("Lcom/example/animations30/Activity;");
    if (re) {
        cout << "I " << re << " found " << "Lcom/example/animations30/Activity;" << endl;
    } else {
        cout << "I " << re << " not found " << "Lcom/example/animations30/Activity;" << endl;
    }
}



int main() {
    method_trace::ArgsDefinition mock = testOptionParse();
    testToolMethods(&mock);
}


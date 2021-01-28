#include <iostream>
#include <sstream>
#include <string>
#include <unordered_set>
#include <string.h>

using namespace std;

namespace method_trace {

    struct ArgsDefinition {
          std::string package;
          std::unordered_set<std::string> methods;
    };

    // Converts a class name to a type descriptor
    // (ex. "java.lang.String" to "Ljava/lang/String;")
    static std::string classNameToDescriptor(const char* className);

    static std::string substrOf(const std::string& s, size_t start, size_t end);

    bool ExcludeSpecifyMethods(std::string className);

    bool ExcludeSpecifyClasses(std::string className);

    bool IncludeSpecifyClasses(std::string className);

    bool ParseArgs(const std::string& options, ArgsDefinition* package);

    bool LookupArgsPackage(std::string className, ArgsDefinition* package);

    int LookupArgsMethod(std::string methodName, ArgsDefinition* package);
}

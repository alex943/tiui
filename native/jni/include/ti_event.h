/**

{
	"type": "1",
	"class": "Landroid/widget/FrameLayout;",
	"method": "onMeasure",
	"hash": "56031765",
	"id": "1902572004"
}

**/

#include "jvmti.h"

namespace method_trace {

struct EventDefinition {
  int type;
  std::string className;
  std::string methodName;
  int hashCode;
  int id;
  int threadId;
};

class TiEvent {
    public:
        static void Args(char* options);
        static void Build(jvmtiEnv* jvmti,
                        JNIEnv* jni,
                        jthread thread,
                        jmethodID method,
                        int type);
};

} // namespace

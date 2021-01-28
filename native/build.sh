
function testing() {
  cd jni
  g++ -I include/ ti_string.cpp  -o test/test_ti_string.o
  ./test/test_ti_string.o
}

function build() {
  /Users/ws3/Library/Android/android-ndk-r21d/ndk-build -C . NDK_PROJECT_PATH=.
}

function attach() {
  packegName=$1
  exefile=libmethodtrace.so
  mvcmd="cp /data/local/tmp/${exefile} code_cache/"
  echo $mvcmd

  if test $packegName = "com.taobao.trip"
  then
    adb push ./libs/armeabi-v7a/$exefile /data/local/tmp/
  else
    adb push ./libs/arm64-v8a/$exefile /data/local/tmp/
  fi

  args="package:${packegName}\;methods:performMeasure,onMeasure,onResume,onCreate\;"

  adb shell setenforce 0
  adb shell run-as \
      ${packegName} \
      "cp /data/local/tmp/${exefile} code_cache/"
  adb shell am attach-agent \
      ${packegName} \
      /data/data/${packegName}/code_cache/${exefile}=${args}
}

function debug() {
    adb logcat | ndk-stack -sym obj/local/armeabi-v7a/
}

function run() {
    build $@
    attach $@
}

# sh build.sh run com.example.animations30
# sh build.sh run com.taobao.trip
$@

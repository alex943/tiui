function attach() {
    cd native
    sh build.sh attach $1
}

function build() {
    ./gradlew clean
    ./gradlew shadowJar #./gradlew jar
    rm ui.jar
    cp build/libs/*.jar ./dist/ui.jar
    # 1.
    # -Djava.ext.dirs=/Users/ws3/Library/Android/sdk/tools/lib/x86_64:/Users/ws3/Library/Android/sdk/tools/lib is not supported.  Use -classpath instead.
    # Error: Could not create the Java Virtual Machine.
    # Error: A fatal exception has occurred. Program will exit.

    # 2.
    # 错误: 找不到或无法加载主类 .Users.ws3.Library.Android.sdk.tools.lib.x86_64:.Users.ws3.Library.Android.sdk.tools.lib
    # 原因: java.lang.ClassNotFoundException: /Users/ws3/Library/Android/sdk/tools/lib/x86_64:/Users/ws3/Library/Android/sdk/tools/l
#    JAVACMD=/Library/Java/JavaVirtualMachines/jdk-11.0.8.jdk/Contents/Home/bin/java
#    $JAVACMD -Xmx1600M -XstartOnFirstThread \
#        -Djava.ext.dirs="/Users/ws3/Library/Android/sdk/tools/lib/x86_64:/Users/ws3/Library/Android/sdk/tools/lib" \
#        "/Users/ws3/Library/Android/sdk/tools" \
#        -jar "./ui.jar"
}

function app() {
    cd ./dist
    $JAVA_HOME/bin/java -Xmx1600M -XstartOnFirstThread \
        -Djava.ext.dirs="$ANDROID_HOME/tools/lib/x86_64:$ANDROID_HOME/tools/lib" \
        -Dcom.android.uiautomator2.bindir="$ANDROID_HOME/tools" \
        -jar "./ui.jar"
}

function aR() {
    build
    run
}

$@


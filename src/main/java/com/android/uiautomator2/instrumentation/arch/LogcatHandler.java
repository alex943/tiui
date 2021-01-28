package com.android.uiautomator2.instrumentation.arch;

import com.android.ddmlib.logcat.LogCatTimestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogcatHandler {

    private static SimpleDateFormat logcatTimestampFormatter =
            new SimpleDateFormat("MM-dd HH:mm:ss.SSS");

    public static LogCatTimestamp currentTime() {
        String startLogcatAt = logcatTimestampFormatter.format(
                new Date(System.currentTimeMillis()));
        return LogCatTimestamp.fromString(startLogcatAt);
    }
}

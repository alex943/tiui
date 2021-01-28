package com.android.uiautomator2.instrumentation;

import com.android.ddmlib.logcat.LogCatTimestamp;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogcatTest {

    @Test
    public void testSameTime() {
        Date d = new Date(System.currentTimeMillis());
        SimpleDateFormat logcatTimestampFormatter =
                new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
        String startLogcatAt = logcatTimestampFormatter.format(d);
        LogCatTimestamp t1 = LogCatTimestamp.fromString(startLogcatAt);
        LogCatTimestamp t2 = LogCatTimestamp.fromString(startLogcatAt);
        Assert.assertTrue(t1.equals(t2));
    }

    @Test
    public void testNotTime() {
        long now = System.currentTimeMillis();
        Date d = new Date(now);
        Date d2 = new Date(now + 1000);
        SimpleDateFormat logcatTimestampFormatter =
                new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
        String startLogcatAt = logcatTimestampFormatter.format(d);
        String startLogcatAt2 = logcatTimestampFormatter.format(d2);
        LogCatTimestamp t1 = LogCatTimestamp.fromString(startLogcatAt);
        LogCatTimestamp t2 = LogCatTimestamp.fromString(startLogcatAt2);
        Assert.assertFalse(t1.equals(t2));
    }
}

package com.android.uiautomator2.instrumentation;

import com.android.uiautomator2.instrumentation.viewtree.ViewNodeProperties;
import org.junit.Assert;
import org.junit.Test;

public class ViewPropertiesTest {
    public static String getFileSpec() {
        return "/Users/ws3/Documents/test/android2/uiautomatorviewer/apk/app-debug.apk";
    }
    public static String getIdHex() {
        return "7f08010a";
    }
    public static String getNameSpec() {
        return "textEnd";
    }
    public static String getPackageSpec() {
        return "com.example.animations30";
    }
    @Test
    public void testId() {
        ViewNodeProperties properties2 = ViewNodeProperties.getInstance();
        properties2.setProperties(getFileSpec(), getPackageSpec());
        Assert.assertTrue(getIdHex().equals(properties2.getId(getNameSpec())));
    }
}

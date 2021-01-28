package com.android.uiautomator2.instrumentation.mock;

import com.android.uiautomator2.instrumentation.model.Event;

public class OnPackageCallEvent extends Event {

    public OnPackageCallEvent(String mark, int type, int hashcode) {
        setType(type);
        setMethodName("test_" + mark);
        setClassName("Landroid/view/View;" + mark);
        setHashCode(hashcode);
    }
}

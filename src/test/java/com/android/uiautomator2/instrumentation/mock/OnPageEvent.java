package com.android.uiautomator2.instrumentation.mock;

import com.android.uiautomator2.instrumentation.model.Event;

public class OnPageEvent extends Event {
    public OnPageEvent(String mark, int type, int hashcode) {
        setType(type);
        setMethodName("onCreate");
        setClassName("Landroid/app/Activity;" + mark);
        setHashCode(hashcode);
    }
}

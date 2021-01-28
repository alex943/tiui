package com.android.uiautomator2.instrumentation.mock;

import com.android.uiautomator2.instrumentation.model.Event;

public class OnClassEvent extends Event {
    public OnClassEvent(String className, String methodName, int type, int hashcode) {
        setType(type);
        setMethodName(methodName);
        setClassName(className);
        setHashCode(hashcode);
    }
}

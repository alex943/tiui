package com.android.uiautomator2.instrumentation.mock;

import com.android.uiautomator2.instrumentation.model.Event;


public class PerformEvent extends Event {
    public PerformEvent(String mark, int type, int hashcode) {
        setType(type);
        this.setMethodName("performMeasure");
        setClassName("Landroid/view/View;" + mark);
        setHashCode(hashcode);
    }
}

package com.android.uiautomator2.instrumentation.mock;

import com.android.uiautomator2.instrumentation.model.Event;

public class OnMeasureEvent extends Event {
    public OnMeasureEvent(String mark, int type, int hashcode) {
        setType(type);
        setMethodName("onMeasure");
        setClassName("Landroid/view/View;" + mark);
        setHashCode(hashcode);
    }
}

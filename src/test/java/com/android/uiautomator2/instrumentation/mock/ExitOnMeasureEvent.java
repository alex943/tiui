package com.android.uiautomator2.instrumentation.mock;

import com.android.uiautomator2.instrumentation.model.Event;

import java.util.Random;

public class ExitOnMeasureEvent extends Event {
    static Random random = new Random();
    public ExitOnMeasureEvent() {
        setType(2);
        setMethodName("onMeasure");
        setClassName("Landroid/view/View;");
        setHashCode(random.nextInt());
    }
}

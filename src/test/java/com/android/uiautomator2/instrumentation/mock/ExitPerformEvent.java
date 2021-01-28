package com.android.uiautomator2.instrumentation.mock;

import com.android.uiautomator2.instrumentation.model.Event;

import java.util.Random;

public class ExitPerformEvent extends Event {
    static Random random = new Random();
    public ExitPerformEvent() {
        setType(2);
        this.setMethodName("performMeasure");
        setClassName("Landroid/view/View;");
        setHashCode(random.nextInt());
    }
}

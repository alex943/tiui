package com.android.uiautomator2.instrumentation.viewtree;

public interface ViewTreeObserver {

    public void onMeasured(ViewNode view);
    public void onLayouted(ViewNode view);
}

package com.android.uiautomator2.instrumentation.page;

public interface PageObserver {
    public void onCreated(Page page);
    public void onResumed(Page page);
}

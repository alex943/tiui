package com.android.uiautomator2.instrumentation.viewtree;

import com.android.uiautomator2.instrumentation.model.Event;
import com.android.uiautomator2.tree.UiNode;

public interface ViewTree {

    public void add(Event event);

    public void addObserver(ViewTreeObserver observer);

    public ViewNode findViewByNode(UiNode node);
}

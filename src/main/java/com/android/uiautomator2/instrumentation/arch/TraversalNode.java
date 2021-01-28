package com.android.uiautomator2.instrumentation.arch;

import java.util.Collection;

public interface TraversalNode<E> {
    public Collection<E> getNodeChildren();
}

package com.android.uiautomator2.instrumentation.arch;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.List;

public abstract class Traversal<T extends TraversalNode> {

    public static final int BFS = 0;
    public static final int DFS = 1;

    public static final int PRE_ORDER = 1;
    public static final int POST_ORDER = 2;

    public abstract T getRoot();

    public void traversal(int callType, TraversalCallback<T> callback) {
        if (callback == null) return ;

        ArrayDeque<T> events = new ArrayDeque<>();
        T root = getRoot();
        events.push(root);

        while (events.size() > 0) {
            T node;
            if (callType == BFS) {
                node = events.pollFirst();
            } else {
                node = events.pollFirst();
            }
            callback.onNode(node);
            if (node != null && node.getNodeChildren() != null) {
                if (callType == BFS) {
                    for (Object child : node.getNodeChildren()) {
                        events.addLast((T) child);
                    }
                } else {
                    // dfs will show the same order with BFS in same level.
                    Collection<T> children = node.getNodeChildren();
                    children.forEach(
                            child -> events.addFirst((T) child)
                    );
                }
            }
        }
    }

}

package com.android.uiautomator2.instrumentation.viewtree;

import com.android.uiautomator2.Logger;
import com.android.uiautomator2.instrumentation.arch.Traversal;
import com.android.uiautomator2.instrumentation.arch.TraversalCallback;
import com.android.uiautomator2.instrumentation.model.Event;
import com.android.uiautomator2.tree.BasicTreeNode;
import com.android.uiautomator2.tree.UiNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class ViewTreeImpl extends Traversal<ViewNode> implements ViewTree {

    class ViewTreeImplTraversal implements TraversalCallback<ViewNode> {
        private ViewNode mSpecView = null;
        private UiNode mNode;
        public ViewNode getLastView() {
            return mSpecView;
        }
        public void setNode(UiNode node) {
            mNode = node;
        }
        @Override
        public boolean onNode(ViewNode view) {
            if (view.getEvent() != null) { // skip to root, cause root has no Event.
                if (matchNode(mNode, view)) {
                    mSpecView = view;
                    return false;
                }
            }
            return true;
        }
    }

    public int level = 0;
    private ViewNode root, current;
    private List<ViewTreeObserver> observers;
    private ViewTreeImplTraversal mTraversal = new ViewTreeImplTraversal();

    public ViewTreeImpl() {
        this.root = new ViewNode();
        this.current = root;
        this.observers = new ArrayList<>();
    }

    @Override
    public ViewNode getRoot() {
        return root;
    }

    // method wouild duplicated. for example:
    //      TextView.onMeasure() // hashcode = 100
    //      AsbTextView.onMeasure() // hashcode = 100, the sub-view of the TextView
    // Skipped while event as future child of this view is duplicated with it.
    private boolean isDuplicated(Event event) {
        return this.current != null && this.current.getEvent() != null
                && event.getHashCode() == this.current.getEvent().getHashCode();
    }

    @Override
    public void add(Event event) {
        if (event.getType() == PRE_ORDER) {
            if (isDuplicated(event)) {
                return;
            }
            this.current = this.current.addChildEvent(event);
            if (this.current.getEvent() != null) {
                this.current.getEvent().setLevel(level++);
            }
        } else if (event.getType() == POST_ORDER) {
            if (!this.current.isEmpty()) {
                if (this.current.getEvent() != null &&
                        event.getHashCode() == this.current.getEvent().getHashCode()) {
                    this.current = this.current.getParent();
                    if (this.current.getEvent() != null) {
                        this.current.getEvent().setLevel(--level);
                    }
                }
            }
        }
    }

    @Override
    public void addObserver(ViewTreeObserver observer) {
        observers.add(observer);
    }

    private boolean compareNode(BasicTreeNode node, ViewNode view) {
        if (view == null || view.getEvent() == null) {
            return false;
        }

        if (!(node instanceof UiNode)) {
            return false;
        }

        return true; //view.getEvent().compareTo(((UiNode) node).event) == 0;
    }

    private boolean matchNode(UiNode node, ViewNode view) {
        if (view == null || view.getEvent() == null || node == null) {
            return false;
        }
        boolean sameCurrent = compareNode(node, view);
        boolean sameParent = compareNode(node.getParent(), view.getParent());
        boolean sameChildrenCount = node.getChildCount() == view.getChildrenCount();

        return sameParent && sameChildrenCount && sameCurrent ;
    }

    @Override
    public ViewNode findViewByNode(UiNode node) {
        Logger.d("findViewByNode");
        mTraversal.setNode(node);
        traversal(BFS, mTraversal);
        return mTraversal.getLastView();
    }

    public void clear() {
        this.current.clear();;
        this.root.clear();
        this.observers.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        ArrayDeque<ViewNode> deque = new ArrayDeque();
        deque.push(getRoot());
        while(deque.size() != 0) { // BFS
            ViewNode view = deque.pop();
            sb.append(view.toString()).append("\n");
            if (view.getNodeChildren() != null) {
                for (ViewNode child : view.getNodeChildren()) {
                    deque.push(child);
                }
            }
        }
        return sb.toString();
    }
}

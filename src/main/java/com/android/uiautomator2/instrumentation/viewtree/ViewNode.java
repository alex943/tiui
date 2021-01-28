package com.android.uiautomator2.instrumentation.viewtree;

import com.android.uiautomator2.instrumentation.arch.TraversalNode;
import com.android.uiautomator2.instrumentation.model.Event;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewNode implements
        Comparable<Event>, TraversalNode<ViewNode> {

    public int refCount = 1;
    public int level;
    public Event event;
    public ViewNode parent;
    private List<ViewNode> children;

    public ViewNode() {
    }

    public ViewNode(Event e) {
        event = e;
    }

    public boolean isRoot() {
        return isEmpty() && parent == null;
    }

    public void clear() {
        event = null;
        parent = null;
        children = null;
    }

    public boolean isEmpty() {
        return event == null;
    }

    public Event getEvent() {
        return event;
    }

    public ViewNode getParent() {
        return parent;
    }

    public void setParent(ViewNode parent) {
        this.parent = parent;
    }

    public boolean hasChildren() {
        return children != null && children.size() != 0;
    }

    public List<ViewNode> getNodeChildren() {
        return children;
    }

    public int getChildrenCount() {
        if (!hasChildren()) return 0;
        return children.size();
    }

    public ViewNode getChildAt(int i) {
        return children.get(i);
    }

    private ViewNode containsChild(Event event2) {
        if (this.event != null && this.event.equals(event2)) {
            return this;
        }
        if (children == null) {
            children = new ArrayList<>();
            return null;
        }
        for (ViewNode view : children) {
            if (view.event.equals(event2)) {
                view.event.update(event2);
                return view;
            }
        }
        return null;
    }

    private void addChildView(ViewNode child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        Event event = child.getEvent();
//        if (event == null) {
//            Logger.d("[log] is null");
//        } else {
//            Logger.d("[log] " + event.getClassName() + " " + event.getMethodName());
//        }
        children.add(child);
    }



    public ViewNode addChildEvent(Event event) {
        ViewNode child = containsChild(event);
        if (child == null) {
            child = new ViewNode(event);
            child.setParent(this);
            addChildView(child);
        } else {
            child.refCount++;
        }
        return child;
    }

    @Override
    public String toString() {
        String p = "null";
        if (parent != null && parent.event != null) p = parent.event.getClassName();
        return "View{" + "event2=" + event + ", parent=" + p + ", count=" + refCount + '}';
    }

    @Override
    public int compareTo(@NotNull Event o) {
        return getEvent().equals(o) ? 0 : -1;
    }
}

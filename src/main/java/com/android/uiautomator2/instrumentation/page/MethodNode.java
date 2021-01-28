/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.uiautomator2.instrumentation.page;

import com.android.uiautomator2.instrumentation.arch.TraversalNode;
import com.android.uiautomator2.instrumentation.model.Event;
import com.android.uiautomator2.tree.UiNode;

import java.util.Collection;
import java.util.HashMap;

public class MethodNode extends UiNode implements TraversalNode<MethodNode> {

    private int level;
    public int refCount = 1;
    private Event event;
    private HashMap<String, MethodNode> nodeChildren;
    private MethodNode nodeParent;
    private Event endEvent;

    public MethodNode(Event event) {
        this.event = event;
    }

    public String getDisplayName() {
        if (event == null) {
            return "";
        }
        String[] splited = event.getClassName().split("/");
        String className = splited[splited.length - 1];
        className = className.substring(0, className.length() - 1);
        return className + " #" + event.getMethodName() + " Count:" + refCount + " Cost:" + getMethodCost() + "ms";
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    public Collection<MethodNode> getNodeChildren() {
        return nodeChildren.values();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MethodNode)) {
            return false;
        }
        MethodNode method = (MethodNode) obj;
        if (this.event == null || method.event == null) {
            return false;
        }
        return this.event.getMethodName().equals(method.event.getMethodName());
    }

    @Deprecated
    public void clear() {
        super.clearAllChildren();
        this.nodeChildren.clear();
        this.mParent = null;
        this.event = null;
    }

    public MethodNode getMethodNode(Event event) {
        if (event == null) {
            MethodNode node = new MethodNode(event);
            addNodeChildren(node);
            return node;
        }
        if (nodeChildren == null) {
            nodeChildren = new HashMap<>();
        }
        if (nodeChildren.containsKey(event.getMethodName())) {
            MethodNode duplicated = nodeChildren.get(event.getMethodName());
            duplicated.refCount += 1;
            return duplicated;
        } else {
            MethodNode node = new MethodNode(event);
            addNodeChildren(node);
            return node;
        }
    }

    public void addNodeChildren(MethodNode node) {
        if (nodeChildren == null) {
            nodeChildren = new HashMap<>();
        }
        String methodName = "Null";
        if (node.event != null) {
            methodName = node.event.getMethodName();
        }
        node.setLevel(level + 1);
        node.setNodeParent(this);
        nodeChildren.put(methodName, node);
        super.addChild(node);
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setLevel(int n) {
        level = n;
    }
    public void setNodeParent(MethodNode node) {
        nodeParent = node;
    }

    public MethodNode getNodeParent() {
        return nodeParent;
    }

    public void setMethodEndEvent(Event event) {
        endEvent = event;
    }

    public long getMethodCost() {
        if (event == null || endEvent == null) {
            return 0;
        }
        return endEvent.subtraction(this.event);
    }

}

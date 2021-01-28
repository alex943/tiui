package com.android.uiautomator2.instrumentation.page;

import com.android.uiautomator2.Logger;
import com.android.uiautomator2.instrumentation.arch.Traversal;
import com.android.uiautomator2.instrumentation.model.Event;
import com.android.uiautomator2.tree.BasicTreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Page extends Traversal<MethodNode> {
    private static List<String> lifeCycle = Arrays.asList(
            "onCreate", "onStart", "onResume", "onPause", "onStop", "onDestroy");
    private HashMap<String, MethodNode> lifeCycleNodes = new HashMap<>();

    private List<PageObserver> observers = new ArrayList<>();
    private MethodNode root, current;

    @Override
    public MethodNode getRoot() {
        return root;
    }

    public void add(Event event) {
        if (event == null)
            return;

        String methodName = event.getMethodName();
        String classnName = event.getClassName();

        if (event.getType() == 2) { // exit
            if (current != null && current.getNodeParent() != null) {
                current.setMethodEndEvent(event);
                current = current.getNodeParent();
            }
        } else if (event.getType() == 1) { //enter
            if (lifeCycle.contains(methodName) && (classnName.contains("Activity")
                            || classnName.contains("Fragment")
                            || classnName.contains("appcompat"))) { // is lifecycle

                Logger.d("Getting started #" + methodName
                                + " lifecycle record at " + classnName );

                if (lifeCycleNodes.containsKey(methodName)) {
                    root = lifeCycleNodes.get(methodName);
                    current = root;
                } else {
                    root = new MethodNode(event);
                    lifeCycleNodes.put(methodName, root);
                    current = root;
                }
            } else { // not life cycle
                if (current != null) {
                    current = current.getMethodNode(event);
                }
            }
        }
    }

    public void clear() {
        lifeCycleNodes.clear();
    }

    public void addObserver(PageObserver observer) {
        observers.add(observer);
    }

    public BasicTreeNode getOutput() {
        MethodNode dummy = new MethodNode(null);
        for (MethodNode node : lifeCycleNodes.values()) {
            dummy.addNodeChildren(node);
        }
        return dummy;
    }
}

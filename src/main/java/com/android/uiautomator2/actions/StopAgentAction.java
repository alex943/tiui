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

package com.android.uiautomator2.actions;

import com.android.uiautomator2.Logger;
import com.android.uiautomator2.UiAutomatorHelper;
import com.android.uiautomator2.UiAutomatorViewer;
import com.android.uiautomator2.instrumentation.TiRuntime;
import com.android.uiautomator2.instrumentation.viewtree.ViewNode;
import com.android.uiautomator2.instrumentation.viewtree.ViewTreeImpl;
import com.android.uiautomator2.tree.BasicTreeNode;
import com.android.uiautomator2.tree.UiNode;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import java.util.ArrayDeque;

public class StopAgentAction extends Action {
    private UiAutomatorViewer mViewer;

    public StopAgentAction(UiAutomatorViewer viewer) {
        super("&Stop");
        mViewer = viewer;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return ImageHelper.loadImageDescriptorFromResource("images/agent_stop.png");
    }

    private void printViewTree() {
        ViewTreeImpl viewTree = TiRuntime.getInstance().getViewTree();
        Logger.d(viewTree.toString());
    }

    private void dumpViewProperties() {
        TiRuntime.getInstance().stop();
        ViewTreeImpl viewTree = TiRuntime.getInstance().getViewTree();
        UiAutomatorHelper.UiAutomatorResult result = ScreenshotAction.getResult();
        BasicTreeNode root = result.model.getXmlRootNode();
        ArrayDeque<BasicTreeNode> nodeList = new ArrayDeque<>();
        nodeList.push(root);
        while (nodeList.size() != 0) {
            BasicTreeNode node = nodeList.pop();
            if (node instanceof UiNode) {
                UiNode uiNode = (UiNode) node;
                ViewNode sameView = viewTree.findViewByNode(uiNode);
                String clazz = uiNode.getAttribute("class");
                String resUI = uiNode.getAttribute("resource-id");
//                if (sameView != null) {
//                    System.out.println("<pair>");
//                    System.out.println(sameView.toString());
//                    System.out.println(sameView.getEvent().toString());
//                    System.out.println(clazz);
//                    System.out.println(resUI);
//                    System.out.println("");
//                    String count = String.valueOf(sameView.refCount);
//                    uiNode.addAtrribute("onMeasure", count);
//                } else {
//                    System.out.println("<not found>");
//                    System.out.println(clazz + " " + resUI);
//                    System.out.println("");
//                }
            }
            if (node.getChildrenList() != null) {
                for (BasicTreeNode child : node.getChildrenList()) {
                    nodeList.push(child);
                }
            }
        }
        //Logger.d("redraw..");
        mViewer.mUiAutomatorView.reload(result.model);
    }

    private void dumpPageProperties() {
        TiRuntime.getInstance().stop();
        mViewer.mUiAutomatorView.mPageViewPanel.setInput();
    }

    @Override
    public void run() {
        //printViewTree();
        dumpPageProperties();
    }
}

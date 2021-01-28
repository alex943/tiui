package com.android.uiautomator2.instrumentation.page;

import com.android.uiautomator2.Logger;
import com.android.uiautomator2.instrumentation.TiRuntime;
import com.android.uiautomator2.tree.BasicTreeNodeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;

public class PagePanelView extends TreeViewer {

    public PagePanelView(Composite parent, int style) {
        super(parent, style);
        init();
    }

    public void init() {
        setContentProvider(new BasicTreeNodeContentProvider());
        setLabelProvider(new LabelProvider());
    }

    public void setInput() {
        Page current = TiRuntime.getInstance().getPage();
        setInput(current.getOutput());
        current.clear();
    }
}

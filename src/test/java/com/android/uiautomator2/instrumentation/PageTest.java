package com.android.uiautomator2.instrumentation;

import com.android.uiautomator2.instrumentation.arch.TraversalCallback;
import com.android.uiautomator2.instrumentation.mock.OnPackageCallEvent;
import com.android.uiautomator2.instrumentation.mock.OnPageEvent;
import com.android.uiautomator2.instrumentation.page.MethodNode;
import com.android.uiautomator2.instrumentation.page.Page;
import org.junit.Test;

import static com.android.uiautomator2.instrumentation.arch.Traversal.BFS;

public class PageTest {

    @Test
    public void testOutput() {
        Page page = new Page();

        page.add(new OnPageEvent("11", 1, 1));
        page.add(new OnPageEvent("12", 1, 1));

        page.add(new OnPageEvent("13", 1, 1));

            page.add(new OnPackageCallEvent("a", 1, 1));
            page.add(new OnPackageCallEvent("a", 2, 1));

            page.add(new OnPackageCallEvent("c", 1, 1));
                page.add(new OnPackageCallEvent("b", 1, 1));
                    page.add(new OnPackageCallEvent("d", 1, 1));
                    page.add(new OnPackageCallEvent("d", 2, 1));
                page.add(new OnPackageCallEvent("b", 2, 1));
            page.add(new OnPackageCallEvent("c", 2, 1));

            page.add(new OnPackageCallEvent("e", 1, 1));
            page.add(new OnPackageCallEvent("e", 2, 1));

        page.add(new OnPageEvent("12", 2, 1));

        // check
        page.traversal(BFS, new TraversalCallback<MethodNode>() {
            @Override
            public boolean onNode(MethodNode event) {
                System.out.println(event);
                return false;
            }
        });
    }
}

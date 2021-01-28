package com.android.uiautomator2.instrumentation;

import com.android.uiautomator2.instrumentation.arch.Traversal;
import com.android.uiautomator2.instrumentation.arch.TraversalCallback;
import com.android.uiautomator2.instrumentation.mock.OnMeasureEvent;
import com.android.uiautomator2.instrumentation.mock.PerformEvent;
import com.android.uiautomator2.instrumentation.model.Event;
import com.android.uiautomator2.instrumentation.viewtree.ViewNode;
import com.android.uiautomator2.instrumentation.viewtree.ViewTreeImpl;
import org.junit.Assert;
import org.junit.Test;

public class ViewTreeTest {

    public Event[] enter, exit;
    public int postion = 0;

    public void build(int nu) {
        enter = new Event[nu + 1];
        exit = new Event[nu + 1];
        while (nu >= 0) {
            Event e, x;
            if (nu == 0) {
                e = new PerformEvent("a", 1, nu);
                x = new PerformEvent("a", 2, nu);
            } else {
                e = new OnMeasureEvent(String.valueOf('a' + nu - postion), 1, nu);
                x= new OnMeasureEvent(String.valueOf('a' + nu - postion), 2, nu);
            }
            enter[nu] = e;
            exit[nu--] = x;
            postion++;
        }
    }

    public ViewTreeImpl viewTree() {
        int nu = 0;
        ViewTreeImpl viewTree = new ViewTreeImpl(); // traversal will like:
        viewTree.add(enter[nu++]);  // tree: a , visitor: a  as Root
        viewTree.add(enter[nu++]); // tree: a.b , visitor: b
        viewTree.add(enter[nu++]); // tree: a.b.c , visitor: c

        viewTree.add(enter[nu]); // tree: a.b.c.d , visitor: d
        viewTree.add(exit[nu--]); // tree: a.b.c , no visit

        viewTree.add(enter[nu++]); // tree: a.b.c.e , visitor: e
        viewTree.add(exit[nu--]); // tree: a.b.c    no visit

        viewTree.add(exit[nu--]); // tree: a.b  no visit
        viewTree.add(exit[nu--]); // tree: a    no visit

        return viewTree;
    }

    @Test
    public void testTraversal() {
        // a, b, c, d, e
        // 0, 1, 2, 3, 4
        //    a0
        //   b1
        //  c2
        // d3  e4

        build(4);

        int[] hashcodes = new int[enter.length];
        for (int i = 0 ; i < enter.length; i++) {
            Event e = enter[i];
            hashcodes[i] = e.getHashCode();
        }

        ViewTreeImpl viewTree = viewTree();

        viewTree.traversal(Traversal.BFS, new TraversalCallback<ViewNode>() {
            int position = 0;
            @Override
            public boolean onNode(ViewNode view) {
                Event event = view.getEvent();
                if (event != null) {
                    if (event.getHashCode() != hashcodes[position++]) {
                        Assert.assertTrue(false);
                    }
                }
                return false;
            }
        });
    }

}

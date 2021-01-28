package com.android.uiautomator2.instrumentation;

import com.android.uiautomator2.instrumentation.mock.OnClassEvent;
import com.android.uiautomator2.instrumentation.page.MethodNode;
import org.junit.Assert;
import org.junit.Test;

public class MethodNodeTest {

    @Test
    public void testDisplayName() {
        MethodNode node = new MethodNode(
                new OnClassEvent("Landroid/app/Activity;", "onMeasure", 1, 1));
        boolean display = "Activity(onMeasure)".equals(node.getDisplayName());
        Assert.assertTrue(display);
    }
}

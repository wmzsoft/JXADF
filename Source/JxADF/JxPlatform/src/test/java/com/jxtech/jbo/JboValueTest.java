package com.jxtech.jbo;

import org.junit.Assert;
import org.junit.Test;

public class JboValueTest {

    @Test
    public void testIsReadonly() {
        JboValue v = new JboValue();
        v.setReadonly(false);
        Assert.assertFalse(v.isReadonly());
        v.setReadonly(true);
        v.setRequired(false);
        Assert.assertTrue(v.isReadonly());
    }

    @Test
    public void testIsRequired() {
        JboValue v = new JboValue();
        v.setRequired(false);
        v.setVisible(true);
        Assert.assertFalse(v.isRequired());
        v.setRequired(true);
        Assert.assertTrue(v.isRequired());
    }

    @Test
    public void testIsVisible() {
        JboValue v = new JboValue();
        v.setReadonly(true);
        v.setVisible(false);
        Assert.assertFalse(v.isVisible());
        v.setVisible(true);
        Assert.assertTrue(v.isVisible());
    }

}

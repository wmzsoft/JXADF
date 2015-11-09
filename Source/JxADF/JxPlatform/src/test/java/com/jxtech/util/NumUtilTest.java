package com.jxtech.util;

import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author wmzsoft@gmail.com
 * @date 2015.04
 */
public class NumUtilTest {
    private static final Logger LOG = LoggerFactory.getLogger(NumUtilTest.class);

    @Test
    public void testFormat() {
        double v = 1000.01110D;
        Assert.assertEquals("100,001.1%", NumUtil.format(v, "%"));
        Assert.assertEquals("$1,000.01", NumUtil.format(v, "$"));
        Assert.assertEquals("22", NumUtil.format(22, null));
        Assert.assertEquals("33322.09", NumUtil.format(33322.09, null));
        LOG.debug(NumUtil.format(v, "currency"));
        LOG.debug(NumUtil.format(2.0E9, null));
        double a = 1002;
        double b = 21323;
        double d = a/b;
        
        LOG.debug("d="+NumUtil.round(d, 2));
    }

}

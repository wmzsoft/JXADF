package com.jxtech.util;

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public class ClassUtilTest {

    @Test
    public void testGetStaticMethod() {
        Object obj = ClassUtil.getStaticMethod("com.jxtech.util.DateUtil", "now", null, null);
        if (obj == null) {
            fail("failed");
        }
    }

}

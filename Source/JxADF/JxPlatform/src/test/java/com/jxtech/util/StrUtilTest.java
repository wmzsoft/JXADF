package com.jxtech.util;

import java.text.MessageFormat;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author wmzsoft@gmail.com
 * @date 2014.11
 */
public class StrUtilTest {
    private static final Logger LOG = LoggerFactory.getLogger(StrUtilTest.class);

    @Test
    public void testToJson() {
        assertEquals("abc\\\"def", StrUtil.toJson("abc\"def"));
        assertEquals("a\\tbc\\nd\\ref", StrUtil.toJson("a\tbc\nd\ref"));
    }

    @Test
    public void testToString() {
        Object[] obj = new Object[] { "abc", "def" };
        assertTrue("abc,def".equals(StrUtil.toString(obj)));
    }

    @Test
    public void testDeleteLastChar() {
        StringBuilder sb = new StringBuilder("ab  c,d,    ");
        String s1 = StrUtil.removeLastChar(sb.toString());
        String s2 = StrUtil.deleteLastChar(sb).toString();
        assertTrue(s1.equals(s2));
    }

    @Test
    public void testIndexOf() {
        assertEquals(0, StrUtil.indexOf("abc", "a"));
        assertEquals(1, StrUtil.indexOf("abc", "b|d|1"));
        assertEquals(1, StrUtil.indexOf("abc", "b|d|1", 1));
        assertEquals(3, StrUtil.indexOf("abcd", "xb|d|1", 1));
        assertEquals(-1, StrUtil.indexOf("abcd", "xb|xd|1", 1));

    }

    @Test
    public void testHowManyStr() {
        assertEquals(2, StrUtil.howManyStr("abcb", "(?i)B"));
        assertEquals(7, StrUtil.howManyStr("a''b'''c''b", "'"));
    }

    @Test
    public void testContact() {
        Assert.assertNull(StrUtil.contact(null));
        Assert.assertEquals("ABCDEFG", StrUtil.contact("A", "B", "C", "DEFG"));
    }

    @Test
    public void testMessage() {
        String msg = "{0},hello";
        String ok = MessageFormat.format(msg, new Object[] { "wmz" });
        Assert.assertEquals("wmz,hello", ok);
    }
    
    @Test
    public void testR(){
        String str = "${base}/jxbox";
        String nstr = str.replaceAll("\\$\\{base\\}", "/jxweb");
        Assert.assertEquals("/jxweb/jxbox", nstr);
    }
}

package com.jxtech.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
public class FileUtilTest {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtilTest.class);

    @Test
    public void testFormatPath() {
        LOG.debug(FileUtil.formatPath("file:/D:\\work\\hello"));
        LOG.debug(FileUtil.formatPath("file:\\/D:/work/hello"));
    }

    @Test
    public void testByteArrayToHex() {
        byte[] b = new byte[] { 1, 2, 12, 43 };
        Assert.assertEquals("01020C2B", FileUtil.byteArrayToHex(b));
    }

    @Test
    public void testGetFileName() {
        String path = "D:\\abc\\a.png";
        Assert.assertEquals("a.png", FileUtil.getFileName(path));
    }
}

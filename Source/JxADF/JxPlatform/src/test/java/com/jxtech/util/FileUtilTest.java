package com.jxtech.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        LOG.debug( FileUtil.formatPath("file:/D:\\work\\hello"));
        LOG.debug( FileUtil.formatPath("file:\\/D:/work/hello"));
    }

}

package com.jxtech.util;

import com.jxtech.jbo.util.JxException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
public class UrlUtilTest {
    private static final Logger LOG = LoggerFactory.getLogger(UrlUtilTest.class);

    @Test
    public void testGetUrlContent() {
        LOG.debug((String) UrlUtil.getUrlContent("http://www.baidu.com", "UTF-8"));
    }

    @Test
    public void testDownloadFile() {
        try {
            String local = UrlUtil.downloadFile("http://svn.jxtech.net/style/imgs/jj.png", null, "*.*");
            LOG.debug(local);
        } catch (JxException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}

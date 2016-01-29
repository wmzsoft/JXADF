package com.jxtech.util;

import com.jxtech.jbo.util.JxException;

import junit.framework.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        //LOG.debug((String) UrlUtil.getUrlContent("http://www.baidu.com", "UTF-8"));
    }

    //@Test
    public void testDownloadFile() {
        try {
            String local = UrlUtil.downloadFile("http://svn.jxtech.net/style/imgs/jj.png", null, "*.*");
            LOG.debug(local);
        } catch (JxException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Test
    public void testMather() {
        String s1 = "/jxbpmcomposer/lookup.*action";
        String s2 = "/jxweb/jxbpmcomposer/lookup_user-active-list-table.action";
        Pattern p = Pattern.compile(s1);
        Matcher matcher = p.matcher(s2);
        Assert.assertEquals(true, matcher.find());
    }
    
    @Test
    public void testLoginMe(){
        String url = "http://127.0.0.1/jxweb/jxemail/index.action?username=admin&password=123456";
        String sid = ""+UrlUtil.getUrlContent(url, false);
        LOG.debug(sid);
    }
}

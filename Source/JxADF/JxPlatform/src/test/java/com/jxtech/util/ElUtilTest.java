package com.jxtech.util;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.base.DataMap;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.ELUtil;

/**
 * Created by cxm on 15/4/10
 */
public class ElUtilTest {
    private static final Logger LOG = LoggerFactory.getLogger(ElUtilTest.class);

    @Test
    public void testGetConditionELValue() {
        Assert.assertTrue(ELUtil.getConditionELValue("${4 >3 && 4 >= 2}"));
    }

    @Test
    public void testParseJboElValue() {
        try {
            JboSetIFace js = new JboSet();
            JboIFace jbo = js.getJbo(true);
            Map<String, Object> data = new DataMap<String, Object>();
            data.put("A", "V1");
            data.put("b", "V2");
            jbo.setData(data);
            String el = "${a}-${b}${cc}dd";
            Assert.assertEquals("V1-V2dd", ELUtil.parseJboElValue(jbo, el));
            el = "${}${a}-${b}${cc}dd";
            Assert.assertEquals("V1-V2dd", ELUtil.parseJboElValue(jbo, el));
        } catch (JxException e) {
            LOG.error(e.getMessage(), e);
        }
    }

}

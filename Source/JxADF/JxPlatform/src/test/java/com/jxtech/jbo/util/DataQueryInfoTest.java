package com.jxtech.jbo.util;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.util.DateUtil;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.07
 * 
 */
public class DataQueryInfoTest {
    private static Logger LOG = LoggerFactory.getLogger(DataQueryInfoTest.class);

    @Test
    public void testGetQueryId() {
        DataQueryInfo dqi = new DataQueryInfo();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("X like ?", "aaa%");
        params.put("amx = ?", DateUtil.sqlDate());
        params.put("xy=?", new Date());
        params.put("yy>?", 123445);
        dqi.setParams(params);
        Assert.assertEquals(dqi.getQueryId(false), dqi.getQueryId(true));
    }

}

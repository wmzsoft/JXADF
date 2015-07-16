package com.jxtech.util;

import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
public class JsonUtilTest {
    private static final Logger LOG = LoggerFactory.getLogger(JsonUtilTest.class);

    @Test
    public void testGetJsonOfList() {
    }

    @Test
    public void testFromJson() {
        String json = "[{\"MAP\":\"1\",\"X\":\"2\"}]";
        List<Map<String, Object>> list = JsonUtil.fromJson(json);
        Assert.assertEquals("1", list.get(0).get("MAP"));
        LOG.debug(list.toString());
        json = "";
        Assert.assertNull(JsonUtil.fromJson(json));        
    }
    
    @Test
    public void testToJson(){
        String json = "[{\"MAP\":\"1\",\"X\":\"2\"}]";
        List<Map<String, Object>> list = JsonUtil.fromJson(json);
        LOG.debug(JsonUtil.toJson(list));
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("A", "\tabc");
        map.put("B", "\r\nx\fabc");
        list.add(map);
        LOG.debug(JsonUtil.toJson(list));        
    }
}

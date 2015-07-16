package com.jxtech.util;

import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class MapUtil {

    public static String toString(Map<?, ?> map) {
        if (map == null) {
            return null;
        }
        @SuppressWarnings("rawtypes")
        Iterator iter = map.entrySet().iterator();
        StringBuffer sb = new StringBuffer();
        while (iter.hasNext()) {
            @SuppressWarnings("rawtypes")
            Map.Entry entry = (Map.Entry) iter.next();
            sb.append("(" + entry.getKey() + "," + entry.getValue() + ")\r\n");
        }
        return sb.toString();
    }
}

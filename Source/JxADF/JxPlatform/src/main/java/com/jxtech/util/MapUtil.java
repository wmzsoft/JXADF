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
        StringBuilder sb = new StringBuilder();
        while (iter.hasNext()) {
            @SuppressWarnings("rawtypes")
            Map.Entry entry = (Map.Entry) iter.next();
            sb.append("(").append(entry.getKey()).append(",").append(entry.getValue()).append(")\r\n");
        }
        return sb.toString();
    }
}

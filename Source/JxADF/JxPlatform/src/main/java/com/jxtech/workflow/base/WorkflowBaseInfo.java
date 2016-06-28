package com.jxtech.workflow.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.util.JxException;

/**
 * 架构性质的类，后面添加。 Created by cxm on 2014/8/15.
 */
public abstract class WorkflowBaseInfo {
    public abstract boolean isRouteAble();

    public abstract Map<String, JboIFace> getActionMaps();

    /**
     * 返回ID=描述
     * 
     * @return
     * @throws JxException
     */
    public Map<String, String> getActionMap() throws JxException {
        Map<String, JboIFace> maps = getActionMaps();
        if (maps != null && !maps.isEmpty()) {
            Iterator<Entry<String, JboIFace>> iter = maps.entrySet().iterator();
            Map<String, String> list = new HashMap<String, String>();
            while (iter.hasNext()) {
                Entry<String, JboIFace> entry = iter.next();
                list.put(entry.getKey(), entry.getValue().getString("INSTRUCTION"));
            }
            return list;
        }
        return null;
    }

    public Map<String, String> getActionUserMap() {
        return null;
    }

    public abstract String getStatus();

    public abstract boolean isComplete();
}

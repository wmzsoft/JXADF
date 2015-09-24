package com.jxtech.workflow.base;

import java.util.Map;

/**
 * 架构性质的类，后面添加。 Created by cxm on 2014/8/15.
 */
public abstract class WorkflowBaseInfo {
    public abstract boolean isRouteAble();

    public abstract Map<String, String> getActionMap();

    public Map<String, String> getActionUserMap() {
        return null;
    }

    public abstract String getStatus();

    public abstract boolean isComplete();
}

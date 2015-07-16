package com.jxtech.app.dashboard.let;

import java.util.Map;

/**
 * Created by cxm on 2014/11/24.
 */
public interface DashboardLetIFace {
    public String getLetData(Map<String, String> params);

    public String getLetContent(Map<String, String> params);
}

package com.jxtech.app.dashboard.plugin;

import java.util.Map;

/**
 * Created by chenxiaomin@jxtech.net on 2015/1/12.
 */
public interface IDashboardLetPlugin {
    public static String IFRAME_TYPE = "iframe";
    public static String DIV_TYPE = "div";
    public static String INNER_TYPE = "inner";

    /**
     * 获取插件在pom中定义deartifactid值
     *
     * @return
     */
    public String getArtifactId();

    public String getTitle();

    public String getPreview();

    public String getDescription();

    public String getType();

    public String getContent();

    public String getLetData(Map<String, String> paramMap);
}

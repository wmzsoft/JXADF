package com.jxtech.app.jxvars;

/**
 * 保存统一的变量配置信息
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.05
 * 
 */
public interface JxVars {
    public static final String CACHE_PREX = "JXVARS.";

    public String getValue(String key);

    public String getValue(String key, String def);

    public void setValue(String key, String value);
}

package com.jxtech.app.jxvars;

import com.jxtech.util.SysPropertyUtil;

/**
 * 保存统一的变量配置信息
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.05
 * 
 */
public class JxVarsImpl implements JxVars {

    @Override
    public String getValue(String key) {
        return getValue(key, null);
    }

    @Override
    public String getValue(String key, String def) {
        return System.getProperty(key, def);
    }

    @Override
    public void setValue(String key, String value) {
        SysPropertyUtil.storeSystemProperties(key, value);
    }

}

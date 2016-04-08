package com.jxtech.app.jxvars;

import com.jxtech.util.StrUtil;
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

    @Override
    public long getLong(String key, long def) {
        String value = getValue(key, null);
        if (!StrUtil.isNull(value)) {
            try {
                return Long.parseLong(value);
            } catch (Exception e) {
                return def;
            }
        }
        return def;
    }

    @Override
    public double getDouble(String key, double def) {
        String value = getValue(key, null);
        if (!StrUtil.isNull(value)) {
            try {
                return Double.parseDouble(value);
            } catch (Exception e) {
                return def;
            }
        }
        return 0;
    }

    @Override
    public boolean getBoolean(String key, boolean def) {
        String value = getValue(key, null);
        return StrUtil.isTrue(value, def);
    }

}

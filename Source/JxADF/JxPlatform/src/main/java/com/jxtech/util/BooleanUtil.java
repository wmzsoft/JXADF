package com.jxtech.util;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.11
 * 
 */
public class BooleanUtil {
    public static final String DEFAULT = "TRUE/FALSE";
    public static final String YORN = "Y/N";
    public static final String YESORNO = "YES/NO";
    public static final String ACTIVE = "ACTIVE/INACTIVE";

    /**
     * 格式化
     * 
     * @param value
     * @param formatter
     * @param defaultvalue
     * @return
     */
    public static String format(Object value, String formatter, boolean defaultvalue) {
        if (formatter == null) {
            formatter = DEFAULT;
        }
        String[] vals = formatter.split("/");
        if (StrUtil.isTrue((String) value, defaultvalue)) {
            if (vals.length >= 2) {
                return vals[0];
            } else {
                return "true";
            }
        } else {
            if (vals.length >= 2) {
                return vals[1];
            } else {
                return "false";
            }
        }
    }

}

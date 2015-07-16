package com.jxtech.common;

import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.util.StrUtil;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * User: jfy Date: 13-10-19 上午9:37
 */
public class JxFunctions {

    /**
     * 支持扩展的基础 ${jxfn:property("userid")}
     * 
     * @param propname
     * @return
     */
    public static String getUserProperty(String propname) {
        return getProperty(null, propname);
    }

    /**
     * 支持扩展的基础 ${jxfn:property(sessionScope.jxuserinfo,"userid")}
     * 
     * @param userinfo
     * @param propname
     * @return
     */
    public static String getProperty(JxUserInfo userinfo, String propname) {
        try {
            if (userinfo == null) {
                userinfo = JxSession.getJxUserInfo();
            }
            return PropertyUtils.getProperty(userinfo, propname).toString();
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * 返回SQL的条件
     * 
     * @param attribute
     * @param value
     * @return
     */
    public static String getSqlCause(String attribute, Object value) {
        String cause = "";
        if (null != value && !StrUtil.isNull(value.toString())) {
            cause = " and " + attribute + " = '" + value + "'";
        }
        return cause;
    }

}

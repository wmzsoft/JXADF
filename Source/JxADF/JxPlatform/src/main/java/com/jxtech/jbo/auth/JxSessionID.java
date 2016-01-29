package com.jxtech.jbo.auth;

import java.util.Date;

import com.jxtech.app.jxvars.JxVars;
import com.jxtech.app.jxvars.JxVarsFactory;
import com.jxtech.util.DateUtil;
import com.jxtech.util.StrUtil;

/**
 * 用于构造JX Session 的标识
 * 
 * @author wmzsoft@gmail.com
 *
 */
public class JxSessionID {
    public static final String ID = "jxsessionid";
    public static String MYKEY = null;

    /**
     * 通过SessionID解析用户名
     * 
     * @param sessionid
     * @return
     */
    public static String getUserId(String sessionid) {
        if (StrUtil.isNull(sessionid)) {
            return null;
        }
        String[] sid = sessionid.split(";");
        if (sid.length == 2) {
            String gid = genId(sid[1]);
            if (sessionid.equals(gid)) {
                return sid[1];
            }
        }
        return null;
    }

    /**
     * 通过用户名生成唯一ID
     * 
     * @param userId
     * @return
     */
    public static String genId(String userId) {
        String key = StrUtil.contact(userId, ".", getKey());
        return StrUtil.contact(StrUtil.md5(key), ";", userId);
    }

    /**
     * 生成唯一KEY，第一次访问时自动生成。
     * 
     * @return
     */
    public static String getKey() {
        if (MYKEY == null) {
            MYKEY = String.valueOf(DateUtil.datetimeToLong(new Date()) + Math.random() + Math.round(1000.1D));
        }
        return MYKEY;
    }
}

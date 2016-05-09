package com.jxtech.jbo.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private static long sessiontime = 0;// 限时2小时
    private static Map<String, Date> users = new HashMap<String, Date>();// 将生成的KEY的时间保存起来，超时无效

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
                // 检查一下生成时间，是否超期
                Date gd = users.get(sid[1]);
                if (gd == null) {
                    return null;
                } else if (DateUtil.subDays(new Date(), gd) > getSessiontime()) {
                    users.remove(sid[1]);
                    return null;
                }
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
        if (StrUtil.isNull(userId)) {
            return null;
        }
        String key = StrUtil.contact(userId, ".", getKey());
        users.put(userId, new Date());// 记录时间
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

    /**
     * 获得超时设定，默认值为2小时,7200000 毫秒
     * 
     * @return
     */
    public static long getSessiontime() {
        if (sessiontime <= 0) {
            sessiontime = JxVarsFactory.getInstance().getLong("jxsession.sesstiontime", 7200000);
        }
        return sessiontime;
    }

    /**
     * 移出当前生成了KEY的帐号
     */
    public static void removeCurrentUser() {
        String userid = JxSession.getUserId();
        if (!StrUtil.isNull(userid)) {
            users.remove(userid);
        }
    }
}

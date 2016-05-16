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
    private static long sessiontime = 0;// 限时2小时
    // 将生成的KEY\时间保存起来，超时无效
    private static final Map<String, String[]> users = new HashMap<String, String[]>();

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
        long now = (new Date()).getTime();
        long st = getSessiontime();
        for (Map.Entry<String, String[]> entry : users.entrySet()) {
            String[] val = entry.getValue();
            if (sessionid.equals(val[0])) {
                long delta = now - Long.parseLong(val[1]);
                if (delta < st) {
                    return entry.getKey();
                } else {
                    // 超时，移出
                    users.remove(entry.getKey());
                    return null;
                }
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
        String[] sessions = users.get(userId);
        long now = (new Date()).getTime();
        if (sessions != null) {
            long delta = now - Long.parseLong(sessions[1]);
            // 如果10分钟之内，不需要重新生成ID了。
            if (delta < getSessiontime() && delta < 600000) {
                return sessions[0];
            }
        }
        String key = StrUtil.contact(userId, ".", getKey());
        String md5 = StrUtil.md5(key);
        String[] ticket = new String[] { md5, String.valueOf(now) };
        users.put(userId, ticket);// 记录时间
        return md5;
    }

    /**
     * 生成唯一KEY，第一次访问时自动生成。
     * 
     * @return
     */
    public static String getKey() {
        return String.valueOf(DateUtil.datetimeToLong(new Date()) + Math.random() + Math.round(1000.1D));
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

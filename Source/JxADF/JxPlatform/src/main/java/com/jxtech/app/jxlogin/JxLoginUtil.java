package com.jxtech.app.jxlogin;

import java.util.HashMap;
import java.util.Map;

import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;

/**
 * 登陆接口工具类 登陆验证请调用JxLoginFactory.login
 * 
 * @author luopiao@jxtech.net
 * 
 */
public class JxLoginUtil {

    public static boolean login(String userid, String password, boolean relogin) throws JxException {
        JxLoginImpl login = new JxLoginImpl();
        return login.login(userid, password, relogin,null);
    }

    public static boolean login(String userid, String password, boolean relogin, String langcode) throws JxException {
        JxLoginImpl login = new JxLoginImpl();
        Map<String, Object> params = new HashMap<String, Object>();
        if (StrUtil.isNull(langcode)){
            langcode = "zh-CN";
        }
        params.put(JxUserInfo.LANG_CODE, langcode);
        return login.login(userid, password, relogin, params);
    }
}

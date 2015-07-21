package com.jxtech.app.jxlogin;

import java.util.Map;

import com.jxtech.jbo.auth.AuthenticateFactory;
import com.jxtech.jbo.auth.AuthenticateIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2015年05月25日把JxSession的登陆login 方式移到了JxLoginImpl实现
 * 
 * @author luopiao@jxtech.net
 * 
 */
public class JxLoginImpl implements JxLogin {
    private static final Logger LOG = LoggerFactory.getLogger(JxLoginImpl.class);

    @Override
    public boolean login(String userid, String password, boolean relogin) throws JxException {
        return login(userid, password, relogin, null);

    }

    @Override
    public boolean beforeLogin(String userid, String password, boolean relogin, Map<String, Object> params) {
        if (StrUtil.isNull(userid)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean afterLogin(String userid, String password, boolean relogin, Map<String, Object> params) {
        return true;
    }

    @Override
    public boolean login(String userid, String password, boolean relogin, Map<String, Object> params) throws JxException {
        if (!beforeLogin(userid, password, relogin, params)) {
            return false;
        }
        LOG.debug("login user:" + userid);
        JxUserInfo userinfo = null;
        if (!relogin) {// 不需要重新登录
            userinfo = JxSession.getJxUserInfo();
            if (userinfo != null) {
                if (userid.equalsIgnoreCase(userinfo.getLoginid())) {
                    return true;
                }
            }
        }
        // 登录
        AuthenticateIFace auth = AuthenticateFactory.getAuthenticateInstance();
        userinfo = auth.getUserInfo(userid, password);
        if (userinfo == null) {
            return false;
        } else if (params != null) {
            Object lang = params.get(JxUserInfo.LANG_CODE);
            if (lang != null) {
                String code = (String)lang;
                code = code.replace('-', '_');
                userinfo.setLangcode(code);
            }
        }
        // 加载登录之后的操作
        return afterLogin(userid, password, relogin, params);
    }

}

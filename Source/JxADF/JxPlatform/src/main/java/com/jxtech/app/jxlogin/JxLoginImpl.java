package com.jxtech.app.jxlogin;

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

        if (StrUtil.isNull(userid)) {
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
        AuthenticateIFace auth = AuthenticateFactory.getAuthenticateInstance();
        userinfo = auth.getUserInfo(userid, password);
        if (userinfo != null) {
            // putSession(USER_INFO, userinfo);
            JxSession.putSession(JxSession.USER_INFO, userinfo);
        } else {
            return false;
        }
        return true;

    }

    @Override
    public void beforeLogin() {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterLogin(JxUserInfo userinfo) {
        // TODO Auto-generated method stub

    }

    @Override
    public void loginFailure(String userid) {
        // TODO Auto-generated method stub

    }

}

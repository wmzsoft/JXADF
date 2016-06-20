package com.jxtech.app.jxlogin;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.jxtech.app.usermetadata.UserMetadataSetIFace;
import com.jxtech.distributed.Configuration;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.AuthenticateFactory;
import com.jxtech.jbo.auth.AuthenticateIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JboUtil;
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
    public boolean afterLogin(JxUserInfo user, Map<String, Object> params) {
        // 放入Session中。
        JxSession.putSession(JxSession.USER_INFO, user);
        // 获得Session的有效时间
        HttpSession session = JxSession.getSession();
        if (session != null) {
            int timeout = session.getMaxInactiveInterval();
            Configuration.getInstance().setSessionTimeOut(timeout * 1000);//转换为耗秒
        }
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
                String code = (String) lang;
                code = code.replace('-', '_');
                userinfo.setLangcode(code);
                // 保存Longcode
                JboSetIFace ju = JboUtil.getJboSet("UserMetadata");
                if (ju instanceof UserMetadataSetIFace) {
                    ((UserMetadataSetIFace) ju).saveUserMetadata(JxUserInfo.LANG_CODE, code);
                }
            }
        }
        // 加载登录之后的操作
        return afterLogin(userinfo, params);
    }

}

package com.jxtech.jbo.auth;

import com.jxtech.app.jxvars.JxVarsFactory;
import com.jxtech.jbo.auth.ctp.PermissionCtpDao;
import com.jxtech.util.ClassUtil;
import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录认证模块的工厂类
 * 
 * @author Administrator
 */
public class AuthenticateFactory {
    private static Logger LOG = LoggerFactory.getLogger(AuthenticateFactory.class);
    public static final String AUTHENTICATE_CLASS = "jx.authenticate.class";

    public static String implClass;

    /**
     * 如果插件想定义自己的验证类，则只需要AuthenticateFactory.setAuthenticateImpl(String implClass);
     * 
     * @return
     */
    public static AuthenticateIFace getAuthenticateInstance() {
        if (StrUtil.isNull(implClass)) {
            implClass = JxVarsFactory.getInstance().getValue(AUTHENTICATE_CLASS);
        }
        if (!StrUtil.isNull(implClass)) {
            try {
                Object obj = ClassUtil.getInstance(implClass);
                if (obj instanceof AuthenticateIFace) {
                    return (AuthenticateIFace) obj;
                }
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }
        return new PermissionCtpDao();
    }

    public static void setAuthenticateImpl(String implClass) {
        AuthenticateFactory.implClass = implClass;
    }

}

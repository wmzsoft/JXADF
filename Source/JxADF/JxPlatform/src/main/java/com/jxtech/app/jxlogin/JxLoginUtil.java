package com.jxtech.app.jxlogin;

import com.jxtech.jbo.util.JxException;
import com.jxtech.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 登陆接口工具类 登陆验证请调用JxLoginFactory.login
 * 
 * @author luopiao@jxtech.net
 * 
 */
public class JxLoginUtil {
    private static final Logger LOG = LoggerFactory.getLogger(JxLoginUtil.class);
    // 实现类<名称，类名>
    private static Map<String, String> loginImpls = new HashMap<String, String>();

    public static boolean login(String userid, String password, boolean relogin) throws JxException {
        if (loginImpls.size() < 1) {
            LOG.debug("登陆实现类为0");
            return new JxLoginImpl().login(userid, password, relogin);
        }
        for (Map.Entry<String, String> entry : loginImpls.entrySet()) {
            Object obj = ClassUtil.getInstance(entry.getValue());
            if (obj instanceof JxLogin) {
                if (!((JxLogin) obj).login(userid, password, relogin)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 设置登陆接口实现类
     * 
     * @param implClass
     */
    public static void putImplClass(String key, String implClass) {
        loginImpls.put(key, implClass);
    }

    /**
     * 移去登陆接口实现类
     * 
     * @param implClass
     */
    public static void removeImplClass(String key) {
        loginImpls.remove(key);
    }

}

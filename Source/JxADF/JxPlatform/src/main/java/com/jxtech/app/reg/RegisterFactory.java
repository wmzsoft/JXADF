package com.jxtech.app.reg;

import com.jxtech.jbo.util.JxException;
import com.jxtech.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/6 0006.
 */

public class RegisterFactory {
    private static final Logger LOG = LoggerFactory.getLogger(RegisterFactory.class);
    // 实现类
    private static String regClass = "";

    /**
     *  注册
     *
     * @param account 账号
     * @return
     * @throws JxException
     */
    public static boolean Register(String account, String password, String mobile, String requestor, String company) throws JxException {
        if (null == regClass || regClass.isEmpty()) {
            LOG.warn("你未安装注册插件。");
            return false;
        }

        Object obj = ClassUtil.getInstance(regClass);
        if (obj != null && obj instanceof IRegister) {
            return ((IRegister) obj).Register(account, password, mobile, requestor, company);
        }

        return false;
    }

    public static String RegisterUrl(){
        if (null == regClass || regClass.isEmpty()) {
            LOG.warn("你未安装注册插件。");
            return "";
        }

        Object obj = ClassUtil.getInstance(regClass);
        if (obj != null && obj instanceof IRegister) {
            return ((IRegister) obj).RegisterUrl();
        }
        return "";
    }

    public static String getRegClass() {
        return regClass;
    }

    public static void setImplClass(String regClass) {
        LOG.debug("安装注册插件：" + regClass);
        RegisterFactory.regClass = regClass;
    }
}

package com.jxtech.app.jxvars;

import com.jxtech.util.ClassUtil;
import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 保存统一的变量配置信息
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.05
 * 
 */
public class JxVarsFactory {
    private static final Logger LOG = LoggerFactory.getLogger(JxVarsFactory.class);

    // 实现变量管理的类
    private static String implClass = null;
    // 实际类
    private static JxVars jxvarsInstance = new JxVarsImpl();
    private static boolean flag = true;

    public static JxVars getInstance() {
        if (jxvarsInstance != null && flag) {
            return jxvarsInstance;
        }
        return newInstance();
    }

    public static void setImplClass(String implClass) {
        JxVarsFactory.implClass = implClass;
        flag = false;
    }

    private static JxVars newInstance() {
        flag = true;
        if (StrUtil.isNull(implClass)) {
            jxvarsInstance = new JxVarsImpl();
            return jxvarsInstance;
        }
        Object obj = ClassUtil.getInstance(implClass);
        if (obj instanceof JxVars) {
            jxvarsInstance = (JxVars) obj;
        } else {
            LOG.warn("配置的变量管理类" + implClass + "不能初始化。");
            jxvarsInstance = new JxVarsImpl();
        }
        return jxvarsInstance;
    }
}

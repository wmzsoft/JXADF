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

    public static JxVars getInstance() {
        if (StrUtil.isNull(implClass)) {
            return new JxVarsImpl();
        }
        Object obj = ClassUtil.getInstance(implClass);
        if (obj != null && obj instanceof JxVars) {
            return (JxVars) obj;
        } else {
            LOG.warn("配置的变量管理类" + implClass + "不能初始化。");
        }
        return null;
    }

    public static void setImplClass(String implClass) {
        JxVarsFactory.implClass = implClass;
    }
}

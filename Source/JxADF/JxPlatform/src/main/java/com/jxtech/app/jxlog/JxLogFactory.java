package com.jxtech.app.jxlog;

import com.jxtech.jbo.util.JxException;
import com.jxtech.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志管理
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public class JxLogFactory {
    private static final Logger LOG = LoggerFactory.getLogger(JxLogFactory.class);
    // 实现类，可在日志的插件类中初始化。
    private static String implClass = null;

    public static JxLog getJxLog(String app, String jboname) {
        if (implClass == null) {
            try {
                return JxLogImpl.getInstance(app, jboname);
            } catch (JxException e) {
                LOG.error(e.getMessage(), e);
            }
        } else {
            Object obj = ClassUtil.getInstance(implClass);
            if (obj != null && obj instanceof JxLog) {
                JxLog jl = (JxLog) obj;
                jl.setApp(app);
                jl.setJboname(jboname);
                return jl;
            } else {
                LOG.warn("配置的日志管理类" + implClass + "不能初始化。");
            }
        }
        return null;
    }

    public static void setImplClass(String implClass) {
        JxLogFactory.implClass = implClass;
    }

}

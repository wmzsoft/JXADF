package com.jxtech.app.jxlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.util.CacheUtil;
import com.jxtech.util.ClassUtil;
import com.jxtech.util.StrUtil;

/**
 * 日志管理
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public class JxLogFactory {
    private static final Logger LOG = LoggerFactory.getLogger(JxLogFactory.class);
    public static final String CACHE_PREX = "jxlog.instance.";
    // 实现类，可在日志的插件类中初始化。
    private static String implClass = null;

    public static JxLog getJxLog(String app, String jboname) {
        if ("JXLOG".equalsIgnoreCase(jboname)) {
            // 这个是避免死循环的操作
            return null;
        }
        if (implClass == null) {
            return new JxLogImpl(app, jboname);
        } else {
            String key = StrUtil.contact(CACHE_PREX, app, ".", jboname);
            Object obj = CacheUtil.getBase(key);
            if (obj instanceof JxLog) {
                return (JxLog) obj;
            }
            obj = ClassUtil.getInstance(implClass);
            if (obj instanceof JxLog) {
                JxLog jl = (JxLog) obj;
                jl.setApp(app);
                jl.setJboname(jboname);
                CacheUtil.putBaseCache(key, jl);// 保存缓存
                return jl;
            } else {
                LOG.warn("配置的日志管理类" + implClass + "不能初始化。");
            }
        }
        return null;
    }

    public static void setImplClass(String implClass) {
        JxLogFactory.implClass = implClass;
        CacheUtil.removeBaseOfStartWith(CACHE_PREX);
    }

}

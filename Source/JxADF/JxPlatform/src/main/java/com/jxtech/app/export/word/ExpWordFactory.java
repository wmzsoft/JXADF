package com.jxtech.app.export.word;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.util.ClassUtil;
import com.jxtech.util.StrUtil;

/**
 * 处理Word导出的工厂类
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.11
 *
 */
public class ExpWordFactory {
    private static Logger LOG = LoggerFactory.getLogger(ExpWordFactory.class);
    // 实现类
    private static String implClassName;

    public static ExpWord getInstance() {
        if (StrUtil.isNull(implClassName)) {
            implClassName = "com.jxtech.expword.common.ExpImpl";
        }
        Object obj = ClassUtil.getInstance(implClassName, true);
        if (obj instanceof ExpWord) {
            return (ExpWord) obj;
        }
        LOG.warn("请注册正确的Word导出类：" + implClassName);
        return null;
    }

    public static String getImplClassName() {
        return implClassName;
    }

    public static void setImplClassName(String implClassName) {
        ExpWordFactory.implClassName = implClassName;
    }
}

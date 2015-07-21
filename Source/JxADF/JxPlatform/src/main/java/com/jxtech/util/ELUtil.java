package com.jxtech.util;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JxException;

/**
 * 处理EL表达式，本处只处理Groovy表达式。
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class ELUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ELUtil.class);

    /**
     * 支持用户信息（JxUserInfo）EL表达式
     * 
     * @param userinfo
     * @param expression
     * @return
     */
    public static String getElOfJxUser(JxUserInfo userinfo, String expression) {
        return getElValue(null, null, userinfo, expression);
    }

    public static String getElValue(String expression) {
        return getElValue(null, null, JxSession.getJxUserInfo(), expression);
    }

    /**
     * 支持登录账号信息、JBO信息的EL表达式
     * 
     * @param jbo
     * @param expression
     * @return
     */
    public static String getJboElValue(JboIFace jbo, String expression) {
        return getElValue(null, jbo, JxSession.getJxUserInfo(), expression);
    }

    public static String getJboSetElValue(JboSetIFace jboset, String expression) {
        return getElValue(jboset, null, JxSession.getJxUserInfo(), expression);
    }

    /**
     * 解析EL表达式
     * 
     * @param jboset
     * @param jbo
     * @param userinfo
     * @param expression
     * @return
     */
    public static String getElValue(JboSetIFace jboset, JboIFace jbo, JxUserInfo userinfo, String expression) {
        if (StrUtil.isNull(expression)) {
            return "";
        }
        try {
            GroovyShell sh = getGroovyShell(jboset, jbo, userinfo);
            String el = "\"" + expression + "\"";
            Object value = sh.evaluate(el);
            if (value instanceof String) {
                return (String) value;
            } else {
                return String.valueOf(value);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage() + "\r\n" + expression);
        }
        return expression;
    }

    public static GroovyShell getGroovyShell(JboSetIFace jboset, JboIFace jbo, JxUserInfo userinfo) {
        Binding b = new Binding();
        if (jboset != null) {
            b.setVariable("jboset", jboset);
        }
        if (jbo != null) {
            b.setVariable("jbo", jbo);
        }
        if (userinfo != null) {
            b.setVariable(JxSession.USER_INFO, userinfo);
        }
        return new GroovyShell(b);
    }

    /**
     * 解析${字段}-${其他字段}这种格式的EL表达式
     * 
     * @return
     */
    public static String parseJboElValue(JboIFace jbi, String el) {
        if (jbi == null || StrUtil.isNull(el)) {
            return el;
        }
        StringBuilder sb = new StringBuilder(el);
        int pos = sb.indexOf("${");
        while (pos >= 0) {
            int end = sb.indexOf("}", pos);
            if (end > pos) {
                String attr = sb.substring(pos + 2, end);
                String val = null;
                try {
                    val = jbi.getString(attr);
                } catch (JxException e) {
                    LOG.error(e.getMessage(), e);
                }
                if (val == null) {
                    sb.delete(pos, end + 1);
                } else {
                    sb.replace(pos, end + 1, val);
                }
            } else {
                // 没有结束标记了，直接退出。
                break;
            }
            pos = sb.indexOf("${");
        }
        return sb.toString();
    }

    /**
     * 获取针对字符串比较、数字比较直接的与或非关系计算出来的boolean值
     * 
     * @param expression 形如${条件1 &&[|| !] 条件2}
     * @return
     */
    public static boolean getConditionELValue(String expression) {
        Binding b = new Binding();
        GroovyShell sh = new GroovyShell(b);
        String el = "\"" + expression + "\"";
        Object value = sh.evaluate(el);
        return "true".equalsIgnoreCase(value.toString());
    }

}

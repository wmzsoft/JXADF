package com.jxtech.i18n;

import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 国际化资源工具类 Created by cxm on 2014/8/18.
 */
public class JxLangResourcesUtil {
    private static Locale myLocale;
    private static ResourceBundle bundle; // 默认系统通用的语言资源
    private static String lang;
    private static ResourceBundle appBundle;// 应用程序语言资源

    private static final Logger LOG = LoggerFactory.getLogger(JxLangResourcesUtil.class);

    public static void init() {
        myLocale = Locale.getDefault();// 获得系统默认的国家/语言环境
        lang = myLocale.toString();
        bundle = ResourceBundle.getBundle("res.lang", myLocale);// 根
    }

    /**
     * 重新加载语言
     * 
     * @lang
     */
    public static void reloadBundle(String lang) {
        myLocale = new Locale(lang);
        setLang(lang);
        bundle = ResourceBundle.getBundle("res.lang", myLocale);// 根
        LOG.debug("system loaded language :" + myLocale);
    }

    /**
     * 加载特定的资源
     * 
     * @param type
     * @return
     */
    public static ResourceBundle getResourceBundle(String type) {
        JxUserInfo userInfo = JxSession.getJxUserInfo();
        String userLangCode = "zh_CN"; // 默认为中文语言
        if (null != userInfo) {
            userLangCode = userInfo.getLangcode();
            if (userLangCode != null && userLangCode.indexOf("-") > 0) {
                userLangCode = userLangCode.replace("-", "_");
            }
        }
        ResourceBundle rb = getResourceBundle(type, userLangCode);
        if (rb == null) {
            if (userLangCode != null && userLangCode.indexOf('_') > 0) {
                String[] country = userLangCode.split("_");
                rb = getResourceBundle(type, country[0]);
            }
        }
        return rb;
    }

    /**
     * 加载语言包
     * 
     * @param type
     * @param langCode
     * @return
     */
    public static ResourceBundle getResourceBundle(String type, String langCode) {
        StringBuffer sb = new StringBuffer();
        sb.append("/");
        sb.append(type.replaceAll("\\.", "/"));
        sb.append("_");
        sb.append(langCode);
        sb.append(".properties");

        URL url = JxLangResourcesUtil.class.getClassLoader().getResource(sb.toString());
        if (null != url) {
            Locale local = new Locale("zh", "CN");
            if (langCode.indexOf('-') > 0) {
                String[] countryLang = langCode.split("-");
                local = new Locale(countryLang[0], countryLang[1]);
            } else if (langCode.indexOf("_") > 0) {
                String[] countryLang = langCode.split("_");
                local = new Locale(countryLang[0], countryLang[1]);
            } else {
                local = new Locale(langCode);
            }
            return ResourceBundle.getBundle(type, local);
        } else {
            // LOG.debug("加载语言包失败：" + sb.toString());
            return null;
        }
    }

    /**
     * 获取值
     * 
     * @param key key有2中模式，一种app.appname开头，一种其他任意字符串。
     * @return
     */
    public static String getString(String key) {
        try {
            JxUserInfo userInfo = JxSession.getJxUserInfo();
            if (null != userInfo) {
                reloadBundle(userInfo.getLangcode());
            }
        } catch (Exception e) {
            LOG.info("还没有没有加载JxSession类！");
        }

        String value = "";
        if (!StrUtil.isNull(key) && key.indexOf("app.") == 0) {
            String[] keys = key.split("\\.");
            int keysLen = keys.length;
            if (keysLen >= 3) {
                String resPackage = keys[1];
                String resKey = "";
                for (int i = 2; i < keysLen; i++) {
                    resKey = keys[i];
                    if (i < keysLen - 1) {
                        resKey += ".";
                    }
                }

                appBundle = JxLangResourcesUtil.getResourceBundle("res.app." + resPackage);
                if (appBundle.containsKey(resKey)) {
                    value = appBundle.getString(resKey);
                } else {
                    value = "";
                }
            }

        } else {
            if (bundle!=null && bundle.containsKey(key)) {
                value = bundle.getString(key);
            } else {
                value = "";
            }
        }
        return value;
    }

    /**
     * 针对“您好，{0},{1}”通配的国际化信息处理
     * 
     * @param key
     * @param params，从0开始的
     * @return
     */
    public static String getString(String key, Object[] params) {
        String msg = getString(key);
        return MessageFormat.format(msg, params);
    }

    public static String getLang() {
        return lang;
    }

    public static void setLang(String lang) {
        JxLangResourcesUtil.lang = lang;
    }
}

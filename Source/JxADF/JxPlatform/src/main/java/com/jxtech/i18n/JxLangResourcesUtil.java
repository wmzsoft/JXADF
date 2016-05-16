package com.jxtech.i18n;

import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.util.StrUtil;

/**
 * 国际化资源工具类 Created by cxm on 2014/8/18.
 */
public class JxLangResourcesUtil {

    /**
     * 定义语言包，并进行缓存
     */
    private static final Map<String, ResourceBundle> bundles = new HashMap<String, ResourceBundle>();

    private static final Logger LOG = LoggerFactory.getLogger(JxLangResourcesUtil.class);

    /**
     * 加载语言包
     * 
     * @param type
     * @param langCode
     * @return
     */
    public static ResourceBundle getResourceBundle(String type, String langCode) {
        StringBuilder sb = new StringBuilder();
        sb.append('/');
        if (type != null) {
            sb.append(type.replaceAll("\\.", "/").toLowerCase());
        }
        sb.append('_');
        if (langCode != null) {
            sb.append(langCode);
        }
        sb.append(".properties");
        String rname = sb.toString();
        if (bundles.containsKey(rname)) {
            return bundles.get(rname);// 从缓存中获得
        }
        ResourceBundle rb = null;
        URL url = JxLangResourcesUtil.class.getClassLoader().getResource(rname);
        if (null != url) {
            Locale local;
            if (StrUtil.isNull(langCode)) {
                local = Locale.getDefault();
            } else {
                String[] countryLang = langCode.split("-|_");
                if (countryLang.length == 2) {
                    local = new Locale(countryLang[0], countryLang[1]);
                } else {
                    local = new Locale(langCode);
                }
            }
            rb = ResourceBundle.getBundle(type, local);
        } else {
            LOG.warn("加载语言包失败：" + sb.toString());
        }
        if (rb==null){
            rb = new JxResourceBundle();
        }
        bundles.put(rname, rb);// 空值也要PUT进去，下次应该也读不到
        return rb;
    }

    public static ResourceBundle getResourceBundle(String type) {
        return getResourceBundle(type, getMyLanguage());
    }

    /**
     * 获得登录用户的语言设定
     * 
     * @return
     */
    private static String getMyLanguage() {
        String lang = null;// 获得语言
        try {
            JxUserInfo userInfo = JxSession.getJxUserInfo();
            if (null != userInfo) {
                lang = userInfo.getLangcode();
            }
        } catch (Exception e) {
            LOG.info("please login.." + e.getMessage());
        }
        if (StrUtil.isNull(lang)) {
            Locale locale = Locale.getDefault();
            lang = StrUtil.contact(locale.getLanguage(), "_", locale.getCountry());
        }
        return lang;
    }

    /**
     * 获取值
     * 
     * @param key
     *            key有2中模式，一种app.appname开头，一种其他任意字符串。
     * @return
     */
    public static String getString(String key) {
        if (StrUtil.isNull(key)) {
            return null;
        }
        // 获得类型
        if (key.indexOf("app.") == 0) {
            // 应该的格式为：app.[appname].key
            String[] keys = key.split("\\.");
            int keysLen = keys.length;
            if (keysLen >= 3) {
                ResourceBundle appBundle = getResourceBundle("res.app." + keys[1]);
                if (appBundle != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 2; i < keysLen; i++) {
                        sb.append(keys[i]).append('.');
                    }
                    return appBundle.getString(StrUtil.deleteLastChar(sb).toString());
                }
            }
        } else {
            // 直接读取lang包中的信息
            ResourceBundle langbundle = getResourceBundle("res.lang");
            if (langbundle != null) {
                return langbundle.getString(key);
            }
        }
        return null;
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

}

package com.jxtech.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 专用于插件的语言包
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.10
 * 
 */
public interface LanguageIface {

    public ResourceBundle getResourceBundle(Locale locale);

    public ResourceBundle getResourceBundle();

    public String getI18n(String key);

    public String getI18n(String key, Object[] params);
}

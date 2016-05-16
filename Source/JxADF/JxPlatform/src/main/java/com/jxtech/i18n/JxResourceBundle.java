package com.jxtech.i18n;

import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * 构建一个空的国际化资源文件，避免Freemarker报错
 * 
 * @author Administrator
 *
 */
public class JxResourceBundle extends ResourceBundle {

    @Override
    public Enumeration<String> getKeys() {
        return null;
    }

    @Override
    protected Object handleGetObject(String key) {
        return null;
    }

}

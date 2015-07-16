package com.jxtech.jbo.base;

import java.io.Serializable;

/**
 * 存放Key、Value组件
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class KeyValue implements Serializable {
    private static final long serialVersionUID = 8211098805504481267L;

    private String key;
    private String value;

    public KeyValue() {

    }

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

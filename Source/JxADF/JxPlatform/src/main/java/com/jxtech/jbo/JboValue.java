package com.jxtech.jbo;

import java.io.Serializable;

/**
 * 每个字段的所有信息（只处理单个字段的信息） JxAttribute 指单个字段的属性（每条记录都一样，不变）, JboValue，指每条记录都不一样，变化的。
 *
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class JboValue implements Serializable {
    public static final long READONLY = 1L;// 只读
    public static final long REQUIRED = 2L;// 必填
    public static final long VISIBLE = 3L; //可见

    private static final long serialVersionUID = -5899672332412281875L;

    private String attributeName;// 字段名
    private Object value;// 字段值
    private long flag = -1;// 字段属性信息，只读、必填等,-1表示未初始化
    private boolean modify;// 当前字段是否修改

    public JboValue() {
        super();
    }

    public JboValue(String attributeName) {
        this.attributeName = attributeName;
    }

    public JboValue(String attributeName, Object value, long flag) {
        this.attributeName = attributeName;
        this.value = value;
        this.flag = flag;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public boolean isFlag(long myflag, boolean defaultValue) {
        if (flag == -1) {
            return defaultValue;
        }
        return ((flag & myflag) == myflag);
    }

    public void setFlag(long myflag, boolean value) {
        if (flag == -1) {
            flag = 0;
        }
        if (value) {
            flag = flag | myflag;
        } else {
            flag = flag ^ myflag;
        }
    }

    public boolean isReadonly() {
        return isFlag(READONLY, false);
    }

    public void setReadonly(boolean readonly) {
        setFlag(READONLY, readonly);
    }

    public boolean isRequired() {
        return isFlag(REQUIRED, false);
    }

    public void setRequired(boolean required) {
        setFlag(REQUIRED, required);
    }

    public boolean isVisible() {
        return isFlag(VISIBLE, false);
    }

    public void setVisible(boolean visible) {
        setFlag(VISIBLE, visible);
    }

    public long getFlag() {
        return flag;
    }

    public void setFlag(long flag) {
        this.flag = flag;
    }

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}

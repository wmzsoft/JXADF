package com.jxtech.jbo.base;

import java.io.Serializable;

/**
 * 字段的动态属性，JxAttribute为静态属性，是数据库中配置好的，只取一次，这里是动态的，每个jboset一个
 * 
 * @author Administrator
 * 
 */
public class JxAttributeProp implements Serializable {

    private static final long serialVersionUID = 3452001195719546033L;
    // 只读标记
    public static final long FLAG_READONLY = 1L;
    // 必填标记
    public static final long FLAG_REQUIRED = 2L;
    private JxAttribute attribute;
    private boolean requiredflag=false;//是否设定了必填
    private boolean required;//必填
    private boolean readonly;//只读
    private long flag = 0L;

    public JxAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(JxAttribute attribute) {
        this.attribute = attribute;
    }

    public boolean isRequired() {
        if (!requiredflag && attribute != null) {
            return attribute.isRequired();
        }
        return required;
    }

    public void setRequired(boolean required) {
        if (required) {
            flag = flag | FLAG_REQUIRED;
        }
        requiredflag = true;
        this.required = required;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        if (readonly) {
            flag = flag | FLAG_READONLY;
        }
        this.readonly = readonly;
    }

    public long getFlag() {
        return flag;
    }

    public void setFlag(long flag) {
        this.flag = flag;
    }
}

package com.jxtech.jbo.field;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.util.JxException;

public class Field implements FieldIFace {
    protected JboIFace jbo;

    /**
     * 字段初始化
     */
    @Override
    public void init() throws JxException {

    }

    /**
     * 当字段值发生改变后执行的方法 子类可以覆盖 newVal : 新值
     */
    @Override
    public void execute(String attributeName, Object newVal) throws JxException {
        jbo.setObject(attributeName, newVal);
    }

    @Override
    public void setJbo(JboIFace jbo) throws JxException {
        this.jbo = jbo;
    }

    @Override
    public JboIFace getJbo() throws JxException {
        return this.jbo;
    }

}

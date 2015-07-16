package com.jxtech.jbo.field;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.util.JxException;

public interface FieldIFace {
    void init() throws JxException;

    void execute(String attributeName, Object newVal) throws JxException;

    void setJbo(JboIFace jbo) throws JxException;

    JboIFace getJbo() throws JxException;
}

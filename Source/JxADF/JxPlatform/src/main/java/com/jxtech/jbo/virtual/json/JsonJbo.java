package com.jxtech.jbo.virtual.json;

import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;
import com.jxtech.jbo.virtual.VirtualJbo;

/**
 * 虚拟Jbo，不会进行持久化
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
public class JsonJbo extends VirtualJbo {

    private static final long serialVersionUID = -5992033337478808909L;

    public JsonJbo(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

}

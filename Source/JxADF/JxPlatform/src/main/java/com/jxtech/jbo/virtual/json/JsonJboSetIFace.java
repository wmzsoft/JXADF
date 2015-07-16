package com.jxtech.jbo.virtual.json;

import com.jxtech.jbo.JboSetIFace;

/**
 * 虚拟Jbo，不会进行持久化
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
public interface JsonJboSetIFace extends JboSetIFace {
    public void setJsonHead(String jsonHead);

    public String getJsonHead();
}

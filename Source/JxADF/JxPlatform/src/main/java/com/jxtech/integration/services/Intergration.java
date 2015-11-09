package com.jxtech.integration.services;

import com.jxtech.jbo.util.JxException;

/**
 * 集成接口
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.11
 * 
 */
public interface Intergration {
    /**
     * 将传入的JSON数据持久化
     * 
     * @param jsonData
     * @return
     * @throws JxException
     */
    public boolean execute(String jsonData) throws JxException;
}

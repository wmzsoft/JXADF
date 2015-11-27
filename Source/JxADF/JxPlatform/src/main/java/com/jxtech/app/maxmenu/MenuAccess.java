package com.jxtech.app.maxmenu;

import java.util.List;
import java.util.Map;

import com.jxtech.jbo.util.JxException;

/**
 * 菜单访问接口
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.07
 * 
 */
public interface MenuAccess {

    /**
     * 保存访问记录
     * 
     * @param app
     * @throws JxException
     */
    public void saveAccess(String app) throws JxException;

    /**
     * 保存访问记录
     * 
     * @param app
     * @param params
     *            存放其它客户端信息
     * @throws JxException
     */
    public void saveAccess(String app, Map<String, Object> params) throws JxException;

    /**
     * 获得访问记录。最喜欢的、最近访问的
     * 
     * @return
     * @throws JxException
     */
    public Map<String, List<Map<String, Object>>> getAccess() throws JxException;

}

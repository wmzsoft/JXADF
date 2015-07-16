package com.jxtech.jbo.auth;

import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JxException;

/**
 * 登录认证模块，如果要实现自己的登录方式，请继承此方法
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.04.11
 * 
 */
public interface AuthenticateIFace {
    /**
     * 获得用户信息
     * 
     * @param userid
     * @return
     * @throws JxException
     */
    public JxUserInfo getUserInfo(String userid) throws JxException;

    public JxUserInfo getUserInfo(String userid, String password) throws JxException;

}

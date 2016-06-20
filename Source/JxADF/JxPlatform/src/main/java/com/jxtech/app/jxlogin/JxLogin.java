package com.jxtech.app.jxlogin;

import java.util.Map;

import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JxException;

/**
 * 登陆接口
 * 
 * @author luopiao@jxtech.net
 * 
 */
public interface JxLogin {

    /**
     * 登陆接口
     * 
     * @param userid 用户id
     * @param password 用户密码
     * @param relogin 是否重新登陆
     * @return 是否登陆成功
     */
    public boolean login(String userid, String password, boolean relogin) throws JxException;

    /**
     * 登录系统
     * 
     * @param userid 用户名
     * @param password 密码
     * @param relogin 是否重新登录
     * @param params 参数
     * @return
     * @throws JxException
     */
    public boolean login(String userid, String password, boolean relogin, Map<String, Object> params) throws JxException;

    /**
     * 登陆之前
     */
    public boolean beforeLogin(String userid, String password, boolean relogin, Map<String, Object> params);

    /**
     * 登陆成功之后
     * 
     * @param userinfo
     */
    public boolean afterLogin(JxUserInfo user, Map<String, Object> params);

}

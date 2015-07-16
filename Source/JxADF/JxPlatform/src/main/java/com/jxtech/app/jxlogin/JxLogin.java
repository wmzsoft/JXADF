package com.jxtech.app.jxlogin;

import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JxException;

/**
 * 登陆接口
 * @author luopiao@jxtech.net
 *
 */
public interface JxLogin {

	/**
	 * 登陆接口
	 * @param userid 用户id
	 * @param password 用户密码
	 * @param relogin 是否重新登陆
	 * @return 是否登陆成功
	 */
	public boolean login(String userid, String password, boolean relogin) throws JxException ;
	
	
	
	/**
	 * 登陆之前
	 */
	public void beforeLogin();
	
	/**
	 * 登陆成功之后
	 * @param userinfo
	 */
	public void afterLogin(JxUserInfo userinfo);
	
	
	/**
	 * 登陆失败之后
	 */
	public void loginFailure(String userid);
	

}

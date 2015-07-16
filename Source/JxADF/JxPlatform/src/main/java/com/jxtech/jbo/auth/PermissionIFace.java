package com.jxtech.jbo.auth;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jxtech.jbo.util.JxException;

/**
 * 定义权限相关的处理
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public interface PermissionIFace extends java.io.Serializable {
    public Map<String, String> getFunctions();

    public void setFunctions(Map<String, String> functions);

    public Map<String, String> getUserFunctions();

    public void setUserFunctions(Map<String, String> userFunctions);

    public Map<String, String> getSecurityRestrict();

    public void setSecurityRestrict(Map<String, String> securityRestrict);

    /**
     * 是否有权限
     * 
     * @param pageid 应用程序名
     * @param methodName 方法名
     * @return
     */
    public boolean hasFunctions(String pageid, String methodName);

    public boolean hasFunctions(String url);

    public boolean isIgoreSecurity(String url);

    public void putIgnoreSecurity(String url, String app);

    public void putIgnoreSecuritys(String[] urls, String app);

    public Set<String> getRoles(String userid);

    /**
     * 当前登录用户是否有权限
     * 
     * @param app 应用程序名
     * @param url URL路径
     * @return
     */
    public boolean isPermission(String app, String url) throws JxException;

    /**
     * 检查是否有权限，没有权限，则直接重定向到没有权限的页面。
     * 
     * @param app
     * @param request
     * @param response
     * @return
     * @throws JxException
     */
    public boolean isPermission(String app, HttpServletRequest request, HttpServletResponse response) throws JxException;
}

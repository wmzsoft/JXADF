package com.jxtech.jbo.auth;

import java.util.Set;

import com.jxtech.jbo.App;
import com.jxtech.jbo.AppSet;
import com.jxtech.jbo.base.JxApps;
import com.jxtech.jbo.base.JxAppsDao;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class JxSession {
    private static final Logger LOG = LoggerFactory.getLogger(JxSession.class);
    public static final String APPS = "jxtech_session_apps";
    public static final String USER_INFO = "jxuserinfo";
    public static final String USER_MESSAGE = "jx_message";// 保存需要长时间输出的消息，需与progress组合使用
    public static final String USER_PROGRESS = "jx_progress";// 执行进度
    public static final String RENDERER = "renderer";

    public static void putMessageAndProgress(String message, double progress) {
        putSession(USER_MESSAGE, message);
        putSession(USER_PROGRESS, String.valueOf(progress));
    }

    public static boolean putSession(String key, Object value) {
        if (key == null) {
            return false;
        }
        HttpSession session = getSession();
        if (session == null) {
            LOG.warn("没有获得正确的Session");
            return false;
        }
        if (value == null) {
            session.removeAttribute(key);
        } else {
            session.setAttribute(key, value);
        }
        return true;
    }

    /**
     * 获得Session中的值
     * 
     * @param key
     * @return
     */
    public static Object getSession(String key) {
        HttpSession session = getSession();
        if (session != null) {
            return session.getAttribute(key);
        } else {
            ActionContext actx = ActionContext.getContext();
            if (actx != null) {
                return actx.getSession().get(key);
            }
            return null;
        }
    }

    /**
     * 返回当前用户访问的是否为移动应用
     * 
     * @return
     */
    public static boolean isMobile() {
        Object renderer = getSession(RENDERER);
        if (renderer == null) {
            return false;
        } else {
            String rend = ((String) renderer).trim().toUpperCase();
            if ("".equals(rend) || "HTML".equals(rend)) {
                return false;
            }
        }
        return true;
    }

    public static void logout() {
        HttpSession session = getSession();
        if (session != null) {
            session.setAttribute(USER_INFO, null);
            session.setAttribute(APPS, null);
            session.invalidate();
        } else {
            LOG.warn("Session获取出错。");
        }
    }

    public static HttpSession getSession() {
        WebContext ctx = WebContextFactory.get();
        if (ctx != null) {
            return ctx.getSession();
        }
        HttpServletRequest request = getRequest();
        if (request != null) {
            return request.getSession();
        }
        return null;
    }

    public static HttpServletRequest getRequest() {
        ActionContext actx = ActionContext.getContext();
        if (actx != null) {
            return ServletActionContext.getRequest();
        }
        return null;
    }

    public static void putApp(String appName, String appType) throws JxException {
        LOG.debug("before put App " + appName + "." + appType);
        AppSet apps = getApps();
        apps.getAllAppName();
        apps.putApp(appName, appType);
        putSession(APPS, apps);
        LOG.debug("after put App " + appName + "." + appType);
        apps.getAllAppName();
    }

    public static void removeApp(String appName, String appType) {
        AppSet apps = getApps();
        apps.removeApp(appName, appType);
    }

    public static AppSet getApps() {
        Object obj = getSession(APPS);
        AppSet apps;
        if (obj instanceof AppSet) {
            apps = (AppSet) obj;
        } else {
            apps = new AppSet();
        }
        return apps;
    }

    public static App getMainApp() throws JxException {
        return getApps().getApp();
    }

    public static App getApp() throws JxException {
        AppSet apps = getApps();
        return apps.getApp();
    }

    /**
     * 返回当前应用程序的名称
     * 
     * @return
     * @throws JxException
     */
    public static String getAppName() throws JxException {
        AppSet apps = getApps();
        return apps.getAppName();
    }

    /**
     * 获得应用程序名称
     * 
     * @param jboname
     * @param onlyJboName 仅仅通过Jboname来获得应用程序名称
     * @return
     * @throws JxException
     */
    public static String getAppNameOfJboname(String jboname, boolean onlyJboName) throws JxException {
        if (onlyJboName) {
            JxApps japp = JxAppsDao.getAppByMaintbname(jboname);
            if (japp != null) {
                return japp.getApp();
            }
        }
        App app = getApp();
        if (app != null) {
            return app.getAppName();
        }
        JxApps japp = JxAppsDao.getAppByMaintbname(jboname);
        if (japp != null) {
            return japp.getApp();
        }
        return null;
    }

    public static App getApp(String appNameType) throws JxException {
        AppSet apps = getApps();
        App app = null;
        if (apps != null) {
            app = apps.getApp(appNameType);
        }
        if (null == app) {
            LOG.debug("找不到对应的主应用，从主应用的refApp找一下试试看。。");
            if (null != getMainApp()) {
                app = getMainApp().getRefApp().get(appNameType);
            } else {
                LOG.info("没有找到主应用程序：" + appNameType);
            }
        }

        // 对于***/lookup类型的app，不需要重新获取app
        if (app != null) {
            if (app.getJboset() == null && !app.getAppName().contains("/")) {
                app = new App(app.getAppName());
            }
        }
        return app;
    }

    public static App getApp(String appname, String appType) throws JxException {
        return getApp(appname + "." + appType);
    }

    /**
     * 获得App，如果不存在，可创建
     * 
     * @param appname
     * @param appType
     * @param isCreate
     * @return
     * @throws JxException
     */
    public static App getApp(String appname, String appType, boolean isCreate) throws JxException {
        App app = getApp(appname + "." + appType);
        if (app == null && isCreate) {
            app = new App(appname);
            putApp(appname, appType);
        }
        return app;
    }

    /**
     * 获得用户信息
     * 
     * @return
     */
    public static JxUserInfo getJxUserInfo() {
        Object obj = getSession(USER_INFO);
        if (obj == null) {
            return null;
        }
        return (JxUserInfo) obj;
    }

    /**
     * 是否有某功能的权限
     * 
     * @param pageid
     * @param methodName
     * @return
     */
    public static boolean hasPermission(String pageid, String methodName) {
        if (StrUtil.isNull(pageid) || StrUtil.isNull(methodName)) {
            return true;
        }
        JxUserInfo usrinfo = getJxUserInfo();
        if (usrinfo == null) {
            return false;
        }
        PermissionIFace permission = PermissionFactory.getPermissionInstance();
        if (permission == null) {
            return true;
        }
        return permission.hasFunctions(pageid, methodName);
    }

    /* 之前的登陆接口 */
    /*
     * public static boolean login(String userid, String password, boolean relogin) throws JxException { if (StrUtil.isNull(userid)) { return false; } LOG.debug("login user:" + userid); JxUserInfo userinfo = null; if (!relogin) {// 不需要重新登录 userinfo = getJxUserInfo(); if (userinfo != null) { if (userid.equalsIgnoreCase(userinfo.getLoginid())) { return true; } } } AuthenticateIFace auth =
     * AuthenticateFactory.getAuthenticateInstance(); userinfo = auth.getUserInfo(userid, password); if (userinfo != null) { putSession(USER_INFO, userinfo); } else { return false; } return true; }
     */

    public static boolean loginBySsoUser(String userid) {
        if (StrUtil.isNull(userid)) {
            return false;
        }
        LOG.debug("login user:" + userid);
        JxUserInfo userinfo = null;

        AuthenticateIFace dao = AuthenticateFactory.getAuthenticateInstance();
        try {
            userinfo = dao.getUserInfo(userid);
        } catch (JxException e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
        if (userinfo != null) {
            putSession(USER_INFO, userinfo);
        } else {
            return false;
        }
        return true;
    }

    /*
     * public static boolean login(String userid, String password) throws JxException { return login(userid, password, false); }
     */

    /**
     * 获取当前session的国际化语言
     * 
     * @return
     * @throws JxException
     */
    public static String getUserLang() {
        String lang = "";
        JxUserInfo userInfo = getJxUserInfo();
        if (null != userInfo) {
            lang = userInfo.getLangcode();
        }
        return lang.replace("_", "-");
    }

    /**
     * 添加匿名登录信息，无用户ID
     * 
     * @param langCode
     * @param force
     * @return
     */
    public static JxUserInfo loadAnonymity(String langCode, boolean force) {
        JxUserInfo userinfo = getJxUserInfo();
        if (userinfo == null) {
            userinfo = new JxUserInfo();
            putSession(USER_INFO, userinfo);
        }
        if (!StrUtil.isNull(langCode) || force) {
            userinfo.setLangcode(langCode);
        }
        return userinfo;
    }

    /**
     * 获得用户ID
     * 
     * @return
     */
    public static String getUserId() {
        JxUserInfo userinfo = getJxUserInfo();
        if (userinfo != null) {
            return userinfo.getUserid();
        }
        return null;
    }

    /**
     * 是否为超级管理员,具备所有权限
     * 
     * @return
     */
    public static boolean isSuperManager() {
        JxUserInfo userinfo = getJxUserInfo();
        if (userinfo != null) {
            Set<String> roles = userinfo.getRoles();
            if (roles != null) {
                return roles.contains("1");
            }
        }
        return false;
    }
}

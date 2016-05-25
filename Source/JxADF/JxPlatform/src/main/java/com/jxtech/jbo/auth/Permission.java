package com.jxtech.jbo.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.app.pubrole.PubRoleUserSetIFace;
import com.jxtech.app.usermetadata.DefaultMetadata;
import com.jxtech.app.usermetadata.MetaData;
import com.jxtech.db.DBFactory;
import com.jxtech.db.DataQuery;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.CacheUtil;
import com.jxtech.util.StrUtil;

/**
 * 定义权限相关的处理
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public abstract class Permission implements PermissionIFace {
    private static final long serialVersionUID = -1004580625367988151L;
    private Logger LOG = LoggerFactory.getLogger(Permission.class);
    // 定义系统不需要权限检查的地址,无需登录就可访问的页面
    private static Map<String, String> ignoreSecurity;
    // 登录之后不检查权限
    private static Map<String, String> loginSecurity = new HashMap<String, String>();
    // 定义系统的功能点,<功能有含义的关键字,功能唯一标识>
    private Map<String, String> functions;

    // 定义用户具有的功能点<功能唯一标识,角色ID>
    private Map<String, String> userFunctions;

    // 定义用户具有的数据权限<应用程序.表名,限制条件>
    private Map<String, String> securityRestrict;

    @Override
    public Map<String, String> getFunctions() {
        return functions;
    }

    @Override
    public void setFunctions(Map<String, String> functions) {
        this.functions = functions;
    }

    @Override
    public Map<String, String> getUserFunctions() {
        return userFunctions;
    }

    @Override
    public void setUserFunctions(Map<String, String> userFunctions) {
        this.userFunctions = userFunctions;
    }

    @Override
    public Map<String, String> getSecurityRestrict() {
        return securityRestrict;
    }

    @Override
    public void setSecurityRestrict(Map<String, String> securityRestrict) {
        this.securityRestrict = securityRestrict;
    }

    /**
     * 是否具有某个页面的某个操作权限
     * 
     * @param pageid
     * @param methodName
     * @return
     */
    @Override
    public boolean hasFunctions(String pageid, String methodName) {
        return true;
    }

    @Override
    public boolean isIgoreSecurity(String url) {
        if (StrUtil.isNull(url) || JxSession.isSuperManager()) {
            return true;
        }
        Map<String, String> map = getIgnoreSecurity();
        if (map.containsKey(url)) {
            return true;
        } else {
            // 通过正则表达式判断
            for (Map.Entry<String, String> entry : map.entrySet()) {
                Pattern p = Pattern.compile(entry.getKey());
                Matcher matcher = p.matcher(url);
                if (matcher.find()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void putIgnoreSecurity(String url, String app) {
        if (StrUtil.isNull(url)) {
            return;
        }
        getIgnoreSecurity().put(url, app);
    }

    public void putIgnoreSecuritys(String[] urls, String app) {
        if (urls == null) {
            return;
        }
        Map<String, String> map = getIgnoreSecurity();
        for (int i = 0; i < urls.length; i++) {
            map.put(urls[i], app);
        }
    }

    public static Map<String, String> getIgnoreSecurity() {
        if (ignoreSecurity == null) {
            ignoreSecurity = new HashMap<String, String>();
        }
        return ignoreSecurity;
    }

    public static void setIgnoreSecurity(Map<String, String> ignoreSecurity) {
        Permission.ignoreSecurity = ignoreSecurity;
    }

    public static void putLoginSecurity(String appname, String url) {
        loginSecurity.put(appname, url);
    }

    /**
     * 检查是否只需登录即可使用的地址，Appname与url，只需传入一个即可。
     * 
     * @param appname
     * @param url
     * @return
     */
    public static boolean isIgnoreLoginSecurity(String appname, String url) {
        if (!StrUtil.isNull(appname)) {
            appname = appname.toUpperCase();
            return loginSecurity.containsKey(appname);
        } else if (!StrUtil.isNull(url)) {
            if (loginSecurity.containsValue(url)) {
                return true;
            } else {
                for (Map.Entry<String, String> entry : loginSecurity.entrySet()) {
                    Pattern p = Pattern.compile(entry.getValue());
                    Matcher matcher = p.matcher(url);
                    if (matcher.find()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasFunctions(String url) {
        return true;
    }

    public Set<String> getRoles(String userid) {
        try {
            JboSetIFace js = JboUtil.getJboSet("PUB_ROLE_USER");
            if (js instanceof PubRoleUserSetIFace) {
                return ((PubRoleUserSetIFace) js).getRoles(userid);
            }
        } catch (JxException e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    /**
     * 当前登录用户是否有权限
     * 
     * @param app
     *            应用程序名
     * @param url
     *            URL路径
     * @return
     */
    public boolean isPermission(String app, String url) throws JxException {
        if (JxSession.isSuperManager()) {
            return true;
        }
        String ckey = StrUtil.contact(JxSession.getUserId(), ".", app, ".", url);
        Object obj = CacheUtil.getPermission(ckey);
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        boolean flag = isIgnoreLoginSecurity(null, url);
        if (!flag) {
            // 忽略路径
            flag = isIgoreSecurity(url);
            if (!flag) {
                if (StrUtil.isNull(app)) {
                    flag = hasFunctions(url);
                } else {
                    flag = (getCount(app) > 0);
                }
            }
        }
        CacheUtil.putPermissionCache(ckey, new Boolean(flag));
        return flag;
    }

    /**
     * 检查是否有权限，没有权限，则直接重定向到没有权限的页面。
     * 
     * @param app
     * @param request
     * @param response
     * @return
     * @throws JxException
     */
    public boolean isPermission(String app, HttpServletRequest request, HttpServletResponse response) throws JxException {
        String url = request.getRequestURI();
        if (isPermission(app, url)) {
            return true;
        }
        String noperm = DefaultMetadata.getInstance().get(MetaData.NOT_PERMISSION);
        try {
            request.getRequestDispatcher(noperm).forward(request, response);
        } catch (ServletException e) {
            LOG.error(e.getLocalizedMessage(), e);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 检查应用程序的有权菜单数量
     * 
     * @param app
     * @return
     * @throws JxException
     */
    public int getCount(String app) throws JxException {
        if (StrUtil.isNull(app)) {
            LOG.info("检查权限时，应用程序名为空。");
            return -1;
        }
        // 针对lookup页面就不做检查了吧
        if (app.indexOf('/') > 0) {
            return 1;
        }
        String userid = JxSession.getUserId();
        String key = StrUtil.contact(userid, ".count.", app, ".");
        Object obj = CacheUtil.getPermission(key);
        if (obj != null) {
            return (int) obj;
        }
        DataQuery dq = DBFactory.getDataQuery(null, null);
        StringBuilder wc = new StringBuilder();
        wc.append(" OPERATION=1 and menu_id in (select maxmenuid from maxmenu where  upper(app)=upper(?))");
        wc.append(" and role_id in (select role_id from PUB_ROLE_USER where upper(user_id)=upper(?))");
        int c = dq.count("PUB_ROLE_OPERATION", wc.toString(), new Object[] { app, userid });
        LOG.debug("检查权限：app=" + app + ",共有:" + c + "\r\n select count(*) from  PUB_ROLE_OPERATION where " + wc.toString() + "\r\n" + JxSession.getUserId());
        CacheUtil.putPermissionCache(key, c);
        return c;
    }
}

package com.jxtech.jbo.auth.impl;

import com.jxtech.app.maxapps.MaxappsSetIFace;
import com.jxtech.db.DBFactory;
import com.jxtech.db.DataQuery;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.auth.Permission;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.CacheUtil;
import com.jxtech.util.StrUtil;
import com.jxtech.util.SysPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
public class PermissionImpl extends Permission {
    private static final long serialVersionUID = 1276417491910092574L;
    private static final Logger LOG = LoggerFactory.getLogger(PermissionImpl.class);
    private static final String CACHE_PREX = "url.";

    public PermissionImpl() {
        Permission.putLoginSecurity("HOME", "app.action?app=home");
        Permission.putLoginSecurity("TREE", "tree.action");
        Permission.putLoginSecurity("ATTACHMENT", "app=attachment");
        Permission.putLoginSecurity("ATTACHEMENT_JSP", "/WEB-INF/content/app/attachment/*");
        Permission.putLoginSecurity("WORKFLOW", "app=workflow");
    }

    @Override
    public boolean hasFunctions(String url) {
        if (StrUtil.isNull(url) || JxSession.isSuperManager()) {
            // 超级管理员,默认有最大的权限,不用做任何判断
            return true;
        }
        String cachekey = StrUtil.contact(CACHE_PREX, JxSession.getUserId(), ".", url);
        Object obj = CacheUtil.getPermission(cachekey);
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        LOG.debug("权限检查：" + cachekey);
        String context = System.getProperty(SysPropertyUtil.WEB_CONTEXT);
        int pos = url.indexOf(context);
        String appurl = null;
        if (pos > 0) {
            appurl = url.substring(pos + context.length() + 1);
            try {
                if (Permission.isIgnoreLoginSecurity(null, appurl)) {
                    CacheUtil.putPermissionCache(cachekey, new Boolean(true));
                    return true;
                }
                boolean flag;
                if (appurl.startsWith("app.action?") || appurl.equalsIgnoreCase("app.action")) {
                    // 未使用插件的应用程序权限检查
                    flag = checkNoPluginPermission(appurl);
                } else {
                    // 已使用权限的插件应用程序检查
                    flag = checkPluginPermission(appurl);
                }
                CacheUtil.putPermissionCache(cachekey, new Boolean(flag));
                return flag;
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                return false;
            }
        } else {
            // 不是本系统的应用，默认有权限。
            CacheUtil.putPermissionCache(cachekey, new Boolean(true));
            return true;
        }
    }

    /**
     * 未用插件的权限检查
     * 
     * @param url
     * @return
     * @throws JxException
     */
    public boolean checkNoPluginPermission(String url) throws JxException {
        int pos = url.indexOf("app=");
        int end = url.indexOf("&", pos);
        if (pos > 0) {
            String app = null;
            if (end > pos) {
                app = url.substring(pos + 4, end);
            } else {
                app = url.substring(pos + 4);
            }
            return (getCount(app) != 0);
        }
        return true;
    }

    /**
     * 使用插件开发的权限检查
     * 
     * @param url
     * @return
     * @throws JxException
     */
    public boolean checkPluginPermission(String url) throws JxException {
        JboSetIFace js = JboUtil.getJboSet("MAXAPPS");
        if (js instanceof MaxappsSetIFace) {
            MaxappsSetIFace ms = (MaxappsSetIFace) js;
            int pos = url.indexOf('?');
            if (pos > 0) {
                url = url.substring(0, pos);
            }
            JboIFace jbo = ms.getJboByUrl(url);// 处理一个插件中有多个应用的情况
            if (jbo == null) {
                pos = url.lastIndexOf("/");
                if (pos < 0) {
                    return true;
                }
                url = url.substring(0, pos);
                jbo = ms.getJboByUrl(url);
            }
            if (jbo != null) {
                String app = jbo.getString("APP");
                if (Permission.isIgnoreLoginSecurity(app, url)) {
                    return true;
                }
                return getCount(app) != 0;
            }
        }
        return true;
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
        if (StrUtil.isNull(pageid) || StrUtil.isNull(methodName) || JxSession.isSuperManager()) {
            return true;
        }
        String userid = JxSession.getUserId();
        String ckey = StrUtil.contact(userid, ".hasFunctions.", pageid, ".", methodName);
        Object obj = CacheUtil.getPermission(ckey);
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        long maxmenuid = -1;
        try {
            maxmenuid = getMaxmenuids(pageid, methodName, null);
        } catch (JxException e) {
            LOG.error(e.getMessage(), e);
        }
        // 未注册按钮，即不进行权限检查，表示有权限
        if (maxmenuid < 0) {
            CacheUtil.putPermissionCache(ckey, Boolean.TRUE);
            return true;
        }
        DataQuery dq = DBFactory.getDataQuery(null, null);
        StringBuilder wc = new StringBuilder();
        wc.append("menu_id=? and role_id in (select role_id from PUB_ROLE_USER where upper(user_id)=upper(?))");
        try {
            int c = dq.count("PUB_ROLE_OPERATION", wc.toString(), new Object[] { maxmenuid, JxSession.getUserId() });
            LOG.debug("权限：" + pageid + "." + methodName + "=" + c);
            boolean b = c > 0;
            CacheUtil.putPermissionCache(ckey, new Boolean(b));
        } catch (JxException e) {
            LOG.error(e.getMessage());
        }
        return true;
    }

    public long getMaxmenuids(String pageid, String methodName, String description) throws JxException {
        long id = getMaxmenuid(pageid, methodName, description);
        if (id < 0) {
            return getMaxmenuid("GLOBAL", methodName, description);
        }
        return id;
    }

    /**
     * 返回菜单的唯一标识，如果小于0，则表示没有找到菜单。
     * 
     * @param pageid
     * @param methodName
     * @param description
     * @return
     * @throws JxException
     */
    public long getMaxmenuid(String pageid, String methodName, String description) throws JxException {
        // 首先检查当前应用中是否注册了此操作
        JboSetIFace js = JboUtil.getJboSet("maxmenu");
        DataQueryInfo dqi = js.getQueryInfo();
        if (StrUtil.isNull(description)) {
            dqi.setWhereCause("upper(menu)=upper(?) and upper(app)=upper(?)");
            dqi.setWhereParams(new Object[] { methodName, pageid });
        } else {
            dqi.setWhereCause("upper(menu)=upper(?) and upper(app)=upper(?) and description=?");
            dqi.setWhereParams(new Object[] { methodName, pageid, description });
        }
        List<JboIFace> list = js.query();
        if (list != null) {
            if (!list.isEmpty()) {
                return list.get(0).getLong("maxmenuid");
            }
        }
        return -1;
    }

}

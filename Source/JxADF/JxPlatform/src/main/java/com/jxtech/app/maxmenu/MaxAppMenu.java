package com.jxtech.app.maxmenu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.common.JxResource;
import com.jxtech.db.util.JxDataSourceUtil;
import com.jxtech.distributed.Distributed;
import com.jxtech.distributed.DistributedFactory;
import com.jxtech.i18n.LanguageFactory;
import com.jxtech.i18n.LanguageIface;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.util.CacheUtil;
import com.jxtech.util.JsonUtil;
import com.jxtech.util.StrUtil;

/**
 * Created by zhouyu on 2014-10-09. modify by wmzsoft@gmail.com on 2015.07
 */
public class MaxAppMenu {
    private static final Logger LOG = LoggerFactory.getLogger(MaxAppMenu.class);
    public static final String CACHE_PREX = "MAXAPPS.";
    public static final int APP_VISIBLE_ENABLED = 1;
    public static final int APP_VISIBLE_DISABLE = 0;
    public static final int APP_VISIBLE_ALL = -1;

    /**
     * 获得应用程序菜单,用于作菜单使用
     * 
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> getMaxAppMenu() throws SQLException {
        return getMaxAppMenu(APP_VISIBLE_ENABLED, null, true, false);
    }

    /**
     * 返回菜单。
     * 
     * @param visible
     *            1:可视,0:不可视,-1：全部
     * @param where
     *            自定义的Where条件
     * @param permission
     *            true：当前用户的权限
     * @param workflow
     *            true:只显示工作流的应用程序
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getMaxAppMenu(int visible, String where, boolean permission, boolean workflow) throws SQLException {
        String cachekey = StrUtil.contact(JxSession.getUserId(), ".MAXAPPS.", JxSession.getUserLang(), String.valueOf(visible), where, String.valueOf(permission), String.valueOf(workflow));
        Object objv = CacheUtil.getPermission(cachekey);
        if (objv instanceof List) {
            return (List<Map<String, Object>>) objv;
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from MAXAPPS ");
        sql.append(" where ORDERID > 0 ");
        if (permission && JxSession.isSuperManager()) {
            permission = false;
        }
        if (permission) {
            sql.append(" and app in (select app from maxmenu a, pub_role_operation b,pub_role_user c ");
            sql.append(" where a.maxmenuid = b.menu_id and b.role_id=c.role_id and b.OPERATION=1 ");
            if (visible > 0) {
                sql.append(" and a.visible=").append(visible);
            }
            sql.append(" and c.user_id = ?) ");
        }
        if (workflow) {
            // 只显示工作流的应用
            sql.append(" and length(custapptype)>1 ");
        }
        if (!StrUtil.isNull(where)) {
            sql.append(" and (").append(where).append(')');
        }
        sql.append(" or apptype = 'MODULE'");
        sql.append(" order by orderid asc, apptype desc");

        Connection conn = null;
        List<Map<String, Object>> result = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JxDataSourceUtil.getConnection(null);
            ps = conn.prepareStatement(sql.toString());
            JxUserInfo userInfo = JxSession.getJxUserInfo();

            if (userInfo != null) {
                result = new ArrayList<Map<String, Object>>();
                if (permission) {
                    ps.setString((int) 1, userInfo.getUserid());
                }
                rs = ps.executeQuery();
                // ResourceBundle zTreeBundle = JxLangResourcesUtil.getResourceBundle("res.tree.menu");
                LanguageIface language = LanguageFactory.getLanguage(null);
                // 加载本地App
                Map<String, String> localApps = this.getLoadLocalApps();
                // 加载分布式插件
                Distributed dist = DistributedFactory.getDistributed();
                Map<String, List<Map<String, Object>>> distApp = dist.getApps();
                while (rs.next()) {
                    String url = rs.getString("appUrl");
                    String appname = rs.getString("app").toUpperCase();
                    String rurl = this.getRealUrl(url, appname, localApps, distApp);
                    if (!StrUtil.isNull(url) && StrUtil.isNull(rurl)) {
                        continue;
                    }
                    Map<String, Object> menuMap = new HashMap<String, Object>();
                    menuMap.put("maxAppsId", rs.getInt("maxAppsId"));
                    menuMap.put("app", appname);
                    String tname = null;
                    if (language != null) {
                        tname = language.getI18n("MAXAPPS.APP." + rs.getString("app"));
                    }
                    if (!StrUtil.isNull(tname)) {
                        menuMap.put("description", tname);
                    } else {
                        menuMap.put("description", rs.getString("description"));
                    }
                    menuMap.put("appType", rs.getString("appType"));
                    menuMap.put("parent", rs.getString("parent"));
                    menuMap.put("appUrl", rurl);
                    menuMap.put("mainTbName", rs.getString("mainTbName"));
                    menuMap.put("orderId", rs.getString("orderId"));
                    result.add(menuMap);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            JxDataSourceUtil.closeResultSet(rs);
            JxDataSourceUtil.closeStatement(ps);
            JxDataSourceUtil.close(conn);
        }
        removeInvaildMenu(result);
        CacheUtil.putPermissionCache(cachekey, result);// 缓存菜单
        return result;
    }

    /**
     * 移出无效的菜单,只有模块，没有应用的需要移出
     * 
     * @param list
     */
    private void removeInvaildMenu(List<Map<String, Object>> list) {
        if (list == null) {
            return;
        }
        for (Iterator<Map<String, Object>> it = list.iterator(); it.hasNext();) {
            Map<String, Object> menu = it.next();
            if ("MODULE".equalsIgnoreCase((String) menu.get("appType"))) {
                if (!hasApp(list, (String) menu.get("app"))) {
                    it.remove();
                }
            }
        }
    }

    /**
     * 这个模块中，是否有应用
     * 
     * @param list
     * @param module
     * @return
     */
    private boolean hasApp(List<Map<String, Object>> list, String module) {
        if (StrUtil.isNull(module)) {
            return false;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> menu = list.get(i);
            String parent = (String) menu.get("parent");
            if (module.equals(parent)) {
                String apptype = (String) menu.get("appType");
                if ("MODULE".equalsIgnoreCase(apptype)) {
                    // 如果是子模块，则递归调用
                    return hasApp(list, (String) menu.get("app"));
                } else if ("APP".equalsIgnoreCase(apptype)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getMaxAppMenuJson(int visible, String where, boolean permission, boolean workflow) throws SQLException {
        List<Map<String, Object>> list = getMaxAppMenu(visible, where, permission, workflow);
        return JsonUtil.toJson(list);
    }

    /**
     * 检查URL所在的App是否存在，如果在分布式环境中，则直接加载分布式地址。
     * 
     * @param url
     * @param app
     * @param localApps
     * @param distributeApps
     * @return
     */
    private String getRealUrl(String url, String app, Map<String, String> localApps, Map<String, List<Map<String, Object>>> distributeApps) {
        if (StrUtil.isNull(url)) {
            return null;
        }
        if (url.startsWith("app.action") || url.startsWith("http://")) {
            return url;
        }
        // 以下是插件需要处理的
        if (StrUtil.isNull(app)) {
            return url;
        }
        // 检查本地插件
        if (localApps != null) {
            if (localApps.containsKey(app)) {
                return url;
            }
            String surl = "/" + url.substring(0, url.lastIndexOf('/') + 1);
            Iterator<Entry<String, String>> iter = localApps.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                if (entry.getValue().startsWith(surl)) {
                    return url;
                }
            }
        }
        // 分布式列表中查找对应的App
        if (distributeApps != null) {
            List<Map<String, Object>> list = distributeApps.get(app);
            if (list != null && !list.isEmpty()) {
                Map<String, Object> dto = list.get(0);
                String surl = String.valueOf(dto.get("URL"));
                String home = String.valueOf(dto.get("HOME"));
                return StrUtil.contact(home,surl);
            }
        }
        return null;
    }

    /**
     * 获得本地加载的APP
     * 
     * @return <appname,appurl>
     */
    public Map<String, String> getLoadLocalApps() {
        Map<String, String> apps = new HashMap<String, String>();
        Map<String, Map<String, Object>> bundles = JxResource.getMyBundles();
        Iterator<Entry<String, Map<String, Object>>> iter = bundles.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Map<String, Object>> bnd = iter.next();
            Map<String, Object> val = bnd.getValue();
            if (val != null) {
                Object jurl = val.get("Jx-AppURL");
                if (jurl != null) {
                    String[] jurls = jurl.toString().split(",");
                    for (int i = 0; i < jurls.length; i++) {
                        if (jurls[i].length() > 2) {
                            int pos = jurls[i].indexOf('/', 1);
                            String appname;
                            if (pos > 1) {
                                appname = jurls[i].substring(1, pos);
                            } else {
                                appname = jurls[i].substring(1);
                            }
                            apps.put(appname.toUpperCase(), jurls[i]);
                        }
                    }
                }
            }
        }
        return apps;
    }

}

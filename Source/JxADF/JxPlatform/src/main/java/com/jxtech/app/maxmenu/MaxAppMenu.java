package com.jxtech.app.maxmenu;

import com.jxtech.common.JxResource;
import com.jxtech.db.util.JxDataSourceUtil;
import com.jxtech.i18n.LanguageFactory;
import com.jxtech.i18n.LanguageIface;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.util.CacheUtil;
import com.jxtech.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by zhouyu on 2014-10-09.
 */
public class MaxAppMenu {
    private static final Logger LOG = LoggerFactory.getLogger(MaxAppMenu.class);
    public static final String CACHE_PREX = "MENU.";

    /**
     * 获得应用程序菜单
     *
     * @param session
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getMaxAppMenu(HttpSession session) throws SQLException {
        StringBuilder ck = new StringBuilder(CACHE_PREX);
        ck.append(JxSession.getUserId()).append(".");
        ck.append(JxSession.getUserLang());
        String cachekey = ck.toString();
        Object objv = CacheUtil.getPermission(cachekey);
        if (objv instanceof List) {
            return (List<Map<String, Object>>) objv;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select * from MAXAPPS ");
        sql.append("where ORDERID > 0 and app in (select app ");
        sql.append("from maxmenu a, pub_role_operation b,pub_role_user c ");
        sql.append("where a.maxmenuid = b.menu_id and b.role_id=c.role_id and a.visible=1 ");
        sql.append("and c.user_id = ?) ");
        sql.append("or apptype = 'MODULE'");
        sql.append("order by orderid asc, apptype desc");

        Connection conn = null;
        List<Map<String, Object>> result = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JxDataSourceUtil.getConnection(null);
            ps = conn.prepareStatement(sql.toString());
            JxUserInfo userInfo = null;
            if (session != null) {
                Object obj = session.getAttribute(JxSession.USER_INFO);
                if (obj != null) {
                    userInfo = (JxUserInfo) obj;
                }
            }

            if (userInfo != null) {
                result = new ArrayList<Map<String, Object>>();
                ps.setString((int) 1, userInfo.getUserid());
                rs = ps.executeQuery();
                // ResourceBundle zTreeBundle = JxLangResourcesUtil.getResourceBundle("res.tree.menu");
                LanguageIface language = LanguageFactory.getLanguage(null);
                Map<String, Map<String, Object>> bundles = JxResource.getMyBundles();
                while (rs.next()) {
                    String url = rs.getString("appUrl");
                    if (!StrUtil.isNull(url)) {
                        if (!url.startsWith("app.action") && !url.startsWith("http://")) {
                            // 检查插件是否安装，如果没有安装，则不需要加载菜单
                            url = "/" + url.substring(0, url.lastIndexOf('/') + 1);
                            boolean isInstall = false;// 是否安装了插件，默认没有安装
                            for (Map.Entry<String, Map<String, Object>> bnd : bundles.entrySet()) {
                                Map<String, Object> val = bnd.getValue();
                                if (val != null) {
                                    Object jurl = val.get("Jx-AppURL");
                                    if (jurl != null) {
                                        String[] jurls = jurl.toString().split(",");
                                        for (int i = 0; i < jurls.length; i++) {
                                            if (jurls[i].startsWith(url)) {
                                                isInstall = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            if (!isInstall) {
                                continue;// 没有安装，就不显示到界面了。
                            }
                        }
                    }
                    Map<String, Object> menuMap = new HashMap<String, Object>();
                    menuMap.put("maxAppsId", rs.getInt("maxAppsId"));
                    menuMap.put("app", rs.getString("app"));
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
                    menuMap.put("appUrl", rs.getString("appUrl"));
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
        for (Iterator<Map<String, Object>> it = list.iterator(); it.hasNext(); ) {
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
}

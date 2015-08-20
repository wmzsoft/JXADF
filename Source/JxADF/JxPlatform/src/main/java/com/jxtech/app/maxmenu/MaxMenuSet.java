package com.jxtech.app.maxmenu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.db.util.JxDataSourceUtil;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxConstant;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.CacheUtil;
import com.jxtech.util.StrUtil;

/**
 * @author wmzsoft@gmail.com
 * @date 2014.09
 */
public class MaxMenuSet extends JboSet implements MaxMenuSetIFace {

    private static final long serialVersionUID = 1237889392366755494L;
    private static final Logger LOG = LoggerFactory.getLogger(MaxMenuSet.class);

    @Override
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new MaxMenu(this);
        return currentJbo;
    }

    /**
     * 获得应用程序菜单
     *
     * @param appname    应用程序名
     * @param appType    应用程序类型，列表、还是主程序
     * @param tabDisplay 显示到更多中，还是显示到Toolbar中。
     * @param parent     父级菜单
     * @return
     * @throws JxException
     */
    @SuppressWarnings("unchecked")
    public List<JboIFace> getMenus(String appname, String appType, String tabDisplay, String parent) throws JxException {
        if (StrUtil.isNull(appname)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(JxSession.getUserId()).append(".");
        sb.append(appname).append(".");
        sb.append(appType).append(".");
        sb.append(tabDisplay).append(".");
        sb.append(parent);
        String cachekey = sb.toString();
        Object obj = CacheUtil.getPermission(cachekey);
        if (obj instanceof List) {
            return (List<JboIFace>) obj;
        }
        LOG.debug("参数信息：appname=" + appname + ",appType=" + appType + ",tabDisplay=" + tabDisplay + ",parent=" + parent);
        JboSetIFace js = JboUtil.getJboSet("MAXMENU");
        DataQueryInfo dqi = js.getQueryInfo();
        Map<String, Object> params = new HashMap<String, Object>();
        if (!StrUtil.isNull(appType)) {
            params.put("(APPTYPE='ALL' or APPTYPE=?)", appType.toUpperCase());
        }
        if (!StrUtil.isNull(tabDisplay)) {
            params.put("(TABDISPLAY='ALL' or TABDISPLAY=?)", tabDisplay.toUpperCase());
        }
        if (!StrUtil.isNull(parent)) {
            params.put("PARENT=?", parent.toUpperCase());
        } else {
            if (JxDataSourceUtil.isDbOfSystemMySql() || JxDataSourceUtil.isDbOfSystemMsSqlServer()) {
                params.put("(PARENT is null or PARENT='')", null);
            } else {
                params.put("PARENT is null", null);
            }
        }
        params.put("(APP='GLOBAL' or APP=?)", appname.toUpperCase());
        params.put("VISIBLE=?", 1);
        params.put(getPermissionSql(), null);
        dqi.setParams(params);
        dqi.setOrderby("POSITION");
        List<JboIFace> list = js.queryAll();
        CacheUtil.putPermissionCache(cachekey, list);
        return list;
    }

    /**
     * 返回权限检查的SQL脚本。
     *
     * @return
     */
    public String getPermissionSql() {
        StringBuffer sb = new StringBuffer();
        sb.append("MAXMENUID in (select menu_id from PUB_ROLE_OPERATION where  OPERATION=1 and role_id in (");
        sb.append("select role_id from PUB_ROLE_USER where upper(user_id)=upper('${jxuserinfo.userid}')))");
        return sb.toString();
    }

    @Override
    public List<JboIFace> query(String shipname) throws JxException {
        List<JboIFace> superJboList = super.query(shipname);
        if (!StrUtil.isNull(shipname) && shipname.equalsIgnoreCase("PUB_ROLE_MAXMENU_ALL")) {
            JboIFace parent = getParent();
            if (null != parent) {
                JboSetIFace roleOperationSet = parent.getRelationJboSet("PUB_ROLE_OPERATIONROLE_IDP", JxConstant.READ_RELOAD);
                List<JboIFace> roleOperationList = roleOperationSet.queryAll();
                for (JboIFace jbo : superJboList) {
                    for (JboIFace roleOperation : roleOperationList) {
                        if (jbo.getUidValue().equalsIgnoreCase(roleOperation.getString("MENU_ID"))) {
                            jbo.setObject("INROLE", "1");
                            break;
                        } else {
                            jbo.setObject("INROLE", "0");
                        }
                    }
                }
            }
        }

        return superJboList;
    }

    public String toggleOperationInRole(String params) throws JxException {
        String result = "fail";
        String[] param = params.split(",");
        String action = param[0];
        String menuId = param[1];

        JboIFace mainJbo = JxSession.getMainApp().getJbo();
        JboSetIFace jboSet = mainJbo.getRelationJboSet("PUB_ROLE_OPERATIONROLE_IDP");

        if ("1".equalsIgnoreCase(action)) {
            JboIFace jbo = jboSet.add();
            jbo.setObject("ROLE_ID", mainJbo.getObject("ROLE_ID"));
            jbo.setObject("MENU_ID", menuId);

        } else {
            List<JboIFace> allRoleOperationList = jboSet.queryAll();
            for (JboIFace roleOperation : allRoleOperationList) {
                String roleOperationId = roleOperation.getString("MENU_ID");

                if (roleOperationId.equalsIgnoreCase(menuId)) {
                    roleOperation.delete();
                }
            }
        }

        jboSet.commit();
        result = "ok";

        return result;
    }
}

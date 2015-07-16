package com.jxtech.app.maxapps;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.auth.Permission;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JxException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public class MaxappsSet extends JboSet implements MaxappsSetIFace {

    private static final long serialVersionUID = -6721115904710010842L;

    @Override
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new Maxapps(this);
        return currentJbo;
    }

    @Override
    public JboIFace getJboByUrl(String appurl) throws JxException {
        DataQueryInfo dqi = getQueryInfo();
        dqi.setWhereCause("appurl like ?");
        dqi.setWhereParams(new Object[] { appurl + "%" });
        List<JboIFace> list = query();
        if (list != null) {
            if (!list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }

    /*
     * * 获取用户授权的APP名称集合* 包括已授权和免授权的应用* 对于MODULE，至少要有1个及以上授权的APP
     */
    public Set<String> getAuthApps(String userid) throws JxException {
        Set<String> apps = new HashSet<String>();
        DataQueryInfo dqi = getQueryInfo();
        // 1、获取已明确授权的应用
        dqi.setWhereCause(" app in ( " + "select m.app " + "from PUB_ROLE_OPERATION o, MAXMENU m,PUB_ROLE_USER r  " + "where o.menu_id=m.maxmenuid " + "and o.role_id=r.role_id " + "and r.user_id='" + userid + "') ");
        List<JboIFace> list = queryAll();
        for (JboIFace app : list) {
            apps.add(app.getString("app"));
        }
        // 2、补充免授权的应用、有授权应用的模块
        dqi.setWhereCause(" apptype='APP' ");
        list = queryAll();
        String appName;
        String parentName;
        for (JboIFace app : list) {
            appName = app.getString("app");
            parentName = app.getString("parent");
            if (!apps.contains(appName)) {
                if (Permission.isIgnoreLoginSecurity(appName, null))
                    apps.add(appName);
            }
            if (apps.contains(appName)) {
                apps.add(parentName);
            }
        }

        return apps;
    }
}

package com.jxtech.app.maxmenu;

import java.util.List;

import com.jxtech.app.pubrole.PubRoleUserSetIFace;
import com.jxtech.jbo.Jbo;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;

/**
 * @author wmzsoft@gmail.com
 * @date 2014.09
 */
public class MaxMenu extends Jbo {

    private static final long serialVersionUID = -7134806480782368269L;

    public MaxMenu(JboSetIFace jboset) throws JxException {
        super(jboset);
    }

    @Override
    public void afterLoad() throws JxException {
        super.afterLoad();
        if (!isToBeAdd()) {
            setReadonly("MENU", true);
        }
    }

    @Override
    public boolean setDefaultValue() throws JxException {
        if (!super.setDefaultValue()) {
            return false;
        }
        JboIFace parent = getParent();
        if (parent != null) {
            if ("MAXAPPS".equalsIgnoreCase(parent.getJboName())) {
                this.setString("APP", parent.getString("APP"));
            }
        }
        return true;
    }

    /**
     * 获得菜单对应角色的操作权限 即：pub_role_operation.operation
     * 
     * @return 默认无权限
     * @throws JxException
     */
    public int getRolepermissionoperation() throws JxException {
        return getRolePermission("permissionoperation");
    }

    public int getRolepermissionmanage() throws JxException {
        return getRolePermission("permissionmanage");
    }

    public int getRolepermissiongrantmanage() throws JxException {
        return getRolePermission("permissiongrantmanage");
    }

    public int getRolepermissions() throws JxException {
        return getRolePermission("PERMISSIONS");
    }

    /**
     * 查询pub_role_operation中的记录
     * 
     * @param field
     * @param defaultvalue,查到记录之后，如果字段为空，返回的值。
     * @return
     * @throws JxException
     */
    private int getRolePermission(String field) throws JxException {
        if (!getData().containsKey(field.toUpperCase())) {
            initRolePermission();
        }
        return (int) getLong(field);
    }

    /**
     * 初始化角色权限
     * 
     * @throws JxException
     */
    private void initRolePermission() throws JxException {
        JboIFace parent = getParent();
        if (parent == null) {
            return;
        }
        if ("PUB_ROLE".equalsIgnoreCase(parent.getJboName())) {
            // 角色管理
            JboSetIFace ro = JboUtil.getJboSet("PUB_ROLE_OPERATION");
            DataQueryInfo dqi = ro.getQueryInfo();
            dqi.setWhereCause("role_id=? and menu_id=?");
            String roleid = parent.getString("role_id");
            String menuid = getString("maxmenuid");
            dqi.setWhereParams(new Object[] { roleid, menuid });
            List<JboIFace> list = ro.query();
            // 登录用户的权限
            boolean ok = false;
            if (list != null) {
                if (list.size() > 0) {
                    ok = true;
                    JboIFace jbo = list.get(0);
                    // 将3个权限全部处理下来
                    // 1.处理角色操作权限
                    Object oper = jbo.getObject("OPERATION");
                    if (oper == null) {
                        oper = 1;
                    }
                    setObject("permissionoperation", oper);
                    setObject("permissionmanage", jbo.getLong("MANAGE"));
                    setObject("permissiongrantmanage", jbo.getLong("GRANTMANAGE"));
                }
            }
            if (!ok) {
                // 设定默认值
                setObject("permissionoperation", 0);
                setObject("permissionmanage", 0);
                setObject("permissiongrantmanage", 0);
            }
            long perm = 0;
            JboSetIFace ru = JboUtil.getJboSet("PUB_ROLE_USER");
            if (ru instanceof PubRoleUserSetIFace) {
                PubRoleUserSetIFace prus = (PubRoleUserSetIFace) ru;
                perm = prus.getPermissions(JxSession.getUserId(), menuid);
                setObject("PERMISSIONS", perm);
                if (prus.isManager(perm)) {
                    // 具备管理权限
                    setString("operation_disabled", null);
                } else {
                    setString("operation_disabled", "disabled");
                }
                if (prus.isGrantManager(perm)) {
                    // 具备授权管理权限
                    setString("manage_disabled", null);
                    setString("grantmanage_disabled", null);
                } else {
                    setString("manage_disabled", "disabled");
                    setString("grantmanage_disabled", "disabled");
                }
            }
        }
    }
}

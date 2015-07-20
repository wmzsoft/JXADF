package com.jxtech.app.pubrole;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.app.maxmenu.MaxMenuSetIFace;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSet;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;

/**
 * 角色用户信息- 健新科技优化实现
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.05
 */
public class PubRoleUserSet extends JboSet implements PubRoleUserSetIFace {

    /**
     *
     */
    private static final long serialVersionUID = 5184840635624431623L;
    private Logger LOG = LoggerFactory.getLogger(PubRoleUserSet.class);

    @Override
    protected JboIFace getJboInstance() throws JxException {
        currentJbo = new PubRoleUser(this);
        return currentJbo;
    }

    public List<JboIFace> getRoleJboList(String userId) {
        List<JboIFace> roleJboList = new ArrayList<JboIFace>();
        DataQueryInfo dqInfo = getQueryInfo();
        dqInfo.setWhereCause("USER_ID = ?");
        dqInfo.setWhereParams(new Object[] { userId });
        try {
            List<JboIFace> roleUserList = queryAll();
            JboSetIFace roleJboSet = JboUtil.getJboSet("PUB_ROLE");
            for (JboIFace roleUserJbo : roleUserList) {
                roleJboList.add(roleJboSet.queryJbo("", roleUserJbo.getString("ROLE_ID")));
            }
        } catch (JxException e) {
            LOG.error(e.getMessage(), e);
        }

        return roleJboList;
    }



    @Override
    public Set<String> getRoles(String userid) throws JxException {
        if (StrUtil.isNull(userid)) {
            return null;
        }
        DataQueryInfo dqInfo = getQueryInfo();
        dqInfo.setWhereCause("USER_ID = ?");
        dqInfo.setWhereParams(new Object[] { userid });
        List<JboIFace> list = queryAll();
        if (list != null) {
            int size = list.size();
            Set<String> roles = new HashSet<String>();
            for (int i = 0; i < size; i++) {
                roles.add(list.get(i).getString("ROLE_ID"));
            }
            return roles;
        }
        return null;
    }

    /**
     * 获得某个用户对某个菜单具备的权限.
     * 
     * @param userid
     * @param menuid
     * @return
     * @throws JxException
     */
    public long getPermissions(String userid, String menuid) throws JxException {
        if (StrUtil.isNull(userid) || StrUtil.isNull(menuid)) {
            return 0L;
        }
        Set<String> roles = getRoles(userid);
        if (roles != null) {
            if (roles.contains("1")) {
                // 超级管理员,具备所有的权限
                return MaxMenuSetIFace.PERMISSION_ALL;
            }
        }
        JboSetIFace js = JboUtil.getJboSet("pub_role_operation");
        DataQueryInfo dqi = js.getQueryInfo();
        String where = "role_id in (select role_id from pub_role_user where user_id=?) AND menu_id=?";
        dqi.setWhereCause(where);
        dqi.setWhereParams(new Object[] { userid, menuid });
        List<JboIFace> list = js.queryAll();
        if (list != null) {
            int size = list.size();
            long perm = 0L;
            for (int i = 0; i < size; i++) {
                JboIFace jbo = list.get(i);
                long p = jbo.getLong("OPERATION");
                if (jbo.getBoolean("MANAGE")) {
                    p = p | MaxMenuSetIFace.PERMISSION_MANAGE;
                }
                if (jbo.getBoolean("GRANTMANAGE")) {
                    p = p | MaxMenuSetIFace.PERMISSION_GRANTMANAGE;
                }
                p = p | jbo.getLong("PERMISSIONS");
                perm = perm | p;
            }
            return perm;
        }
        return 0L;
    }

    /**
     * permission权限是否具有管理操作的权限
     * 
     * @param permission
     * @return
     */
    public boolean isManager(long permission) {
        return (permission & MaxMenuSetIFace.PERMISSION_MANAGE) == MaxMenuSetIFace.PERMISSION_MANAGE;
    }

    /**
     * permission权限是否具备分配管理员权限、授权管理员权限
     * 
     * @param permission
     * @return
     */
    public boolean isGrantManager(long permission) {
        return (permission & MaxMenuSetIFace.PERMISSION_GRANTMANAGE) == MaxMenuSetIFace.PERMISSION_GRANTMANAGE;
    }
}

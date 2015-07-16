package com.jxtech.app.pubrole;

import java.util.Set;

import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.07
 * 
 */
public interface PubRoleUserSetIFace extends JboSetIFace {

    /**
     * 获得用户的所有角色ID号
     * 
     * @param userid
     * @return
     * @throws JxException
     */
    public Set<String> getRoles(String userid) throws JxException;

    /**
     * 获得某个用户对某个菜单具备的权限.
     * 
     * @param userid
     * @param menuid
     * @return
     * @throws JxException
     */
    public long getPermissions(String userid, String menuid) throws JxException;

    /**
     * permission权限是否具有管理操作的权限
     * 
     * @param permission
     * @return
     */
    public boolean isManager(long permission);

    /**
     * permission权限是否具备分配管理员权限、授权管理员权限
     * 
     * @param permission
     * @return
     */
    public boolean isGrantManager(long permission);

}

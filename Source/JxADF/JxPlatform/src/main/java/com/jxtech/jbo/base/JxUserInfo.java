package com.jxtech.jbo.base;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.auth.PermissionIFace;
import com.jxtech.jbo.util.JxException;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class JxUserInfo implements Serializable {

    private static final long serialVersionUID = -5878682879440234549L;
    public static final String LANG_CODE = "LANGCODE";

    // 共有信息
    private String loginid;// 用户信息,康拓普中为user_id
    private String personid;// maximo员工标识,康拓普中为employee_id
    private String userid;// =Pub_user.user_id
    private PermissionIFace permission;// 用户权限信息
    private String displayname;// 姓名
    private JboIFace department;// 部门信息,Pub_department
    private JboIFace user;// 用户信息，Pub_user
    // maximo信息
    private String orgid;
    private String siteid;
    private String langcode;

    private Map<String, String> metadata;// 用户元数据。
    private String loginIp;// 登录的IP地址
    private String loginMachine;// 登录的机器名

    private Set<String> roles;// 存放角色的ID

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }

    public String getPersonid() {
        return personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }

    public String getLangcode() {
        return langcode;
    }

    public void setLangcode(String langcode) {
        this.langcode = langcode;
    }

    public PermissionIFace getPermission() {
        return permission;
    }

    public void setPermission(PermissionIFace permission) {
        this.permission = permission;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public JboIFace getDepartment() {
        return department;
    }

    public void setDepartment(JboIFace department) {
        this.department = department;
    }

    public JboIFace getUser() {
        return user;
    }

    public void setUser(JboIFace user) {
        this.user = user;
    }

    public String getDepartmentId() throws JxException {
        if (department != null) {
            return department.getString("department_id");
        }
        return null;
    }

    public String getDepartmentCode() throws JxException {
        if (department != null) {
            return department.getString("code");
        }
        return null;
    }

    public String getDepartmentName() throws JxException {
        if (department != null) {
            return department.getString("name");
        }
        return null;
    }

    public String getDepartmentParentId() throws JxException {
        if (department != null) {
            return department.getString("super_department_id");
        }
        return null;
    }

    public int getDepartmentLevel() throws JxException {
        if (department != null) {
            return (int) department.getLong("tree_level");
        }
        return 0;
    }

    public String getUserid() {
        if (userid != null) {
            userid = userid.toUpperCase();
        }
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginMachine() {
        return loginMachine;
    }

    public void setLoginMachine(String loginMachine) {
        this.loginMachine = loginMachine;
    }

    public Set<String> getRoles() {
        if (roles == null && permission != null) {
            roles = permission.getRoles(userid);
        }
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

}

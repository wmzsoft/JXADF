package com.jxtech.workflow.iface;

import java.util.Map;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.util.JxException;

/**
 * 工作流角色中，用户自定义角色
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.10
 */
public interface WFRoleUser {

    /**
     * 根据Java类获得用户信息
     * 
     * @param users 当前已有的用户列表
     * @param role 角色(WFTASKROLE)Jbo
     * @param form 表单Jbo
     * @param nodeinfo 工作流节点(wfnode)Jbo
     * @return
     * @throws JxException
     */
    public Map<String, JboIFace> getUsers(Map<String, JboIFace> users, JboIFace role, JboIFace form, JboIFace nodeinfo) throws JxException;
}

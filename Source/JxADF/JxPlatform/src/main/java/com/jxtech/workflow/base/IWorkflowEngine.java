package com.jxtech.workflow.base;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.util.JxException;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by cxm on 2014/8/13.
 */
public interface IWorkflowEngine {

    /**
     * 发送工作流程序入口
     * 
     * @param jbi 发送工作流的业务数据对象
     * @param params
     * @return
     * @throws JxException
     */
    public boolean route(JboIFace jbi, Map<String, Object> params) throws JxException;

    /**
     * 预览当前节点的信息
     * 
     * @param appname 应用程序名
     * @param jboname 表单名
     * @param uid 表单记录ID
     * @return
     * @throws JxException
     */
    public WorkflowBaseInfo pretreatment(String appname, String jboname, String uid) throws JxException;

    /**
     * 获得工作流历史，当前未处理的所有信息
     * 
     * @param jbi
     * @return
     * @throws JxException
     */
    public List<JboIFace> getRouteCurrentList(JboIFace jbi) throws JxException;

    /**
     * 将与cJbo相关的等待targetJbo变为可以发送的工作流（可以出现在待办中)
     * 
     * @param cJbo 当前jbo
     * @param targetJbo 和jbo相关的等待jbo
     * @throws JxException
     */
    public void routeHoldon(JboIFace cJbo, JboIFace targetJbo) throws JxException;
    
    public InputStream readImage(String uid, String instanceId) throws JxException;


    /**
     * 获取工作流角色
     *
     * @param   roleSet
     * @param   roleName
     * @return
     */
    public List<JboIFace> getRoles(JboSetIFace roleSet, String roleName);
    /**
     * 根据角色获取下面的用户
     *
     * @param   userSet
     * @param   roleName
     * @return
     */
    public List<JboIFace> getRoleUsers(JboSetIFace userSet, String roleName);

    /**
     * 添加角色用户
     *
     * @param   roleName
     * @param   userName
     * @return
     */
    public boolean addRoleUser(String roleName, String userName, String password);

    /**
     * 删除角色用户
     *
     * @param   roleName
     * @param   userName
     * @return
     */
    public boolean delRoleUser(String roleName, String userName);


}

package com.jxtech.tag.workflow;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * databean Created by cxm on 2014/8/15.
 */
public class Workflow extends JxBaseUIBean {
    private String routeBtnVisible;// 是否显示提交按钮,取值为：TRUE,FALSE

    private String fromUid;// 单据ID号
    private String fromApp;// 应用程序名
    private String fromAppType;// 应用程序类型
    private String fromJboname;// 应用程序主表名
    private String noteRequired;// 意见必填，取值为：TRUE,FALSE，默认为False

    private String complete;

    // 操作列表
    private Map<String, String> actions; // 当前用户在当前节点可以操作的列表
    // 操作用户列表
    private Map<String, String> actionUsers; // 当前用户在当前节点可以操作的列表对应用户,|分割

    private String status; // 当前状态
    private String nextStatus; // 下一个状态

    public Workflow(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return StrUtil.contact("workflow/", getRenderer(), "workflow");
    }

    @Override
    public String getDefaultOpenTemplate() {
        return StrUtil.contact("workflow/", getRenderer(), "workflow-close");
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();

        if (null != getRouteBtnVisible()) {
            addParameter("routeBtnVisible", findString(getRouteBtnVisible()));
        }

        if (null != actions) {
            addParameter("actions", actions);
        }

        if (null != actionUsers) {
            addParameter("actionUsers", actionUsers);
        }

        addParameter("complete", findString(complete));
    }

    public String getFromUid() {
        return fromUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public String getFromApp() {
        return fromApp;
    }

    public void setFromApp(String fromApp) {
        this.fromApp = fromApp;
    }

    public String getFromAppType() {
        return fromAppType;
    }

    public void setFromAppType(String fromAppType) {
        this.fromAppType = fromAppType;
    }

    public String getFromJboname() {
        return fromJboname;
    }

    public void setFromJboname(String fromJboname) {
        this.fromJboname = fromJboname;
    }

    public String getNoteRequired() {
        return noteRequired;
    }

    public void setNoteRequired(String noteRequired) {
        this.noteRequired = noteRequired;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNextStatus() {
        return nextStatus;
    }

    public void setNextStatus(String nextStatus) {
        this.nextStatus = nextStatus;
    }

    public Map<String, String> getActions() {
        return actions;
    }

    public void setActions(Map<String, String> actions) {
        this.actions = actions;
    }

    public String getRouteBtnVisible() {
        return routeBtnVisible;
    }

    public void setRouteBtnVisible(String routeBtnVisible) {
        this.routeBtnVisible = routeBtnVisible;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public Map<String, String> getActionUsers() {
        return actionUsers;
    }

    public void setActionUsers(Map<String, String> actionUsers) {
        this.actionUsers = actionUsers;
    }
}

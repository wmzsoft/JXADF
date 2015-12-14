package com.jxtech.tag.workflow;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.jbo.App;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.util.StrUtil;
import com.jxtech.workflow.base.IWorkflowEngine;
import com.jxtech.workflow.base.WorkflowBaseInfo;
import com.jxtech.workflow.base.WorkflowEngineFactory;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * Oracle bpm 12c 工作流tag标签 Created by cxm on 2014/8/15.
 */
public class WorkflowTag extends JxBaseUITag {

    private static final long serialVersionUID = 1L;

    private String fromUid;// 单据ID号
    private String fromApp;// 应用程序名
    private String fromAppType;// 应用程序类型
    private String fromJboname;// 应用程序主表名
    private String noteRequired;// 意见必填，取值为：TRUE,FALSE，默认为False
    private String instanceid;

    // 操作列表
    private Map<String, String> actions; // 当前用户在当前节点可以操作的列表
    // 操作用户列表
    private Map<String, String> actionUsers; // 当前用户在当前节点可以操作的列表对应用户,|分割

    private String status; // 当前状态
    private String nextStatus; // 下一个状态

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowTag.class);

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        fromUid = request.getParameter("fromUid");
        fromApp = request.getParameter("fromapp");
        fromAppType = request.getParameter("fromapptype");
        fromJboname = request.getParameter("fromjboname");
        noteRequired = request.getParameter("noterequired");
        instanceid = request.getParameter("instanceid");
        super.initPropertiesValue(false);
        return new Workflow(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        Workflow wf = (Workflow) component;

        if (StrUtil.isNull(fromUid)) {
            // 所有的按钮都不显示
            setParamsDefaultValue(wf, false);
            return;
        }
        // 如果包含多个单据，则以第一个为准。
        String[] uids = fromUid.split(",");
        if (uids.length < 1) {
            // 一个没有
            setParamsDefaultValue(wf, false);
            return;
        }
        // 以第一个为准
        String uid = uids[0];

        try {
            // String workFlowType = JboUtil.getAppWorkflowEngine(fromApp.toUpperCase());
            String workFlowType = "OBPM";
            if (!StrUtil.isNull(instanceid) && instanceid.indexOf("JXBPM.")>=0){
                workFlowType="JXBPM";
            }
            App app = JxSession.getApp(fromApp, fromAppType);
            if (app != null) {
                JboSetIFace js = app.getJboset();
                if (js != null) {
                    workFlowType = js.getWorkflowEngine();
                }
            }else{
                JboSetIFace js = JboUtil.getJboSet(fromJboname);
                js.setAppname(fromApp);
                workFlowType = js.getWorkflowEngine();
            }
            IWorkflowEngine wfEngine = WorkflowEngineFactory.getWorkflowEngine(workFlowType);
            if (wfEngine != null) {
                WorkflowBaseInfo wfi = wfEngine.pretreatment(fromApp, fromJboname, uid);
                if (wfi != null) {
                    wf.setRouteBtnVisible(wfi.isRouteAble() ? "TRUE" : "FALSE");
                    wf.setActions(wfi.getActionMap());
                    wf.setActionUsers(wfi.getActionUserMap());
                }
            } else {
                LOG.debug("获得工作流引擎失败：" + workFlowType);
            }
        } catch (JxException e) {
            LOG.error(e.getMessage());
        }
        wf.setNoteRequired(noteRequired);
    }

    private void setParamsDefaultValue(Workflow wf, boolean def) {
        String value = String.valueOf(def).toUpperCase();
        wf.setRouteBtnVisible(value);
        wf.setNoteRequired(value);
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

    public Map<String, String> getActionUsers() {
        return actionUsers;
    }

    public void setActionUsers(Map<String, String> actionUsers) {
        this.actionUsers = actionUsers;
    }

    public String getInstanceid() {
        return instanceid;
    }

    public void setInstanceid(String instanceid) {
        this.instanceid = instanceid;
    }
}

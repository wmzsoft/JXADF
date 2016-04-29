package com.jxtech.tag.bztag.wfstatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.views.annotations.StrutsTag;

import com.jxtech.jbo.JboIFace;
import com.jxtech.tag.comm.JxBaseUIBean;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 工作流状态显示
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.09
 * 
 */
@StrutsTag(name = "wfstatus", tldTagClass = "com.jxtech.tag.bztag.wfstatus.WfStatusTag", description = "workflow status")
public class WfStatus extends JxBaseUIBean {

    private JboIFace jbo;
    protected String mystyle;// 样式
    private String app;// 应用程序名称
    private String bpmengine;// 工作流类型
    private String instanceid;// 工作流实例ID
    private String ownerid;// 工作流主表记录ID
    private String statusDesc;// 显示状态值
    private String statusValue;// 状态值

    public WfStatus(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return StrUtil.contact("bztag/wfstatus/", getRenderer(), "wfstatus-close");
    }

    @Override
    public String getDefaultOpenTemplate() {
        return StrUtil.contact("bztag/wfstatus/", getRenderer(), "wfstatus");
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (jbo != null) {
            addParameter("jbo", jbo);
        }
        if (label != null) {
            addParameter("label", getI18NValue(label));
        }
        if (mystyle != null) {
            addParameter("mystyle", findString(mystyle));
        }
        if (app != null) {
            addParameter("app", findString(app));
        }
        if (bpmengine != null) {
            addParameter("bpmengine", findString(bpmengine).toLowerCase());
        }
        if (instanceid != null) {
            addParameter("instanceid", findString(instanceid));
        }
        if (ownerid != null) {
            addParameter("ownerid", findString(ownerid));
        }
        if (statusDesc != null) {
            addParameter("statusDesc", findString(statusDesc));
        }
        if (statusValue != null) {
            addParameter("statusValue", findString(statusValue));
        }
    }

    public void setJbo(JboIFace jbo) {
        this.jbo = jbo;
    }

    public void setMystyle(String mystyle) {
        this.mystyle = mystyle;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public void setBpmengine(String bpmengine) {
        this.bpmengine = bpmengine;
    }

    public void setInstanceid(String instanceid) {
        this.instanceid = instanceid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }

}

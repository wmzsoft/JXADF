package com.jxtech.tag.form;

import com.jxtech.jbo.JboIFace;
import com.jxtech.tag.comm.JxBaseUIBean;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
@StrutsTag(name = "form", tldTagClass = "com.jxtech.tag.form.FormTag", description = "Form")
public class Form extends JxBaseUIBean {
    // private static final Logger LOG = LoggerFactory.getLogger(Form.class);
    protected String jboname;
    protected String apprestrictions;// 限制条件
    protected JboIFace jbo;
    protected String fromid;// 从哪里过来的。
    protected String fromApp;// 从哪个APP过来的
    protected String fromAppType;// 从哪个Apptype过来的
    protected String fromJboname;// 从哪个Jboname过来的
    protected String fromUid;//
    private String uid;
    private String instanceid;// 流程实例ID
    private String instancestatus;// 流程状态，0：未启动
    private String type; // //默认值：edit（和后台jbo对象有交互）；view（和jbo没有关联，仅仅用来显示）
    protected String relationship; // 关系名 (一般用于列表的扩展表单使用，配合mainApp=false使用)

    public Form(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "form/" + getRenderer() + "form-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "form/" + getRenderer() + "form";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (jboname != null) {
            addParameter("jboname", findString(jboname));
        }
        if (apprestrictions != null) {
            addParameter("apprestrictions", findString(apprestrictions));
        }
        addParameter("jbo", jbo);
        addParameter("fromid", fromid);
        addParameter("uid", uid);
        addParameter("fromApp", fromApp);
        addParameter("fromAppType", fromAppType);
        addParameter("fromJboname", fromJboname);
        addParameter("fromUid", fromUid);
        addParameter("instanceid", instanceid);
        addParameter("type", StrUtil.isNull(type) ? "edit" : findString(type).toLowerCase());
        if (null != relationship) {
            addParameter("relationship", relationship);
        }
        if (instancestatus != null) {
            addParameter("instancestatus", findString(instancestatus));
        }
    }

    @Override
    public boolean usesBody() {
        return true;
    }

    @StrutsTagAttribute(description = "set Jbo Name", type = "String")
    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public void setJbo(JboIFace jbo) {
        this.jbo = jbo;
    }

    @StrutsTagAttribute(description = "setFromid", type = "String")
    public void setFromid(String fromid) {
        this.fromid = fromid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setFromApp(String fromApp) {
        this.fromApp = fromApp;
    }

    public void setFromAppType(String fromAppType) {
        this.fromAppType = fromAppType;
    }

    public void setFromJboname(String fromJboname) {
        this.fromJboname = fromJboname;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public void setApprestrictions(String apprestrictions) {
        this.apprestrictions = apprestrictions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public void setInstanceid(String instanceid) {
        this.instanceid = instanceid;
    }

    public void setInstancestatus(String instancestatus) {
        this.instancestatus = instancestatus;
    }

}

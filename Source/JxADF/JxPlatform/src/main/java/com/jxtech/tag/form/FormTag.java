package com.jxtech.tag.form;

import com.jxtech.common.JxLoadResource;
import com.jxtech.jbo.App;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.jbo.util.JboUtil;
import com.jxtech.jbo.util.JxConstant;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.body.BodyTag;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.Tag;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */

public class FormTag extends JxBaseUITag {
    private static final Logger LOG = LoggerFactory.getLogger(FormTag.class);
    private static final long serialVersionUID = 1L;
    protected String jboname;
    protected String apprestrictions;// 限制条件
    protected String fromid;// 从哪里过来的。
    protected String fromApp;// 从哪个APP过来的
    protected String fromAppType;// 从哪个Apptype过来的
    protected String fromJboname;// 从哪个Jboname过来的
    protected String fromUid;//
    protected String flag;// init 初始化页面，add添加记录
    private String uid;
    private String instanceid; // 流程实例ID
    private JboIFace jbo;
    private JboSetIFace jboset;
    protected String type; // 默认值：edit（和后台jbo对象有交互）；view（和jbo没有关联，仅仅用来显示）
    protected String relationship; // 子表扩展界面需要使用此属性
    private HttpServletRequest req;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        uid = request.getParameter("uid");
        fromUid = request.getParameter("fromUid");
        fromid = request.getParameter("fromid");
        fromApp = request.getParameter("fromapp");
        fromAppType = request.getParameter("fromapptype");
        fromJboname = request.getParameter("fromjboname");
        flag = request.getParameter("flag");
        super.initPropertiesValue(false);
        req = request;
        return new Form(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        Form form = (Form) component;
        form.setJboname(jboname);
        if (!StrUtil.isNull(jboname)) {
            JxUserInfo userInfo = JxSession.getJxUserInfo();
            String langCode = "";
            if (null != userInfo) {
                langCode = userInfo.getLangcode();
            }
            JxLoadResource.loadForm(req, langCode);
        }
        form.setUid(uid);
        form.setFromApp(fromApp);
        form.setFromAppType(fromAppType);
        form.setFromJboname(fromJboname);
        form.setFromUid(fromUid);
        form.setApprestrictions(apprestrictions);
        form.setRelationship(relationship);
        Tag tag = findAncestorWithClass(this, BodyTag.class);
        App myapp = null;
        String appName = null;
        String appType = null;
        try {
            // 当有relationship的时候，应该获取parent为mainapp,此app应该设置为mainApp=false
            if (tag != null && (tag instanceof BodyTag)) {
                BodyTag body = (BodyTag) tag;
                appName = body.getAppName();
                appType = body.getAppType();
                myapp = JxSession.getApp(appName, appType);
            }
            if (myapp == null) {
                myapp = JxSession.getApp();
            }
            if (myapp != null) {

                if (null != relationship) {
                    jboset = JxSession.getMainApp().getJbo().getRelationJboSet(relationship);
                } else {
                    jboset = myapp.findJboSet(jboname, null, JxConstant.READ_CACHE);
                }

                if (!"view".equalsIgnoreCase(type)) {
                    myapp.setJboset(jboset);
                }
                LOG.debug("jboname=" + jboname + ",appname.apptype=" + appName + "." + appType + ",myapp.getAppNameType()=" + myapp.getAppNameType());
            } else {
                LOG.debug("找不到APP，appName=" + appName + ",appType=" + appType);
            }
            if (!StrUtil.isNull(jboname) && jboset == null) {
                jboset = JboUtil.getJboSet(jboname);
                LOG.debug("新开了一个Jboset=" + jboname);
                if (myapp != null && !"view".equalsIgnoreCase(type)) {
                    myapp.setJboset(jboset);
                }
            }
            if (jboset != null) {
                if (!StrUtil.isNull(appName)) {
                    jboset.setAppname(appName);
                }
                if ("add".equalsIgnoreCase(flag)) {
                    if (jboset.getJbolist() != null) {
                        jboset.getJbolist().clear();
                    }
                    jbo = jboset.add();
                    // begin 由于修改了add方法，在add事，先判断是否可以添加，没有实例化jbo，所以无法获取uid by jww 2014-03-11 10:36
                    form.setUid(jbo.getUidValue());
                    // end by jww 2014-03-11 10:36
                } else if (!StrUtil.isNull(uid)) {
                    // 加载原来的记录
                    if (jboset.getJbolist() != null) {
                        jboset.getJbolist().clear();
                    }
                    jbo = jboset.queryJbo(uid);
                    if (null != jbo) {
                        instanceid = jbo.getString("WFT_INSTANCEID");
                        form.setInstanceid(instanceid);
                    }
                } else if (!StrUtil.isNull(apprestrictions)) {
                    // 根据查询结果集得到第一条记录
                    jboset.getQueryInfo().setWhereCause(apprestrictions);
                    jboset.query();
                    jbo = jboset.getJbo();
                } else {
                    jbo = jboset.getJbo();
                }

                if (null != jbo && !StrUtil.isNull(flag)) {
                    if ("readonly".equalsIgnoreCase(flag)) {
                        jbo.setReadonly(true);
                    }

                }

                form.setJbo(jbo);
            }
        } catch (JxException e) {
            LOG.error(e.getMessage());
        }
        form.setFromid(fromid);
        form.setType(StrUtil.isNull(type) ? "edit" : type.toLowerCase());
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public JboIFace getJbo() {
        return jbo;
    }

    public void setJbo(JboIFace jbo) {
        this.jbo = jbo;
    }

    public String getJboname() {
        return jboname;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }

    public JboSetIFace getJboset() {
        return jboset;
    }

    public void setJboset(JboSetIFace jboset) {
        this.jboset = jboset;
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
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

    public String getFromUid() {
        return fromUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public String getApprestrictions() {
        return apprestrictions;
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

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}

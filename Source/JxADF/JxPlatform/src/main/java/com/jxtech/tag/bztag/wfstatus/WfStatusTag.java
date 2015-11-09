package com.jxtech.tag.bztag.wfstatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.jbo.JboIFace;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.tag.form.FormTag;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 工作流状态显示
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.09
 * 
 */
public class WfStatusTag extends JxBaseUITag {

    private static final long serialVersionUID = -7635373123146489854L;
    private static Logger LOG = LoggerFactory.getLogger(WfStatusTag.class);

    protected String dataattribute;// 属性名
    protected String mystyle;// 样式

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super.initPropertiesValue(false);
        return new WfStatus(stack, request, response);
    }

    @Override
    protected void populateParams() {
        WfStatus status = (WfStatus) component;
        Tag tag = findAncestorWithClass(getParent(), FormTag.class);
        JboIFace jbo;
        if (tag != null) {
            FormTag form = (FormTag) tag;
            jbo = form.getJbo();
            if (jbo != null) {
                status.setJbo(jbo);
                try {
                    status.setApp(jbo.getJboSet().getAppname());// 应用程序
                    status.setOwnerid(jbo.getUidValue());// uid
                    String insid = jbo.getWorkflowInstanceId();
                    if (StrUtil.isNull(insid)) {
                        insid = jbo.getWorkflowId();
                    }
                    status.setInstanceid(insid);// instanceid
                    status.setBpmengine(jbo.getJboSet().getWorkflowEngine());// bpm type
                    String sv = jbo.getString(JboIFace.WF_STATUS_COLUMN);
                    status.setStatusValue(sv);
                    String sd = jbo.getString(dataattribute);
                    if (StrUtil.isNull(sd)) {
                        sd = sv;
                    }
                    status.setStatusDesc(sd);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        status.setLabel(label);
        status.setMystyle(mystyle);
    }

    public String getDataattribute() {
        return dataattribute;
    }

    public void setDataattribute(String dataattribute) {
        this.dataattribute = dataattribute;
    }

    public String getMystyle() {
        return mystyle;
    }

    public void setMystyle(String mystyle) {
        this.mystyle = mystyle;
    }
}

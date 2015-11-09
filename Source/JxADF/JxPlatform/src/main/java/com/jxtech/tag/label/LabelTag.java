package com.jxtech.tag.label;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.tag.form.FormTag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.Tag;

public class LabelTag extends JxBaseUITag {

    private static final long serialVersionUID = -5562491446735195983L;
    private static final Logger LOG = LoggerFactory.getLogger(LabelTag.class);
    protected String type;
    protected JboIFace jbo;
    protected String dataattribute;
    private Object dataValue;
    protected String labeltip;//标签提示
    protected String valuetip;//值的提示

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super.initPropertiesValue(false);
        return new Label(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        Label lab = (Label) component;
        lab.setType(type);
        lab.setValue(value);
        lab.setDataattribute(dataattribute);
        lab.setLabeltip(labeltip);
        lab.setValuetip(valuetip);
        dataValue = null;
        Tag tag = findAncestorWithClass(this, FormTag.class);
        try {
            if (tag != null && dataattribute != null) {
                FormTag ft = ((FormTag) tag);
                jbo = ft.getJbo();
                if (jbo != null) {
                    lab.setJbo(jbo);
                    dataValue = jbo.getString(dataattribute);
                    lab.setDataValue(dataValue);
                }
            }
        } catch (JxException e) {
            LOG.error(e.getMessage(),e);
        }

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JboIFace getJbo() {
        return jbo;
    }

    public void setJbo(JboIFace jbo) {
        this.jbo = jbo;
    }

    public String getDataattribute() {
        return dataattribute;
    }

    public void setDataattribute(String dataattribute) {
        this.dataattribute = dataattribute;
    }

    public Object getDataValue() {
        return dataValue;
    }

    public void setDataValue(Object dataValue) {
        this.dataValue = dataValue;
    }

    public String getLabeltip() {
        return labeltip;
    }

    public void setLabeltip(String labeltip) {
        this.labeltip = labeltip;
    }

    public String getValuetip() {
        return valuetip;
    }

    public void setValuetip(String valuetip) {
        this.valuetip = valuetip;
    }

}

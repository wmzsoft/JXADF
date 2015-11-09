package com.jxtech.tag.label;

import com.jxtech.jbo.JboIFace;
import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
@StrutsTag(name = "label", tldTagClass = "com.jxtech.tag.label.LabelTag", description = "Label")
public class Label extends JxBaseUIBean {

    protected String type;
    protected String dataattribute;
    private Object dataValue;
    private JboIFace jbo;
    protected String labeltip;// 标签提示
    protected String valuetip;// 值的提示

    public Label(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "label/label-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "label/label";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (type != null) {
            addParameter("type", findString(type).toUpperCase());
        }
        if (dataattribute != null) {
            addParameter("dataattribute", findString(dataattribute).toUpperCase());
        }
        if (labeltip != null) {
            addParameter("labeltip", findString(labeltip));
        }
        if (valuetip != null) {
            addParameter("valuetip", findString(valuetip));
        }
        if (jbo != null) {
            addParameter("jbo", jbo);
        }
        addParameter("dataValue", dataValue);
        addParameter("value", getI18NValue(value));
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDataattribute(String dataattribute) {
        this.dataattribute = dataattribute;
    }

    public void setDataValue(Object dataValue) {
        this.dataValue = dataValue;
    }

    public void setJbo(JboIFace jbo) {
        this.jbo = jbo;
    }

    public void setLabeltip(String labeltip) {
        this.labeltip = labeltip;
    }

    public void setValuetip(String valuetip) {
        this.valuetip = valuetip;
    }

}

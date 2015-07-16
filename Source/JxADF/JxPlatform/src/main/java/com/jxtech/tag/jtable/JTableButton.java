package com.jxtech.tag.jtable;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
@StrutsTag(name = "jtablebutton", tldTagClass = "com.jxtech.tag.jtable.JTableButtonTag", description = "JTableButton")
public class JTableButton extends JxBaseUIBean {
    private String label;
    private String mxevent;
    private String icon;

    public JTableButton(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "jtable/jtablebutton-close.ftl";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "jtable/jtablebutton.ftl";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (label != null) {
            addParameter("label", findString(label));
        }
        if (mxevent != null) {
            addParameter("mxevent", findString(mxevent));
        }
        if (icon != null) {
            addParameter("icon", findString(icon));
        }
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setMxevent(String mxevent) {
        this.mxevent = mxevent;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}

package com.jxtech.tag.jtable;

import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

public class JTableButtonTag extends JxBaseUITag {
    private static final long serialVersionUID = -7342514210984146779L;
    private String label;
    private String mxevent;
    private String icon;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new JTableButton(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        JTableButton button = (JTableButton) component;
        button.setLabel(label);
        button.setMxevent(mxevent);
        button.setIcon(icon);
    }

    @Override
    public int doStartTag() throws JspException {
        int dst = super.doStartTag();
        Tag tag = getParent();
        if (tag instanceof JTableTag) {
            JTableTag table = (JTableTag) tag;
            table.getButtons().add((JTableButton) component);
        }
        return dst;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMxevent() {
        return mxevent;
    }

    public void setMxevent(String mxevent) {
        this.mxevent = mxevent;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}

package com.jxtech.tag.layout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.views.annotations.StrutsTag;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.07
 * 
 */
@StrutsTag(name = "LayoutCol", tldTagClass = "com.jxtech.tag.layout.LayoutColTag", description = "Layout Column")
public class LayoutCol extends JxBaseUIBean {

    private String flex;

    public LayoutCol(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "layout/" + getRenderer() + "layout-col-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "layout/" + getRenderer() + "layout-col";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        addParameter("flex", findString(flex));
    }

    public void setFlex(String flex) {
        this.flex = flex;
    }
}

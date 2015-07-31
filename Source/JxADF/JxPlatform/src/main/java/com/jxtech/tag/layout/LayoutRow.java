package com.jxtech.tag.layout;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.07
 * 
 */

@StrutsTag(name = "LayoutPanel", tldTagClass = "com.jxtech.tag.layout.LayoutPanelTag", description = "Layout Row")
public class LayoutRow extends JxBaseUIBean {

    public LayoutRow(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "layout/" + getRenderer() + "layout-row-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "layout/" + getRenderer() + "layout-row";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
    }
}

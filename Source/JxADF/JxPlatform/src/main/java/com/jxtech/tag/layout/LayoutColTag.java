package com.jxtech.tag.layout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts2.components.Component;

import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.07
 * 
 */
public class LayoutColTag extends JxBaseUITag {

    private static final long serialVersionUID = -7344873216448594532L;

    private String flex;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new LayoutCol(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        LayoutCol layoutCol = (LayoutCol) component;
        layoutCol.setFlex(flex);
    }

    @Override
    public int doStartTag() throws JspException {
        Tag tag = this.getParent();
        if (tag instanceof LayoutTag) {
            LayoutTag p = (LayoutTag) tag;
            p.getCols().add((LayoutCol) component);
        }
        return super.doStartTag();
    }

    public void setFlex(String flex){ this.flex = flex;}

}

package com.jxtech.tag.layout;

import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.07
 * 
 */
public class LayoutRowTag extends JxBaseUITag {

    private static final long serialVersionUID = -1714196880336288456L;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new LayoutRow(stack, request, response);
    }

    @Override
    protected void populateParams() {
        // TODO Auto-generated method stub
        super.populateParams();
    }

    @Override
    public int doStartTag() throws JspException {
        Tag tag = getParent();
        if (tag instanceof LayoutTag) {
            LayoutTag p = (LayoutTag) tag;
            p.getRows().add((LayoutRow) component);
        }
        return super.doStartTag();
    }
}

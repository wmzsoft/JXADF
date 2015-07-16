package com.jxtech.tag.section;

import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class SectioncolTag extends JxBaseUITag {

    private static final long serialVersionUID = 1L;
    protected String type;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super.initPropertiesValue(false);
        return new Sectioncol(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        Sectioncol sectioncol = (Sectioncol) component;
        sectioncol.setType(type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

package com.jxtech.tag.section;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
@StrutsTag(name = "sectioncol", tldTagClass = "com.jxtech.tag.section.SectioncolTag", description = "section col")
public class Sectioncol extends JxBaseUIBean {
    protected String type;

    public Sectioncol(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "section/" + getRenderer() + "sectioncol-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "section/" + getRenderer() + "sectioncol";
    }

    @Override
    public boolean usesBody() {
        return true;
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (type != null) {
            addParameter("type", findString(type).toUpperCase());
        }
    }

    public void setType(String type) {
        this.type = type;
    }

}

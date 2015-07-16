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
@StrutsTag(name = "section", tldTagClass = "com.jxtech.tag.section.SectionTag", description = "section")
public class Section extends JxBaseUIBean {

    protected String type;

    public Section(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "section/" + getRenderer() + "section-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "section/" + getRenderer() + "section";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (type != null) {
            addParameter("type", findString(type).toUpperCase());
        }
    }

    @Override
    public boolean usesBody() {
        return true;
    }

    public void setType(String type) {
        this.type = type;
    }

}

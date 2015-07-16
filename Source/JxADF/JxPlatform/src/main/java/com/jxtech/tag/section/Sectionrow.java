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
@StrutsTag(name = "sectionrow", tldTagClass = "com.jxtech.tag.section.SectionrowTag", description = "Section Row")
public class Sectionrow extends JxBaseUIBean {
    protected String type;

    public Sectionrow(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "section/" + getRenderer() + "sectionrow-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "section/" + getRenderer() + "sectionrow";
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

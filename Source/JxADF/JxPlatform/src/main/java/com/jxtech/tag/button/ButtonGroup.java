package com.jxtech.tag.button;

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
@StrutsTag(name = "buttongroup", tldTagClass = "com.jxtech.tag.button.ButtonGroupTag", description = "ButtonGroupTag")
public class ButtonGroup extends JxBaseUIBean {
    private String type;// 按钮

    public ButtonGroup(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return super.getStdDefaultTemplate("buttongroup", true);
    }

    @Override
    public String getDefaultOpenTemplate() {
        return super.getStdDefaultOpenTemplate("buttongroup", true);
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (type != null) {
            addParameter("type", findString(type));
        }
    }

    public void setType(String type) {
        this.type = type;
    }

}

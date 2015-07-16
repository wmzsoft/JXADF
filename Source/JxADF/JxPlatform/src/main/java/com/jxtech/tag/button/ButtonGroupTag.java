package com.jxtech.tag.button;

import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ButtonGroupTag extends JxBaseUITag {

    private static final long serialVersionUID = 7605821610136567006L;
    protected String type;// 按钮分组类型，MENU_BAR

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new ButtonGroup(stack, request, response);
    }

    @Override
    protected void populateParams() {
        if (!super.isRenderer("buttongroup")) {
            return;
        }
        super.populateParams();
        ButtonGroup bg = (ButtonGroup) component;
        bg.setType(type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

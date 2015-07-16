package com.jxtech.tag.tabs;

import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public class TabUlTag extends JxBaseUITag {
    private static final long serialVersionUID = 711517315264513803L;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new TabUl(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
    }

}

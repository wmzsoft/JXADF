package com.jxtech.tag.tabs;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 *
 */
public class Tabs extends JxBaseUIBean {

    public Tabs(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "tabs/tabs-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "tabs/tabs";
    }

}

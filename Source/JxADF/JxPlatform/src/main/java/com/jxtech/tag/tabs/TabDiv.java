package com.jxtech.tag.tabs;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public class TabDiv extends JxBaseUIBean {

    private String active;

    public TabDiv(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return StrUtil.contact("tabs/", getRenderer(), "tabs-div-close");
    }

    @Override
    public String getDefaultOpenTemplate() {
        return StrUtil.contact("tabs/", getRenderer(), "tabs-div");
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        addParameter("active", active);
    }

    public void setActive(String active) {
        this.active = active;
    }

}

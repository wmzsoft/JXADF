package com.jxtech.tag.tabs;

import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmai.com
 * @date 2014.12
 */
public class TabDivTag extends JxBaseUITag {

    /**
     * 
     */
    private static final long serialVersionUID = -4164897831321557688L;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new TabDiv(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
    }

}

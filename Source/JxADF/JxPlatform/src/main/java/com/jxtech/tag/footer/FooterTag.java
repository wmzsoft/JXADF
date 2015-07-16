package com.jxtech.tag.footer;

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

public class FooterTag extends JxBaseUITag {

    private static final long serialVersionUID = 1L;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super.initPropertiesValue(false);
        return new Footer(stack, request, response);
    }

}

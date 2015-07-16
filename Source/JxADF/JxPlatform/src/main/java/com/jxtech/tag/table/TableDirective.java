package com.jxtech.tag.table;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.velocity.components.AbstractDirective;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TableDirective extends AbstractDirective {

    @Override
    public String getBeanName() {
        return "table";
    }

    @Override
    protected Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Table(stack, req, res);
    }

}

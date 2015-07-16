package com.jxtech.tag.table;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.velocity.components.AbstractDirective;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TablecolDirective extends AbstractDirective {

    @Override
    protected Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Tablecol(stack, req, res);
    }

    @Override
    public String getBeanName() {
        return "tablecol";
    }

}

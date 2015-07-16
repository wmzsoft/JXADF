package com.jxtech.tag.table;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.freemarker.tags.TagModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.07
 */
public class TablecolModel extends TagModel {

    public TablecolModel(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        super(stack, req, res);
    }

    @Override
    protected Component getBean() {
        return new Tablecol(stack, req, res);
    }

}

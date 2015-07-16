package com.jxtech.tag.tab;

import com.jxtech.common.JxLoadResource;
import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cxm
 * @date 2014.03.21
 */
public class TabGroupTag extends JxBaseUITag {

    private static final long serialVersionUID = -2763800518070390708L;
    protected String id;
    protected List<Tab> tabs = new ArrayList<Tab>();

    private HttpServletRequest req;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        req = request;
        return new TabGroup(stack, request, response);
    }

    @Override
    public int doStartTag() throws JspException {
        if (tabs != null) tabs.clear();
        return super.doStartTag();
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        TabGroup tg = (TabGroup) component;
        tg.setId(id);
        tg.setTabs(tabs);

        JxLoadResource.loadTable(req);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public List<Tab> getTabs() {
        return tabs;
    }

    public void setTabs(List<Tab> tabs) {
        this.tabs = tabs;
    }

}

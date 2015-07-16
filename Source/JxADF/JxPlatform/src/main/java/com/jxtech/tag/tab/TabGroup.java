package com.jxtech.tag.tab;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 
 * @author cxm
 * @date 2014.03.21
 * 
 */
@StrutsTag(name = "tabgroup", tldTagClass = "com.jxtech.tag.button.TabGroupTag", description = "TabGroupTag")
public class TabGroup extends JxBaseUIBean {
    protected String id;
    protected String page;

    private List<Tab> tabs;

    public TabGroup(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "tab/tabgroup-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "tab/tabgroup";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (null != id) {
            addParameter("id", findString(id));
        }
        if (null != page) {
            addParameter("page", findString(page));
        }
        if (null != tabs) {
            addParameter("tabs", tabs);
        }
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setTabs(List<Tab> tabs) {
        this.tabs = tabs;
    }

}

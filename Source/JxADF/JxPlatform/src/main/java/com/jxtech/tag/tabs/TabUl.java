package com.jxtech.tag.tabs;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public class TabUl extends JxBaseUIBean {

    private Tabs tabs;
    private List<TabLi> tabli = new ArrayList<TabLi>();

    public TabUl(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return StrUtil.contact("tabs/", getRenderer(), "tabs-ul-close");
    }

    @Override
    public String getDefaultOpenTemplate() {
        return StrUtil.contact("tabs/", getRenderer(), "tabs-ul");
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (tabs != null) {
            this.addParameter("tabs", tabs);
            addParameter("tabsid", tabs.getId());
        }
        addParameter("tabli", tabli);
    }

    public void setTabs(Tabs tabs) {
        this.tabs = tabs;
    }

    public List<TabLi> getTabli() {
        return tabli;
    }

    public void setTabli(List<TabLi> tabli) {
        this.tabli = tabli;
    }

}

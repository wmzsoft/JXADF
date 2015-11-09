package com.jxtech.tag.tabs;

import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public class TabsTag extends JxBaseUITag {
    private static final long serialVersionUID = -384752584495910818L;
    protected String beforeLoadEvent;// 事件
    protected String tabCreateEvent;//创建Tab的事件
    protected String activateEvent;
    protected String beforeActivateEvent;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new Tabs(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        Tabs tabs = (Tabs) this.component;
        tabs.setBeforeLoadEvent(beforeLoadEvent);
    }

    public String getBeforeLoadEvent() {
        return beforeLoadEvent;
    }

    public void setBeforeLoadEvent(String beforeLoadEvent) {
        this.beforeLoadEvent = beforeLoadEvent;
    }

    public String getTabCreateEvent() {
        return tabCreateEvent;
    }

    public void setTabCreateEvent(String tabCreateEvent) {
        this.tabCreateEvent = tabCreateEvent;
    }

    public String getActivateEvent() {
        return activateEvent;
    }

    public void setActivateEvent(String activateEvent) {
        this.activateEvent = activateEvent;
    }

    public String getBeforeActivateEvent() {
        return beforeActivateEvent;
    }

    public void setBeforeActivateEvent(String beforeActivateEvent) {
        this.beforeActivateEvent = beforeActivateEvent;
    }

}

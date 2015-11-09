package com.jxtech.tag.tabs;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public class Tabs extends JxBaseUIBean {
    protected String beforeLoadEvent;// 事件
    protected String tabCreateEvent;//创建Tab的事件
    protected String activateEvent;
    protected String beforeActivateEvent;

    public Tabs(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "tabs/tabs-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "tabs/tabs";
    }

    public void setBeforeLoadEvent(String beforeLoadEvent) {
        this.beforeLoadEvent = beforeLoadEvent;
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (beforeLoadEvent != null) {
            this.addParameter("beforeLoadEvent", findString(beforeLoadEvent));
        }
        if (tabCreateEvent != null) {
            this.addParameter("tabCreateEvent", findString(tabCreateEvent));
        }
        if (activateEvent != null) {
            this.addParameter("activateEvent", findString(activateEvent));
        }
        if (beforeActivateEvent != null) {
            this.addParameter("beforeActivateEvent", findString(beforeActivateEvent));
        }
    }

    public void setTabCreateEvent(String tabCreateEvent) {
        this.tabCreateEvent = tabCreateEvent;
    }

    public void setActivateEvent(String activateEvent) {
        this.activateEvent = activateEvent;
    }

    public void setBeforeActivateEvent(String beforeActivateEvent) {
        this.beforeActivateEvent = beforeActivateEvent;
    }

}

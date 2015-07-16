package com.jxtech.tag.fragment;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 页面片段，专用于包含页面的处理
 *
 * @author wmzsoft@gmail.com
 * @date 2013.7
 */

@StrutsTag(name = "Fragment", tldTagClass = "com.jxtech.tag.fragment.FragmentTag", description = "Fragment")
public class Fragment extends JxBaseUIBean {

    protected String url;
    protected String app;
    protected String type;

    protected String lazyload;
    protected String displayMode;//子元素list-table的显示模式

    public Fragment(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "fragment/" + getRenderer() + "fragment";
    }

    @Override
    protected String getDefaultTemplate() {
        return "fragment/" + getRenderer() + "fragment-close";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (url != null) {
            addParameter("url", findString(url));
        } else {
            addParameter("url", "app.action");
        }
        if (app != null) {
            addParameter("app", findString(app));
        }
        if (type != null) {
            addParameter("type", findString(type));
        }

        if (displayMode != null){
            addParameter("displayMode",findString(displayMode));
        }

        addParameter("lazyload", findString(lazyload).toUpperCase());
    }

    @StrutsTagAttribute(description = "setUrl", defaultValue = "app.action", type = "String")
    public void setUrl(String url) {
        this.url = url;
    }

    @StrutsTagAttribute(description = "setApp", defaultValue = "", type = "String")
    public void setApp(String app) {
        this.app = app;
    }

    @StrutsTagAttribute(description = "setType", defaultValue = "", type = "String")
    public void setType(String type) {
        this.type = type;
    }

    @StrutsTagAttribute(description = "setLazyload", defaultValue = "FALSE", type = "String")
    public void setLazyload(String lazyload) {
        this.lazyload = lazyload;
    }

    @StrutsTagAttribute(description = "setDisplayMode", defaultValue = "wrap", type = "String")
    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
    }
}

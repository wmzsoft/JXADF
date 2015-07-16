package com.jxtech.tag.head;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
@StrutsTag(name = "head", tldTagClass = "com.jxtech.tag.head.HeadTag", description = "Head")
public class Head extends JxBaseUIBean {

    protected String skin;
    protected String headtype;// 头类型，TOP：最外层的菜单框

    public Head(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "head/" + getRenderer() + "head-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "head/" + getRenderer() + "head";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (skin != null) {
            this.addParameter("skin", findString(skin));
        }
        if (headtype != null) {
            addParameter("headtype", findString(headtype).toUpperCase());
        }
        addParameter("title", getI18NValue(title));
    }

    @Override
    public boolean usesBody() {
        return true;
    }

    @StrutsTagAttribute(description = "setSkin", type = "String")
    public void setSkin(String skin) {
        this.skin = skin;
    }

    public void setHeadtype(String headtype) {
        this.headtype = headtype;
    }

}

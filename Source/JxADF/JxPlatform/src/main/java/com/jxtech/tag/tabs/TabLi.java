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
public class TabLi extends JxBaseUIBean {
    private String url;
    private String title;

    public TabLi(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "tabs/tabs-li-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "tabs/tabs-li";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (url != null) {
            addParameter("url", findString(url));
        }
        if (title != null) {
            addParameter("title", findString(title));
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

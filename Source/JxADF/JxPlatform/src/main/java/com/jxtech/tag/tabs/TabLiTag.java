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
public class TabLiTag extends JxBaseUITag {

    private static final long serialVersionUID = -8904343415252917354L;
    private String url;
    private String title;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new TabLi(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        TabLi li = (TabLi) component;
        li.setUrl(url);
        li.setTitle(title);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

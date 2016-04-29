package com.jxtech.tag.tabs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts2.components.Component;

import com.jxtech.jbo.auth.JxSession;
import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;

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

    @Override
    public int doStartTag() throws JspException {
        int i = super.doStartTag();
        if (!JxSession.isMobile()) {
            return i;
        }
        Tag parent = getParent();
        if (parent instanceof TabUlTag) {
            TabUlTag tabul = (TabUlTag) parent;
            tabul.getTabli().add(this);
            // 设定第一个tab为活动的Tab
            if (tabul.getTabli().size() == 1) {
                Tag tag = tabul.getParent();
                if (tag instanceof TabsTag) {
                    ((TabsTag) tag).setActivetab(this.url);
                }
            }
            TabUl ul = (TabUl) tabul.getComponent();
            ul.getTabli().add((TabLi) component);
        }
        return i;
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

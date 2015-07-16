package com.jxtech.tag.tab;

import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * @author cxm
 * @date 2014.03.21
 */
public class TabTag extends JxBaseUITag {
    private static final long serialVersionUID = -2375902000071350324L;

    protected String page; // 内容
    protected String refreshonclick;// 点击tab重新加载

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new Tab(stack, request, response);
    }

    @Override
    public int doStartTag() throws JspException {
        super.doStartTag();
        Tag parent = getParent();
        if (parent instanceof TabGroupTag) {
            Tab tab = (Tab) component;
            TabGroupTag tabGroupTag = (TabGroupTag) parent;
            tabGroupTag.getTabs().add(tab);
        }
        return SKIP_BODY;
    }

    @Override
    protected void populateParams() {
        Tab tab = (Tab) component;
        tab.setPage(page);
        tab.setTitle(title);
        tab.setRefreshonclick(refreshonclick);
        super.populateParams();
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRefreshonclick() {
        return refreshonclick;
    }

    public void setRefreshonclick(String refreshonclick) {
        this.refreshonclick = refreshonclick;
    }
}

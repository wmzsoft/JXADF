package com.jxtech.tag.tabs;

import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.Tag;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.12
 * 
 */
public class TabUlTag extends JxBaseUITag {
    private static final long serialVersionUID = 711517315264513803L;

    // 记录子节点信息
    public List<TabLiTag> tabli = new ArrayList<TabLiTag>();

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new TabUl(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        TabUl tul = (TabUl) component;
        Tag parent = this.getParent();
        if (parent instanceof TabsTag) {
            tul.setTabs((Tabs) ((TabsTag) parent).getComponent());
        }
    }

    public List<TabLiTag> getTabli() {
        return tabli;
    }

    public void setTabli(List<TabLiTag> tabli) {
        this.tabli = tabli;
    }

}

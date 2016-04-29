package com.jxtech.tag.tabs;

import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.Tag;

/**
 * 
 * @author wmzsoft@gmai.com
 * @date 2014.12
 */
public class TabDivTag extends JxBaseUITag {

    /**
     * 
     */
    private static final long serialVersionUID = -4164897831321557688L;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new TabDiv(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        Tag tag = getParent();
        if (tag instanceof TabsTag) {
            String activeurl = ((TabsTag) tag).getActivetab();
            if (StrUtil.compareStr(activeurl, "#" + id, "=")) {
                TabDiv td = (TabDiv) component;
                td.setActive("in active");
            }
        }
    }

}

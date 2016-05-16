package com.jxtech.tag.layout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts2.components.Component;

import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.07
 * 
 */
public class LayoutPanelTag extends JxBaseUITag {

    private static final long serialVersionUID = -1714196880336288456L;

    private String region;
    private String size;
    private String minSize;
    private String maxSize;
    private String space;
    private String resizable;
    private String closable;
    private String status;
    private String scrollable;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new LayoutPanel(stack, request, response);
    }

    @Override
    protected void populateParams() {
        // TODO Auto-generated method stub
        super.populateParams();

        LayoutPanel layoutPanel = (LayoutPanel) component;
        layoutPanel.setRegion(region);
        layoutPanel.setSize(size);
        layoutPanel.setMinSize(minSize);
        layoutPanel.setMaxSize(maxSize);
        layoutPanel.setClosable(closable);
        layoutPanel.setSpace(space);
        layoutPanel.setResizable(resizable);
        layoutPanel.setStatus(status);
        layoutPanel.setScrollable(scrollable);
    }

    @Override
    public int doStartTag() throws JspException {
        Tag tag = getParent();
        if (tag instanceof LayoutTag) {
            LayoutTag p = (LayoutTag) tag;
            p.getPanels().add((LayoutPanel) component);
        }
        return super.doStartTag();
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setMinSize(String minSize) {
        this.minSize = minSize;
    }

    public void setMaxSize(String maxSize) {
        this.maxSize = maxSize;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public void setClosable(String closable) {
        this.closable = closable;
    }

    public void setResizable(String resizable) {
        this.resizable = resizable;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setScrollable(String scrollable) {
        this.scrollable = scrollable;
    }
}

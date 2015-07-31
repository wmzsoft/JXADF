package com.jxtech.tag.layout;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts2.components.Component;

import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.07
 * 
 */
public class LayoutTag extends JxBaseUITag {

    private static final long serialVersionUID = -1666667794205059083L;
    // 面板
    private List<LayoutPanel> panels = new ArrayList<LayoutPanel>();
    private List<LayoutRow> rows = new ArrayList<LayoutRow>();
    private List<LayoutCol> cols = new ArrayList<LayoutCol>();
    //size
    private String minWidth;
    private String minHeight;
    private String width;
    private String height;
    private String maxWidth;
    private String maxHeight;
    private String enableLayout;
    private String storable;
    private String displayMode;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new Layout(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        Layout layout = (Layout) component;
        layout.setPanels(panels);
        layout.setMinWidth(minWidth);
        layout.setMinHeight(minHeight);
        layout.setWidth(width);
        layout.setHeight(height);
        layout.setMaxWidth(maxWidth);
        layout.setMaxHeight(maxHeight);
        layout.setEnableLayout(enableLayout);
        layout.setStorable(storable);
        layout.setDisplayMode(displayMode);
    }

    @Override
    public int doStartTag() throws JspException {
        panels.clear();
        rows.clear();
        cols.clear();
        return super.doStartTag();
    }

    public List<LayoutPanel> getPanels() {
        return panels;
    }

    public List<LayoutRow> getRows(){ return rows;}

    public List<LayoutCol> getCols(){ return cols;}

    public void setPanels(List<LayoutPanel> panels) {
        this.panels = panels;
    }

    public void setMinWidth(String minWidth) { this.minWidth = minWidth;}

    public void setMinHeight(String minHeight) { this.minHeight = minHeight;}

    public void setWidth(String width) { this.width = width;}

    public void setHeight(String height){ this.height = height;}

    public void setMaxWidth(String maxWidth) { this.maxWidth = maxWidth;}

    public void setMaxHeight(String maxHeight) { this.maxHeight = maxHeight;}

    public void setEnableLayout(String enableLayout) { this.enableLayout = enableLayout;}

    public void setStorable(String storable) { this.storable = storable;}

    public void setDisplayMode(String displayMode){ this.displayMode = displayMode;}
}

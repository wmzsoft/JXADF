package com.jxtech.tag.layout;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.views.annotations.StrutsTag;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.07
 * 
 */
@StrutsTag(name = "Layout", tldTagClass = "com.jxtech.tag.layout.LayoutTag", description = "Layout")
public class Layout extends JxBaseUIBean {
    // 面板
    private List<LayoutPanel> panels = new ArrayList<LayoutPanel>();
    private List<LayoutRow> rows = new ArrayList<LayoutRow>();
    private List<LayoutCol> cols = new ArrayList<LayoutCol>();
    // size
    private String minWidth;
    private String minHeight;
    private String width;
    private String height;
    private String maxWidth;
    private String maxHeight;
    private String enableLayout;
    private String storable;
    private String displayMode;

    public Layout(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "layout/" + getRenderer() + "layout-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "layout/" + getRenderer() + "layout";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        addParameter("panels", panels);
        addParameter("panels", rows);
        addParameter("panels" ,cols);
        addParameter("minWidth",findString(minWidth));
        addParameter("minHeight",findString(minHeight));
        addParameter("width",findString(width));
        addParameter("height",findString(height));
        addParameter("maxWidth",findString(maxWidth));
        addParameter("maxHeight",findString(maxHeight));
        addParameter("enableLayout",findString(enableLayout));
        addParameter("storable",findString(storable));
        displayMode = findString(displayMode);
        if("".equals(displayMode)){
            displayMode = "panel";
        }
        addParameter("displayMode",displayMode);
    }


    public void setPanels(List<LayoutPanel> panels) {
        this.panels = panels;
    }

    public void setRows(List<LayoutRow> rows){ this.rows = rows; }

    public void setCols(List<LayoutCol> cols){ this.cols = cols;}

    public void setMinWidth(String minWidth) { this.minWidth = minWidth;}

    public void setMinHeight(String minHeight) { this.minHeight = minHeight;}

    public void setWidth(String width ){ this.width = width; }

    public void setHeight(String height){ this.height = height; }

    public void setMaxWidth (String maxWidth){ this.maxWidth = maxWidth;}

    public void setMaxHeight (String maxHeight ) { this.maxHeight = maxHeight;}

    public void setEnableLayout(String enableLayout){ this.enableLayout = enableLayout;}

    public void setStorable(String storable) { this.storable = storable;}

    public void setDisplayMode(String displayMode){ this.displayMode = displayMode;}
}

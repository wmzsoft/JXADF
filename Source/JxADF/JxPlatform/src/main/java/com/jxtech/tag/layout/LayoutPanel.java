package com.jxtech.tag.layout;

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

@StrutsTag(name = "LayoutPanel", tldTagClass = "com.jxtech.tag.layout.LayoutPanelTag", description = "Layout Row")
public class LayoutPanel extends JxBaseUIBean {

    private String region;
    private String size;
    private String minSize;
    private String maxSize;
    private String space;
    private String resizable;
    private String closable;
    private String status;
    private String scrollable;

    public LayoutPanel(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "layout/" + getRenderer() + "layout-panel-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "layout/" + getRenderer() + "layout-panel";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        region = findString(region);
        if ("".equals(region)) {
            region = "center";
        }
        addParameter("region", region);
        addParameter("panelSize", findString(size));
        addParameter("minSize", findString(minSize));
        addParameter("maxSize", findString(maxSize));
        space = findString(space);
        if ("".equals(space)) {
            space = "0";
        }
        addParameter("space", findString(space));

        closable = findString(closable);
        if ("".equals(closable)) {
            closable = "false";
        }
        addParameter("closable", closable);

        resizable = findString(resizable);
        if ("".equals(resizable)) {
            resizable = "false";
        }
        addParameter("resizable", resizable);

        status = findString(status);
        if ("".equals(status)) {
            status = "open";
        }
        addParameter("status", status);

        scrollable = findString(scrollable);
        if ("".equals(scrollable) || "true".equals(scrollable)) {
            scrollable = "scrollable";
        } else {
            scrollable = "unScrollable";
        }
        addParameter("scrollable", scrollable);
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
        this.status = scrollable;
    }
}

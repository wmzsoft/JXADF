package com.jxtech.tag.comm;

import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractClosingTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.07
 * 
 */
public class JxBaseUITag extends AbstractClosingTag {
    protected String readonly;
    protected String visible;
    protected String requiredposition;
    protected String required;
    private static final long serialVersionUID = 1L;
    private String renderer;// 默认为HTML，可为MOBILE/
    private String rendererTag;// 渲染指定的Tag标签,其它的全部不渲染

    public void initPropertiesValue(boolean all) {
        if (all) {
            readonly = null;
            visible = null;
            cssClass = null;
            cssErrorClass = null;
            cssStyle = null;
            cssErrorStyle = null;
            title = null;
            disabled = null;
            label = null;
            labelSeparator = null;
            labelPosition = null;
            requiredposition = null;
            name = null;
            required = null;
            tabindex = null;
            value = null;
            template = null;
            theme = null;
            templateDir = null;
            onclick = null;
            ondblclick = null;
            onmousedown = null;
            onmouseup = null;
            onmouseover = null;
            onmousemove = null;
            onmouseout = null;
            onfocus = null;
            onblur = null;
            onkeypress = null;
            onkeydown = null;
            onkeyup = null;
            onselect = null;
            onchange = null;
            accesskey = null;
            key = null;
            tooltip = null;
            tooltipConfig = null;
            javascriptTooltip = null;
            tooltipDelay = null;
            tooltipCssClass = null;
            tooltipIconPath = null;
        }
    }

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        renderer = request.getParameter("renderer");
        rendererTag = request.getParameter("rendererTag");
        if (StrUtil.isNull(renderer)) {
            renderer = (String) request.getSession().getAttribute("renderer");
        } else {
            request.getSession().setAttribute("renderer", renderer);
        }
        return null;
    }

    /**
     * 是否渲染某个Tag
     * 
     * @param tagname
     * @return
     */
    public boolean isRenderer(String tagname) {
        if (!StrUtil.isNull(rendererTag)) {
            String rt = "," + rendererTag + ",";
            if (rt.indexOf("," + tagname + ",") < 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void populateParams() {
        super.populateParams();
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    public String getRequiredposition() {
        return requiredposition;
    }

    public void setRequiredposition(String requiredposition) {
        this.requiredposition = requiredposition;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public String getRendererTag() {
        return rendererTag;
    }

    public void setRendererTag(String rendererTag) {
        this.rendererTag = rendererTag;
    }

    public String getReadonly() {
        return readonly;
    }

    public String getVisible() {
        return visible;
    }

}

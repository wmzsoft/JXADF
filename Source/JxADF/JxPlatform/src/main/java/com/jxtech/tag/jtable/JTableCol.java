package com.jxtech.tag.jtable;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
@StrutsTag(name = "jtablecol", tldTagClass = "com.jxtech.tag.jtable.JTableColTag", description = "JTableCol")
public class JTableCol extends JxBaseUIBean {
    private String attribute;// 字段名
    private String startHtml;// 开始HTML代码
    private String html;// 结束HTML代码
    private String visibleHead;// 是否显示列头
    private String dataDisplay;// 显示值
    private String label;// 标签
    private String mxevent;// 事件
    private String icon;// 图标
    private String render;// 显示方式：TEXT、HTML LABELS

    public JTableCol(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "jtable/jtablecol-close.ftl";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "jtable/jtablecol.ftl";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (attribute != null) {
            addParameter("attribute", findString(attribute));
        }
        if (startHtml != null) {
            addParameter("startHtml", findString(startHtml));
        }
        if (html != null) {
            addParameter("html", findString(html));
        }
        if (visibleHead != null) {
            addParameter("visibleHead", findValue(visibleHead, Boolean.class));
        }
        if (dataDisplay != null) {
            addParameter("dataDisplay", findString(dataDisplay));
        }
        if (label != null) {
            addParameter("label", findString(label));
        }
        if (mxevent != null) {
            addParameter("mxevent", findString(mxevent));
        }
        if (icon != null) {
            addParameter("icon", findString(icon));
        }
        if (render != null) {
            addParameter("render", findString(render));
        }
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setStartHtml(String startHtml) {
        this.startHtml = startHtml;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public void setVisibleHead(String visibleHead) {
        this.visibleHead = visibleHead;
    }

    public void setDataDisplay(String dataDisplay) {
        this.dataDisplay = dataDisplay;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setMxevent(String mxevent) {
        this.mxevent = mxevent;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setRender(String render) {
        this.render = render;
    }

}

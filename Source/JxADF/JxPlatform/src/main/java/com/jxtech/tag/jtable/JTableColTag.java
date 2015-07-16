package com.jxtech.tag.jtable;

import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2014.11
 * 
 */
public class JTableColTag extends JxBaseUITag {

    private static final long serialVersionUID = 743672725551914145L;
    private String attribute;// 字段名
    private String startHtml;// 开始HTML代码
    private String html;// 结束HTML代码
    private String visibleHead;// 是否显示列头
    private String dataDisplay;// 显示值
    private String label;// 标签
    private String mxevent;// 事件
    private String icon;// 图标
    private String render;// 显示方式：TEXT、HTML LABELS

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new JTableCol(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        JTableCol col = (JTableCol) component;
        col.setAttribute(attribute);
        col.setStartHtml(startHtml);
        col.setHtml(html);
        col.setVisible(visibleHead);
        col.setDataDisplay(dataDisplay);
        col.setLabel(label);
        col.setMxevent(mxevent);
        col.setIcon(icon);
        col.setRender(render);
    }

    @Override
    public int doStartTag() throws JspException {
        int dst = super.doStartTag();
        Tag tag = getParent();
        if (tag instanceof JTableTag) {
            JTableTag table = (JTableTag) tag;
            table.getCols().add((JTableCol) component);
        }
        return dst;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getStartHtml() {
        return startHtml;
    }

    public void setStartHtml(String startHtml) {
        this.startHtml = startHtml;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getVisibleHead() {
        return visibleHead;
    }

    public void setVisibleHead(String visibleHead) {
        this.visibleHead = visibleHead;
    }

    public String getDataDisplay() {
        return dataDisplay;
    }

    public void setDataDisplay(String dataDisplay) {
        this.dataDisplay = dataDisplay;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMxevent() {
        return mxevent;
    }

    public void setMxevent(String mxevent) {
        this.mxevent = mxevent;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRender() {
        return render;
    }

    public void setRender(String render) {
        this.render = render;
    }
}

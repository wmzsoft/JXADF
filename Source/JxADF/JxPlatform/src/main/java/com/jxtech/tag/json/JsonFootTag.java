package com.jxtech.tag.json;

import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.tag.table.TableTag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.03
 * 
 */

public class JsonFootTag extends JxBaseUITag {

    private static final long serialVersionUID = -3542610026800114447L;
    private String attributeLabel;// 属性标签
    private String attribute;// 属性
    private String fixText;// 扩展字符串

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new JsonFoot(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        JsonFoot foot = (JsonFoot) component;
        foot.setAttribute(attribute);
        foot.setAttributeLabel(attributeLabel);
        foot.setFixText(fixText);
    }

    @Override
    public int doEndTag() throws JspException {
        Tag tag = getParent();
        if (tag != null) {
            JsonFoot foot = (JsonFoot) component;
            if (tag instanceof JsonTag) {
                ((JsonTag) tag).getJsonFoot().add(foot);
            } else if (tag instanceof TableTag) {
                ((TableTag) tag).getJsonFoot().add(foot);
            }
        }
        return super.doEndTag();
    }

    public String getAttributeLabel() {
        return attributeLabel;
    }

    public void setAttributeLabel(String attributeLabel) {
        this.attributeLabel = attributeLabel;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getFixText() {
        return fixText;
    }

    public void setFixText(String fixText) {
        this.fixText = fixText;
    }

}

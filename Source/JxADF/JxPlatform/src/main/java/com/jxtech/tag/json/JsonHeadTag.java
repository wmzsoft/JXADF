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
public class JsonHeadTag extends JxBaseUITag {

    private static final long serialVersionUID = 485392439154143435L;
    private String attributeLabel;// 属性标签
    private String attribute;// 属性
    private String fixText;// 扩展字符串

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new JsonHead(stack, request, response);
    }

    @Override
    protected void populateParams() {
        JsonHead head = (JsonHead) component;
        head.setAttribute(attribute);
        head.setAttributeLabel(attributeLabel);
        head.setFixText(fixText);
        super.populateParams();
    }

    @Override
    public int doStartTag() throws JspException {
        int flag = super.doStartTag();
        Tag tag = getParent();
        if (tag != null) {
            JsonHead head = (JsonHead) component;
            if (tag instanceof JsonTag) {
                ((JsonTag) tag).getJsonHead().add(head);
            } else if (tag instanceof TableTag) {
                ((TableTag) tag).getJsonHead().add(head);
            }
        }
        return flag;
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

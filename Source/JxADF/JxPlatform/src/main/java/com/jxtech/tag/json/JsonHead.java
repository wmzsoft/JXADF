package com.jxtech.tag.json;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2015.03
 * 
 */
@StrutsTag(name = "jsonHead", tldTagClass = "com.jxtech.tag.json.JsonHeadTag", description = "Json Head Attribute")
public class JsonHead extends JxBaseUIBean {

    private String attributeLabel;// 属性标签
    private String attribute;// 属性
    private String fixText;// 扩展字符串

    public JsonHead(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "json/json-head-close.ftl";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "json/json-head.ftl";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (attributeLabel != null) {
            addParameter("attributeLabel", findString(attributeLabel));
        }
        if (attribute != null) {
            addParameter("attribute", findString(attribute));
        }
        if (fixText != null) {
            addParameter("fixText", findString(fixText));
        }
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

    public void setFixText(String fixText) {
        this.fixText = fixText;
    }

    public String getFixText() {
        return fixText;
    }


}

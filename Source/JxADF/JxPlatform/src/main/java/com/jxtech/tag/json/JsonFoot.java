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
@StrutsTag(name = "jsonFoot", tldTagClass = "com.jxtech.tag.json.JsonFootTag", description = "Json Footer")
public class JsonFoot extends JxBaseUIBean {
    private String attributeLabel;// 属性标签
    private String attribute;// 属性
    private String fixText;// 扩展字符串

    public JsonFoot(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "json/json-foot-close.ftl";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "json/json-foot.ftl";
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

    public void setAttributeLabel(String attributeLabel) {
        this.attributeLabel = attributeLabel;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setFixText(String fixText) {
        this.fixText = fixText;
    }

    public String getAttributeLabel() {
        return attributeLabel;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getFixText() {
        return fixText;
    }

}

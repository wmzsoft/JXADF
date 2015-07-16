package com.jxtech.tag.datepicker;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author smellok@126.com
 * @date 2013.09
 */
@StrutsTag(name = "datepicker", tldTagClass = "com.jxtech.tag.datepicker.DatePickerTag", description = "Datepicker")
public class DatePicker extends JxBaseUIBean {
    protected String name; // 控件名称
    protected String label; // 控件标签
    protected String format; // 时间格式

    public DatePicker(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return super.getStdDefaultTemplate("datepicker", false);
    }

    @Override
    public String getDefaultOpenTemplate() {
        return super.getStdDefaultOpenTemplate("datepicker", false);
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        addParameter("name", name);
        addParameter("label", label);
        addParameter("format", format);
    }

    @Override
    @StrutsTagAttribute(description = "setName", type = "String")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    @StrutsTagAttribute(description = "setLabel", type = "String")
    public void setLabel(String label) {
        this.label = label;
    }

    @StrutsTagAttribute(description = "setFormat", type = "String")
    public void setFormat(String format) {
        this.format = format;
    }
}

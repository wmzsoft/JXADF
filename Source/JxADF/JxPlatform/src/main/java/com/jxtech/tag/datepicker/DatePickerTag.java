package com.jxtech.tag.datepicker;

import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author smellok@126.com
 * @date 2013.09
 */
public class DatePickerTag extends JxBaseUITag {
    private static final long serialVersionUID = 1L;
    protected String label; // 控件标签
    protected String name; // 控件的名称
    protected String format; // 时间格式

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super.initPropertiesValue(false);
        return new DatePicker(stack, request, response);
    }

    @Override
    protected void populateParams() {
        if (!super.isRenderer("datepicker")) {
            return;
        }
        super.populateParams();
        DatePicker ui = (DatePicker) component;
        ui.setLabel(label);
        ui.setName(name);
        ui.setFormat(format);
    }

    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return this.format;
    }
}

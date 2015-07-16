package com.jxtech.workflow.option;

public class WftParamAttr {
    private String label;
    private String attribute;
    private Object value;
    private boolean required;

    public WftParamAttr() {

    }

    public WftParamAttr(String label, String attribute, String value, boolean required) {
        setLabel(label);
        setAttribute(attribute);
        setValue(value);
        setRequired(required);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

}

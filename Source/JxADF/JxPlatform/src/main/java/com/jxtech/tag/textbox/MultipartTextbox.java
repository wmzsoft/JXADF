package com.jxtech.tag.textbox;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
@StrutsTag(name = "multipartTextbox", tldTagClass = "com.jxtech.tag.textbox.MultipartTextboxTag", description = "MultipartTextbox")
public class MultipartTextbox extends Textbox {

    protected String descdataattribute;
    protected String descinputmode;
    private Object desDataValue;
    protected String jboname;

    private String queryValue;// 查询值

    public MultipartTextbox(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "multipartTextbox-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "multipartTextbox";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (descdataattribute != null) {
            addParameter("descdataattribute", findString(descdataattribute).toUpperCase());
        }
        if (descinputmode != null) {
            addParameter("descinputmode", findString(descinputmode).toUpperCase());
        }
        if (inputmode != null) {
            addParameter("inputmode", inputmode.toUpperCase());
        }
        if (queryValue != null) {
            addParameter("queryValue", queryValue);
        }
        if (null != jboname) {
            addParameter("jboname", jboname);
        }
        addParameter("desDataValue", desDataValue);
    }

    public void setDescdataattribute(String descdataattribute) {
        this.descdataattribute = descdataattribute;
    }

    public void setDescinputmode(String descinputmode) {
        this.descinputmode = descinputmode;
    }

    public void setDesDataValue(Object desDataValue) {
        this.desDataValue = desDataValue;
    }

    @Override
    public void setQueryValue(String queryValue) {
        this.queryValue = queryValue;
    }

    public void setJboname(String jboname) {
        this.jboname = jboname;
    }
}

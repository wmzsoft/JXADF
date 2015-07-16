package com.jxtech.tag.textbox;

import com.jxtech.jbo.util.JxException;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */

public class MultipartTextboxTag extends TextboxTag {

    protected String inputmode;
    protected String descdataattribute;
    protected String descinputmode;
    private Object desDataValue;
    private static final Logger LOG = LoggerFactory.getLogger(MultipartTextboxTag.class);

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        readonly = null;
        super.initPropertiesValue(false);
        return new MultipartTextbox(stack, request, response);
    }

    public String getDescdataattribute() {
        return descdataattribute;
    }

    public void setDescdataattribute(String descdataattribute) {
        this.descdataattribute = descdataattribute;
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        MultipartTextbox text = (MultipartTextbox) component;
        text.setDescdataattribute(descdataattribute);
        text.setDescinputmode(descinputmode);
        text.setInputmode(inputmode);
        if (null != jboset) {
            text.setJboname(jboset.getJboname());
        }
        if (jbo != null && !StrUtil.isNull(descdataattribute)) {
            try {
                text.setDesDataValue(jbo.getObject(descdataattribute));
            } catch (JxException e) {
                LOG.error(e.getMessage());
            }
        }

        String cause = "";
        if (!StrUtil.isNull(wherecause)) {
            cause = wherecause;
        } else if (columnAttribute != null) {
            cause = columnAttribute.getWherecause();
        }

        Map<String, Object> queryParam = null;
        if (jboset != null) {
            queryParam = jboset.getQueryInfo().getParams();
            if (null != queryParam) {
                String key;
                if (cause != null) {
                    key = dataattribute.toUpperCase() + cause;
                    if (queryParam.get(key) != null) {
                        text.setQueryValue((String) queryParam.get(key));
                    }
                }
            }
        }
    }

    public String getDescinputmode() {
        return descinputmode;
    }

    public void setDescinputmode(String descinputmode) {
        this.descinputmode = descinputmode;
    }

    public Object getDesDataValue() {
        return desDataValue;
    }

    public void setDesDataValue(Object desDataValue) {
        this.desDataValue = desDataValue;
    }

    @Override
    public String getInputmode() {
        return inputmode;
    }

    @Override
    public void setInputmode(String inputmode) {
        this.inputmode = inputmode;
    }
}

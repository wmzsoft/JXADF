package com.jxtech.tag.checkbox;

import com.jxtech.jbo.base.JxAttribute;
import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sl wmzsosft@gmail.com
 * @date 2013.08.22
 */
@StrutsTag(name = "checkbox", tldTagClass = "com.jxtech.tag.Checkbox.CheckboxTag", description = "Checkbox")
public class Checkbox extends JxBaseUIBean {

    protected String dataattribute;
    protected String colspan;
    protected String rowspan;
    protected String checked;
    protected String notChecked;
    protected String inputmode;
    protected String mystyle;
    protected String fragmentid;
    protected String wherecause;
    private String dataValue;
    private JxAttribute jxattribute;
    protected String readonly;
    protected String visible;
    private String cause;
    private String queryValue;
    protected String queryType;

    public Checkbox(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return super.getStdDefaultTemplate("checkbox", true);
    }

    @Override
    public String getDefaultOpenTemplate() {
        return super.getStdDefaultOpenTemplate("checkbox", true);
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (dataattribute != null) {
            addParameter("dataattribute", findString(dataattribute).toUpperCase());
        }
        if (colspan != null) {
            addParameter("colspan", findString(colspan));
        }
        if (rowspan != null) {
            addParameter("rowspan", findString(rowspan));
        }
        if (checked != null) {
            addParameter("checked", findString(checked));
        }
        if (notChecked != null) {
            addParameter("notChecked", findString(notChecked));
        }
        if (inputmode != null) {
            addParameter("inputmode", findString(inputmode).toUpperCase());
        }
        if (mystyle != null) {
            addParameter("mystyle", findString(mystyle).toUpperCase());
        }
        if (fragmentid != null) {
            addParameter("fragmentid", findString(fragmentid));
        }
        if (wherecause != null) {
            addParameter("wherecause", findString(wherecause));
        }
        if (dataValue != null) {
            addParameter("dataValue", findString(dataValue));
        }
        if (jxattribute != null) {
            addParameter("jxattribute", jxattribute);
        }
        if (readonly != null) {
            addParameter("readonly", findValue(readonly, Boolean.class));
        }
        if (inputmode != null) {
            String im = findString(inputmode).toUpperCase();
            addParameter("inputmode", im);
            if ("READONLY".equals(im)) {
                addParameter("readonly", true);
            }
        }
        if (label != null) {
            addParameter("label", getI18NValue(label));
        }

        if (null != cause) {
            addParameter("cause", findString(cause));
        }

        if (null != queryValue) {
            addParameter("queryValue", findString(queryValue));
        }

        if (null != queryType) {
            addParameter("queryType", findString(queryType).toUpperCase());
        }
    }

    public void setDataattribute(String dataattribute) {
        this.dataattribute = dataattribute;
    }

    public void setColspan(String colspan) {
        this.colspan = colspan;
    }

    public void setRowspan(String rowspan) {
        this.rowspan = rowspan;
    }

    public void setInputmode(String inputmode) {
        this.inputmode = inputmode;
    }

    public void setMystyle(String mystyle) {
        this.mystyle = mystyle;
    }

    public void setFragmentid(String fragmentid) {
        this.fragmentid = fragmentid;
    }

    public void setWherecause(String wherecause) {
        this.wherecause = wherecause;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public void setJxattribute(JxAttribute jxattribute) {
        this.jxattribute = jxattribute;
    }

    public void setNotChecked(String notChecked) {
        this.notChecked = notChecked;
    }

    @Override
    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    @Override
    public void setVisible(String visible) {
        this.visible = visible;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public void setQueryValue(String queryValue) {
        this.queryValue = queryValue;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
}

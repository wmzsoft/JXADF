package com.jxtech.tag.checkbox;

import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.base.JxAttribute;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.tag.form.FormTag;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.Tag;
import java.util.Map;

/**
 * 参见：http://svn.jxtech.net:8081/display/TECH/checkbox
 *
 * @author sl wmzsosft@gmail.com
 * @date 2013.08.22
 */

public class CheckboxTag extends JxBaseUITag {

    private static final long serialVersionUID = -5562491446735195983L;
    private static final Logger LOG = LoggerFactory.getLogger(CheckboxTag.class);
    protected String dataattribute;
    protected String colspan;
    protected String rowspan;
    protected String checked;
    protected String notChecked;
    protected String inputmode;
    protected String mystyle;
    protected String fragmentid;
    protected String wherecause;
    private JboIFace jbo;
    private JboSetIFace jboset;
    private String dataValue;
    private String cause;
    private String queryValue;
    private JxAttribute jxattribute;
    protected String labeltip;// 标签提示
    protected String valuetip;// 值的提示

    protected String queryType; // 查询类型（有CHECKBOX和SELECT2中）

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super.initPropertiesValue(false);
        return new Checkbox(stack, request, response);
    }

    @Override
    protected void populateParams() {
        if (!super.isRenderer("checkbox")) {
            return;
        }
        Tag tag = findAncestorWithClass(getParent(), FormTag.class);
        if (tag != null && dataattribute != null) {
            cause = " = ?";
            FormTag ft = ((FormTag) tag);
            jbo = ft.getJbo();
            jboset = ft.getJboset();
            if (jbo != null && !StrUtil.isNull(dataattribute)) {
                try {
                    dataValue = jbo.getString(dataattribute);
                    jxattribute = jboset.getJxAttribute(dataattribute);

                    Map<String, Object> queryParam = jboset.getQueryInfo().getParams();
                    if ("query".equalsIgnoreCase(inputmode)) {
                        queryParam = jboset.getQueryInfo().getParams();

                        if (queryParam != null) {
                            String key;
                            if (cause != null) {
                                if ("query".equalsIgnoreCase(inputmode) && dataattribute.indexOf(".") > 0) {
                                    DataQueryInfo dataQueryInfo = new DataQueryInfo();
                                    key = dataQueryInfo.getRelationShipCauseCondition(jboset.getJboname(), dataattribute, cause);
                                } else {
                                    key = dataattribute.toUpperCase() + cause;
                                }
                                if (queryParam.get(key) != null) {
                                    queryValue = (String) queryParam.get(key);
                                }
                            }
                        }
                    } else {
                        if (!"true".equalsIgnoreCase(readonly)) {
                            readonly = String.valueOf(jbo.isReadonly(dataattribute));
                        }
                    }
                } catch (JxException e) {
                    LOG.error(e.getMessage());
                }
            }
        }
        Checkbox chk = (Checkbox) component;
        chk.setDataattribute(dataattribute);
        if (StrUtil.isNull(dataValue)) {
            dataValue = value;
        }
        chk.setDataValue(dataValue);
        chk.setRowspan(rowspan);
        chk.setColspan(colspan);
        chk.setChecked(checked);
        chk.setInputmode(inputmode);
        chk.setMystyle(mystyle);
        chk.setFragmentid(fragmentid);
        chk.setWherecause(wherecause);
        chk.setJxattribute(jxattribute);
        chk.setNotChecked(notChecked);
        chk.setReadonly(readonly);
        chk.setVisible(visible);
        chk.setLabeltip(labeltip);
        chk.setValuetip(valuetip);
        cause = " = ?";
        chk.setCause(cause);
        chk.setQueryValue(queryValue);
        chk.setQueryType(queryType);
        super.populateParams();
    }

    public String getDataattribute() {
        return dataattribute;
    }

    public void setDataattribute(String dataattribute) {
        this.dataattribute = dataattribute;
    }

    public JboIFace getJbo() {
        return jbo;
    }

    public void setJbo(JboIFace jbo) {
        this.jbo = jbo;
    }

    public JboSetIFace getJboset() {
        return jboset;
    }

    public void setJboset(JboSetIFace jboset) {
        this.jboset = jboset;
    }

    public String getColspan() {
        return colspan;
    }

    public void setColspan(String colspan) {
        this.colspan = colspan;
    }

    public String getRowspan() {
        return rowspan;
    }

    public void setRowspan(String rowspan) {
        this.rowspan = rowspan;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getInputmode() {
        return inputmode;
    }

    public void setInputmode(String inputmode) {
        this.inputmode = inputmode;
    }

    public String getMystyle() {
        return mystyle;
    }

    public void setMystyle(String mystyle) {
        this.mystyle = mystyle;
    }

    public String getFragmentid() {
        return fragmentid;
    }

    public void setFragmentid(String fragmentid) {
        this.fragmentid = fragmentid;
    }

    public String getWherecause() {
        return wherecause;
    }

    public void setWherecause(String wherecause) {
        this.wherecause = wherecause;
    }

    public String getNotChecked() {
        return notChecked;
    }

    public void setNotChecked(String notChecked) {
        this.notChecked = notChecked;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getLabeltip() {
        return labeltip;
    }

    public void setLabeltip(String labeltip) {
        this.labeltip = labeltip;
    }

    public String getValuetip() {
        return valuetip;
    }

    public void setValuetip(String valuetip) {
        this.valuetip = valuetip;
    }
}

package com.jxtech.tag.textbox;

import com.jxtech.common.JxLoadResource;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.base.JxAttribute;
import com.jxtech.jbo.base.KeyValue;
import com.jxtech.jbo.util.DataQueryInfo;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.body.BodyTag;
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

import java.util.List;
import java.util.Map;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class TextboxTag extends JxBaseUITag {
    private static final Logger LOG = LoggerFactory.getLogger(TextboxTag.class);
    private static final long serialVersionUID = 1L;
    protected String dataattribute;
    protected String colspan;
    protected String rowspan;
    protected String lookup;
    protected String lookupWidth;
    protected String lookupHeight;
    protected JboIFace jbo;
    protected JboSetIFace jboset;
    protected String rows;// 输入框显示多行，大于1，则使用<textarea>标签
    protected String cols;// 输入框显示多长
    protected String inputmode;// 输入模式
    protected String mystyle;// 显示类型，支持：td,none,默认为添加TD
    protected JxAttribute columnAttribute;// 某个字段的maxAttribute属性
    protected String wherecause;// 自定义查询条件。
    protected String render;// 渲染，取值包括:CKEDIT、WIKI等
    protected String renderExtends;// 渲染扩展属性
    protected String format;// 显示格式
    private Object dataValue;
    private String queryValue;// 查询值
    private String queryValue2;// 查询值
    private String cause;// 查询条件1
    private String cause2;// 查询条件2
    protected String urlParamValue;// 采用LINK渲染的时候，URL的参数值
    protected String urlTarget;// 采用Link渲染的时候，链接的Target
    protected String labeltip;// label的提示文字
    protected String valuetip;// 值的提示文字
    private HttpServletRequest req;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        readonly = null;
        super.initPropertiesValue(false);
        req = request;
        return new Textbox(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        boolean isInput = false;// 默认输入框为：Textarea，而不是Input
        Textbox text = (Textbox) component;
        text.setDataattribute(dataattribute);
        text.setColspan(colspan);
        text.setRowspan(rowspan);
        text.setLookup(lookup);
        text.setLookupWidth(lookupWidth);
        text.setLookupHeight(lookupHeight);
        text.setRows(rows);
        text.setCols(cols);
        text.setInputmode(inputmode);
        text.setMystyle(mystyle);
        text.setFormat(format);
        text.setRequired(required);
        text.setLabeltip(labeltip);
        text.setValuetip(valuetip);
        columnAttribute = null;
        Map<String, JxAttribute> jxattributes = null;
        Map<String, Object> queryParam = null;
        String jboName = "";
        cause = null;
        cause2 = null;
        queryValue = null;
        queryValue2 = null;
        dataValue = null;
        Tag tag = findAncestorWithClass(this, FormTag.class);
        try {
            if (tag != null && dataattribute != null) {
                FormTag ft = ((FormTag) tag);

                /**
                 * 获取当前的应用，传给数据bean，在数据bean中需要通过appname来获取国际化资源
                 */
                Tag bodyTag = findAncestorWithClass(getParent(), BodyTag.class);
                if (bodyTag != null) {
                    BodyTag bt = (BodyTag) bodyTag;
                    String appName = bt.getAppName();
                    if (!StrUtil.isNull(appName)) {
                        text.setAppName(appName);
                    }
                }

                jbo = ft.getJbo();
                jboset = ft.getJboset();
                jboName = ft.getJboname();
                if (jboset != null) {
                    jxattributes = jboset.getJxAttributes();
                    if (jxattributes != null) {
                        columnAttribute = jxattributes.get(dataattribute);
                    }

                    if (jbo != null) {
                        text.setJbo(jbo);
                        dataValue = jbo.getString(dataattribute);
                        if (!"true".equalsIgnoreCase(readonly)) {
                            readonly = String.valueOf(jbo.isReadonly(dataattribute));
                        }
                        text.setReadonly(readonly);

                        if (!"true".equalsIgnoreCase(required)) {
                            required = String.valueOf(jbo.isRequired(dataattribute));
                        }
                        text.setRequired(required);

                        if ("query".equalsIgnoreCase(inputmode)) {
                            queryParam = jboset.getQueryInfo().getParams();
                        }
                    } else if ("query".equalsIgnoreCase(inputmode)) {
                        queryParam = jboset.getQueryInfo().getParams();
                    } else {
                        LOG.debug("Form中，连Jbo都没找到。");
                    }
                } else {
                    LOG.info("Form中没有找到对应的Jbo set");
                }
            } else {
                LOG.info("没找到对应的Form");
            }
        } catch (JxException e) {
            LOG.error(e.getMessage());
        }

        if (!StrUtil.isNull(wherecause)) {
            cause = wherecause;
        } else if (columnAttribute != null) {
            cause = columnAttribute.getWherecause();
            cause2 = columnAttribute.getWherecause2();
        }
        if (queryParam != null) {
            String key = null;
            if (cause != null) {
                if ("query".equalsIgnoreCase(inputmode) && dataattribute.indexOf(".") > 0) {
                    DataQueryInfo dataQueryInfo = new DataQueryInfo();
                    try {
                        key = dataQueryInfo.getRelationShipCauseCondition(jboName, dataattribute, cause);
                    } catch (JxException e) {
                        LOG.error(e.getMessage(), e);
                    }
                } else {
                    key = dataattribute.toUpperCase() + cause;
                }
                if (queryParam.get(key) != null) {
                    queryValue = (String) queryParam.get(key);
                }
            }
            if (cause2 != null) {
                if ("query".equalsIgnoreCase(inputmode) && dataattribute.indexOf(".") > 0) {
                    DataQueryInfo dataQueryInfo = new DataQueryInfo();
                    try {
                        key = dataQueryInfo.getRelationShipCauseCondition(jboName, dataattribute, cause2);
                    } catch (JxException e) {
                        LOG.error(e.getMessage(), e);
                    }
                } else {
                    key = dataattribute.toUpperCase() + cause2;
                }
                if (queryParam.get(key) != null) {
                    queryValue2 = (String) queryParam.get(key);
                }
            }
        }
        text.setCause(cause);
        text.setQueryValue(queryValue);
        text.setCause2(cause2);
        text.setQueryValue2(queryValue2);
        text.setColumnAttribute(columnAttribute);
        if (StrUtil.isObjectNull(dataValue)){
            dataValue = value;
        }
        text.setDataValue(dataValue);
        text.setRender(render);
        text.setRenderExtends(renderExtends);
        if ("CKEDITOR".equalsIgnoreCase(render)) {
            JxLoadResource.loadCKEditor(req);
        } else if ("PASSWORD".equalsIgnoreCase(render) || "HIDDEN".equalsIgnoreCase(render)) {
            isInput = true;
        } else if ("query".equalsIgnoreCase(inputmode) || "queryImmediately".equalsIgnoreCase(inputmode)) {
            isInput = true;
        }
        if (!StrUtil.isNull(urlParamValue)) {
            urlParamValue = this.findString(urlParamValue);
            if (jbo != null) {
                try {
                    if (urlParamValue.indexOf(',') > 0) {
                        List<KeyValue> mlist = StrUtil.toList(urlParamValue);
                        if (mlist != null) {
                            int msize = mlist.size();
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < msize; i++) {
                                KeyValue kv = mlist.get(i);
                                sb.append(kv.getKey()).append("=").append(jbo.getURLString(kv.getValue())).append("&");
                            }
                            if (sb.length() > 2) {
                                StrUtil.deleteLastChar(sb);
                            }
                            urlParamValue = sb.toString();
                        }
                    } else {
                        urlParamValue = jbo.getURLString(urlParamValue);
                    }
                } catch (Exception e) {
                    LOG.warn(e.getMessage(), e);
                }
            }
            text.setUrlParamValue(urlParamValue);
        }
        text.setUrlTarget(urlTarget);
        String maxtype = getMaxType();
        text.setMaxtype(maxtype);
        if ("DATE".equalsIgnoreCase(maxtype) || "DATETIME".equalsIgnoreCase(maxtype) || "TIME".equalsIgnoreCase(maxtype)) {
            JxLoadResource.loadJquerTimepicker(req);
            isInput = true;
        } else if (!isInput && columnAttribute != null) {
            isInput = columnAttribute.isBoolean() || columnAttribute.isNumOrDateTime();
        }
        text.setReadonly(readonly);
        text.setVisible(visible);
        text.setButtonType(getButtonType());
        text.setInput(isInput);
    }

    /**
     * 获得输入框后面是否要添加按钮
     * 
     * @return
     */
    private String getButtonType() {
        if (StrUtil.isTrue(readonly, false) || "readonly".equalsIgnoreCase(readonly)) {
            return null;
        } else if (!StrUtil.isTrue(visible, true)) {
            return null;
        }
        String maxtype = getMaxType();
        if ("DATE".equalsIgnoreCase(maxtype) || "DATETIME".equalsIgnoreCase(maxtype) || "TIME".equalsIgnoreCase(maxtype)) {
            return maxtype;
        }
        return null;
    }

    /**
     * 获得数据类型
     * 
     * @return
     */
    private String getMaxType() {
        String maxtype = null;
        if (null != dynamicAttributes.get("maxtype")) {
            maxtype = dynamicAttributes.get("maxtype").toString().toUpperCase();
        } else if (null != columnAttribute) {
            maxtype = columnAttribute.getMaxType().toUpperCase();
        }
        return maxtype;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDataattribute() {
        return dataattribute;
    }

    public void setDataattribute(String dataattribute) {
        this.dataattribute = dataattribute;
    }

    public Object getDataValue() {
        return dataValue;
    }

    public void setDataValue(Object dataValue) {
        this.dataValue = dataValue;
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

    public String getLookup() {
        return lookup;
    }

    public void setLookup(String lookup) {
        this.lookup = lookup;
    }

    public JboIFace getJbo() {
        return jbo;
    }

    public void setJbo(JboIFace jbo) {
        this.jbo = jbo;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getCols() {
        return cols;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public JboSetIFace getJboset() {
        return jboset;
    }

    public void setJboset(JboSetIFace jboset) {
        this.jboset = jboset;
    }

    public String getInputmode() {
        return inputmode;
    }

    public void setInputmode(String inputmode) {
        this.inputmode = inputmode;
    }

    public String getQueryValue() {
        return queryValue;
    }

    public void setQueryValue(String queryValue) {
        this.queryValue = queryValue;
    }

    public String getMystyle() {
        return mystyle;
    }

    public void setMystyle(String mystyle) {
        this.mystyle = mystyle;
    }

    public JxAttribute getColumnAttribute() {
        return columnAttribute;
    }

    public void setColumnAttribute(JxAttribute columnAttribute) {
        this.columnAttribute = columnAttribute;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getCause2() {
        return cause2;
    }

    public void setCause2(String cause2) {
        this.cause2 = cause2;
    }

    public String getQueryValue2() {
        return queryValue2;
    }

    public void setQueryValue2(String queryValue2) {
        this.queryValue2 = queryValue2;
    }

    public String getWherecause() {
        return wherecause;
    }

    public void setWherecause(String wherecause) {
        this.wherecause = wherecause;
    }

    public String getRender() {
        return render;
    }

    public void setRender(String render) {
        this.render = render;
    }

    public String getLookupWidth() {
        return lookupWidth;
    }

    public void setLookupWidth(String lookupWidth) {
        this.lookupWidth = lookupWidth;
    }

    public String getLookupHeight() {
        return lookupHeight;
    }

    public void setLookupHeight(String lookupHeight) {
        this.lookupHeight = lookupHeight;
    }

    public String getRenderExtends() {
        return renderExtends;
    }

    public void setRenderExtends(String renderExtends) {
        this.renderExtends = renderExtends;
    }

    public String getUrlParamValue() {
        return urlParamValue;
    }

    public void setUrlParamValue(String urlParamValue) {
        this.urlParamValue = urlParamValue;
    }

    public String getUrlTarget() {
        return urlTarget;
    }

    public void setUrlTarget(String urlTarget) {
        this.urlTarget = urlTarget;
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

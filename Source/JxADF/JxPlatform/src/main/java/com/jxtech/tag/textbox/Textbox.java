package com.jxtech.tag.textbox;

import com.jxtech.i18n.JxLangResourcesUtil;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.base.JxAttribute;
import com.jxtech.tag.comm.JxBaseUIBean;
import com.jxtech.util.ELUtil;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */

@StrutsTag(name = "textbox", tldTagClass = "com.jxtech.tag.textbox.TextboxTag", description = "Textbox")
public class Textbox extends JxBaseUIBean {

    protected String dataattribute;
    protected String colspan;
    protected String rowspan;
    protected String required;// 要求必填
    protected String lookup;
    protected String lookupWidth;
    protected String lookupHeight;
    protected String rows;// 输入框显示多行，大于1，则使用<textarea>标签
    protected String cols;// 输入框显示多长
    protected String inputmode;// 输入模式
    protected String queryValue;// 查询值
    protected String queryValue2;// 查询值2
    protected String mystyle;// 显示类型，支持：td,none,默认为添加TD
    protected JxAttribute columnAttribute;// 某个字段的maxAttribute属性
    protected String render;// 渲染，取值包括:CKEDIT、WIKI等
    protected String readonly;
    protected String visible;
    protected String renderExtends;// 文本编辑器显示样式
    protected String format;// 显示格式
    private Object dataValue;
    private String cause;// 自定义查询条件
    private String cause2;
    private JboIFace jbo;
    private String appName; // 应用名称
    private String maxtype;// 数据类型
    private String buttonType;// 输入框后面需要跟的按钮类型，为DATE、DATETIME、TIME，为空，表示不跟，这是一个计算值

    public Textbox(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "textbox/" + getRenderer() + "textbox-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "textbox/" + getRenderer() + "textbox";
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
        addParameter("dataValue", dataValue);
        addParameter("cause", cause);
        addParameter("cause2", cause2);
        addParameter("queryValue", queryValue);
        addParameter("queryValue2", queryValue2);
        addParameter("columnAttribute", columnAttribute);
        addParameter("format", format);
        if (maxtype != null) {
            addParameter("maxtype", maxtype);
        }
        if (buttonType != null) {
            addParameter("buttonType", buttonType);
        }
        if (cause != null)
            addParameter("cause", findString(cause));
        if (lookup != null) {
            addParameter("lookup", findString(lookup));
        }
        if (null != lookupWidth) {
            addParameter("lookupWidth", findString(lookupWidth));
        }
        if (null != lookupHeight) {
            addParameter("lookupHeight", findString(lookupHeight));
        }
        if (cols != null) {
            addParameter("cols", findString(cols));
        }
        if (rows != null) {
            addParameter("rows", findString(rows));
        }
        if (readonly != null) {
            addParameter("readonly", findValue(readonly, Boolean.class));
        }
        if (required != null) {
            addParameter("required", findValue(required, Boolean.class));
        }
        if (inputmode != null) {
            String im = findString(inputmode).toUpperCase();
            addParameter("inputmode", im);
            if ("READONLY".equals(im)) {
                addParameter("readonly", true);
            }
        }

        if (mystyle != null) {
            addParameter("mystyle", findString(mystyle).toUpperCase());
        }

        if (render != null) {
            addParameter("render", findString(render).toUpperCase());
        }

        if (jbo != null) {
            if (renderExtends != null) {
                addParameter("renderExtends", findString(ELUtil.parseJboElValue(jbo, renderExtends)));
            }

            addParameter("jbo", jbo);

            /*try {

                JboValue value = jbo.getValue(dataattribute, false);

                if (!StrUtil.isNull(required)) {
                    if ("FALSE".equalsIgnoreCase(required)) {
                        addParameter("required", false);
                    } else if ("TRUE".equalsIgnoreCase(required)) {
                        addParameter("required", true);
                    } else {
                        if (null != value) {
                            addParameter("required", value.isRequired());
                        }
                    }
                } else {
                    if (null != value) {
                        addParameter("required", value.isRequired());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }*/

        } else {
            if (renderExtends != null) {
                addParameter("renderExtends", findString(renderExtends));
            }
        }

        addParameter("label", getI18NValue(label));

        if (!StrUtil.isNull(appName)) {
            addParameter("appBundle", JxLangResourcesUtil.getResourceBundle("res.app." + appName));
        }

        if (null != cssClass) {
            dynamicAttributes.put("cssClass", findString(cssClass));
        }
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @StrutsTagAttribute(description = "setDataattribute", type = "String")
    public void setDataattribute(String dataattribute) {
        this.dataattribute = dataattribute;
    }

    public void setDataValue(Object dataValue) {
        this.dataValue = dataValue;
    }

    public void setColspan(String colspan) {
        this.colspan = colspan;
    }

    public void setRowspan(String rowspan) {
        this.rowspan = rowspan;
    }

    @StrutsTagAttribute(description = "setLookup", type = "String")
    public void setLookup(String lookup) {
        this.lookup = lookup;
    }

    @StrutsTagAttribute(description = "setRows", defaultValue = "1", type = "Integer")
    public void setRows(String rows) {
        this.rows = rows;
    }

    @StrutsTagAttribute(description = "setCols", defaultValue = "20", type = "Integer")
    public void setCols(String cols) {
        this.cols = cols;
    }

    @StrutsTagAttribute(description = "setInputmode", defaultValue = "edit", type = "String")
    public void setInputmode(String inputmode) {
        this.inputmode = inputmode;
    }

    public void setQueryValue(String queryValue) {
        this.queryValue = queryValue;
    }

    public void setQueryValue2(String queryValue2) {
        this.queryValue2 = queryValue2;
    }

    public void setMystyle(String mystyle) {
        this.mystyle = mystyle;
    }

    public void setColumnAttribute(JxAttribute columnAttribute) {
        this.columnAttribute = columnAttribute;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public void setCause2(String cause2) {
        this.cause2 = cause2;
    }

    public void setRender(String render) {
        this.render = render;
    }

    public JboIFace getJbo() {
        return jbo;
    }

    public void setJbo(JboIFace jbo) {
        this.jbo = jbo;
    }

    @Override
    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    @Override
    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setLookupWidth(String lookupWidth) {
        this.lookupWidth = lookupWidth;
    }

    public void setLookupHeight(String lookupHeight) {
        this.lookupHeight = lookupHeight;
    }

    public void setRenderExtends(String renderExtends) {
        this.renderExtends = renderExtends;
    }

    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }

    public void setMaxtype(String maxtype) {
        this.maxtype = maxtype;
    }
}

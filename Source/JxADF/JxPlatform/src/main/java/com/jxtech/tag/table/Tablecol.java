package com.jxtech.tag.table;

import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.base.JxAttribute;
import com.jxtech.jbo.base.KeyValue;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.comm.JxBaseUIBean;
import com.jxtech.tag.select.Select;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.07
 */

@StrutsTag(name = "tablecol", tldTagClass = "com.jxtech.tag.table.TablecolTag", description = "Tablecol")
public class Tablecol extends JxBaseUIBean {

    private static final Logger LOG = LoggerFactory.getLogger(Tablecol.class);
    protected String type;

    protected String mxevent_desc;

    protected String mxevent;
    protected String mxevent_icon;
    protected String mxevent_disabled;// 事件中的按钮是否可以点击
    protected String mxevent_render;
    protected String dataattribute;
    protected String dataDisplay;// 数据显示，一般用于根据不同的值，显示不同的图标

    protected String lookup;
    protected String lookupWidth;
    protected String lookupHeight;
    protected String lookupWrite;

    protected String linkedcontrolid;

    protected String filterable;
    protected String filtername;// 过滤字段名
    protected String sortable;// 可排序
    protected String sortname;// 排

    protected String startHtml;// 在值前面显示HTML代码
    protected String html;// 直接在列中显示HTML
    protected String render;
    protected String visibleHead;// 是否显示列头，如果为false，则将此列与前一列合并显示在一列中。
    protected Table table;

    protected String urlType;// URL 的类型，目前支持 comtop,report,自定义URL地址

    protected String urlParamName;// URL的参数名

    protected String urlParamValue;// URL参数值

    protected List<KeyValue> allUrlParams;// 所有参数的键值对

    protected String urlTarget;// URL的Target参数

    protected JxAttribute jxattribute;

    protected String title;// td表格鼠标悬置显示的提示

    protected String align; // td中的内容对其方式

    protected String format;// 显示格式
    protected String inputsize;// 如果为输入框时，显示大小
    protected String maxlength;// 表格中最大显示多少个字符
    protected String width;
    protected String height;
    protected String note;// 注释
    protected String descdataattribute;
    private Object desDataValue;
    private String secondAttributes;// 可以填写多个属性,自动在TD中生成"属性=值"
    private Select sec;
    private String dataname;// 主要用于JSON，显示字段名

    public Tablecol(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "table/tablecol";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "table/tablecol-close";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (null != type) {
            addParameter("type", findString(type).toUpperCase());
        }
        if (null != mxevent_desc) {
            addParameter("mxevent_desc", findString(mxevent_desc));
        }
        if (null != mxevent) {
            String mxeventUp = findString(mxevent).toUpperCase();
            if ("SELECTRECORD".equals(mxeventUp) || "DELROW".equals(mxeventUp) || "LOOKUP".equals(mxeventUp) || "WORKFLOW".equals(mxeventUp) || "ATTACHMENT".equals(mxeventUp) || "LINK".equals(mxeventUp)) {
                addParameter("mxevent", findString(mxeventUp));
            } else {
                addParameter("mxevent", findString(mxevent));
                addParameter("mxevent_render", findString(mxevent_render));
            }
        }
        if (null != dataattribute) {
            addParameter("dataattribute", findString(dataattribute).toUpperCase());
        }
        if (null != lookup) {
            addParameter("lookup", findString(lookup).toUpperCase());
        }
        if (null != lookupWidth) {
            addParameter("lookupWidth", findString(lookupWidth));
        }
        if (null != lookupHeight) {
            addParameter("lookupHeight", findString(lookupHeight));
        }

        if (null != lookupWrite) {
            addParameter("lookupWrite", findString(lookupWrite).toUpperCase());
        }
        if (null != linkedcontrolid) {
            addParameter("linkedcontrolid", findString(linkedcontrolid).toUpperCase());
        }
        if (null != mxevent_icon) {
            addParameter("mxevent_icon", findString(mxevent_icon));
        }
        if (null != mxevent_disabled) {
            addParameter("mxevent_disabled", mxevent_disabled);
        }
        if (null != filterable) {
            addParameter("filterable", findValue(filterable, Boolean.class));
        }
        if (null != filtername) {
            addParameter("filtername", findString(filtername));
        }
        if (null != sortable) {
            addParameter("sortable", findValue(sortable, Boolean.class));
        }
        if (null != sortname) {
            addParameter("sortname", findString(sortname));
        }
        if (null != startHtml) {
            addParameter("startHtml", findString(startHtml));
        }
        if (null != html) {
            addParameter("html", findValue(html, String.class));
        }
        if (null != render) {
            addParameter("render", findString(render).toUpperCase());
        }
        if (null != title) {
            addParameter("title", findValue(title, String.class));
        }
        if (null != align) {
            addParameter("align", findValue(align, String.class));
        }
        if (null != note) {
            addParameter("note", findValue(note, String.class));
        }
        if (null != readonly) {
            Object r = findValue(readonly, String.class);
            boolean ro = false;
            if ("readonly".equalsIgnoreCase((String) r)) {
                ro = true;
            } else {
                ro = StrUtil.isTrue((String) r, false);
            }
            addParameter("readonly", r);
            addParameter("readonlyb", ro);
        }
        if (null != dataname) {
            addParameter("dataname", findString(dataname));
        }
        addParameter("jxattribute", jxattribute);
        if (jxattribute != null) {
            addParameter("isBoolean", jxattribute.isBoolean());
        }
        addParameter("urlType", urlType);
        if (urlParamName != null) {
            addParameter("urlParamName", findString(urlParamName));
        }
        if (urlParamValue != null) {
            addParameter("urlParamValue", findString(urlParamValue));
        }
        addParameter("allUrlParams", allUrlParams);
        addParameter("urlTarget", urlTarget);
        addParameter("format", format);
        addParameter("dataDisplay", dataDisplay);
        if (width != null) {
            addParameter("width", findString(width));
        }
        if (height != null) {
            addParameter("height", findString(height));
        }
        if (maxlength != null) {
            addParameter("maxlength", findString(maxlength));
        }
        if (inputsize != null) {
            addParameter("inputsize", findString(inputsize));
        }
        if (null != sec) {
            addParameter("sec", sec);
        }
        if (visible != null) {
            addParameter("visible", findValue(visible, Boolean.class));
        }
        if (visibleHead != null) {
            addParameter("visibleHead", findValue(visibleHead, Boolean.class));
        }
        if (descdataattribute != null) {
            addParameter("descdataattribute", findString(descdataattribute));
        }

        table = (Table) findAncestor(Table.class);
        if (table != null) {
            addParameter("table", table);
        }
        if (secondAttributes != null) {
            addParameter("secondAttributes", findString(secondAttributes));
        }
        addParameter("label", findLabel());

        if (null != cssClass) {
            dynamicAttributes.put("cssClass", findString(cssClass));
        }
    }

    /**
     * 获得表格Head的标题
     * 
     * @return
     */
    public String findLabel() {
        String lb = getI18NValue(label);
        if (StrUtil.isNull(lb)) {
            if (table != null) {
                try {
                    ResourceBundle rb = (ResourceBundle) table.getParameters().get("appBundle");
                    if (rb != null) {
                        lb = rb.getString(dataattribute);
                        if (!StrUtil.isNull(lb)) {
                            return lb;
                        }
                    }
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                }
                if (jxattribute != null) {
                    lb = jxattribute.getTitle();
                    if (!StrUtil.isNull(lb)) {
                        return lb;
                    }
                }
                JboSetIFace jboset = (JboSetIFace) table.getParameters().get("jboset");
                if (jboset != null) {
                    try {
                        JxAttribute ja = jboset.getJxAttribute(dataattribute);
                        if (ja != null) {
                            return ja.getTitle();
                        }
                    } catch (JxException e) {
                        LOG.error(e.getMessage());
                    }
                }
            }
        }
        return lb;
    }

    @StrutsTagAttribute(description = "setType", type = "String")
    public void setType(String type) {
        this.type = type;
    }

    @StrutsTagAttribute(description = "setMxevent_desc", type = "String")
    public void setMxevent_desc(String mxevent_desc) {
        this.mxevent_desc = mxevent_desc;
    }

    @StrutsTagAttribute(description = "setMxevent", type = "String")
    public void setMxevent(String mxevent) {
        this.mxevent = mxevent;
    }

    @StrutsTagAttribute(description = "setDataattribute", type = "String")
    public void setDataattribute(String dataattribute) {
        this.dataattribute = dataattribute;
    }

    @StrutsTagAttribute(description = "setLookup", type = "String")
    public void setLookup(String lookup) {
        this.lookup = lookup;
    }

    public void setLookupWidth(String lookupWidth) {
        this.lookupWidth = lookupWidth;
    }

    public void setLookupHeight(String lookupHeight) {
        this.lookupHeight = lookupHeight;
    }

    @StrutsTagAttribute(description = "setLookupWrite", type = "String")
    public void setLookupWrite(String lookupWrite) {
        this.lookupWrite = lookupWrite;
    }

    @StrutsTagAttribute(description = "setLinkedcontrolid", type = "String")
    public void setLinkedcontrolid(String linkedcontrolid) {
        this.linkedcontrolid = linkedcontrolid;
    }

    @StrutsTagAttribute(description = "setMxevent_icon", type = "String")
    public void setMxevent_icon(String mxevent_icon) {
        this.mxevent_icon = mxevent_icon;
    }

    @StrutsTagAttribute(description = "setFilterable", defaultValue = "true", type = "Boolean")
    public void setFilterable(String filterable) {
        this.filterable = filterable;
    }

    public void setMxevent_render(String mxevent_render) {
        this.mxevent_render = mxevent_render;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getDataattribute() {
        return dataattribute;
    }

    @StrutsTagAttribute(description = "setSortable", defaultValue = "true", type = "Boolean")
    public void setSortable(String sortable) {
        this.sortable = sortable;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public void setJxattribute(JxAttribute jxattribute) {
        this.jxattribute = jxattribute;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    public void setUrlParamName(String urlParamName) {
        this.urlParamName = urlParamName;
    }

    public void setUrlParamValue(String urlParamValue) {
        this.urlParamValue = urlParamValue;
    }

    public void setAllUrlParams(List<KeyValue> allUrlParams) {
        this.allUrlParams = allUrlParams;
    }

    public void setUrlTarget(String urlTarget) {
        this.urlTarget = urlTarget;
    }

    @Override
    @StrutsTagAttribute(description = "setTitle", type = "String")
    public void setTitle(String title) {
        this.title = title;
    }

    @StrutsTagAttribute(description = "setAlign", type = "String")
    public void setAlign(String align) {
        this.align = align;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setDataDisplay(String dataDisplay) {
        this.dataDisplay = dataDisplay;
    }

    public void setInputsize(String inputsize) {
        this.inputsize = inputsize;
    }

    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

    public void setFiltername(String filtername) {
        this.filtername = filtername;
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    @StrutsTagAttribute(description = "setNote", type = "String")
    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public Select getSec() {
        return sec;
    }

    public void setSec(Select sec) {
        this.sec = sec;
    }

    public String getDescdataattribute() {
        return descdataattribute;
    }

    public void setDescdataattribute(String descdataattribute) {
        this.descdataattribute = descdataattribute;
    }

    public Object getDesDataValue() {
        return desDataValue;
    }

    public void setDesDataValue(Object desDataValue) {
        this.desDataValue = desDataValue;
    }

    public void setStartHtml(String startHtml) {
        this.startHtml = startHtml;
    }

    public void setRender(String render) {
        this.render = render;
    }

    public void setVisibleHead(String visibleHead) {
        this.visibleHead = visibleHead;
    }

    public void setSecondAttributes(String secondAttributes) {
        this.secondAttributes = secondAttributes;
    }

    public void setDataname(String dataname) {
        this.dataname = dataname;
    }

    public String getDataname() {
        return dataname;
    }

    public String getSecondAttributes() {
        return secondAttributes;
    }

    public String getVisibleHead() {
        return visibleHead;
    }

    public String getStartHtml() {
        return startHtml;
    }

    public void setMxevent_disabled(String mxevent_disabled) {
        this.mxevent_disabled = mxevent_disabled;
    }

    public String getFormat() {
        return format;
    }

    public JxAttribute getJxattribute() {
        return jxattribute;
    }

    public String getDataDisplay() {
        return dataDisplay;
    }

}

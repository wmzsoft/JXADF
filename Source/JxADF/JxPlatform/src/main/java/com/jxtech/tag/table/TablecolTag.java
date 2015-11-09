package com.jxtech.tag.table;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.common.BeanUtil;
import com.jxtech.common.JxParams;
import com.jxtech.jbo.JboSetIFace;
import com.jxtech.jbo.base.JxAttribute;
import com.jxtech.jbo.base.KeyValue;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author wmzsoft@gmail.com
 * @date 2013.07
 */
public class TablecolTag extends JxBaseUITag {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(TablecolTag.class);
    protected String type;
    protected String mxevent_desc;
    protected String mxevent;
    protected String mxevent_icon;
    protected String mxevent_disabled;// 事件中的按钮是否可以点击
    protected String mxevent_render;
    protected String dataattribute;// 字段名
    protected String dataDisplay;// 数据显示，一般用于根据不同的值，显示不同的图标
    protected String lookup;
    protected String lookupWidth;
    protected String lookupHeight;
    protected String lookupWrite; // 配合lookup lookup框内容是否可编辑
    protected String linkedcontrolid;
    protected String filterable;// 是否可以过滤
    protected String filtername;// 过滤字段名
    protected String sortable;// 可排序
    protected String sortname;// 排序字段名
    protected String startHtml;// 在值前面显示HTML代码
    protected String html;// 直接在列中显示HTML
    protected String render;
    protected String visibleHead;// 是否显示列头，如果为false，则将此列与前一列合并显示在一列中。
    protected String urlType;// URL 的类型，目前支持 comtop,report,自定义URL地址
    protected String urlParamName;// URL的参数名
    protected String urlParamValue;// URL参数值
    protected List<KeyValue> allUrlParams;// 所有参数的键值对
    protected String urlTarget;// URL的Target参数
    protected Table table;
    protected JxAttribute jxattribute;
    protected String title; // td表格鼠标悬置显示的提示
    protected String align; // td表格对齐方式
    protected String format;// 显示格式
    protected String inputsize;// 如果为输入框时，显示大小
    protected String maxlength;// 表格中最大显示多少个字符
    protected String width;
    protected String height;
    protected String note;
    protected String descdataattribute;// lookup显示属性
    private Object desDataValue;// lookup值
    private String secondAttributes;// 可以填写多个属性,自动在TD中生成"属性=值"
    private String dataname;// 主要用于JSON，显示字段名

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super.initPropertiesValue(false);
        return new Tablecol(stack, request, response);
    }

    @Override
    protected void populateParams() {
        Tablecol col = (Tablecol) component;
        col.setId(id);
        col.setType(type);
        col.setMxevent_desc(mxevent_desc);
        col.setMxevent(mxevent);
        col.setMxevent_disabled(mxevent_disabled);
        col.setDataattribute(dataattribute);
        col.setLookup(lookup);
        col.setLookupWidth(lookupWidth);
        col.setLookupHeight(lookupHeight);
        col.setLookupWrite(lookupWrite);
        col.setLinkedcontrolid(linkedcontrolid);
        col.setMxevent_icon(mxevent_icon);
        col.setMxevent_render(mxevent_render);
        col.setFilterable(filterable);
        col.setFiltername(filtername);
        col.setSortable(sortable);
        col.setSortname(sortname);
        col.setStartHtml(startHtml);
        col.setHtml(html);
        col.setRender(render);
        col.setVisibleHead(visibleHead);
        col.setFormat(format);
        col.setTable(table);
        col.setInputsize(inputsize);
        col.setMaxlength(maxlength);
        col.setWidth(width);
        col.setHeight(height);
        col.setNote(note);
        col.setReadonly(readonly);
        col.setSecondAttributes(secondAttributes);
        col.setDataname(dataname);
        JxParams jparam = BeanUtil.getJxParams();
        if (jparam != null) {
            if ("report".equalsIgnoreCase(urlType)) {
                col.setUrlType(jparam.getReportUrl());
            } else {
                col.setUrlType(urlType);
            }
        } else {
            col.setUrlType(urlType);
        }
        col.setUrlParamName(urlParamName);
        col.setUrlParamValue(urlParamValue);
        transformUrlParams(); // 转换所有参数的值
        col.setAllUrlParams(allUrlParams);
        col.setUrlTarget(urlTarget);
        col.setTitle(title);
        col.setAlign(align);
        col.setDataDisplay(dataDisplay);
        col.setVisible(visible);
        col.setDescdataattribute(descdataattribute);
        Tag tag = findAncestorWithClass(this, TableTag.class);
        if (tag != null && dataattribute != null) {
            TableTag ttag = (TableTag) tag;
            JboSetIFace jboset = ttag.getJboset();
            if (jboset != null) {
                try {
                    Object jo = jboset.getJxAttribute(dataattribute.toUpperCase());
                    if (jo != null) {
                        jxattribute = (JxAttribute) jo;
                        col.setJxattribute(jxattribute);
                        boolean rq = StrUtil.isTrue(required, false);
                        jboset.setRequired(dataattribute, rq);
                    }
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        super.populateParams();
    }

    @Override
    public int doStartTag() throws JspException {
        int i = super.doStartTag();

        Tag parent = getParent();
        if (parent instanceof TableTag) {
            Tablecol col = (Tablecol) component;
            TableTag tbtag = (TableTag) parent;
            List<Tablecol> columns = tbtag.getColumns();
            int ci = tbtag.getColindex();
            if (ci > columns.size()) {
                ci = columns.size() - 1;
            }
            if (ci < 0) {
                ci = 0;
            }
            if (StrUtil.isNull(dataattribute) || StrUtil.isNull(tbtag.getCustColumns())) {
                columns.add(ci, col);
                tbtag.setColindex(ci + 1);
            } else {
                int idx = tbtag.getTablecol(dataattribute);
                if (idx >= 0) {
                    tbtag.getColumns().remove(idx);
                    tbtag.getColumns().add(idx, col);
                    tbtag.setColindex(idx + 1);
                }
            }
        }
        return i;
    }

    public JxAttribute getJxattribute() {
        return jxattribute;
    }

    public void setJxattribute(JxAttribute jxattribute) {
        this.jxattribute = jxattribute;
    }

    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    public String getUrlParamName() {
        return urlParamName;
    }

    public void setUrlParamName(String urlParamName) {
        this.urlParamName = urlParamName;
    }

    public String getUrlParamValue() {
        return urlParamValue;
    }

    public void setUrlParamValue(String urlParamValue) {
        this.urlParamValue = urlParamValue;
    }

    public List<KeyValue> getAllUrlParams() {
        return allUrlParams;
    }

    public void setAllUrlParams(List<KeyValue> allUrlParams) {
        this.allUrlParams = allUrlParams;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMxevent_desc() {
        return mxevent_desc;
    }

    public void setMxevent_desc(String mxevent_desc) {
        this.mxevent_desc = mxevent_desc;
    }

    public String getMxevent() {
        return mxevent;
    }

    public void setMxevent(String mxevent) {
        this.mxevent = mxevent;
    }

    public String getDataattribute() {
        return dataattribute;
    }

    public void setDataattribute(String dataattribute) {
        this.dataattribute = dataattribute;
    }

    public String getLookup() {
        return lookup;
    }

    public void setLookup(String lookup) {
        this.lookup = lookup;
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

    public String getLinkedcontrolid() {
        return linkedcontrolid;
    }

    public void setLinkedcontrolid(String linkedcontrolid) {
        this.linkedcontrolid = linkedcontrolid;
    }

    public String getMxevent_icon() {
        return mxevent_icon;
    }

    public void setMxevent_icon(String mxevent_icon) {
        this.mxevent_icon = mxevent_icon;
    }

    public String getMxevent_render(){return mxevent_render; }

    public  void setMxevent_render(String mxevent_render){ this.mxevent_render = mxevent_render;}

    public String getFilterable() {
        return filterable;
    }

    public void setFilterable(String filterable) {
        this.filterable = filterable;
    }

    public String getSortable() {
        return sortable;
    }

    public void setSortable(String sortable) {
        this.sortable = sortable;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getUrlTarget() {
        return urlTarget;
    }

    public void setUrlTarget(String urlTarget) {
        this.urlTarget = urlTarget;
    }

    protected void transformUrlParams() {
        allUrlParams = StrUtil.toList(urlParamValue);
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDataDisplay() {
        return dataDisplay;
    }

    public void setDataDisplay(String dataDisplay) {
        this.dataDisplay = dataDisplay;
    }

    public String getInputsize() {
        return inputsize;
    }

    public void setInputsize(String inputsize) {
        this.inputsize = inputsize;
    }

    public String getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

    public String getFiltername() {
        return filtername;
    }

    public void setFiltername(String filtername) {
        this.filtername = filtername;
    }

    public String getSortname() {
        return sortname;
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLookupWrite() {
        return lookupWrite;
    }

    public void setLookupWrite(String lookupWrite) {
        this.lookupWrite = lookupWrite;
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

    public String getStartHtml() {
        return startHtml;
    }

    public void setStartHtml(String startHtml) {
        this.startHtml = startHtml;
    }

    public String getRender() {
        return render;
    }

    public void setRender(String render) {
        this.render = render;
    }

    public String getVisibleHead() {
        return visibleHead;
    }

    public void setVisibleHead(String visibleHead) {
        this.visibleHead = visibleHead;
    }

    public String getSecondAttributes() {
        return secondAttributes;
    }

    public void setSecondAttributes(String secondAttributes) {
        this.secondAttributes = secondAttributes;
    }

    public String getDataname() {
        return dataname;
    }

    public void setDataname(String dataname) {
        this.dataname = dataname;
    }

    public String getMxevent_disabled() {
        return mxevent_disabled;
    }

    public void setMxevent_disabled(String mxevent_disabled) {
        this.mxevent_disabled = mxevent_disabled;
    }

}

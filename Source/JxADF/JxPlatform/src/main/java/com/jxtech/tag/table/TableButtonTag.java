package com.jxtech.tag.table;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts2.components.Component;

import com.jxtech.jbo.auth.JxSession;
import com.jxtech.tag.comm.JxBaseUITag;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 表格中thead中的Button
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.10
 */
public class TableButtonTag extends JxBaseUITag {

    private static final long serialVersionUID = 1157986749559737176L;
    private String mxevent; // 事件
    private String mxtype;// 类型 button,icon
    private String params;// 参数
    private String lookup;
    private String lookupWidth;
    private String lookupHeight;
    private String url;
    private String icon;

    protected boolean permission;// 是否有此功能的权限

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new TableButton(stack, request, response);
    }

    @Override
    protected void populateParams() {
        super.populateParams();
        TableButton tb = (TableButton) component;
        tb.setMxevent(mxevent);
        tb.setMxtype(mxtype);
        tb.setLookup(lookup);
        tb.setLookupWidth(lookupWidth);
        tb.setLookupHeight(lookupHeight);
        tb.setParams(params);
        tb.setUrl(url);
        tb.setIcon(icon);
        Tag tag = getParent();
        permission = true;
        // 判断按钮是否有权限，通过按钮ID来确定
        if (tag instanceof TableTag) {
            TableTag tg = (TableTag) tag;
            permission = JxSession.hasPermission(tg.getAppName(), id);
        }
        tb.setPermission(permission);
        tb.setReadonly(readonly);
        tb.setVisible(visible);
    }

    @Override
    public int doStartTag() throws JspException {
        int dst = super.doStartTag();
        Tag tag = getParent();
        if (tag instanceof TableTag) {
            TableTag table = (TableTag) tag;
            table.getTableButtons().add((TableButton) component);
        }
        return dst;
    }

    public String getMxevent() {
        return mxevent;
    }

    public void setMxevent(String mxevent) {
        this.mxevent = mxevent;
    }

    public String getMxtype() {
        return mxtype;
    }

    public void setMxtype(String mxtype) {
        this.mxtype = mxtype;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getLookup() {
        return lookup;
    }

    public void setLookup(String lookup) {
        this.lookup = lookup;
    }

    public boolean isPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
}

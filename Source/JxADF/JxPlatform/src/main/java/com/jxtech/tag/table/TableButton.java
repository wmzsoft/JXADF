package com.jxtech.tag.table;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 表格中thead中的Button
 *
 * @author wmzsoft@gmail.com
 * @date 2013.10
 */
@StrutsTag(name = "TableButton", tldTagClass = "com.jxtech.tag.table.TableButtonTag", description = "TableButton")
public class TableButton extends JxBaseUIBean {

    private String mxevent; // 事件
    private String mxtype;// 类型 button,icon
    private String params;// 参数
    private String lookup;
    private String lookupWidth;
    private String lookupHeight;
    private String url;
    private String icon;

    protected boolean permission;// 是否有此功能的权限

    public TableButton(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "table/tablebutton";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "table/tablebutton-close";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (mxevent != null) {
            this.addParameter("mxevent", findString(mxevent));
        }
        if (mxtype != null) {
            addParameter("mxtype", findString(mxtype).toUpperCase());
        }
        if (params != null) {
            addParameter("params", findString(params));
        }
        if (params != null) {
            addParameter("params", findString(params));
        }

        if (lookup != null) {
            addParameter("lookup", findString(lookup));
        }
        if (lookupWidth != null) {
            addParameter("lookupWidth", findString(lookupWidth));
        }
        if (lookupHeight != null) {
            addParameter("lookupHeight", findString(lookupHeight));
        }
        if (url != null) {
            addParameter("url", findString(url));
        }
        if (icon != null) {
            addParameter("icon", findString(icon));
        }
        addParameter("permission", permission);
    }

    public void setMxevent(String mxevent) {
        this.mxevent = mxevent;
    }

    public void setMxtype(String mxtype) {
        this.mxtype = mxtype;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setLookup(String lookup) {
        this.lookup = lookup;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setLookupWidth(String lookupWidth) {
        this.lookupWidth = lookupWidth;
    }

    public void setLookupHeight(String lookupHeight) {
        this.lookupHeight = lookupHeight;
    }
}

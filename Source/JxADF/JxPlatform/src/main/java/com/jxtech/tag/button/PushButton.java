package com.jxtech.tag.button;

import com.jxtech.tag.comm.JxBaseUIBean;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
@StrutsTag(name = "Pushbutton", tldTagClass = "com.jxtech.tag.button.PushButtonTag", description = "Pushbutton")
public class PushButton extends JxBaseUIBean {

    protected String defaultfocus;
    protected String menutype;
    protected String mxevent;
    protected String targetid;
    protected String appType;
    protected boolean permission;// 是否有此功能的权限
    protected String appName;// 应用程序名
    protected String mystyle;// 显示类型，支持：td,none,默认为添加TD
    protected String readonly;
    protected String visible;
    protected String image;// 要显示的图标
    protected String type; // 按钮类型
    protected String ignorepermission;// 忽略权限检查

    public PushButton(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return super.getStdDefaultTemplate("pushbutton", true);
    }

    @Override
    public String getDefaultOpenTemplate() {
        return super.getStdDefaultOpenTemplate("pushbutton", true);
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        if (defaultfocus != null) {
            addParameter("defaultfocus", findValue(defaultfocus, Boolean.class));
        }
        if (menutype != null) {
            addParameter("menutype", findString(menutype).toUpperCase());
        }
        if (mxevent != null) {
            addParameter("mxevent", findString(mxevent));
        }
        if (targetid != null) {
            addParameter("targetid", findString(targetid));
        }
        if (appType != null) {
            addParameter("appType", findString(appType));
        }
        if (appName != null) {
            addParameter("appName", findString(appName));
        }
        if (mystyle != null) {
            addParameter("mystyle", findString(mystyle).toUpperCase());
        }
        if (readonly != null) {
            addParameter("readonly", findValue(readonly, Boolean.class));
        }
        if (image != null) {
            addParameter("image", findString(image));
        }
        if (type != null) {
            addParameter("type", findString(type));
        }
        if (visible == null || "".equals(visible) || "true".equals(visible)) {
            addParameter("visible", true);
        }
        if (ignorepermission != null) {
            addParameter("ignorepermission", findValue(ignorepermission, Boolean.class));
        }
        addParameter("permission", permission);

        addParameter("label", getI18NValue(label));
    }

    @StrutsTagAttribute(description = "setDefaultfocus", type = "Boolean")
    public void setDefaultfocus(String defaultfocus) {
        this.defaultfocus = defaultfocus;
    }

    @StrutsTagAttribute(description = "setMenutype", type = "String")
    public void setMenutype(String menutype) {
        this.menutype = menutype;
    }

    @StrutsTagAttribute(description = "setMxevent", type = "String")
    public void setMxevent(String mxevent) {
        this.mxevent = mxevent;
    }

    @StrutsTagAttribute(description = "setTargetid", type = "String")
    public void setTargetid(String targetid) {
        this.targetid = targetid;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setMystyle(String mystyle) {
        this.mystyle = mystyle;
    }

    @Override
    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    @Override
    public void setVisible(String visible) {
        this.visible = visible;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setIgnorepermission(String ignorepermission) {
        this.ignorepermission = ignorepermission;
    }

}

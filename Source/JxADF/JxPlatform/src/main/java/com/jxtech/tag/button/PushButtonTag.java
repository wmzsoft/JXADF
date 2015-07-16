package com.jxtech.tag.button;

import com.jxtech.jbo.auth.JxSession;
import com.jxtech.tag.body.BodyTag;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.Tag;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 * 
 */
public class PushButtonTag extends JxBaseUITag {

    private static final long serialVersionUID = -5325551764099764420L;
    protected String defaultfocus;
    protected String menutype;
    protected String mxevent;
    protected String targetid;
    protected String appType;
    protected boolean permission;// 是否有此功能的权限
    protected String appName;// 应用程序名
    protected String mystyle;// 显示类型，支持：td,none,默认为添加TD
    protected String image;// 要显示的图标
    protected String type;
    protected String ignorepermission;// 忽略权限检查

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        return new PushButton(stack, request, response);
    }

    @Override
    protected void populateParams() {
        if (!super.isRenderer("pushbutton")) {
            return;
        }
        super.populateParams();
        PushButton pb = (PushButton) component;
        pb.setDefaultfocus(defaultfocus);
        pb.setMenutype(menutype);
        pb.setMxevent(mxevent);
        pb.setTargetid(targetid);
        pb.setAppType(appType);
        pb.setMystyle(mystyle);
        pb.setAppName(appName);
        pb.setIgnorepermission(ignorepermission);
        permission = true;
        Tag tag = findAncestorWithClass(getParent(), BodyTag.class);
        if (tag != null) {
            BodyTag body = (BodyTag) tag;
            if (StrUtil.isNull(body.getAppName()) || StrUtil.isNull(mxevent)) {
            } else {
                permission = JxSession.hasPermission(body.getAppName(), mxevent);
            }
        }
        pb.setPermission(permission);
        pb.setReadonly(readonly);
        pb.setImage(image);
        pb.setVisible(visible);
        pb.setType(type);
    }

    public String getDefaultfocus() {
        return defaultfocus;
    }

    public void setDefaultfocus(String defaultfocus) {
        this.defaultfocus = defaultfocus;
    }

    public String getMenutype() {
        return menutype;
    }

    public void setMenutype(String menutype) {
        this.menutype = menutype;
    }

    public String getMxevent() {
        return mxevent;
    }

    public void setMxevent(String mxevent) {
        this.mxevent = mxevent;
    }

    public String getTargetid() {
        return targetid;
    }

    public void setTargetid(String targetid) {
        this.targetid = targetid;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public boolean isPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getMystyle() {
        return mystyle;
    }

    public void setMystyle(String mystyle) {
        this.mystyle = mystyle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIgnorepermission() {
        return ignorepermission;
    }

    public void setIgnorepermission(String ignorepermission) {
        this.ignorepermission = ignorepermission;
    }
}

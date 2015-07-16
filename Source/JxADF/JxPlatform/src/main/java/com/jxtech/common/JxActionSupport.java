package com.jxtech.common;

import com.jxtech.app.usermetadata.MetaData;
import com.jxtech.jbo.JboIFace;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.base.JxUserInfo;
import com.jxtech.util.ClassUtil;
import com.jxtech.util.ELUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.03.14
 * 
 */
public class JxActionSupport extends ActionSupport {

    private static final long serialVersionUID = 1L;
    private String skinName;

    public JxActionSupport() {
        super();
    }

    @Override
    public String execute() throws Exception {
        ActionContext context = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
        ServletActionContext.getResponse().setHeader("Server", MiscTool.IP);
        if (request != null) {
            request.setAttribute(JxSession.USER_INFO, JxSession.getJxUserInfo());
        }
        return super.execute();
    }

    public String getSkinName() {
        if (skinName == null) {
            skinName = MetaData.getSkin();
        }
        return skinName;
    }

    public void setSkinName(String skinName) {
        this.skinName = skinName;
    }

    public String getUserMetadata(String key) {
        return MetaData.getUserMetadata(key);
    }

    public JxUserInfo getUserInfo() {
        return JxSession.getJxUserInfo();
    }

    public HttpSession getJxSession() {
        return JxSession.getSession();
    }

    public Object getJxSessionkey(String key) {
        return JxSession.getSession(key);
    }

    /**
     * 在FTL中，通过action.invokeStaticMethod(className,method)调用
     * 
     * @param className
     * @param method
     * @return
     */
    public Object invokeStaticMethod(String className, String method) {
        return invokeStaticMethod(className, method, null, null);
    }

    public Object invokeStaticMethod(String className, String method, Class<?> parameterTypes, Object[] params) {
        return ClassUtil.getStaticMethod(className, method, parameterTypes, params);
    }

    public Object getJboElValue(JboIFace jbo, String expression) {
        return ELUtil.getJboElValue(jbo, expression);
    }
}

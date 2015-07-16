package com.jxtech.tag.body;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts2.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxtech.app.jxvars.JxVars;
import com.jxtech.app.jxvars.JxVarsFactory;
import com.jxtech.common.JxResource;
import com.jxtech.jbo.App;
import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.auth.PermissionFactory;
import com.jxtech.jbo.auth.PermissionIFace;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.comm.JxBaseUITag;
import com.jxtech.util.StrUtil;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 * @author wmzsoft@gmail.com
 * @date 2013.08
 */
public class BodyTag extends JxBaseUITag {
    private static final long serialVersionUID = 1L;
    protected String appName;// 应用程序名
    protected String appType;// 应用程序类型
    protected String comtopUrl;
    protected String reportUrl;

    protected String mainApp; // 是否需要作为主要APP，比如Attachment附件一般就不是主应用

    private String msg;// 应用提示消息
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private static final Logger LOG = LoggerFactory.getLogger(BodyTag.class);

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        req = request;
        resp = response;
        return new Body(stack, request, response);

    }

    @Override
    protected void populateParams() {
        if (!super.isRenderer("body")) {
            return;
        }
        super.populateParams();
        Body ui = (Body) component;
        ui.setAppName(appName);
        ui.setAppType(appType);
        try {
            if (appName != null) {
                PermissionIFace perm = PermissionFactory.getPermissionInstance();
                if (!perm.isPermission(appName, req, resp)){
                    return;
                }
                // 默认都为mainApp,附件需要特别指定为false
                if (StrUtil.isNull(mainApp) || "true".equalsIgnoreCase(mainApp)) {
                    JxSession.putApp(appName, appType);
                    ui.setMsg(JxSession.getApp().getMsg());
                } else {
                    // 将app放到主app中的refApp对象中
                    String appNameType = appName + "." + appType;
                    if (null != JxSession.getMainApp()) {
                        if (null == JxSession.getMainApp().getRefApp().get(appNameType)) {
                            try {
                                JxSession.getMainApp().getRefApp().put(appName + "." + appType, new App(appName));
                            } catch (JxException e) {
                                LOG.error(e.getMessage(), e);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        ui.setMainApp(mainApp);
        JxVars vars = JxVarsFactory.getInstance();
        comtopUrl = vars.getValue("jx.comtop.url");
        ui.setComtopUrl(comtopUrl);
        reportUrl = vars.getValue("jx.report.url");
        ui.setReportUrl(reportUrl);
    }

    @Override
    public int doEndTag() throws JspException {
        Body ui = (Body) component;
        ui.setHeadContent(JxResource.getAppHead(req));
        ui.setBodyContent(JxResource.getAppBody(req, appName + "." + appType));
        return super.doEndTag();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getComtopUrl() {
        return comtopUrl;
    }

    public void setComtopUrl(String comtopUrl) {
        this.comtopUrl = comtopUrl;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public void setMainApp(String mainApp) {
        this.mainApp = mainApp;
    }

    public String getMainApp() {
        return this.mainApp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}

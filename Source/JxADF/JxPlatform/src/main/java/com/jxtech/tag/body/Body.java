package com.jxtech.tag.body;

import com.jxtech.jbo.auth.JxSession;
import com.jxtech.jbo.util.JxException;
import com.jxtech.tag.comm.JxBaseUIBean;
import com.jxtech.util.StrUtil;
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
@StrutsTag(name = "body", tldTagClass = "com.jxtech.tag.body.BodyTag", description = "Body")
public class Body extends JxBaseUIBean {
    protected String appName;// 应用程序名
    protected String appType;// 应用程序类型
    protected String comtopUrl;
    protected String reportUrl;
    protected String mainApp;

    private String headContent;// 需要加载的JS代码，放到Body的最前面
    private String bodyContent; // 需要加载的JS代码
    private String msg; // 应用程序消息

    public Body(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    @Override
    protected String getDefaultTemplate() {
        return "body/" + getRenderer() + "body-close";
    }

    @Override
    public String getDefaultOpenTemplate() {
        return "body/" + getRenderer() + "body";
    }

    @Override
    public void evaluateParams() {
        super.evaluateParams();
        addParameter("appName", appName);
        addParameter("appType", appType);
        addParameter("comtopUrl", comtopUrl);
        addParameter("reportUrl", reportUrl);
        if (headContent != null) {
            addParameter("headContent", findString(headContent));
        }
        // msg 格式为 jxAppNameType=msgvalue
        if (null != msg && msg.indexOf("=") >= 0) {
            String[] msgs = msg.split("=");
            if (msgs[0].equalsIgnoreCase(appName + "." + appType)) {
                msg = msgs[1];
            } else {
                msg = "";
            }
        }
        if (mainApp != null) {
            addParameter("mainApp", mainApp);
        }
        addParameter("msg", msg);
        if (!StrUtil.isNull(msg)) {
            try {
                JxSession.getApp().setMsg("");
            } catch (JxException e) {
                // LOG.error(e.getMessage());
            }
        }
        addParameter("lang", JxSession.getUserLang());
        if (bodyContent != null) {
            addParameter("bodyContent", findString(bodyContent));
        }
    }

    @Override
    public boolean usesBody() {
        return true;
    }

    @StrutsTagAttribute(description = "setAppName", type = "String")
    public void setAppName(String appName) {
        this.appName = appName;
    }

    @StrutsTagAttribute(description = "setAppType", type = "String")
    public void setAppType(String appType) {
        this.appType = appType;
    }

    public void setComtopUrl(String comtopUrl) {
        this.comtopUrl = comtopUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setMainApp(String mainApp) {
        this.mainApp = mainApp;
    }

    public void setBodyContent(String js) {
        this.bodyContent = js;
    }

    public void setHeadContent(String headContent) {
        this.headContent = headContent;
    }

}

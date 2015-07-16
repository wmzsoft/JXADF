package com.jxtech.common;

import com.jxtech.app.usermetadata.MetaData;
import com.jxtech.dwr.WebClientBean;
import com.jxtech.jbo.auth.JxSession;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.Calls;
import org.directwebremoting.extend.Replies;
import org.directwebremoting.impl.DefaultRemoter;
import org.directwebremoting.proxy.dwr.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

/**
 * Created by cxm on 2014/9/27
 */
public class DWRSessionService extends DefaultRemoter {

    Logger LOG = LoggerFactory.getLogger(DWRSessionService.class);

    public Replies execute(Calls calls) {
        String methodName = calls.getCall(0).getMethodName();
        if (WebClientBean.IGNORE_PERMISSION.indexOf(methodName + ",") < 0) {
            HttpSession session = WebContextFactory.get().getSession();
            Object su = session.getAttribute(JxSession.USER_INFO);
            if (su == null) {
                logOut();
                return super.execute(new Calls());
            }
        }
        return super.execute(calls);
    }

    public void logOut() {
        // 超时退出到首页
        WebContext wct = WebContextFactory.get();
        StringBuilder sb = new StringBuilder();
        sb.append("window.top.location = '");
        sb.append(wct.getHttpServletRequest().getContextPath());
        sb.append(MetaData.getUserMetadata(MetaData.LOGOUT));
        sb.append("';");
        ScriptBuffer scriptStr = new ScriptBuffer(sb.toString());
        Util utilThis = new Util(wct.getScriptSession());
        utilThis.addScript(scriptStr);
    }

}

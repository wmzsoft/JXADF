<%@ page language="java" pageEncoding="UTF-8" import="com.jxtech.app.usermetadata.MetaData" %>
<%@ page import="com.jxtech.jbo.auth.JxSession,com.jxtech.jbo.base.JxUserInfo,com.jxtech.db.util.JxDataSourceUtil" %>
<%@ page import="com.jxtech.util.BrowserUtils" %>
<%
    String path = request.getContextPath();
    String LOCAL_PARAM = "locale";
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
    String loginId = null;
    if (!JxDataSourceUtil.isDbconntected()){
        String url = System.getProperty("com.jxtech.install.url");
        response.sendRedirect(basePath + url);
    }else{
        JxUserInfo userInfo = JxSession.getJxUserInfo();
        if (userInfo != null) {
            loginId = userInfo.getLoginid();
        }
        if ((loginId == null || "".equals(loginId))  && request.getRemoteUser() == null) {
            loginId = JxSession.getUserid(request);
            if (!JxSession.loginBySsoUser(loginId)){
                loginId=null;
            }else{
                String renderer = request.getParameter("renderer");
                if (null==renderer ||  "".equals(renderer)) {
                    if (BrowserUtils.isPhone(request)) {
                        // 如果是手机端，强行使用Bootstrap
                        request.getSession().setAttribute(JxSession.RENDERER, "bootstrap");
                    }
                }else{
                    request.getSession().setAttribute(JxSession.RENDERER, renderer);
                }                
            }
        } 
        if ((loginId == null || "".equals(loginId))  && request.getRemoteUser() == null) {
            response.sendRedirect(basePath + MetaData.getUserMetadata("LOGIN"));
        } else {
            String ps = request.getQueryString();
            String url = MetaData.getHomePage();
            if ( ps != null){
                if (url.indexOf("?") != -1 ){
                    url += "&" + ps;
                }else{
                    url += "?" + ps;
                }
            }
            response.sendRedirect(basePath + url);
        }
    }
%>

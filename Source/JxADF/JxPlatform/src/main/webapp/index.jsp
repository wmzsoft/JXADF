<%@ page language="java" pageEncoding="UTF-8" import="com.jxtech.app.usermetadata.MetaData" %>
<%@ page import="com.jxtech.jbo.auth.JxSession,com.jxtech.jbo.base.JxUserInfo" %>
<%
    String path = request.getContextPath();
    String LOCAL_PARAM = "locale";
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
    String loginId = null;
    JxUserInfo userInfo = JxSession.getJxUserInfo();
    if (userInfo != null) {
        loginId = userInfo.getLoginid();
    }
    if ((loginId == null || "".equals(loginId))  && request.getRemoteUser() == null) {
        response.sendRedirect(basePath + MetaData.getUserMetadata("LOGIN"));
    } else {
        String lang = request.getParameter(LOCAL_PARAM);
        String url = MetaData.getHomePage();
        if ( lang != null){
            if (url.indexOf("?") != -1 ){
                url += "&" + LOCAL_PARAM + "=" + lang;
            }else
                url += "?" + LOCAL_PARAM + "=" + lang;
        }
        response.sendRedirect(basePath + url);
    }
%>

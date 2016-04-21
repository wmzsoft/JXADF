<%@ page language="java" pageEncoding="UTF-8" import="com.jxtech.jbo.auth.JxSession"%>

<%
    String contextPath = request.getContextPath();
    JxSession.logout();
   if (request.getRemoteUser() !=  null) {
       String loginUrl = System.getProperty("CAS_SERVER_URL");

       if (loginUrl != null) {
           String logoutUrl = loginUrl.replace("/login", "/logout");
           String appIndex = request.getRequestURL().toString();

           int pos = appIndex.indexOf(contextPath);
           appIndex = appIndex.substring(0, pos + contextPath.length());
           response.sendRedirect(logoutUrl + "?service=" + appIndex);
        }
    }
    else
        response.sendRedirect(contextPath+"/");
%>

<html>
<head>
    <title>你已安全退出本系统</title>
</head>
<body>
你已安全退出本系统,准备跳转到登录页面！
</body>
</html>

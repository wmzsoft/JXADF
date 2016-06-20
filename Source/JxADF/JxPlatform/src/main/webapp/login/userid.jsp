<%@ page import="com.jxtech.jbo.auth.JxSession" %>
<%@ page import="com.jxtech.jbo.base.JxUserInfo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
JxUserInfo usr=(JxUserInfo)session.getAttribute(JxSession.USER_INFO);
if(usr!=null){%><%=usr.getUserid()%><%}%>
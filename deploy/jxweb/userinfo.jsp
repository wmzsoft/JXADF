<%@ page import="com.jxtech.jbo.auth.JxSession" %>
<%@ page import="com.jxtech.jbo.base.JxUserInfo" %>
<%@ page contentType="text/html;charset=GBK" language="java" %>
<html>
<head><title>SNA登录用户信息</title></head>
<body>
<span style="color:#ff0000">SNA登录验证:
   <%
       JxUserInfo usr=(JxUserInfo)session.getAttribute(JxSession.USER_INFO);
       if(usr!=null) out.println("当前用户:"+usr.getUser().getString("name"));
       else out.println("用户没有登录");
   %>
   </span>

</body>
</html>
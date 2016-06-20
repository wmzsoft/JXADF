<%@ page import="com.jxtech.jbo.auth.JxSession" %>
<%@ page import="com.jxtech.jbo.base.JxUserInfo" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String tourl = request.getParameter("oldurl");
    if (tourl !=null && !"".equals(tourl)){
        if ( tourl.indexOf('?')<0 ){
            tourl = tourl + "?";
        }else{
            tourl = tourl + "&";
        }
        try{
            JxUserInfo usr=(JxUserInfo)session.getAttribute(JxSession.USER_INFO);
            if(usr!=null){
                tourl = tourl + "jsessionid="+request.getRequestedSessionId();
                String referer = request.getParameter("referer");
                if (referer !=null && !"".equals(referer)){
                    tourl = tourl + "&referer="+URLEncoder.encode(referer, "UTF-8");
                }
            }else{
                tourl = tourl + "jsessionid=1";
            }
        }catch(Exception e){}
        response.sendRedirect(tourl);
    }
%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page import="com.jxtech.app.usermetadata.MetaData" %>
<%@ page import="com.jxtech.distributed.Distributed,com.jxtech.distributed.DistributedFactory" %>
<%@ taglib prefix="s" uri="/WEB-INF/tlds/struts-tags.tld"%>
<%
	String path = request.getContextPath();
	StringBuilder sb = new StringBuilder();
	sb.append(request.getScheme()).append("://");
	sb.append(request.getServerName());
	
	int port = request.getServerPort();
	if (port !=80){
		sb.append(":").append(port);
	}
	sb.append(request.getAttribute("javax.servlet.forward.request_uri").toString());
	String query=request.getQueryString();
	if (query !=null){
	    sb.append("?").append(query);
	}
	Distributed dist = DistributedFactory.getDistributed();
	String url404 = sb.toString();
	String myurl = dist.getDistributedUrl(url404);
	if (!url404.equals(myurl)){
	    response.sendRedirect(myurl);
	}else{
	    String skinName = MetaData.getSkin();
	   	if (null==skinName){
	   		skinName="default";
	   	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
       <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
       <link href="<%=path%>/skin/<%=skinName%>/css/default.css" rel="stylesheet" type="text/css"/>
    </head>
    
	<body>
          <div class="warm">
              <img src="<%=path%>/skin/<%=skinName%>/images/home/404-file.png" width="520" height="311">
          </div>
	</body>
</html>
<%    
	}
%>
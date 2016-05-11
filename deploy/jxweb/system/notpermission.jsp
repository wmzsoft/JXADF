<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page import="com.jxtech.app.usermetadata.MetaData" %>
<%@ taglib prefix="s" uri="/WEB-INF/tlds/struts-tags.tld"%>
<%
    String path = request.getContextPath();
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
        <style>
            .notpermission{
                width: 100%;
            }
            .notpermission img{
                display: block;
                max-width: 100%;
                margin: 0 auto;
            }
        </style>
    </head>
    
	<body>
          <div class="notpermission">
              <img src="<%=path%>/skin/<%=skinName%>/images/home/403.jpg">
          </div>
	</body>
</html>
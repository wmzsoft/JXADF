<%@ page import="com.jxtech.common.MiscTool"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="jxui" uri="/WEB-INF/tlds/jxui.tld" %>
<%@ taglib prefix="jxfn" uri="/WEB-INF/tlds/jxfunc.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    response.setHeader("P3P", "CP=CAO PSA OUR");
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String skin = "default";
    response.setHeader("Server", MiscTool.IP);
    request.setAttribute("server", MiscTool.IP);
    request.setAttribute("osgi", "");
    response.resetBuffer();
%>

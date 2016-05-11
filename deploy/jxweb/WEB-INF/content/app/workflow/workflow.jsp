<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.jxtech.i18n.JxLangResourcesUtil" %>
<%@ include file="/WEB-INF/content/common/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta name="msapplication-tap-highlight" content="no"/>
    <meta name="viewport" content="user-scalable=no, initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width"/>

    <title><%=JxLangResourcesUtil.getString("app.workflow.TITLE")%></title>
    <link href='<%=path%>/javascript/bootstrap/bootstrap/3.3.4/css/bootstrap.min.css' rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="<%=path%>/skin/default/css/default.css" title="skin"/>
	<script type='text/javascript' src='<%=path%>/javascript/jquery-ui-1.10.3/jquery-1.9.1.min.js'></script>

    <script type="text/javascript">
        var jx_appType = "workflow";
        $(function () {
            $('#ok').tooltip(
                {
                    content: function () {
                        return "<li>同意-发送<ul><li>1、如果流程未启动则启动流程。</li><li>2、如果流程已启动，则发送到下一节点。</li></ul></li>";
                    }
                });
        });
        function viewRouteHistory(me,e){
        	var appname=$("#fromApp").val();
        	var insid=getUrlParamValue(window.location.href,"instanceid");
        	var myurl="./app.action?app=workflow&type=workflowinfo&appname="+appname+"&uid="+$("#fromUid").val()+"&instanceid="+insid;
        	windowopen(myurl,'_vh');
        }
    </script>
</head>
<body>
<jxui:workflow id="s"/>
<jxui:form id="workflow"> 
	<div class="input-group">
        <jxui:pushbutton label="{app.workflow.ROUTECOMMON}" mxevent="routeCommon" id="routeCommonBtn"
                         style="display:none" title='<%=JxLangResourcesUtil.getString("app.workflow.ROUTECOMMON")%>' menutype="INPUT"/>
        <jxui:pushbutton label="{app.workflow.APPDIALOGCLOSE}" mxevent="appDialogClose"
                         title='<%=JxLangResourcesUtil.getString("app.workflow.APPDIALOGCLOSETIP")%>'
                         menutype="INPUT"/>
        <jxui:pushbutton label="{app.workflow.viewhistory}" mxevent="viewRouteHistory"
                         menutype="INPUT"/>
	</div>
	<div class="panel panel-info" id="actionstr">
		<div class="panel-heading">
			<h3 class="panel-title"><%=JxLangResourcesUtil.getString("app.workflow.OPERATION") %></h3>
		</div>
  		<div class="panel-body">
			<span id="actionContent"></span>
		</div>
	</div>
	<div class="panel panel-default" id="memotr">
  		<div class="panel-heading">
    		<h3 class="panel-title"><%=JxLangResourcesUtil.getString("app.workflow.OPINION") %>
    		<span id="noteRequired" style="display:none"><font color="red">*</font></span>
    		</h3>
  		</div>
	    <div class="panel-body">
	    	<jxui:textbox label="" name="note" id="note" cols="90" rows="6" mystyle="none"/>
	    </div>
	</div>
</jxui:form>
<script type='text/javascript' src='<%=path%>/javascript/jxkj/jxcommon.js'></script>
<script type='text/javascript' src='<%=path%>/javascript/jxkj/jx.inputtexthelper.js'></script>
<script type='text/javascript' src='<%=path%>/javascript/bootstrap/bootstrap/3.3.4/js/bootstrap.min.js'></script>
</body>
</html>


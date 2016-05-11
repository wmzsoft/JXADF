<%@ include file="/WEB-INF/content/common/fragment-header.jsp"%>
<%
request.setAttribute("departid", request.getParameter("departid"));
%>
<jxui:table jboname="PUB_DEPARTMENT" id="plisttable" 
	apprestrictions=" DEPARTMENT_ID=${departid} "
	selectmode="none" label="当前部门" footVisible="false">
	<jxui:tablecol label="选择" dataattribute="DEPARTMEN_ID" mxevent="lookup" />
	<jxui:tablecol dataattribute="NAME" label="当前部门名称" />
</jxui:table>


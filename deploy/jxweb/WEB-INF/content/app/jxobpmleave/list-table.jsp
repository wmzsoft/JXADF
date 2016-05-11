<%@ include file="/WEB-INF/content/common/fragment-header.jsp"%>
<jxui:table jboname="JX_OBPM_LEAVE" id="listtable" selectmode="multiple">
	<jxui:tablecol dataattribute="LEAVE_NUM" type="link" mxevent="selectrecord" />
	<jxui:tablecol dataattribute="LEAVE_DESC" />
	<jxui:tablecol dataattribute="LEAVE_DAYS" />
	<jxui:tablecol dataattribute="LEAVE_START" />
	<jxui:tablecol dataattribute="CREATOR_ID" />
	<jxui:tablecol dataattribute="CREATE_DATE" />
	<jxui:tablecol dataattribute="WFT_TRANSACTOR" />
	<jxui:tablecol dataattribute="WFSTATUS.DESCRIPTION" mxevent="link" urlTarget="workflowinfo" urlType="me" urlParamName="app.action?app=workflow&type=workflowinfo&" urlParamValue="appname:'JXOBPMLEAVE',uid:JX_OBPM_LEAVE_ID,instanceid:WFT_INSTANCEID"/>

</jxui:table>

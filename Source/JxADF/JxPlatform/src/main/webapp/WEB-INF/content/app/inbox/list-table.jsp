<%@ include file="/WEB-INF/content/common/fragment-header.jsp"%>
<jxui:table jboname="WFINBOX" id="listtable" selectmode="none" apprestrictions="ASSIGNER = '${jxfn:userProperty('userid')}'">
    <jxui:tablecol dataattribute="WFINBOXAPP.DESCRIPTION" mxevent="link" urlTarget="_self" urlType="me" urlParamName="\${APP}/index.action"/>
    <jxui:tablecol dataattribute="DESCRIPTION" mxevent="link" urlTarget="_self" urlType="me" urlParamName="\${APP}/index_main.action?uid=\${OWNERID}"/>
    <jxui:tablecol dataattribute="STATUS" mxevent="link" urlTarget="workflowinfo" urlType="me" urlParamName="app.action?app=workflow&type=workflowinfo&" urlParamValue="appname:APP,uid:OWNERID,instanceid:WFID"/>
	<jxui:tablecol dataattribute="APP" />
	<jxui:tablecol dataattribute="OWNERID" />
	<jxui:tablecol dataattribute="WFINBOXSENDER.NAME" />
	<jxui:tablecol dataattribute="STARTDATE" />
</jxui:table>
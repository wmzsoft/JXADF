<%@ include file="/WEB-INF/content/common/fragment-header.jsp"%>
<jxui:table jboname="WFINBOX" id="listtable" selectmode="none" apprestrictions="ASSIGNER = '${jxfn:userProperty('userid')}'">
    <jxui:tablecol dataattribute="WFINBOXAPP.DESCRIPTION" secondAttributes="APP" mxevent="link" urlTarget="_self" urlType="me" urlParamName="\${WFINBOXAPP.appurl}"/>
    <jxui:tablecol dataattribute="DESCRIPTION" mxevent="gotoAppFromInbox" secondAttributes="OWNERID" type="LINK"/>
	<jxui:tablecol dataattribute="WFINBOXSENDER.DISPLAYNAME" />
    <jxui:tablecol dataattribute="STARTDATE" mxevent="link" urlTarget="workflowinfo" urlType="me" urlParamName="app.action?app=workflow&type=workflowinfo&" urlParamValue="appname:APP,uid:OWNERID,instanceid:WFID"/>
	<jxui:tablecol dataattribute="MEMO" />
</jxui:table>
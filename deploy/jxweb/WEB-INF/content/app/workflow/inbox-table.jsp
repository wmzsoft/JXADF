<%@ include file="/WEB-INF/content/common/fragment-header.jsp"%>
 	  <jxui:table jboname="inboxview" id="inboxtable" selectmode="none" 
 	  			apprestrictions="upper(transactorid)=upper('${sessionScope.jxuserinfo.userid}')">
	  	<jxui:tablecol  dataattribute="INBOXVIEWWORKFLOW_ID.description||'-'||taskname||'【'||num||'】'" label="任务" sortable="false"
	  		 mxevent="link" urlType="me" urlParamName="app.action?type=list&app=" urlParamValue="INBOXVIEWWORKFLOW_ID.APP" urlTarget="_top"
	  		 />
	  </jxui:table>

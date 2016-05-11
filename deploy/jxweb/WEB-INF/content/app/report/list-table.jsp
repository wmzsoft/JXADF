<%@ include file="/WEB-INF/content/common/fragment-header.jsp"%>
<jxui:table jboname="PS_CSG_REPORT" id="listtable" selectmode="multiple"
	trwhereattr="WFT_BACKFLAG" trwhere="==" trwherevalue="T"
	trfontcolor="red" bottomtipvalue="注：红色表示回退的单据。" bottomtipcolor="red"
	apprestrictions="DEPARTMENT_LEVEL=3 and AUDIT_STATUS is not null ${jxfn:projectCause(sessionScope.jxuserinfo,' and project_id=')}"
	orderby ="REPORT_YEAR desc,REPORT_MONTH desc"
	>

	<jxui:tablecol dataattribute="REPORT_YEAR||'年'||REPORT_MONTH||'月'"
		label="报送年月" mxevent="link" urlType="comtop"
		urlParamName="/lcam/project/pgcproject/pgcproject/reportMultiOperateAction.do?type=list&actionType=InitUpdate&"
		urlParamValue="psCsgReportId:PS_CSG_REPORT_ID"
		sortable="flase" />
	<jxui:tablecol dataattribute="DEPARTMENT_NAME" />
	<jxui:tablecol dataattribute="CREATOR" label="申请人" />
	<jxui:tablecol label="A1_基建关键指标(KPI)<br/>完成情况统计表" urlType="comtop" mxevent="link"  mxevent_icon="exePlan.gif"
		urlParamName="/lcam/project/pgcproject/pgcproject/reportMultiOperateAction.do?tab=A9&type=list&actionType=InitUpdate&"
		urlParamValue="psCsgReportId:PS_CSG_REPORT_ID"
		/>
	<%-- <jxui:tablecol label="A2_基建工程安全事故<br/>完成情况统计表" />
	<jxui:tablecol label="A3_基建工程WHS检查<br/>完成情况统计表" />
	<jxui:tablecol label="A4_基建工程进度<br/>完成情况统计表" />
	<jxui:tablecol label="A7_设计<br/>完成情况统计表" /> --%>
	<jxui:tablecol label="A9_电源基建项目<br/>综合信息"  urlType="comtop" mxevent="link" mxevent_icon="exePlan.gif"
                   urlParamName="/lcam/project/pgcproject/pgcproject/reportMultiOperateAction.do?type=list&actionType=InitUpdate&"
                   urlParamValue="psCsgReportId:PS_CSG_REPORT_ID"/>
				   
	<jxui:tablecol dataattribute="wft_transactor" align="center" maxlength="7" type="link"
		mxevent="workflow" mxevent_desc="lcam/project" />




	<%-- <jxui:tablecol label="流程信息" sortable="false" type="link"
		mxevent="workflow" mxevent_desc="lcam/project" />--%>
		
</jxui:table>
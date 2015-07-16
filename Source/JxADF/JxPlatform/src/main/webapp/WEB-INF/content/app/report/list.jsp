<%@ include file="/WEB-INF/content/common/header.jsp"%>
<jxui:head title="统计月报报送-列表" />
<script type="text/javascript">
	
</script>
<jxui:body appName="report" appType="list">
	<jxui:form id="report">
		<jxui:section type="head">
			<jxui:sectionrow>

				<jxui:sectioncol cssClass="head_left">
					<jxui:label value="统计月报报送-列表" type="title" />
					<%-- <jxui:select label="项目" dataattribute="project_id"
						inputmode="queryImmediately" jboname="ps_project"
						wherecause="${sessionScope.jxuserinfo.projectCause}" displayname="PROJECT_NAME"
						fragmentid="listtable" mystyle="none"  options="null:--全部--"/> --%>

					<jxui:select label="基建项目" name="projectid"
						dataattribute="project_id" required="true" jboname="ps_project"
						fragmentid="listtable"
						visible="${(sessionScope.jxuserinfo.projectid=='NULL'||sessionScope.jxuserinfo.projectid==null) && sessionScope.jxuserinfo.user.data['user_type']==0}"
						wherecause="${sessionScope.jxuserinfo.projectCause}"
						displayname="PROJECT_NAME" mystyle="none" orderby="create_date"
						options="null:--全部--" inputmode="queryImmediately"
						style="width:150px;" />
					<!-- 收件箱 -->
					<jxui:appinbox fragmentId="listtable" statusColumn="audit_status"  creatorColumn="creator_id" backflagColumn="WFT_BACKFLAG"/>										
				</jxui:sectioncol>

				<jxui:sectioncol cssClass="head_right">
					<jxui:buttongroup>
						<jxui:pushbutton id="add" label="新建" mxevent="addComtop"
							mxevent_desc="/lcam/project/pgcproject/pgcproject/reportMultiOperateAction.do?type=list&actionType=InitInsert" />
						<jxui:pushbutton label="删除" mxevent="delList"
							fragmentid="listtable" />
						<%-- <jxui:pushbutton label="查询" mxevent="search"
							fragmentid="listtable" appType="list-search" /> --%>
					</jxui:buttongroup>
				</jxui:sectioncol>
			</jxui:sectionrow>

			<jxui:sectionrow>
				<jxui:sectioncol cssClass="head_left">
					<jxui:select dataattribute="REPORT_YEAR" id="yearSelector"
						inputmode="queryImmediately" label="年度" mystyle="none"
						displayvalue="REPORT_YEAR" displayname="YEAR"
						sql="select REPORT_YEAR,REPORT_YEAR||'年' as YEAR from PS_CSG_REPORT group by REPORT_YEAR"
						fragmentid="listtable">
					</jxui:select>

					<jxui:select dataattribute="REPORT_MONTH" id="monthSelector"
						inputmode="queryImmediately" label="月份" mystyle="none"
						fragmentid="listtable"
						options="null:全部,1:1月,2:2月,3:3月,4:4月,5:5月,6:6月,7:7月,8:8月,9:9月,10:10月,11:11月,12:12月">
					</jxui:select>
				</jxui:sectioncol>
			</jxui:sectionrow>

		</jxui:section>
		<jxui:fragment id="listtable" type="list-table" />
	</jxui:form>
	<jxui:footer />
</jxui:body>


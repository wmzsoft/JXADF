<%@ include file="/WEB-INF/content/common/header.jsp"%>
<jxui:head title="请假管理-查询"/>
<jxui:body appName="jxobpmleave" appType="list">
	<jxui:form id="maxmenu-query" jboname="JX_OBPM_LEAVE">
		<jxui:section type="edit">		
		 <jxui:sectionrow>
				<jxui:textbox dataattribute="LEAVE_NUM" inputmode="query"/>
				<jxui:textbox dataattribute="LEAVE_DESC" inputmode="query"/>
			</jxui:sectionrow>
		</jxui:section>
		<jxui:section type="edit">
			<jxui:sectionrow type="head_right">
				<jxui:sectioncol style="text-align:center">
					<jxui:pushbutton label="执行查询" mxevent="searchOk" />
					<jxui:pushbutton label="全部数据" mxevent="searchAll" />
					<jxui:pushbutton label="清空" mxevent="searchClear" />
					<jxui:pushbutton label="关闭" mxevent="appDialogClose" />
				</jxui:sectioncol>
			</jxui:sectionrow>
		</jxui:section>
	</jxui:form>
</jxui:body>

<%@ include file="/WEB-INF/content/common/header.jsp"%>
<jxui:head title="角色-查询"/>
<jxui:body appName="maxmenu" appType="list">
	<jxui:form id="maxmenu-query" jboname="maxmenu">
		<jxui:section type="edit">		
		 <jxui:sectionrow>
				<jxui:textbox dataattribute="role_id" inputmode="query"/>
				<jxui:textbox dataattribute="name" inputmode="query"/>
			</jxui:sectionrow>	 
			<jxui:sectionrow>							
				<jxui:textbox dataattribute="description" inputmode="query" colspan="3"/>
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

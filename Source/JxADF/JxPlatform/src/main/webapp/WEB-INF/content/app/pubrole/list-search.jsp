<%@ include file="/WEB-INF/content/common/header.jsp"%>
<jxui:head title="角色-查询"/>
<jxui:body appName="pubrole" appType="list">
	<jxui:form id="pubrole-query" jboname="pubrole">
		<jxui:section type="edit">		
		 <jxui:sectionrow>
				<jxui:textbox dataattribute="role_id" label="角色ID" inputmode="query"/>
				<jxui:textbox dataattribute="name" label="角色名称" inputmode="query"/>
			</jxui:sectionrow>	 
			<jxui:sectionrow>							
				<jxui:textbox dataattribute="description" label="描述" inputmode="query" colspan="3"/>
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

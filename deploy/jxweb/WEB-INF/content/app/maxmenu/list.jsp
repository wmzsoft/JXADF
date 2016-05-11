<%@ include file="/WEB-INF/content/common/header.jsp"%>
<jxui:head title="应用-操作" />
<jxui:body appName="maxmenu" appType="list">
	<jxui:form id="maxmenu">
		<jxui:section type="head">
			<jxui:sectionrow>
				<jxui:sectioncol cssClass="head_left">

				</jxui:sectioncol>
				<jxui:sectioncol cssClass="head_right">
					<jxui:buttongroup>
						<jxui:pushbutton label="新建" mxevent="add"/>
						<jxui:pushbutton label="删除" mxevent="delList"
							fragmentid="listtable" />
						<jxui:pushbutton label="查询" mxevent="search"
							fragmentid="listtable" appType="list-search" />
					</jxui:buttongroup>
				</jxui:sectioncol>
			</jxui:sectionrow>
		</jxui:section>
		<jxui:fragment id="listtable" type="list-table" />
	</jxui:form>
	<jxui:footer />
</jxui:body>


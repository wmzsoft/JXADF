<%@ include file="/WEB-INF/content/common/header.jsp"%>
<jxui:head title="处罚通知录入与问题整改-查询"/>
<jxui:body appName="PUNISHNOTICE" appType="list">
	<jxui:form id="PUNISHNOTICE-QUERY" jboname="PS_CSG_PUNISH_NOTICE">
		<jxui:section type="edit">		
			<jxui:sectionrow>
                <jxui:select label="单位类别：" options='null:--全部--,1:设计,2:监理,3:施工,4:检测,5:咨询' dataattribute="DEPT_TYPE" inputmode="query" />
                <jxui:select label="通知单状态：" options='null:--全部--,1:未下达,2:已下达,3:整改中,4:申诉中,5:已整改,6:已撤销' dataattribute="STATUS" inputmode="query" />
			</jxui:sectionrow>
			<jxui:sectionrow>
                <jxui:select label="是否申诉过：" options='null:--全部--,0:否,1:是' dataattribute="STATUS" inputmode="query" />
                <jxui:select label="是否确认：" options='null:--全部--,1:未确认,2:已确认' dataattribute="STATUS" inputmode="query" />
            </jxui:sectionrow>
            <jxui:sectionrow>
                <jxui:textbox dataattribute="PUNISH_NOTICE_ID" inputmode="query" colspan="3"/>
            </jxui:sectionrow>
            <jxui:sectionrow>
                <jxui:textbox dataattribute="CHECKER_DATE" inputmode="query" colspan="3"/>
            </jxui:sectionrow>
		</jxui:section>
		<jxui:section type="edit">
			<jxui:sectionrow type="head_right">
				<jxui:sectioncol>
					<jxui:pushbutton label="执行查询" mxevent="searchOk" />
					<jxui:pushbutton label="全部数据" mxevent="searchAll" />
					<jxui:pushbutton label="清空" mxevent="searchClear" />
					<jxui:pushbutton label="关闭" mxevent="appDialogClose" />
				</jxui:sectioncol>
			</jxui:sectionrow>
		</jxui:section>
	</jxui:form>
</jxui:body>

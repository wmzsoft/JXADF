<%@ include file="/WEB-INF/content/common/header.jsp" %>
<jxui:head title="部门-查询"/>
<jxui:body appName="pubdepartment" appType="list">
    <jxui:form id="pubdepartment-query" jboname="PUB_DEPARTMENT">
        <jxui:section type="edit">
            <jxui:sectionrow>
                <jxui:textbox dataattribute="DEPARTMENT_ID" inputmode="query"/>
                <jxui:textbox dataattribute="NAME" inputmode="query"/>
            </jxui:sectionrow>
            <jxui:sectionrow>
                <jxui:select options='null:全部,1:有效,0:无效' dataattribute="state" label="有效？" inputmode="query"/>
                <jxui:textbox dataattribute="DESCRIPTION" inputmode="query"/>
            </jxui:sectionrow>
        </jxui:section>
        <jxui:section type="edit">
            <jxui:sectionrow type="head_right">
                <jxui:sectioncol style="text-align:center">
                    <jxui:pushbutton label="执行查询" mxevent="searchOk"/>
                    <jxui:pushbutton label="全部数据" mxevent="searchAll"/>
                    <jxui:pushbutton label="清空" mxevent="searchClear"/>
                    <jxui:pushbutton label="关闭" mxevent="appDialogClose"/>
                </jxui:sectioncol>
            </jxui:sectionrow>
        </jxui:section>
    </jxui:form>
</jxui:body>

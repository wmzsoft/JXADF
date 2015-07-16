<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<jxui:table jboname="PUB_DEPARTMENT" id="listtable"
            apprestrictions=" 1=1" filterable="true"
            selectmode="none" label="当前部门子部门" orderby="code">
    <jxui:tablecol label="选择" dataattribute="TUID" mxevent="lookup"/>
    <jxui:tablecol dataattribute="DEPARTMENT_ID"/>
    <jxui:tablecol dataattribute="NAME" label="子部门名称"/>
</jxui:table>

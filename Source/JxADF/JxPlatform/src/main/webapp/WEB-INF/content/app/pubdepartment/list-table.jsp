<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<jxui:table jboname="PUB_DEPARTMENT" id="listtable" selectmode="multiple" orderby="DEPARTMENT_ID">
    <jxui:tablecol dataattribute="DEPARTMENT_ID" type="link" mxevent="selectrecord" />
    <jxui:tablecol dataattribute="NAME"/>
    <jxui:tablecol dataattribute="DESCRIPTION" />
    <jxui:tablecol dataattribute="STATE" dataDisplay="{'0':'无效','1':'有效'}" align="center"/>
</jxui:table>
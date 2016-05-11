<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<jxui:table id="role_operation_listtable"
            relationship="PUB_ROLE_OPERATION_ID"
            jboname="PUB_ROLE_OPERATION" title="角色 - 操作" inputmode="edit"
            orderby="role_id">

    <jxui:tablecol dataattribute="ROLE_ID" readonly="true"/>
    <jxui:tablecol dataattribute="OPERATION_ID" readonly="true"/>
</jxui:table>

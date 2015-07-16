<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<jxui:table id="role_operation_listtable" relationship="PUB_ROLE_OPERATION_ID"
            jboname="PUB_ROLE_OPERATION" title="角色 - 操作" orderby="role_id">

    <jxui:tablecol dataattribute="PUB_ROLE_OPERA_MAXASPPS.DESCRIPTION" readonly="true"/>
    <jxui:tablecol dataattribute="PUB_ROLE_OPERATIONOPERATION_ID.MENU" readonly="true"/>
    <jxui:tablecol dataattribute="PUB_ROLE_OPERATIONOPERATION_ID.DESCRIPTION" readonly="true"/>
    <jxui:tablecol dataattribute="V_DELETE" mxevent_icon="delrow" mxevent="delrowwithconfirm" width="30"/>
</jxui:table>

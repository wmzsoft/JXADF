<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<jxui:table id="role_user_listtable" footVisible="false"
            relationship="PUB_ROLE_USERROLE_IDP" jboname="PUB_ROLE_USER" orderby="USER_ID">
    <jxui:tablecol dataattribute="USER_ID" readonly="true"/>
    <jxui:tablecol dataattribute="PUB_ROLE_USERROLE_IDP.NAME" readonly="true"/>
    <jxui:tablecol dataattribute="V_DELETE" mxevent_icon="delrow" mxevent="delrowwithconfirm" width="30"/>
</jxui:table>
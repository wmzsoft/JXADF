<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<jxui:table id="user_role" relationship="PUB_ROLE_USERUSER_IDP" inputmode="readonly"
            jboname="PUB_ROLE_USER" selectmode="NONE"
            title="{app.pubrole.ROLE_TITLE}">
    <jxui:tablecol dataattribute="PUB_ROLE_USERROLE_ID.ROLE_NAME" label="{app.pubdepartment.PUB_ROLE_USER.ROLE_ID}"/>
    <jxui:tablecol dataattribute="PUB_ROLE_USERROLE_ID.DESCRIPTION" label="{app.pubdepartment.PUB_ROLE_USER.ROLE_DESCRIPTION}"/>
    <jxui:tablecol dataattribute="V_DELETE" mxevent="delRow"/>
</jxui:table>

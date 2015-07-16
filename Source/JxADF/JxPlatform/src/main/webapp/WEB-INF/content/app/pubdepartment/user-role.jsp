<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<jxui:table id="user_role" relationship="PUB_ROLE_USERUSER_IDP" inputmode="edit"
            jboname="PUB_ROLE_USER" orderby="USER_ID" title="{app.pubrole.ROLE_TITLE}" footVisible="false">

    <jxui:tablebutton mxevent="add" icon="add" lookup="pubrole/lookup" lookupWidth="600" lookupHeight="400"/>
    <jxui:tablecol dataattribute="PUB_ROLE_USERROLE_ID.ROLE_NAME"/>
    <jxui:tablecol dataattribute="PUB_ROLE_USERROLE_ID.DESCRIPTION"/>
    <jxui:tablecol dataattribute="V_DELETE" mxevent="delRow"/>
</jxui:table>

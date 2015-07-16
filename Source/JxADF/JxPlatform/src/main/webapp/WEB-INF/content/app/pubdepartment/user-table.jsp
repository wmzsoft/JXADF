<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<jxui:table jboname="PUB_USER" id="listtable" selectmode="none"
            relationship="PUB_USERDEPARTMENT_IDP">
    <jxui:tablecol dataattribute="LOGIN_ID" label="{app.pubdepartment.PUB_USER.LOGIN_ID}" type="link" mxevent="showUser"
                   required="false"/>
    <jxui:tablecol dataattribute="DISPLAYNAME" label="{app.pubdepartment.PUB_USER.DISPLAYNAME}" required="false"/>
    <jxui:tablecol dataattribute="PUB_USERDEPARTMENT_ID.NAME" required="false"
                   label="{app.pubdepartment.PUB_USER.DEPARTMENT_NAME}"/>
    <jxui:tablecol dataattribute="SEX" width="50" label="{app.pubdepartment.PUB_USER.SEX}" align="center"
                   dataDisplay="{'0':'女','1':'男'}"/>
    <jxui:tablecol dataattribute="ACTIVE" label="{app.pubdepartment.PUB_USER.ACTIVE}" align="center"
                   dataDisplay="{'0':'否','1':'是'}" width="50"/>
</jxui:table>
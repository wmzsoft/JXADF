<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<jxui:table id="user_listtable" footVisible="false"
            relationship="PUB_ROLE_PUB_USER_ALL" selectmode="none"
            jboname="PUB_USER" orderby="USER_ID">

    <jxui:tablecol dataattribute="USER_ID" label="{app.pubrole.PUB_USER.USER_ID}" readonly='true'/>
    <jxui:tablecol dataattribute="LOGIN_ID" label="{app.pubrole.PUB_USER.LOGIN_ID}" readonly='true'/>
    <jxui:tablecol dataattribute="DISPLAYNAME" label="{app.pubrole.PUB_USER.DISPLAYNAME}" readonly="true"/>
    <jxui:tablecol dataattribute="INROLE" mxevent="toggleuser" dataDisplay="{'1':'YES','0':'NO'}"
                   label="{app.pubrole.user.INROLE}" readonly='true' width="60" align="center"/>
</jxui:table>
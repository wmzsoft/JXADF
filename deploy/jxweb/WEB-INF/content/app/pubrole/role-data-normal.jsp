<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<jxui:table id="data_all_listtable" relationship="PUB_ROLE_SEC_ID" title=" "
            jboname="SECURITYRESTRICT" orderby="APP" bottomtipvalue="{app.pubrole.BOTTOM_TIP}">
    <%--<jxui:tablecol dataattribute="SECURITYRESTRICT_MAXAPPS_ID.DESCRIPTION" mxevent="showSec" type="link"/>--%>
    <jxui:tablecol dataattribute="OBJECTNAME" label="{app.pubrole.SECURITYRESTRICT.OBJECTNAME}" readonly="true" mxevent="showSec" type="link"/>
    <jxui:tablecol dataattribute="RESTRICTION" label="{app.pubrole.SECURITYRESTRICT.RESTRICTION}" readonly="false"/>
    <jxui:tablecol mxevent="delrowwithconfirm" mxevent_icon="delrow" dataattribute="V_DELETE"/>
</jxui:table>
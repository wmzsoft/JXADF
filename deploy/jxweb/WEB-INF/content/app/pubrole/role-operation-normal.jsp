<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<jxui:table id="operation_listtable" selectmode="none" relationship="PUB_ROLE_MAXMENU_ALL" footVisible="false"
            jboname="MAXMENU" orderby="POSITION" bottomtipvalue="{app.pubrole.BOTTOM_TIP}">

    <jxui:tablecol dataattribute="MENU" label="{app.pubrole.MAXMENU.MENU}" readonly="true"/>
    <jxui:tablecol dataattribute="DESCRIPTION" label="{app.pubrole.MAXMENU.DESCRIPTION}" readonly="true"/>
    <jxui:tablecol dataattribute="INROLE" mxevent="toggleoperation" dataDisplay="{'1':'YES','0':'NO'}"
                   label="{app.pubrole.operation.INROLE}" align="center"/>
</jxui:table>
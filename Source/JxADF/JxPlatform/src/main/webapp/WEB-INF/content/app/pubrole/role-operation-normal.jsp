<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<jxui:table id="operation_listtable" selectmode="multiple" relationship="PUB_ROLE_MAXMENU_ALL"
            jboname="MAXMENU" orderby="POSITION" bottomtipvalue="{app.pubrole.BOTTOM_TIP}">

    <jxui:tablecol dataattribute="MENU" label="{app.pubrole.MAXMENU.MENU}" readonly="true"/>
    <jxui:tablecol dataattribute="DESCRIPTION" label="{app.pubrole.MAXMENU.DESCRIPTION}" readonly="true"/>
</jxui:table>
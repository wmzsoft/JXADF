<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<jxui:table jboname="MAXMENU" id="listtable" relationship="APP_MENU" title="选择操作"
            selectmode="multiple" label="操作" orderby="APP">
    <jxui:tablebutton id="add" label="确定" mxevent="lookup" mxtype="button"/>
    <jxui:tablecol dataattribute="MENU"/>
    <jxui:tablecol dataattribute="DESCRIPTION"/>
</jxui:table>

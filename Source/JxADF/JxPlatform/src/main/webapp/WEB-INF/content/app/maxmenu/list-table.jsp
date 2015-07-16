<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<jxui:table jboname="MAXMENU" id="listtable" selectmode="multiple"   apprestrictions="">
    <jxui:tablecol dataattribute="menu" type="link" mxevent="selectrecord"/>
    <jxui:tablecol dataattribute="app"/>
    <jxui:tablecol dataattribute="description"/>
</jxui:table>

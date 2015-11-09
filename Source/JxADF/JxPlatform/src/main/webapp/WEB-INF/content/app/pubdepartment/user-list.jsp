<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<jxui:table jboname="PUB_USER" id="listtable" selectmode="none"
            apprestrictions="active=1">
    <jxui:tablecol dataattribute="user_id" />
    <jxui:tablecol dataattribute="DISPLAYNAME"/>
</jxui:table>
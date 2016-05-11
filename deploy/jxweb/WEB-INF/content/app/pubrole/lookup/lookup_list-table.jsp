<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<jxui:table jboname="PUB_ROLE" id="role_lookup-listtable"
            apprestrictions=" 1=1" title=" "
            selectmode="multiple" orderby="ROLE_ID">
    <jxui:tablebutton mxevent="lookup" icon="add"/>

    <jxui:tablecol dataattribute="ROLE_NAME"/>
    <jxui:tablecol dataattribute="DESCRIPTION"/>
</jxui:table>

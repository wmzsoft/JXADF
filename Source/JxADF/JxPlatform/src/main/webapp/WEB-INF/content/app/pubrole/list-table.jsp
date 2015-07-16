<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<jxui:table jboname="PUB_ROLE" id="listtable" selectmode="single"
            footVisible="false" apprestrictions="">
    <jxui:tablecol dataattribute="ROLE_NAME" type="link" mxevent="showRole" required="false"/>
    <jxui:tablecol mxevent="delrow" dataattribute="V_DELETE" width="30px"/>
    <jxui:tablecol dataattribute="ROLE_ID" cssClass="hideMe"/>
</jxui:table>
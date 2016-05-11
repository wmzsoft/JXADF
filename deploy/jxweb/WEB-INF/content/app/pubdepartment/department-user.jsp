<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<jxui:table id="f_department-user" relationship="PUB_USERDEPARTMENT_IDP"
            jboname="PUB_USER" title="部门 - 用户" orderby="USER_ID">

    <jxui:tablecol dataattribute="USER_ID" readonly="true"/>
    <jxui:tablecol dataattribute="NAME" readonly="true"/>
</jxui:table>

<%@ include file="/WEB-INF/content/common/header.jsp" %>
<jxui:head title="应用模块管理"/>
<jxui:body appName="maxapps" appType="main">
    <jxui:form id="maxapps">
        <jxui:tree id="tree" jboname="MAXAPPS" treeNodeKey="APP" i18n="menu"
                   treeNodeParentKey="PARENT" treeNodeName="DESCRIPTION" leafDisplay="true"
                   whereCause=""
                   orderby="ORDERID" async="true"/>
    </jxui:form>
    <jxui:footer/>
</jxui:body>


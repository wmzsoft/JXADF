<%@ include file="/WEB-INF/content/common/header.jsp" %>
<jxui:head title="Menu"/>
<jxui:body appName="maxapps" appType="menu">
    <jxui:form id="maxapps">
        <div class="bg-menu-top">111</div>
        <jxui:tree id="tree" jboname="MAXAPPS" treeNodeKey="APP" i18n="menu"
                   treeNodeParentKey="PARENT" treeNodeName="DESCRIPTION" leafDisplay="true"
                   whereCause=""
                   orderby="ORDERID" async="true"/>
     
    </jxui:form>
    <jxui:footer/>
</jxui:body>


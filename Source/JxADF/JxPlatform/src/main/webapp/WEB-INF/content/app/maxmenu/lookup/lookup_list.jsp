<%@ include file="/WEB-INF/content/common/header.jsp" %>
<jxui:head title="选择应用操作">
    <script type="text/javascript">
        function zTreeOnClick(event, treeId, treeNode) {
            if (!treeNode.isParent) {
                loadData();
            } else {
                var treeObj = $.fn.zTree.getZTreeObj("tree");
                treeObj.expandNode(treeNode);
            }
        }

        function loadData() {
            dwr.engine.setAsync(false); //设置成同步
            WebClientBean.queryJboSetData("maxmenu/LOOKUP.LOOKUP", "APP_MENU","");
            dwr.engine.setAsync(true); //重新设置成异步
            getTableData("div_listtable", null, null, null, 2);
        }
    </script>
</jxui:head>

<jxui:body appName="maxmenu/LOOKUP" appType="LOOKUP" mainApp="false">
    <jxui:form id="maxmenulookup">
        <div id="tree_div" style="float: left;width:30%;height:350px;overflow:auto;">
            <jxui:tree id="tree" jboname="MAXAPPS" treeNodeKey="APP" i18n="menu"
                       treeNodeParentKey="PARENT" treeNodeName="DESCRIPTION" leafDisplay="true"
                       whereCause="" orderby="ORDERID" async="true"/>
        </div>

        <div style="float: right;width: 69%;">
            <jxui:fragment id="listtable" type="lookup_list-table"/>
        </div>
    </jxui:form>
    <jxui:footer/>
</jxui:body>

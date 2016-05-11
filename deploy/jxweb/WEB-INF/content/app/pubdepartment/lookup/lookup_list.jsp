<%@ include file="/WEB-INF/content/common/header.jsp" %>
<jxui:head title="部门树选"/>
<script type="text/javascript">
    //此功能主要是为了点击树节点时，刷新右边的表格,此方法名、方法参数不能修改，内容自己实现
    function zTreeOnClick(event, treeId, treeNode) {
        var tuid = treeNode.tuid;

        dwr.engine.setAsync(false);
        WebClientBean.queryJboSetData(jx_appNameType, "", "tuid in (select tuid from pub_department start with tuid = '" + tuid +
                "' connect by prior tuid = super_department_id)", function (data) {
            getTableData("div_lookup-listtable", null, function () {
                if ($(".bottom").length > 1) {
                    $(".bottom:last").remove();
                }
            }, null, 3);
        });
        dwr.engine.setAsync(true);
    }
</script>
<jxui:body appName="pubdepartment/lookup" appType="LOOKUP" mainApp="false">
    <jxui:form id="pubdepartmentlookup">
        <div id="tree_div" style="float: left;width:30%;height:370px;overflow:auto;">
            <jxui:tree id="tree" jboname="PUB_DEPARTMENT" mode="LOOKUP"
                       treeNodeKey="TUID" treeNodeParentKey="SUPER_DEPARTMENT_ID"
                       treeNodeName="NAME" hasChildName=""
                       whereCause="${wherecause}"
                       async="true"/>
        </div>
        <div style="float: right;width: 69%;">
            <jxui:fragment id="lookup-listtable" type="lookup_list-table"/>
        </div>
    </jxui:form>
    <jxui:footer/>
</jxui:body>

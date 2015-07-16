<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>

<style type="text/css">
    .fillWidth {
        width: 100%;
    }

    .fillTop {
        vertical-align: top !important;
    }

    .ztree {
        padding-top: 5px;
    }

    .hideMe {
        display: none;
    }
</style>

<table class="fillWidth">
    <tr id="tr_operation_all">
        <td class="fillTop">
            <jxui:tree id="app_tree" jboname="MAXAPPS" treeNodeKey="APP" i18n="menu"
                       treeNodeParentKey="PARENT" treeNodeName="DESCRIPTION" leafDisplay="true"
                       whereCause="" mode="ALONE"
                       orderby="ORDERID" async="true"/>
        </td>

        <td class="fillWidth fillTop">
            <jxui:fragment id="operation_listtable" lazyload="true" type="role-operation-normal"/>
        </td>
    </tr>
</table>

<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>

<table class="fillWidth">
    <tr>
        <td colspan="2" class="td_user_show">
            <jxui:label value="{app.pubrole.SHOW_ALL_USER}"/>
            <input type="checkbox" id="userView" class="userView" onchange="toggleView()"/>
        </td>
    </tr>

    <tr id="tr_all">
        <td class="fillTop">
            <jxui:tree id="tree" jboname="PUB_DEPARTMENT" treeNodeKey="DEPARTMENT_ID"
                       treeNodeParentKey="SUPER_DEPARTMENT_ID" treeNodeName="NAME" leafDisplay="true"
                       whereCause="STATE=1" mode="ALONE"
                       orderby="DEPARTMENT_ID" async="true"/>
        </td>

        <td class="fillWidth fillTop">
            <jxui:fragment id="user_listtable" lazyload="true" type="role-user-normal"/>
        </td>
        <td class="hideMe">
            <jxui:fragment id="role_user_listtable" lazyload="true" type="role-user-hide"/>
        </td>
    </tr>

</table>

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
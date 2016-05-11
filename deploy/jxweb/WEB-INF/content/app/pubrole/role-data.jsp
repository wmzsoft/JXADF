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

<jxui:section>
    <jxui:sectionrow>
        <jxui:sectioncol cssClass="fillTop">
            <jxui:tree id="app_data_tree" jboname="MAXAPPS" treeNodeKey="APP" i18n="menu"
                       treeNodeParentKey="PARENT" treeNodeName="DESCRIPTION" leafDisplay="true"
                       whereCause="" relationship="PUB_ROLE_APP_ALL" mode="ALONE"
                       orderby="ORDERID" async="true"/>
        </jxui:sectioncol>

        <jxui:sectioncol cssClass="fillWidth fillTop">
            <jxui:pushbutton style="float:right" label="{app.pubrole.ADD_SEC}" id="addSecBtn" mxevent="addSec"/>
            <jxui:fragment id="data_all_listtable" type="role-data-normal"/>
        </jxui:sectioncol>
    </jxui:sectionrow>
</jxui:section>

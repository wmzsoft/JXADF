﻿
<%@ include file="/WEB-INF/content/common/header.jsp" %>
<jxui:head title="角色管理">
    <link rel='stylesheet' type='text/css' href='<%=path%>/javascript/zTree/css/zTreeStyle/zTreeStyle.css'/>
    <script type='text/javascript' src='<%=path%>/javascript/zTree/js/jquery.ztree.all-3.5.min.js'></script>
    <style>
        body, form {
            margin: 0 !important;
            padding: 0 !important;
        }

        .ui-tabs {
            padding: 0;
        }

        .ui-tabs .ui-tabs-panel {
            padding: 1px;
        }

        .ztree {
            margin: 0 !important;
        }

        .table_main {
            width: 100%;
        }

        .btn_no_column {
            color : white;
            background: red;
            border: none;
            cursor: pointer;
        }
    </style>

    <script type="text/javascript">
        //树点击
        function zTreeOnClick(event, treeId, treeNode, jbo) {
            if (treeId == "tree") {
                var tuid = treeNode.department_id;

                var userView = $("#userView").attr("checked");
                var showAll = "checked" == userView ? "1" : "0";

                var params = showAll + "," + tuid;
                getJboSetMethodReturnValue("PUB_ROLE", "showRoleUser", params);
                getTableData("div_user_listtable");
            } else if ("app_tree" == treeId) {
                //角色 - 操作
                var app = treeNode.app;
                dwr.engine.setAsync(false);
                WebClientBean.queryJboSetData(jx_appNameType, "PUB_ROLE_MAXMENU_ALL", "APP = '" + app + "'", function (data) {
                    getTableData("div_operation_listtable");
                });
                dwr.engine.setAsync(true);
            } else if ("app_data_tree" == treeId) {
                if (treeNode.isParent) {
                    $("#addSecBtn").attr("disabled", "disabled");
                } else {
                    $("#addSecBtn").removeAttr("disabled");
                }
                var app = treeNode.app;
                dwr.engine.setAsync(false);
                WebClientBean.queryJboSetData(jx_appNameType, "PUB_ROLE_SEC_ID", "APP = '" + app + "'",
                        function (data) {
                            getTableData("div_data_all_listtable");
                        });
                dwr.engine.setAsync(true);
            }
        }

        function setScroll(tableId) {
            if (JxUtil.getScroll(document.body).scrollY) {
                var table = $("#" + tableId);
                var headerTr = $("thead tr:last", table);
                var tableHeight = $(window).height() - headerTr.offset().top - headerTr.outerHeight();

                if (tableId == "user_listtable" || tableId == "role_user_listtable") {
                    table.attr("sheight", tableHeight - 70);
                } else if (tableId == "operation_listtable" || tableId == "role_operation_listtable") {
                    table.attr("sheight", tableHeight - 80);
                }
            }
        }

        function toggleView() {
            var zTreeObj = $.fn.zTree.getZTreeObj("tree");
            var nodes = zTreeObj.getSelectedNodes();
            if (nodes.length > 0) {
                $("#" + nodes[0].tId + "_a").click();
            }
        }
    </script>
</jxui:head>
<jxui:body appName="pubrole" appType="main">
    <jxui:form id="pubrole" jboname="PUB_ROLE">
        <jxui:section type="list">
            <jxui:sectionrow>
                <jxui:sectioncol>
                    <jxui:tabgroup id="details">
                        <jxui:tab title="{app.pubrole.tabUserTile}"
                                  page="app.action?app=pubrole&type=role-user"/>

                        <jxui:tab title="{app.pubrole.tabOperationTile}"
                                  page="app.action?app=pubrole&type=role-operation"/>

                        <jxui:tab title="{app.pubrole.tabDataTile}"
                                  page="app.action?app=pubrole&type=role-data"/>
                    </jxui:tabgroup>
                </jxui:sectioncol>
            </jxui:sectionrow>
        </jxui:section>
    </jxui:form>
    <jxui:footer/>
</jxui:body>

<script type="text/javascript">

    var afterTreeInit = JxUtil.extend(afterTreeInit, function (zTree) {

        var height = $(window).height();

        //修该tree的高度
        $(".ztree").css({
            "width": 220,
            "height": height - 80,
            "overflow": "auto"
        });

        if (zTree.setting.treeId == "tree") {
            var node = zTree.getNodeByTId("tree_1");
            if (node) {
                $("#tree_1_a").click();
            }
        } else if (zTree.setting.treeId == "app_tree") {
            var node = zTree.getNodeByTId("app_tree_1");
            if (node) {
                $("#app_tree_1_a").click();
            }
        }

    });

    var beforeDataTableLoad = JxUtil.extend(beforeDataTableLoad, function (tableId) {
        $("input[value='NO']").removeClass("btn_column").addClass("btn_no_column");
        setScroll(tableId);
    }, true);


    /**
     * 切换用户是否在此角色下
     * @param me
     * @param e
     */
    function toggleuser(me, e) {
        $(me).attr("disabled", "disabled");
        var params = $(me).val() == "YES" ? "0" : "1";
        params += ",";
        params += $("td[dataattribute='USER_ID'] span", $(me).closest("tr")).text();

        var result = getJboSetMethodReturnValue("PUB_USER", "toggleUserInRole", params);

        if ("ok" == result) {
            $(me).val($(me).val() == "YES" ? "NO" : "YES");
            if ("YES" == $(me).val()) {
                $(me).removeClass("btn_no_column").addClass("btn_column");
            } else {
                $(me).removeClass("btn_column").addClass("btn_no_column");
            }
        } else {
            alert(getLangString("pubrole.toggleUserInRoleFailed"));
        }
        $(me).removeAttr("disabled");
    }

    /**
     * 切换角色授权操作
     * @param me
     * @param e
     */
    function toggleoperation(me, e, uid) {
        $(me).attr("disabled", "disabled");
        var params = $(me).val() == "YES" ? "0" : "1";
        params += ",";
        params += uid;

        var result = getJboSetMethodReturnValue("MAXMENU", "toggleOperationInRole", params);

        if ("ok" == result) {
            $(me).val($(me).val() == "YES" ? "NO" : "YES");
            if ("YES" == $(me).val()) {
                $(me).removeClass("btn_no_column").addClass("btn_column");
            } else {
                $(me).removeClass("btn_column").addClass("btn_no_column");
            }
        } else {
            alert(getLangString("pubrole.toggleUserInRoleFailed"));
        }
        $(me).removeAttr("disabled");
    }


    // role-data.jsp
    function addSec(me, e) {
        var roleId = "<%=request.getParameter("roleid")%>";
        var zTreeObj = $.fn.zTree.getZTreeObj("app_data_tree");
        var nodes = zTreeObj.getSelectedNodes();
        if (nodes.length > 0) {
            var app = nodes[0].app;

            appDialog("pubrole", "newdata", $(me).attr("id"), "app.action?flag=add&roleid=" + roleId + "&appid=" + app,
                    600, 400, function () {
                        WebClientBean.rollback("pubrole.sec", function (data) {
                            debug("pubuser rollback result : " + data);
                        });
                    });
        }
    }

    function showSec(me, e, uid) {
        appDialog("pubrole", "newdata", $(me).attr("id"), "app.action?uid=" + uid,
                600, 400, function () {
                    WebClientBean.rollback("pubrole.sec", function (data) {
                        debug("pubuser rollback result : " + data);
                    });
                });
    }

    function refreshRecTable() {
        var roleId = "<%=request.getParameter("uid")%>";
        var zTreeObj = $.fn.zTree.getZTreeObj("app_data_tree");
        var nodes = zTreeObj.getSelectedNodes();
        if (nodes.length > 0) {
            $("#" + nodes[0].tId + "_a").click();
        }
    }
</script>
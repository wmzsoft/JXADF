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
    </style>

    <script type="text/javascript">

        //表格加载完成后，勾选相关的数据
        var afterLoadDataTable = JxUtil.extend(afterLoadDataTable, function (tableId) {
            dwr.engine.setAsync(false);
            if ("user_listtable" == tableId) {
                WebClientBean.getJboListByCause("PUB_ROLE_USER", "role_id=?", "<%=request.getParameter("roleid")%>", function (datas) {
                    $.each(datas, function (idx, data) {
                        var td = $("td[dataattribute='USER_ID'][title='" + data.datas.USER_ID + "']", $("#user_listtable tbody"));
                        $("input[type='checkbox']", td.closest("tr")).attr("checked", "checked");
                    });
                });
            } else if ("operation_listtable" == tableId) {
                var appTree = $.fn.zTree.getZTreeObj("app_tree");
                var nodes = appTree.getSelectedNodes();
                var appCause = "";
                if(nodes.length > 0){
                    appCause = " and menu_id in (select maxmenuid from maxmenu where app = '" + nodes[0].app + "')";
                    debug(appCause);
                }
                WebClientBean.getJboListByCause("PUB_ROLE_OPERATION", "role_id=?" + appCause, "<%=request.getParameter("roleid")%>", function (datas) {
                    $.each(datas, function (idx, data) {
                        $("input[type='checkbox'][value=" + data.datas.PUB_ROLE_OPERATION_ID + "]", $("#operation_listtable tbody")).attr("checked", "checked");
                    });
                });
            }
            dwr.engine.setAsync(true);
        }, true);

        //树点击
        function zTreeOnClick(event, treeId, treeNode, jbo) {
            if (treeId == "tree") {
                var tuid = treeNode.department_id;

                var userView = $("#userView").attr("checked");
                var queryStr = "";
                //选中表示显示所有用户
                if ("checked" == userView) {
                    queryStr = "active = 1 and department_id in (select department_id from pub_department start with department_id = '" + tuid +
                    "' connect by prior department_id = super_department_id)";
                } else {
                    queryStr = "active = 1 and department_id in (select department_id from pub_department start with department_id = '" + tuid +
                    "' connect by prior department_id = super_department_id) and upper(pub_user.user_id) in (select upper(user_id) from pub_role_user " +
                    " where role_id = '<%=request.getParameter("roleid")%>')";
                }

                dwr.engine.setAsync(false);
                WebClientBean.queryJboSetData(jx_appNameType, "PUB_ROLE_PUB_USER_ALL", queryStr, function () {
                    getTableData("div_user_listtable");
                });
                dwr.engine.setAsync(true);
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
                    table.attr("sheight", tableHeight - 70);
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

    var ckOneSelectHandler = JxUtil.extend(ckOneSelectHandler, function (obj, idx) {
        var objTable = $(obj).closest("table");
        if (objTable && objTable.attr("id") == "user_listtable") {
            var objStatus = $(obj).attr("checked");
            var user_id = $(obj).val();
            var params = "0";
            if ("checked" == objStatus) {
                params = "1";
            }

            params += "," + user_id;

            dwr.engine.setAsync(false);
            var result = getJboSetMethodReturnValue("PUB_ROLE_USER", "toggleUser", params);

            if ("ok" == result) {
                WebClientBean.save("pubrole.main", function () {
                    debug("添加角色用户，保存数据成功!");
                });
            }
            dwr.engine.setAsync(true);
        } else if (objTable && objTable.attr("id") == "operation_listtable") {
            var objStatus = $(obj).attr("checked");
            var opId = $(obj).val();
            var params = "0";
            if ("checked" == objStatus) {
                params = "1";
            }

            params += "," + opId;
            dwr.engine.setAsync(false);
            var result = getJboSetMethodReturnValue("PUB_ROLE_OPERATION", "toggleOperation", params);
            if ("ok" == result) {
                WebClientBean.save("pubrole.main", function () {
                    debug("添加角色数据权限，保存数据成功!");
                });
            }
            dwr.engine.setAsync(true);
        }
    }, true);

    var ckPageSelectHandler = JxUtil.extend(ckPageSelectHandler, function (me, id) {
        var tableid = id.substring(3, id.length);
        var needchange = $(me).attr("checked") == undefined ? "checked" : undefined;

        var checkboxs = $("input[type='checkbox'][name!='allbox']", $("#" + tableid));
        for (var c = 0; c < checkboxs.length; c++) {
            var tempBox = $(checkboxs[c]);
            if (needchange == tempBox.attr("checked")) {
                tempBox.click();
            }
        }
    }, false);


    var beforeDataTableLoad = JxUtil.extend(beforeDataTableLoad, function (tableId) {
        setScroll(tableId);
    }, true);


    var afterLoadDataTable = JxUtil.extend(afterLoadDataTable, function afterLoadDataTable(tableId) {
        var uid = "<%=request.getParameter("uid")%>";
        var roleId = "<%=request.getParameter("roleid")%>";

        if (tableId == "user_listtable") {
            dwr.engine.setAsync(false);
            WebClientBean.getJboListByCause("PUB_ROLE_USER", "role_id = ?", roleId, function (datas) {
                $.each(datas, function (idx, data) {
                    var user_id = data.uidValue;

                    var radioBtn = $("input[type='checkbox'][value=" + uid + "]");
                    if (undefined == radioBtn.attr("checked")) {
                        radioBtn.attr("checked", "checked");
                    }
                });
            });
            dwr.engine.setAsync(true);

            if ($("input[name!='allbox']:not(:checked)", $("#" + tableId)).length == 0) {
                $("input[type='checkbox'][name='allbox']").attr("checked", "checked");
            }
        } else if (tableId == "operation_listtable") {
            dwr.engine.setAsync(false);
            WebClientBean.getJboListByCause("PUB_ROLE_OPERATION", "role_id = ?", roleId, function (datas) {
                $.each(datas, function (idx, data) {
                    var opid = data.datas.MENU_ID;
                    var radioBtn = $("input[type='checkbox'][value=" + opid + "]", $("#operation_listtable"));
                    if (undefined == radioBtn.attr("checked")) {
                        radioBtn.attr("checked", "checked");
                    } else {
                        debug(radioBtn);
                        debug(radioBtn.attr("checked"));
                        debug(opid + "not checked");
                    }
                });
            });
            dwr.engine.setAsync(true);

            if ($("input[name!='allbox']:not(:checked)", $("#" + tableId)).length == 0) {
                $("input[type='checkbox'][name='allbox']").attr("checked", "checked");
            }
        }
    }, true);


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
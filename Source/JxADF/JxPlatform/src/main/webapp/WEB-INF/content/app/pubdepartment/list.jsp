<%@ page import="com.jxtech.i18n.JxLangResourcesUtil" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/content/common/header.jsp" %>
<%
    String addTitle = JxLangResourcesUtil.getString("app.pubdepartment.ADD_TITLE");
    String editTitle = JxLangResourcesUtil.getString("app.pubdepartment.EDIT_TITLE");
    String delTtile = JxLangResourcesUtil.getString("app.pubdepartment.DEL_TITLE");
%>
<jxui:head title="部门信息-列表">

    <script type="text/javascript">

        $(function () {
            $("#searchinput").bind({
                "focus": function () {
                    $(".placeholder").addClass("hidefocus");
                    $("#searchExt").removeClass("hidefocus");
                },
                "blur": function () {
                    if ($(this).val() == "") {
                        $(".placeholder").removeClass("hidefocus");
                    } else {
                        $(".placeholder").addClass("hidefocus");
                    }
                },
                "input": function () {
                    showExtList(this);
                },
                "propertychange": function () {
                    showExtList(this);
                },
                "keydown": function () {
                    switch (event.keyCode) {
                        case 38:
                            moveExtList("UP");
                            break;
                        case 40:
                            moveExtList("DOWN");
                            break;
                        case 13 :
                            doSearch();
                            break;
                        default:
                    }
                }
            });

            //下拉搜索操作 交互方式 参考百度
            $("#searchExt").mouseenter(function () {
                $("body").one("click", function () {
                    $("#searchExt").addClass("hidefocus");
                });
            });

        });

        function moveExtList(direction) {
            var searchList = $("#searchList");
            var searchLen = searchList.children().length;
            if (searchLen == 0) return false;

            var cSearchItem = $("li[class='searchFocus']", searchList);
            var newItem = null;

            if ("DOWN" == direction) {
                if (cSearchItem.length == 0) {
                    newItem = $("li:first", searchList);
                } else {
                    newItem = cSearchItem.next();
                }
            } else if ("UP" == direction) {
                if (cSearchItem.length > 0) {
                    newItem = cSearchItem.prev();
                }
            }

            if (newItem != null && newItem.length > 0) {
                newItem.addClass("searchFocus");
                cSearchItem.removeClass("searchFocus");

                $("#searchinput").val(newItem.attr("data-prop-name"));
            }
        }
        /**
         * 在快速搜索输入回车键 执行搜索
         */
        function doSearch() {
            var searchList = $("#searchList");
            var cSearchItem = $("li[class='searchFocus']", searchList);

            if (cSearchItem.length > 0) {
                cSearchItem.click();
            }
        }

        /**
         * 显示列表
         * @param me
         */
        function showExtList(me) {
            var zTreeObj = $.fn.zTree.getZTreeObj("tree");
            var searchStr = $(me).val();

            if ("" == searchStr) {
                $("#searchExt").addClass("hidefocus");
            } else {
                $("#searchExt").removeClass("hidefocus");
                $("#searchList").empty();
            }

            $.each(treeNodes_tree, function (idx, data) {
                if (data.name.toLowerCase().indexOf(searchStr.toLowerCase()) >= 0) {
                    var li = $("<li></li>").text(data.name);
                    li.attr("data-prop-tuid", data.department_id);
                    li.attr("data-prop-name", data.name);

                    li.bind({
                        "click": function () {
                            $("#searchExt").addClass("hidefocus");
                            $("#searchinput").val($(this).text())
                            var searchNode = zTreeObj.getNodeByParam("department_id", data.department_id);
                            zTreeObj.selectNode(searchNode, false);
                            $("#" + searchNode.tId + "_a").click();
                        }
                    });
                    $("#searchList").append(li);
                }
            });
        }

        function getUserRestrict() {
            return  $("#showAllUser").attr("checked") == "checked" ? "" : "ACTIVE=1";
        }

        function zTreeOnClick(event, treeId, treeNode, jbo) {
            var departmentId = treeNode.department_id;

            var restrict = getUserRestrict();
            dwr.engine.setAsync(false);
            WebClientBean.queryJboSetData(jx_appNameType, "PUB_USERDEPARTMENT_IDP", restrict, function (data) {
                getTableData("div_listtable");
            });
            dwr.engine.setAsync(true);
        }

        var afterTreeInit = JxUtil.extend(afterTreeInit, function (zTree) {
            //修该tree的高度
            $(".ztree").css("height", document.documentElement.clientHeight - 60);

            var node = zTree.getNodeByTId("tree_1");
            if (node) {
                $("#tree_1_a").click();
            }

            zTree.setting.edit = {
                drag: {
                    autoExpandTrigger: true,
                    isCopy: false,
                    isMove: false
                },
                enable: true
            };

            zTree.setting.view.addHoverDom = function (treeId, treeNode) {
                var sObj = $("#" + treeNode.tId + "_span");
                var addBtnStr = "#addBtn_" + treeNode.tId;
                if (treeNode.editNameFlag || $(addBtnStr).length > 0) return;

                //配置 新增，编辑，删除3个按钮
                var addStr = "<span class='button add' id='addBtn_" + treeNode.tId +
                        "' title='<%=addTitle%>' onfocus='this.blur();'></span>";
                var editStr = "<span class='button edit' id='editBtn_" + treeNode.tId +
                        "' title='<%=editTitle%>' onfocus='this.blur();'></span>";
                var delStr = "<span class='button remove' id='delBtn_" + treeNode.tId +
                        "' title='<%=delTtile%>' onfocus='this.blur();'></span>";

                sObj.after(editStr).after(addStr);
                //sObj.after(delStr).after(editStr).after(addStr);

                //新建
                var addbtn = $(addBtnStr);
                if (addbtn) addbtn.bind("click", function () {
                    appDialog("pubdepartment", "newdept", treeId, "app.action?flag=add&tuid=" + treeNode.department_id,
                            720, 350, rollbackDepartment,'Add');

                    $(this).parent().click();
                    return false;
                });

                //编辑
                var editBtn = $("#editBtn_" + treeNode.tId);
                if (editBtn) editBtn.bind("click", function () {
                    WebClientBean.getJbo(jx_appNameType, "PUB_DEPARTMENT", "DEPARTMENT_ID", treeNode.department_id, true, function (jbo) {
                        appDialog("pubdepartment", "newdept", treeId, "app.action?uid=" + jbo.uidValue,
                                720, 350, rollbackDepartment,'Edit');
                    });
                    $(this).parent().click();
                    return false;
                });

                //删除
                var delBtn = $("#delBtn_" + treeNode.tId);
                if (delBtn) delBtn.bind("click", function () {
                    var confirmsg = $('<div id="delConfirm"></div>');
                    confirmsg.attr("title", getLangString("jxcommon.delConfirmTitle"));
                    confirmsg.html(getLangString("jxcommon.delConfirmMsg"));

                    $(confirmsg).dialog({
                        resizable: false,
                        height: 180,
                        modal: true,
                        buttons: [
                            {
                                text: getLangString("jxcommon.confirm"),
                                click: function () {
                                    // 执行删除
                                    WebClientBean.delRow(jx_appNameType, "PUB_DEPARTMENT", "", treeNode.tId, true,
                                            {
                                                callback: function (data) {
                                                    if (!data) {
                                                        alert(getLangString("jxcommon.delFail"));
                                                    } else {
                                                        WebClientBean.save(jx_appNameType, function () {
                                                            refreshTree("");
                                                        });
                                                    }
                                                },
                                                errorHandler: errorHandler,
                                                exceptionHandler: exceptionHandler
                                            });
                                    $(this).dialog("close");
                                }
                            },
                            {
                                text: getLangString("jxcommon.cancle"),
                                click: function () {
                                    $(this).dialog("close");
                                }
                            }
                        ]
                    });

                    $(this).parent().click();

                });
            };

            //移除鼠标后的树视图
            zTree.setting.view.removeHoverDom = function (treeId, treeNode) {
                $("#addBtn_" + treeNode.tId).unbind().remove();
                $("#editBtn_" + treeNode.tId).unbind().remove();
                $("#delBtn_" + treeNode.tId).unbind().remove();
            };
        }, true);

        function rollbackDepartment() {
            WebClientBean.rollback("pubdepartment.main", function (data) {
                debug("rollback result : " + data);
            });
        }

        //刷新树节点
        function refreshTree(actionType) {
            var treeObj = $.fn.zTree.getZTreeObj("tree");
            var nodes = treeObj.getSelectedNodes();
            if ("add" == actionType) {
                if (nodes.length > 0) {
                    if (nodes.isParent) {
                        treeObj.reAsyncChildNodes(nodes[0], "refresh");
                    } else {
                        var pNode = nodes[0].getParentNode();
                        treeObj.reAsyncChildNodes(pNode, "refresh");
                    }
                }
            } else {
                if (nodes.length > 0) {
                    var pNode = nodes[0].getParentNode();
                    treeObj.reAsyncChildNodes(pNode, "refresh");
                }
            }

            //刷新对应的json树数据  treeNodes_tree对象
            $.ajax({
                url: "tree.action",
                method: "post",
                data: {
                    "jboname": "PUB_DEPARTMENT",
                    "idKey": "DEPARTMENT_ID",
                    "pidKey": "SUPER_DEPARTMENT_ID",
                    "name": "NAME",
                    "hasChildName": "FALSE",
                    "orderby": "PUB_DEPARTMENT_ID",
                    "cause": "",
                    "leafDisplay": "TRUE"
                },
                success: function (data) {
                    treeNodes_tree = eval(data);
                }

            });
        }


        //用户管理部分
        function addUser(me, e) {
            var zTreeObj = $.fn.zTree.getZTreeObj("tree");
            if (zTreeObj) {
                var nodes = zTreeObj.getSelectedNodes();
                var tuid = 0;
                if (nodes.length > 0) {
                    var node = nodes[0];
                    tuid = node.department_id;
                }

                appDialog("pubdepartment", "newuser", $(me).attr("id"), "app.action?flag=add&tuid=" + tuid, 700, 500, rollbackUser);
            }
        }

        function showUser(me, e, uid) {
            appDialog("pubdepartment", "newuser", $(me).attr("id"), "app.action?uid=" + uid, 780, 500, rollbackUser);
        }

        function refreshUser(actionType) {
            var zTreeObj = $.fn.zTree.getZTreeObj("tree");
            if (zTreeObj) {
                var nodes = zTreeObj.getSelectedNodes();
                if (nodes.length > 0) {
                    zTreeObj.selectNode(nodes[0], false);
                    $("#" + nodes[0].tId + "_a").click();
                }
            }
        }

        function rollbackUser() {
            WebClientBean.rollback("pubdepartment.user", function (data) {
                debug("pubuser rollback result : " + data);
            });
        }

        var beforeDataTableLoad = JxUtil.extend(beforeDataTableLoad, function (tableId) {
            if (JxUtil.getScroll(document.body).scrollY) {
                if (tableId == "listtable") {
                    var table = $("#" + tableId);
                    var headerTr = $("thead tr:last", table);
//                    debug("clientHeight : " + JxUtil.getClientHeight());
//                    debug("top : " + headerTr.offset().top);
//                    debug("headerTr : " + headerTr.outerHeight());
                    var tableHeight = JxUtil.getClientHeight() - headerTr.offset().top - headerTr.outerHeight();
                    table.attr("sheight", tableHeight - 50);
                }
            }
        }, true);

        //部门视图切换
        function toggleDept(me, e) {
            var checked = $(me).attr("checked");
            var cause = "cause=STATE=1";
            if ("checked" == checked) {
                cause = "cause=";
            }

            var url = getAsyncUrl();
            zTreeUrl = departmentAsyncUrlHandler(url, cause);
            var node = zTree.getNodeByTId("tree_1");
            zTree.reAsyncChildNodes(node, "refresh", false);
            zTree.expandNode(node, true, false);
        }

        function departmentAsyncUrlHandler(url, cause) {
            var pos = url.indexOf("&cause");
            var pre = url.substring(0, pos + 1);
            var last = url.substring(pos + 1, url.length);
            var lastpos = last.indexOf("&");
            var lasttail = last.substring(lastpos, last.length);

            return pre + cause + lasttail;
        }

        //用户视图切换
        function toggleUser(me, e) {
            var nodes = zTree.getSelectedNodes();
            if (nodes.length > 0) {
                var node = nodes[0];
                $("#" + node.tId + "_a").click();
            }
        }
        $(function(){
        	//alert(setting_tree);
			setting_tree.async.url=getAsyncUrl1;
        });
        function getAsyncUrl1() {
            var checked = $("#showAllDept").attr("checked");
            var cause = "cause=STATE=1";
            if ("checked" == checked) {
                cause = "cause=";
            }
            if (undefined == zTreeUrl || null == zTreeUrl) {
                return "${pageContext.request.contextPath}/tree.action?jboname=PUB_DEPARTMENT&idKey=department_id&pidKey=super_department_id&name=name&hasChildName=&orderby=DEPARTMENT_ID&"+cause+"&leafDisplay=true&i18n=";
            }

            return zTreeUrl;
        }


    </script>

    <style type="text/css">
        .fillTop {
            vertical-align: top !important;
        }

        .fillWidth {
            width: 100%;
        }

        .ztree li span.button.add {
            background-position: -144px 0;
            margin-left: 2px;
            margin-right: -1px;
            vertical-align: top;
        }

        .ztree {
            width: 280px;
            height: 0;
            margin: 0px 2px 0 0;

            overflow: auto;
        }

        .leftTd {
            /*background: rgb(189, 214, 237);
            border-right: 1px solid rgb(189, 214, 237);*/
        }

        .rightTd {
            vertical-align: top;
            padding: 0 !important;
            margin: 0 !important;
        }

        .searchDiv {
            width: 200px;
            margin: 2px 0 0 8px;
            background: none repeat scroll 0 0 #fff;
            border: 1px solid #bbb;
            border-radius: 2px;
            box-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1) inset;
            font-size: 12px;
            height: 24px;
            line-height: 1.666;
            position: relative;
            float: left;

            display: inline-block;
            vertical-align: middle;
        }

        .placeholder {
            color: #ccc;
            cursor: text;
            font-size: 12px;
            height: 90%;
            left: 9px;
            line-height: 24px;
            overflow: hidden;
            position: absolute;
            top: 0;
        }

        .searchinput {
            width: 100%;
            height: 26px;
            line-height: 26px;
            padding: 0 10px;
            box-sizing: border-box;
            background: none repeat scroll 0 0 transparent;
            border: 0 none;
            font-size: 14px;
            outline: 0 none;
        }

        .searchExt {
            background: none repeat scroll 0 0 #fff;
            box-shadow: 1px 1px 3px #ededed;
            position: absolute;
            left: 8px;
            top: 34px;
            z-index: 9999;
            min-width: 202px;
        }

        .searchExt ul {
            margin: 0;
            padding: 0;
            list-style: none outside none;
        }

        .searchExt ul li {
            margin: 0;
            color: #000;
            cursor: pointer;
            padding: 3px 8px;
            position: relative;
            white-space: nowrap;
            border-bottom: 1px solid #efefef;
        }

        .searchExt ul li:hover, .searchFocus {
            background: #ccc;
            color: #0000ff;
        }

        .hidefocus {
            display: none;
        }

        td .pushbutton {
            margin: 0;
        }
    </style>
</jxui:head>
<jxui:body appName="pubdepartment" appType="list">
    <%--<jxui:appbar/>--%>
    <jxui:section>
        <jxui:sectionrow cssClass="toolbar">
            <jxui:sectioncol cssClass="leftTd">
                <div class="searchDiv" id="searchDiv">
                    <!--搜索placeHolder-->
                    <label class="placeholder">
                        <jxui:label value="{app.pubdepartment.SEARCH_PLACEHOLDER}"/>
                    </label>
                    <!--搜索框-->
                    <input type="text" id="searchinput" class="searchinput" input-helper="disabled"/>
                </div>
                <!--搜索列表-->
                <div class="searchExt hidefocus" id="searchExt">
                    <ul id="searchList">
                    </ul>
                </div>
            </jxui:sectioncol>

            <jxui:sectioncol cssClass="td45">
                <jxui:label value="{app.pubdepartment.SHOW_ALL_DEPARTMENT}"/>
                <input type="checkbox" id="showAllDept" onchange="toggleDept(this, event)"/>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <jxui:label value="{app.pubdepartment.SHOW_ALL_USER}"/>
                <input type="checkbox" id="showAllUser" onchange="toggleUser(this, event)"/>

                <jxui:pushbutton label="{app.pubdepartment.ADD_USER}" style="margin:5px; float:right"
                                 mxevent="addUser" id="addUserBtn"/>
            </jxui:sectioncol>
        </jxui:sectionrow>

        <jxui:sectionrow>
            <jxui:sectioncol cssClass="fillTop leftTd">
                <jxui:tree id="tree" jboname="PUB_DEPARTMENT" treeNodeKey="DEPARTMENT_ID"
                           treeNodeParentKey="SUPER_DEPARTMENT_ID" treeNodeName="NAME" leafDisplay="true"
                           whereCause="STATE=1" orderby="DEPARTMENT_ID" async="true"/>
            </jxui:sectioncol>

            <jxui:sectioncol cssClass="fillWidth rightTd">
                <jxui:fragment id="listtable" type="user-table" lazyload="true"/>
            </jxui:sectioncol>
        </jxui:sectionrow>
    </jxui:section>

</jxui:body>

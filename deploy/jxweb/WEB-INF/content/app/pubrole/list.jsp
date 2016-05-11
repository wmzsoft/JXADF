<%@ include file="/WEB-INF/content/common/header.jsp" %>
<%
    String bsePath = request.getContextPath();
%>
<jxui:head title="角色-列表">
    <script type="text/javascript">
        function mainFormLoad() {
            $("#mainForm").css({
                height: document.documentElement.clientHeight - 52
            });
        }
    </script>
</jxui:head>

<jxui:body appName="pubrole" appType="list">
    <jxui:form id="pubrole">
        <table>
        <tr class="container">
            <td style="width:350px">
                <div class="title">
                    <jxui:label value="{app.pubrole.ROLE_TITLE}"/>
                </div>
                <div class="toolbar">
                    <jxui:pushbutton label="{app.pubrole.ADD_ROLE}" mxevent="addRole"/>
                </div>
                <jxui:fragment id="listtable" type="list-table"/>
            </td>
            <td style="width:10px;">

            </td>
            <td>
                <div class="title">
                    <jxui:label value="{app.pubrole.AUTH_TITLE}"/>
                </div>
                <iframe id="mainForm" src="" frameBorder=0 onload="mainFormLoad()" class="fullover"></iframe>
            </td>
        </tr>
        </table>
    </jxui:form>

    <script type="text/javascript">

        $(function () {
            $(".table_form tr:first").remove();
        });

        var g_uid = -1;
        var afterLoadDataTable = JxUtil.extend(afterLoadDataTable, function (tableId) {
            var $table = $("#" + tableId);

            debug("需要选中行的uid为： " + g_uid);
            if ($table && $table.length > 0) {

                //管理员组不能删除
                var tds = $("td[dataattribute='ROLE_ID']", $table);
                $.each(tds, function (idx, td) {
                    if ($(td).text().trim() === "1") {
                        $("td[dataattribute='V_DELETE']", $(td).closest("tr")).empty();
                    }
                });

                $("td[dataattribute='ROLE_ID'],th[dataattribute='ROLE_ID']").css({
                    "display": "none"
                });

                if (g_uid === -1) {
                    var $dataTr = $("tbody tr", $table);
                    if ($dataTr.length > 0) {
                        var firstRadio = $("td:first input[type='radio']", $($dataTr[0]));
                        firstRadio.click();
                        g_uid = $($dataTr[0]).attr("uid");
                    }
                } else {
                    var secTr = $("tbody tr[uid='" + g_uid + "']");
                    if (secTr.length == 0) {
                        var secTr = $("tbody tr:first");
                    }

                    var firstRadio = $("td:first input[type='radio']", secTr);
                    firstRadio.click();
                }
            }
        }, true);

        function addRole(me, e) {
            popupRole($(me).attr("id"), true, -1);
        }

        function showRole(me, e, uid) {
            var $_tr = $(me).closest("tr");
            $("input[type='radio']", $_tr).click();
            popupRole($(me).attr("id"), false, uid);
        }

        function popupRole(fromid, isnew, uid) {
            var url = "";
            if (isnew) {
                url = "app.action?flag=add";
            } else {
                url = "app.action?uid=" + uid;
            }

            appDialog("pubrole", "newrole", fromid, url, 400, 200, function () {
                WebClientBean.rollback("pubrole.main", function (data) {
                    debug("pubuser rollback result : " + data);
                });
            });
        }

        function refreshRoleTable(newUid) {
            g_uid = newUid;
            getTableData("div_listtable");
        }

        var ckOneSelectHandler = JxUtil.extend(ckOneSelectHandler, function (me, seq) {
            var uid = $(me).val();
            var roleId = $(me).closest("td").siblings("td[dataattribute='ROLE_ID']").text();
            $("#mainForm").attr("src", "app.action?app=pubrole&type=pubrole&uid=" + uid + "&roleid=" + roleId);
        }, true);

        var delrow = JxUtil.extend(delrow, function (me, e) {
            var confirmsg = $('<div id="delConfirm"></div>');
            confirmsg.attr("title", getLangString("jxcommon.delConfirmTitle"));
            confirmsg.html(getLangString("jxcommon.delConfirmMsg"));
            var uid = $(me).closest("tr").attr("uid");
            $(confirmsg).dialog({
                resizable: false,
                height: 180,
                modal: true,
                buttons: [
                    {
                        text: getLangString("jxcommon.confirm"),
                        click: function () {
                            // 执行删除
                            WebClientBean.delRow("pubrole.list", "PUB_ROLE", "", uid, true,
                                    {
                                        callback: function (data) {
                                            if (!data) {
                                                alert(getLangString("jxcommon.delFail"));
                                            } else {
                                                WebClientBean.save("pubrole.list", function () {
                                                    getTableData("div_listtable");
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
        }, false);

        function ajaxSave() {
            WebClientBean.save("pubrole.list", function (jboset) {
                debug("保存成功：");
                g_uid = jboset.jbo.uidValue;
            });
        }

    </script>

    <style type="text/css">
        #div_listtable {
            width: 100%;
            float: left;
        }

        .bottom {
            display: none;
        }

        .container {

        }

        .container td {
            /*border: 1px solid #c0c0c0;*/
            text-align: center;
        }

        .fullover {
            margin: 0;
            width: 100%;
            height: 300px;
            overflow: auto
        }

        .toolbar {
            float: right;
        }
    </style>
</jxui:body>




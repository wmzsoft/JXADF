<%@ include file="/WEB-INF/content/common/header.jsp" %>
<%
    String tuid = request.getParameter("tuid");
    String flag = request.getParameter("flag");
%>
<jxui:head title="部门管理">
    <script type="text/javascript">
        $(function () {
            if ("add" == "<%=request.getParameter("flag")%>") {
                var $_superDeptId = $("input[dataattribute='SUPER_DEPARTMENT_ID']");
                debug($_superDeptId);
                $_superDeptId.focus();
                $_superDeptId.val('<%=tuid%>');
                $_superDeptId.attr("changed", "1");
                $_superDeptId.blur();
            }

            $("#deptId_DESC").removeAttr("readonly").after("<div id='dTree'></div>");

            $("#deptId_DESC").bind({
                focus: function () {
                    debug("focus : ");
                    toggleDepartmentVisible();
                },
                keydown: function () {
                    debug("keydown : " + event.keyCode);
                    if (9 == event.keyCode) {
                        toggleDepartmentVisible();
                    }
                }
            });
            initDTree();

        });

        //初始化部门树
        function initDTree() {
            var dTree = $("#dTree");
            if (dTree && dTree.length > 0) {
                dTree.load("app.action?app=pubdepartment&type=view/dtree&fromid=SUPER_DEPARTMENT_ID", function (data) {
                    var close = $("<span class='close btn_href'>关闭</span>");
                    close.bind("click", function () {
                        toggleDepartmentVisible();
                    });
                    dTree.append(close);
                });
            }
        }

        function toggleDepartmentVisible() {
            var dTree = $("#dTree");
            if (dTree && dTree.length > 0) {
                dTree.toggle("fast");
            }
        }

        function saveDept(me, e) {
            WebClientBean.save(jx_appNameType, function () {
                parent.refreshTree("<%=flag%>");
                appDialogClose(this, event);
            });
        }
    </script>

    <style type="text/css">
        .table_form td.form_td_label {
            width: auto;
        }

        #dTree {
            position: absolute;
            margin-right: 20px;
            z-index: 100;
            background: #FFF;

            display: none;
        }

        .close {
            position: relative;
            left: 230px;
            top: -180px;
            cursor: pointer;
        }

        .ztree {
            height: 160px !important;
        }
    </style>
</jxui:head>
<jxui:body appName="pubdepartment" appType="main" mainApp="false">
    <jxui:form id="pubdepartment" jboname="PUB_DEPARTMENT">
        <jxui:section type="form">
            <jxui:sectionrow>
                <jxui:sectioncol cssClass="head_right">
                    <jxui:pushbutton label="{app.pubdepartment.SAVEDEPT_OK}" mxevent="saveDept"/>
                </jxui:sectioncol>
            </jxui:sectionrow>
            <jxui:sectionrow>
                <jxui:sectioncol>
                    <jxui:section type="edit">
                        <jxui:sectionrow>
                            <jxui:textbox dataattribute="NAME" required="true"/>
                            <jxui:textbox dataattribute="DEPARTMENT_CODE" required="false"/>
                        </jxui:sectionrow>

                        <jxui:sectionrow>
                            <jxui:multipartTextbox dataattribute="SUPER_DEPARTMENT_ID"
                                                   id="deptId"
                                                   descdataattribute="PUB_DEPARTMENTSUPER_DEPARTMENT_ID.NAME"/>
                            <jxui:select options='0:无效,1:有效' dataattribute="STATE"/>
                        </jxui:sectionrow>
                        <jxui:sectionrow>
                            <jxui:textbox dataattribute="DESCRIPTION" colspan="3" rows="2"/>
                        </jxui:sectionrow>
                                    <jxui:sectionrow>
																			<jxui:select label="{app.pubdepartment.PUB_USER.ORGID}"
																			        dataattribute="orgid"
																			        jboname="organization"
																			        wherecause="active=1"
																			        displayname="description"
																			         />     
																			<jxui:select label="{app.pubdepartment.PUB_USER.SITEID}"
																			        dataattribute="siteid"
																			        jboname="site"
																			        wherecause="active=1"
																			        displayname="description"
																			        />     
                                    </jxui:sectionrow>

                    </jxui:section>
                </jxui:sectioncol>
            </jxui:sectionrow>
        </jxui:section>
    </jxui:form>
    <jxui:footer/>
</jxui:body>


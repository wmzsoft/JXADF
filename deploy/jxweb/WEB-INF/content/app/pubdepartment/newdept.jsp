<%@ include file="/WEB-INF/content/common/header.jsp" %>
<%
    String tuid = request.getParameter("tuid");
    String flag = request.getParameter("flag");
%>
<jxui:head title="{app.pubdepartment.page.title}">
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
                            <jxui:textbox dataattribute="DEPARTMENT_ID" required="true" label="{app.pubdepartment.DEPARTMENT_ID}"/>
                            <jxui:textbox dataattribute="NAME" required="true" label="{app.pubdepartment.NAME}"/>
                        </jxui:sectionrow>
                        <jxui:sectionrow>
							<jxui:select label="{app.pubdepartment.SUPER_DEPARTMENT_ID}"
							        dataattribute="SUPER_DEPARTMENT_ID"
							        jboname="pub_department"
							        displayname="name" displayvalue="department_id"
                                    wherecause="state=1"/>
                            <jxui:textbox dataattribute="FULL_NAME"/>
                        </jxui:sectionrow>
                        <jxui:sectionrow>
                            <jxui:checkbox dataattribute="STATE" label="{app.pubdepartment.STATE}"/>
							<jxui:select label="{app.pubdepartment.LEADER}"
							        dataattribute="leader"
							        jboname="pub_user"
							        displayname="displayname" displayvalue="user_id"
                                    ajax="app.action?app=pubdepartment&type=user-list"/>     
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
                        <jxui:sectionrow>
                            <jxui:textbox dataattribute="DESCRIPTION" label="{app.pubdepartment.DESCRIPTION}" colspan="3"/>
                        </jxui:sectionrow>
                    </jxui:section>
                </jxui:sectioncol>
            </jxui:sectionrow>
        </jxui:section>
    </jxui:form>
    <jxui:footer/>
</jxui:body>


<%@ include file="/WEB-INF/content/common/header.jsp" %>
<%
    String tuid = request.getParameter("tuid");
    String flag = request.getParameter("flag");
%>
<jxui:head title="用户管理">
    <script type='text/javascript' src='<%=path%>/javascript/jquery.md5/jquery.md5.js'></script>

    <script type="text/javascript">
        var beforeDataTableLoad = JxUtil.extend(beforeDataTableLoad, function (tableId) {
            if (JxUtil.getScroll(document.body).scrollY) {
                if (tableId == "user_role") {
                    var table = $("#" + tableId);
                    var headerTr = $("thead tr:last", table);
                    var tableHeight = JxUtil.getClientHeight() - headerTr.offset().top - headerTr.outerHeight();
                    table.attr("sheight", tableHeight - 50);
                }
            }
        }, true);

        $(function () {
            if ("add" == "<%=request.getParameter("flag")%>") {
                var $_deptId = $("input[dataattribute='DEPARTMENT_ID']");
                debug($_deptId);
                $_deptId.focus();
                $_deptId.val('<%=tuid%>');
                $_deptId.attr("changed", "1");
                $_deptId.blur();
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
                dTree.load("app.action?app=pubdepartment&type=view/dtree", function (data) {
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

        function saveUser(me, e) {
            var mainForm = $(me).closest("form");

            var passValidation = true;
            var validateElements = $("*[class *= 'validate[']", mainForm);
            for (var i = 0; i < validateElements.length; i++) {
                if ($(validateElements[i]).validationEngine("validate")) {
                    passValidation = false;
                    break;
                }
            }

            if (passValidation) {
                WebClientBean.save(jx_appNameType, function () {
                    parent.refreshUser();
                    appDialogClose(this, event);
                });
            }

        }

    </script>

    <style type="text/css">
        .fillWidth {
            width: 100%;
        }

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
            top: -220px;
            cursor: pointer;
        }

        .checkbox_td_content {
            min-width: 100px;
        }
    </style>
</jxui:head>
<jxui:body appName="pubdepartment" appType="user" mainApp="false">
    <jxui:form id="pubdepartment_user" jboname="PUB_USER" relationship="PUB_USERDEPARTMENT_IDP">
        <%--<jxui:appbar/>--%>
        <jxui:section type="main">
            <jxui:sectionrow>
                <jxui:sectioncol>
                    <jxui:section type="form">
                        <jxui:sectionrow>
                            <jxui:sectioncol cssClass="head_right">
                                <jxui:pushbutton label="{app.pubdepartment.SAVEUSER_OK}" mxevent="saveUser"/>
                            </jxui:sectioncol>
                        </jxui:sectionrow>

                        <jxui:sectionrow>
                            <jxui:sectioncol>
                                <jxui:section type="edit">
                                    <jxui:sectionrow>
                                        <jxui:textbox dataattribute="USER_ID"
                                                      label="{app.pubdepartment.PUB_USER.USER_ID}" required="true"/>
																				<jxui:textbox dataattribute="DISPLAYNAME" 
                                                      label="{app.pubdepartment.PUB_USER.DISPLAYNAME}" required="true"/>
                                        <jxui:textbox dataattribute="Password" label="{app.pubdepartment.PUB_USER.SETPASSWORD}" render="PASSWORD"/>

                                    </jxui:sectionrow>
                                    <jxui:sectionrow>
                                        <jxui:multipartTextbox dataattribute="DEPARTMENT_ID" id="deptId"
                                                               label="{app.pubdepartment.PUB_USER.DEPARTMENT_ID}"
                                                               descdataattribute="PUB_USERDEPARTMENT_ID.NAME"/>
                                        <jxui:checkbox dataattribute="USER_TYPE"
                                                       label="{app.pubdepartment.PUB_USER.USER_TYPE}"/>
                                        <jxui:checkbox dataattribute="ACTIVE"
                                                       label="{app.pubdepartment.PUB_USER.ACTIVE}"/>
                                    </jxui:sectionrow>
                                    <jxui:sectionrow>
                                        <jxui:textbox dataattribute="BIRTHDAY"
                                                      label="{app.pubdepartment.PUB_USER.BIRTHDAY}"/>
                                        <jxui:select dataattribute="SEX" label="{app.pubdepartment.PUB_USER.SEX}"
                                                     colspan="4"
                                                     options='0:女,1:男'/>
                                    </jxui:sectionrow>
                                    <jxui:sectionrow>
                                        <jxui:textbox dataattribute="MOBILE_NUMBER"
                                                      label="{app.pubdepartment.PUB_USER.MOBILE_NUMBER}"/>
                                        <jxui:textbox dataattribute="EMAIL" cssClass="custom[email]" colspan="4"
                                                      label="{app.pubdepartment.PUB_USER.EMAIL}"/>
                                    </jxui:sectionrow>
                                    <jxui:sectionrow>
                                        <jxui:textbox cols="6" colspan="6" dataattribute="ADDRESS"
                                                      label="{app.pubdepartment.PUB_USER.ADDRESS}"/>
                                    </jxui:sectionrow>
                                    <jxui:sectionrow>
                                        <jxui:textbox cols="6" colspan="6" dataattribute="DESCRIPTION"
                                                      labe="{app.pubdepartment.PUB_USER.DESCRIPTION}"/>
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
																			        colspan="4" />     
                                    </jxui:sectionrow>

                                </jxui:section>


                                <jxui:section style="width:100%;">
                                    <jxui:sectionrow>
                                        <jxui:sectioncol>
                                            <jxui:fragment id="user_role" type="user-role"/>
                                        </jxui:sectioncol>
                                    </jxui:sectionrow>
                                </jxui:section>

                            </jxui:sectioncol>
                        </jxui:sectionrow>

                    </jxui:section>
                </jxui:sectioncol>
            </jxui:sectionrow>
        </jxui:section>


    </jxui:form>
    <jxui:footer/>
</jxui:body>  


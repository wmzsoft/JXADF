<%@ include file="/WEB-INF/content/common/header.jsp" %>
<jxui:head title="部门管理">
    <script type="text/javascript">
        var beforeDataTableLoad = JxUtil.extend(beforeDataTableLoad, function (tableId) {
            if (JxUtil.getScroll(document.body).scrollY) {
                if (tableId == "f_department-user") {
                    var table = $("#" + tableId);
                    var headerTr = $("thead tr:last", table);
                    var tableHeight = document.body.clientHeight - headerTr.offset().top - headerTr.outerHeight();
                    table.attr("sheight", tableHeight - 50);
                }
            }
        }, true);
    </script>
</jxui:head>
<jxui:body appName="pubdepartment" appType="main">

    <jxui:form id="pubdepartment" jboname="PUB_DEPARTMENT">
        <jxui:appbar/>

        <jxui:section type="form">
            <jxui:sectionrow>
                <jxui:sectioncol>
                    <jxui:section type="edit">
                        <jxui:sectionrow>
                            <jxui:textbox dataattribute="name" required="true"/>
                            <jxui:multipartTextbox dataattribute="SUPER_DEPARTMENT_ID" required="true"
                                                   descdataattribute="PUB_DEPARTMENTSUPER_DEPARTMENT_ID.NAME"
                                                   descinputmode="readonly"
                                                   lookup="pubdepartment/lookup"/>
                        </jxui:sectionrow>

                        <jxui:sectionrow>
                            <jxui:textbox dataattribute="DEPARTMENT_ID"/>
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

            <jxui:sectionrow>
                <jxui:sectioncol>
                    <jxui:fragment id="f_department-user" type="department-user"/>
                </jxui:sectioncol>
            </jxui:sectionrow>
        </jxui:section>

    </jxui:form>
    <jxui:footer/>
</jxui:body>


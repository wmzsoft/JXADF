<%@ include file="/WEB-INF/content/common/header.jsp" %>
<jxui:head title="角色管理">
    <script type="text/javascript">
        function saveRole(me, e) {
            WebClientBean.save(jx_appNameType, function (jboset) {
                parent.refreshRoleTable(jboset.jbo.uidValue);
                appDialogClose(this, e);
            });
        }
    </script>
    <style type="text/css">
        .form_td_label {
            width: auto !important;
        }
    </style>
</jxui:head>
<jxui:body appName="pubrole" appType="main">

    <jxui:form id="pubrole" jboname="PUB_ROLE">
        <jxui:section type="main">
            <jxui:sectionrow>
                <jxui:sectioncol>
                    <jxui:section type="form">

                        <jxui:sectionrow>
                            <jxui:sectioncol cssClass="head_right">
                                <jxui:pushbutton cols="2" label="{app.pubrole.SAVEROLE_OK}" mxevent="saveRole"/>
                            </jxui:sectioncol>
                        </jxui:sectionrow>

                        <jxui:sectionrow>
                            <jxui:sectioncol>
                                <jxui:section type="edit">
                                    <jxui:sectionrow>
                                        <jxui:textbox dataattribute="ROLE_NAME"/>
                                    </jxui:sectionrow>

                                    <jxui:sectionrow>
                                        <jxui:textbox dataattribute="DESCRIPTION"/>
                                    </jxui:sectionrow>
                                </jxui:section>
                            </jxui:sectioncol>
                        </jxui:sectionrow>
                    </jxui:section>
                </jxui:sectioncol>
            </jxui:sectionrow>
        </jxui:section>
    </jxui:form>
</jxui:body>
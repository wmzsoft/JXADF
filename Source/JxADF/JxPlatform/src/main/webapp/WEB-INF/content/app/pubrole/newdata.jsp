<%@ include file="/WEB-INF/content/common/header.jsp" %>
<jxui:head title="数据权限管理">
    <script type="text/javascript">
        $(function () {
            if ("add" == "<%=request.getParameter("flag")%>") {
                $("input[dataattribute='GROUPNAME']").focus().attr("changed", 1).val("<%=request.getParameter("roleid")%>").blur();
                $("input[dataattribute='APP']").focus().attr("changed", 1).val("<%=request.getParameter("appid")%>").blur();
            }
        });
        function saveSec(me, e) {
            WebClientBean.save(jx_appNameType, function (jboset) {
                parent.refreshRecTable();
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
<jxui:body appName="pubrole" appType="sec">

    <jxui:form id="pubrole" jboname="SECURITYRESTRICT">
        <jxui:section type="main">
            <jxui:sectionrow>
                <jxui:sectioncol>
                    <jxui:section type="form">

                        <jxui:sectionrow>
                            <jxui:sectioncol cssClass="head_right">
                                <jxui:pushbutton cols="2" label="{app.pubrole.SAVEROLE_OK}" mxevent="saveSec"/>
                            </jxui:sectioncol>
                        </jxui:sectionrow>

                        <jxui:sectionrow>
                            <jxui:sectioncol>
                                <jxui:section type="edit">
                                    <jxui:sectionrow>
                                        <jxui:multipartTextbox dataattribute="GROUPNAME"
                                                               label="{app.pubrole.SECURITYRESTRICT.GROUPNAME}"
                                                               descdataattribute="SEC_PUBROLE_ID.ROLE_NAME"/>
                                    </jxui:sectionrow>

                                    <jxui:sectionrow>
                                        <jxui:multipartTextbox dataattribute="APP"
                                                               label="{app.pubrole.SECURITYRESTRICT.APP}"
                                                               descdataattribute="SECURITYRESTRICT_MAXAPPS_ID.DESCRIPTION"/>
                                    </jxui:sectionrow>

                                    <jxui:sectionrow>
																		<jxui:select label='{app.pubrole.SECURITYRESTRICT.OBJECTNAME}'   required="true"
																		              dataattribute="OBJECTNAME" 
																		              jboname="maxobject"
																		              displayname="description" displayvalue="objectname"
																		              ajax="contextPath+'/maxobject/index_list-table.action'"
																		              />                                    	
                                    </jxui:sectionrow>

                                    <jxui:sectionrow>
                                        <jxui:textbox dataattribute="RESTRICTION" cols="80" rows="4"/>
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
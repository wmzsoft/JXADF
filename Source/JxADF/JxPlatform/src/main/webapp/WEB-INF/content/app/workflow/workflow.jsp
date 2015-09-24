<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.jxtech.i18n.JxLangResourcesUtil" %>
<%@ include file="/WEB-INF/content/common/header.jsp" %>
<%
    String routecommon_tip = JxLangResourcesUtil.getString("app.workflow.ROUTECOMMON");
    String appdialogclosetip_tip = JxLangResourcesUtil.getString("app.workflow.APPDIALOGCLOSETIP");

    String operation_lable = JxLangResourcesUtil.getString("app.workflow.OPERATION");
    String opinion_lable = JxLangResourcesUtil.getString("app.workflow.OPINION");
    String designateduser_lable = JxLangResourcesUtil.getString("app.workflow.DESIGNATEDUSER");
%>
<jxui:head title="工作流发送">
    <script type="text/javascript">
        var jx_appType = "workflow";
        $(function () {
            $('#ok').tooltip(
                {
                    content: function () {
                        return "<li>同意-发送<ul><li>1、如果流程未启动则启动流程(上报)。</li><li>2、如果流程已启动，则发送到下一节点。</li></ul></li>";
                    }
                });
        });
    </script>
</jxui:head>
<jxui:body>
    <jxui:workflow id="s"/>
    <jxui:form id="workflow">
        <jxui:section type="main">
            <jxui:sectionrow>
                <jxui:sectioncol>
                    <jxui:section type="form">
                        <jxui:sectionrow>
                            <jxui:sectioncol>
                                <jxui:section type="head">
                                    <jxui:sectionrow>
                                        <jxui:sectioncol type="head_left">
                                            <jxui:label value="{app.workflow.ROUTEBPM}" type="title"/>
                                        </jxui:sectioncol>
                                        <jxui:sectioncol type="head_right" width="70%">
                                            <jxui:pushbutton label="{app.workflow.ROUTECOMMON}" mxevent="routeCommon" id="routeCommonBtn"
                                                             style="display:none" title="<%=routecommon_tip%>" menutype="INPUT"/>
                                            <jxui:pushbutton label="{app.workflow.APPDIALOGCLOSE}" mxevent="appDialogClose"
                                                             title="<%=appdialogclosetip_tip%>"
                                                             menutype="INPUT"/>
                                        </jxui:sectioncol>
                                    </jxui:sectionrow>
                                </jxui:section>
                            </jxui:sectioncol>
                        </jxui:sectionrow>
                        <jxui:sectionrow>
                            <jxui:sectioncol>
                                <jxui:section type="edit">
                                    <tr>
                                        <td class="form_td_label"><%=operation_lable%></td>
                                        <td class="form_td_content" id="actionContent"></td>
                                    </tr>
                                    <jxui:sectionrow>
                                        <td class="form_td_label">
                                            <%=opinion_lable%>
                                            <span id="noteRequired" style="display:none">
                                                <font color="red">*</font>
                                            </span>
                                        </td>
                                        <td class="form_td_content">
                                            <jxui:textbox label="" name="note" id="note" cols="90" rows="6"
                                                          mystyle="none"/>
                                        </td>
                                    </jxui:sectionrow>
                                    <tr id="pointuser" style="display:none;padding:3px 3px;">
                                        <td class="form_td_label"><%=designateduser_lable%></td>
                                        <td class="form_td_content" id="userContent"></td>
                                    </tr>
                                </jxui:section>
                            </jxui:sectioncol>
                        </jxui:sectionrow>
                        <!-- 显示历史记录 -->
                        <%--<jxui:sectionrow>
                            <jxui:sectioncol>
                                <jxui:table jboname="WFINBOXLOG" id="listtable" selectmode="none" footVisible="false" height="150"
                                            apprestrictions="WFID = '${param.instanceid}' AND MEMO IS NOT NULL">

                                    <jxui:tablecol dataattribute="WFINBOXLOGASSIGNER.DISPLAYNAME" label="处理人" sortable="false"
                                                   mxevent="link" urlType="me" urlParamName="app.action?type=list&app="
                                                   urlParamValue="INBOXVIEWWORKFLOW_ID.APP" urlTarget="_top"
                                            />
                                    <jxui:tablecol dataattribute="memo" label="备注" sortable="false"/>

                                </jxui:table>
                            </jxui:sectioncol>
                        </jxui:sectionrow>--%>
                    </jxui:section>
                </jxui:sectioncol>
            </jxui:sectionrow>
        </jxui:section>
    </jxui:form>
</jxui:body>


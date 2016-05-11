<%@ include file="/WEB-INF/content/common/header.jsp" %>
<jxui:head title="应用操作管理"/>
<jxui:body appName="maxmenu" appType="main">
    <jxui:form id="maxmenu" jboname="MAXMENU">
        <jxui:section type="main">
            <jxui:sectionrow>
                <jxui:sectioncol>
                    <jxui:section type="form">
                        <jxui:sectionrow>
                            <jxui:sectioncol>
                                <jxui:section type="head">
                                    <jxui:sectionrow>
                                        <jxui:sectioncol type="head_left">
                                            <jxui:label value="操作信息" type="title"/>
                                        </jxui:sectioncol>
                                        <jxui:sectioncol type="head_right">
                                            <jxui:buttongroup>
                                                <jxui:pushbutton label=" 保 存 " mxevent="save"/>
                                                <jxui:pushbutton label=" 新 建 " mxevent="add"/>
                                                <jxui:pushbutton label=" 返 回 " mxevent="gotoapp" appName="maxmenu"
                                                                 appType="list"/>
                                            </jxui:buttongroup>
                                        </jxui:sectioncol>
                                    </jxui:sectionrow>
                                </jxui:section>

                                <jxui:section type="edit">
                                    <jxui:sectionrow>
                                        <jxui:textbox dataattribute="app"/>
                                        <jxui:textbox dataattribute="menu"/>
                                        <jxui:textbox dataattribute="description"/>
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


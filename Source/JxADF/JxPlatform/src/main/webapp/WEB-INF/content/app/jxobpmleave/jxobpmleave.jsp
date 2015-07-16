<%@ include file="/WEB-INF/content/common/header.jsp"%>
<jxui:head title="{app.jxobpmleave.dochead}"/>
<jxui:body appName="jxobpmleave" appType="main">
 <jxui:form id="jxobpmleave" jboname="JX_OBPM_LEAVE">
     <jxui:appbar />
	<jxui:section type="main">		
		<jxui:sectionrow>
			<jxui:sectioncol>
				<jxui:section type="form">
					<jxui:sectionrow>
						<jxui:sectioncol>
                            <jxui:section type="edit">
								<jxui:sectionrow>
										<jxui:textbox dataattribute="LEAVE_NUM" required="true" />
										<jxui:textbox dataattribute="LEAVE_DESC" required="true"/>
								</jxui:sectionrow>
								<jxui:sectionrow>
										<jxui:textbox dataattribute="LEAVE_DAYS" />
										<jxui:textbox dataattribute="LEAVE_START" />
								</jxui:sectionrow>
								<jxui:sectionrow>
										<jxui:textbox dataattribute="CREATOR_ID" inputmode="readonly"/>
										<jxui:textbox dataattribute="CREATE_DATE" inputmode="readonly"/>
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


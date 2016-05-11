<%@ include file="/WEB-INF/content/common/header.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
	request.setCharacterEncoding("utf-8");
    String isNeeded = request.getParameter("isNeeded");
    String label = request.getParameter("label");
	String os = System.getProperties().getProperty("os.name");
	
	if(null != label && (os.startsWith("win") || os.startsWith("Win"))){
		label = new String(label.getBytes("ISO-8859-1"), "UTF-8");
	}
    request.setAttribute("label", label);
%>
<jxui:head title="附件-列表">
	<script type='text/javascript'>
		function addAm(me, e) {
			$(location)
					.attr(
							'href',
							'${pageContext.request.contextPath}/fileupload/index.jsp?code=${parameters.code[0]}&fromUid=${parameters.fromUid[0]}&readonly=${parameters.readonly[0]}&type=imglist&imgtype=${parameters.imgtype[0]}&vfloder=${parameters.vfolder[0]}&isNeeded=${parameters.isNeeded[0]}&label=${label}');
		}
	</script>
</jxui:head>
<jxui:body appName="attachment" appType="list" mainApp="false">
	<jxui:form id="attachment">
		<%-- <jxui:section type="head">
			<jxui:sectionrow>
				<jxui:sectioncol cssClass="head_left">
					<%
					    if ("true".equals(isNeeded)) {
					%>
					<jxui:label value="（<font color='red'>必须上传附件</font>）" type="title" />
					<%
					    }
					%>
				</jxui:sectioncol>

			</jxui:sectionrow>
		</jxui:section> --%>
		<jxui:fragment id="listtable_${parameters.attid[0]}" type="imglist-table"
			url='app.action?code=${parameters.code[0]}&fromUid=${parameters.fromUid[0]}&readonly=${parameters.readonly[0]}&imgtype=${parameters.imgtype[0]}&imgcols=${parameters.imgcols[0]}&imgwidth=${parameters.imgwidth[0]}&imgheight=${parameters.imgheight[0]}&vfolder=${parameters.vfolder[0]}&isNeeded=${parameters.isNeeded[0] }&label=${label}' />
	</jxui:form>
	<jxui:footer />
</jxui:body>


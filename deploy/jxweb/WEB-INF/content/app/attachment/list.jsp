<%@ include file="/WEB-INF/content/common/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    request.setCharacterEncoding("utf-8");
    String isNeeded = request.getParameter("isNeeded");
    String label = request.getParameter("label");
    String os = System.getProperties().getProperty("os.name");

    if (null != label && (os.startsWith("win") || os.startsWith("Win"))) {
        label = new String(label.getBytes("ISO-8859-1"), "UTF-8");
    }
    request.setAttribute("label", label);
%>
<jxui:head title="附件-列表">
    <script type='text/javascript'>
        var uploadUrl = '${pageContext.request.contextPath}/fileupload/index.jsp?code=${parameters.code[0]}&fromUid=${parameters.fromUid[0]}&readonly=${parameters.readonly[0]}&type=list&imgtype=${parameters.imgtype[0]}&vfolder=${parameters.vfolder[0]}&isNeeded=${parameters.isNeeded[0]}&label=${label}';
        function addAm(me, e) {
            //vfolder ： 可以对同一个记录进行虚拟文件夹分类，实现多字段附件功能
            debug("文件上传的URL为：" + uploadUrl);
            $(location).attr('href', uploadUrl);
        }

        //加载附件
        function loadAttachment(tableid, e) {
            $.ajax({
                url: "app.action?app=attachment&type=list-table&code=${parameters.code[0]}&fromUid=${parameters.fromUid[0]}&readonly=${parameters.readonly[0]}&imgtype=${parameters.imgtype[0]}&imgcols=${parameters.imgcols[0]}&imgwidth=${parameters.imgwidth[0]}&imgheight=${parameters.imgheight[0]}&vfolder=${parameters.vfolder[0]}&attid=${parameters.attid[0]}&operate=${parameters.operate[0]}",
                cache:false,
                data: {
                    "label": getUrlParam("label"),
                    "isNeeded": "<%=isNeeded%>"
                },
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                success: function (data) {
                    var $container = $("#div_listtable_${parameters.attid[0]}");
                    $container.html(data);
                    var $iframe = $("#iframeAtt_${parameters.attid[0]}",parent.document);
                    $iframe.height($(window).height()+$(".table_list_foot_tip").height())
                    resizeDialogWhenFrame();
                }
            });
        }
        //附件需要覆盖此方法。
        var getTableData = JxUtil.extend(getTableData, function (tableid, e) {
            loadAttachment(tableid, e);
        }, false);
    </script>
</jxui:head>
<jxui:body appName="attachment" appType="list" mainApp="false">
    <div id="uploadDialog" title="文件上传"></div>
    <jxui:form id="attachment">
        <div id="div_listtable_${parameters.attid[0]}"></div>
        <script type="text/javascript">
            $(function () {
                loadAttachment();
            });
        </script>
    </jxui:form>
    <jxui:footer/>
</jxui:body>


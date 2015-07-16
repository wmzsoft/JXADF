<%@ page import="com.jxtech.i18n.JxLangResourcesUtil" %>
<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<%
    String readonly = request.getParameter("readonly");
    String label = request.getParameter("label");
    String os = System.getProperties().getProperty("os.name");
    String operate = request.getParameter("operate");
    if (null != label && (os.startsWith("win") || os.startsWith("Win"))) {
        label = new String(label.getBytes("ISO-8859-1"), "UTF-8");
    }
    request.setAttribute("label", label);


    String attach_required = JxLangResourcesUtil.getString("app.attachment.ATTACHMENT_REQUIRED");
    String attach_list_title = JxLangResourcesUtil.getString("app.attachment.ATTACHMENT_LIST_TITLE");
    String attach_tip = JxLangResourcesUtil.getString("app.attachment.ATTACHMENT_TIP");
    String attach_tip_title = JxLangResourcesUtil.getString("app.attachment.ATTACHMENT_TIP_TITLE");
    String attach_upload = JxLangResourcesUtil.getString("app.attachment.ATTACHMENT_UPLOAD");
    String attach_del = JxLangResourcesUtil.getString("app.attachment.ATTACHMENT_DEL");
//String attach_size = JxLangResourcesUtil.getString("app.attachment.ATTACHMENT_SIZE");
%>
<style type="text/css">
    .table_list_thead_table_title{
        text-align: left;
        font-weight: bold;
    }
    .table_list_thead_table_button2{
        display: none;
    }
    .table_list_thead_table_button1{
        text-align: right;
    }
    .table_list_thead_table_button1 input{
        margin-left: 5px;
    }
    .table_list th,
    .table_list tfoot table{
        background-color: rgb(241,241,255) !important;
        color: black !important;
    }
    .table_list thead table{
        background-color: rgb(202, 212, 238) !important;
    }

</style>
<script type="text/javascript">
    $(function () {
        var label = decodeURI(getUrlParam("label")) || "<%= attach_list_title%>";
        var need = getUrlParam("isNeeded") == "true" ? "<span style='color:red'>(<%=attach_required%>)</span>":"";
        var $table_wrap = $("#div_listtable_${parameters.attid[0]}");
        $table_wrap.find(".table_list_thead_table_title").html(label + need);
    });
</script>
<jxui:table jboname="TOP_ATTACHMENT" id="listtable_${parameters.attid[0]}" selectmode="multiple"
            url="app.action?code=${parameters.code[0]}&fromUid=${parameters.fromUid[0]}&readonly=${parameters.readonly[0]}&imgcols=${parameters.imgcols[0]}&imgwidth=${parameters.imgwidth[0]}&imgheight=${parameters.imgheight[0]}&imgtype=${parameters.imgtype[0]}&vfolder=${parameters.vfolder[0]}"
            apprestrictions="state=2 and job_type_code='${parameters.code[0]}' and attachment_id in (select attachment_id from TOP_ATTACHMENT_OBJECT_RELATION where state=1 and object_id='${parameters.fromUid[0]}' ${jxfn:sqlCause('data_from',parameters.vfolder[0])})"
            bottomtipvalue="<%=attach_tip%>" title="<%=attach_tip_title%>">

    <%if ("false".equals(readonly)) {%>
    <jxui:tablebutton id="upl" label="<%=attach_upload%>" mxevent="addAm"/>
    <%if ("OWER".equals(operate)) {%>
    <jxui:tablebutton label="<%=attach_del%>" mxevent="delOwer"/>
    <%} else {%>
    <jxui:tablebutton label="<%=attach_del%>" mxevent="dellist" fragmentid="listtable_${parameters.attid[0]}"/>
    <%}%>
    <%}%>
    <jxui:tablecol dataattribute="FILE_NAME" mxevent="link" urlType="me" urlParamName="filedown?"
                   urlParamValue="id:attachment_id,name:file_name" urlTarget="_blank" label="{app.attachment.ATTACHMENT_FILE_NAME}"/>
    <jxui:tablecol dataattribute="FILE_SIZE" format=',###' label="{app.attachment.ATTACHMENT_SIZE}"/>
    <jxui:tablecol dataattribute="CREATOR_NAME" label="{app.attachment.ATTACHMENT_CREATOR_NAME}"/>
    <jxui:tablecol dataattribute="CREATE_TIME" label="{app.attachment.ATTACHMENT_CREATE_TIME}"/>
</jxui:table>

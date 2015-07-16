<%@ include file="/WEB-INF/content/common/fragment-header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    request.setCharacterEncoding("GBK");
    String readonly = request.getParameter("readonly");
    String label = request.getParameter("label");
    String os = System.getProperties().getProperty("os.name");

    if (null != label && (os.startsWith("win") || os.startsWith("Win"))) {
        label = new String(label.getBytes("ISO-8859-1"), "GBK");
    }
    request.setAttribute("label", label);
%>
<script type='text/javascript'>
    $(function () {
        var need = ("true" == getUrlParam("isNeeded") ? "<font color='red'>(必须上传附件)</font>" : "");

        $(".table_list_thead_table_title", $("#div_listtable_${parameters.attid[0]}")).html(
                (getUrlParam("label") == null || getUrlParam("label") == "") ? "附件列表" + need : getUrlParam("label") + need).css({
                    "text-align": "left",
                    "font-weight": "bold"
                });

        $(".table_list_thead_table_button2", $("#div_listtable_${parameters.attid[0]}")).remove();
        $(".table_list_thead_table_button1", $("#div_listtable_${parameters.attid[0]}")).css({
            "text-align": "right"
        });
        $("input", $(".table_list_thead_table_button1")).css({
            "margin-left": "5px"
        });
        //表标题
        $("th").css({
            "background-color": "rgb(241,241,255)",
            "color": "black"
        });
        //表上面
        $(".table_list thead tr td table").css({
            "background-color": "rgb(202, 212, 238)"
        });
        //表脚
        $(".table_list tfoot tr td table").css({
            "background-color": "rgb(241,241,255)"
        });
    });

    function DISPLAYPS(me, e) {
        //var bb= $(me).children();
        var item = '';
        $("input[name='ck_listtable']").each(function () {
            item += $(this).val() + ",";
        });
        var id = item.substring(0, item.length - 1);
        var url = "./app.action?app=projectphoto&type=photo-prew&x=" + $("#fromUid").val() + "&id=" + id + "";
        window.open(url, '图片浏览', 'top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=yes,location=no, status=no,fullscreen = yes');
    }
</script>

<jxui:table jboname="TOP_ATTACHMENT" id="listtable_${parameters.attid[0]}" selectmode="multiple"
            url="app.action?code=${parameters.code[0]}&fromUid=${parameters.fromUid[0]}&readonly=${parameters.readonly[0]}&imgcols=${parameters.imgcols[0]}&imgwidth=${parameters.imgwidth[0]}&imgheight=${parameters.imgheight[0]}&imgtype=${parameters.imgtype[0]}"
            apprestrictions="state=2 and job_type_code='${parameters.code[0]}' and attachment_id in (select attachment_id from TOP_ATTACHMENT_OBJECT_RELATION where state=1 and object_id='${parameters.fromUid[0]}') ${jxfn:sqlCause('data_from',parameters.vfolder[0])}"
            bottomtipvalue="下载附件，请在文件名链接中右键另存为。" title="附件明细" tabletype="imglist" readonly="<%=readonly%>"
            imgcols="${parameters.imgcols[0]}" imgwidth="${parameters.imgwidth[0]}"
            imgheight="${parameters.imgheight[0]}">
    <%if ("false".equals(readonly)) {%>
    <jxui:tablebutton id="upl" label="上传" mxevent="addAm"/>
    <jxui:tablebutton id="dl" label="删除" mxevent="delList"/>
    <%}%>
    <jxui:tablebutton label="列表" mxevent="href"
                      params="app.action?app=attachment&type=list&fromid=${parameters.fromid[0]}&code=${parameters.code[0]}&fromUid=${parameters.fromUid[0]}&readonly=${parameters.readonly[0]}&imgcols=${parameters.imgcols[0]}&imgtype=${parameters.imgtype[0]}&imgwidth=${parameters.imgwidth[0]}&imgheight=${parameters.imgheight[0]}&vfolder=${parameters.vfolder[0]}&label=${label}&isNeeded=${parameters.isNeeded[0] }"/>
    <jxui:tablecol dataattribute="ATTACHMENT_ID" mxevent="displayps" urlType="me" urlParamName="filedown?type=imglist&"
                   urlParamValue="id:attachment_id,name:file_name" urlTarget="_blank"/>
    <jxui:tablecol dataattribute="FILE_NAME" mxevent="link" urlType="me" urlParamName="filedown?"
                   urlParamValue="id:attachment_id,name:file_name" urlTarget="_blank"/>
</jxui:table>

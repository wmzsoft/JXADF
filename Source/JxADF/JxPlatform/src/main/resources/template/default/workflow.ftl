<#--
/**
$id:obpmworkflow$
$author:smellok
#date:2014.08.15
**/
-->
<#t/>

<input type="hidden" id="status" value="${parameters.status!''}"/>
<input type="hidden" id="nextStatus" value="${parameters.nextStatus!''}"/>

<script type='text/javascript'>
    $(function () {
    <#if (parameters.actions?? && parameters.actions?size > 0)>
        <#assign keys = parameters.actions?keys>
        <#list keys as key>
            var tempChoice = $("<input type='radio' name='action' value='${key}' id='r_${key}' />");
            var tempLabel = $("<label for='r_${key}' style='margin-left:5px'>${parameters.actions[key]}</label>");
            $("#actionContent").append($("<div></div>").css({padding: "3px 0"}).append(tempChoice).append(tempLabel));
            tempChoice.bind("click", function () {
                var action = $(this).attr("value");

                $("div[useraction]").each(function () {
                    if ($(this).attr("useraction") != action) {
                        $(this).hide();
                    } else {
                        $(this).show();
                    }
                });

                resizeDialogWhenFrame();
            });
        </#list>
        <#if (parameters.actionUsers?? && parameters.actionUsers?size > 0)>
            <#assign keys = parameters.actionUsers?keys>
            <#list keys as key>
                var select = "<div useraction='${key}' style='display:none;height:90%;width:97%;padding:3px 3px;'>"
                        + "\n<select id='s_${key}' action='${key}' style='width:100%;' multiple='multiple'>";
                var user = "${parameters.actionUsers[key]}"; //|分割
                if (user.length) {
                    var users = user.split("|");
                    for (var i = 0; i < users.length; i++) {
                        var uid = users[i];
                        var uname = users[i];
                        var ix = uid.indexOf("[");
                        if (ix >= 0) {
                            uname = uid.slice(ix + 1, uid.length - 1);
                            uid = uid.slice(0, ix);
                        }
                        select = select
                                + "\n<option id='o_" + uid + "' value='" + uid + "' selected='selected'>" + uname + "</option>";
                    }
                }
                select = select + "\n</select></div>";
                $("#userContent").append(select);
                $("#s_${key}").select2({placeholder: 'Select a option', allowClear: 'true'});
            </#list>
            $("#pointuser").show();
        </#if>
    <#elseif (parameters.complete!'FALSE') == 'TRUE'>
        $("#actionContent").append("流程已经结束！");
    <#else>
        $("#actionContent").append("没有权限！");
    </#if>

    <#if ((parameters.routeBtnVisible!'FALSE')=='TRUE')>
        $('#routeCommonBtn').css('display', '');
    </#if>
    <#if ((parameters.noteRequired!'FALSE')=='TRUE')>
        $('#noteRequired').css('display', '');
        resizeDialogWhenFrame();
    </#if>
    });
</script>
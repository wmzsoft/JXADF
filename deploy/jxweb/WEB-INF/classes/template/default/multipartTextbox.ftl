<#--
/**
$id:multipartTextbox$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if parameters.inputmode??>
    <#assign inputmode = parameters.inputmode?upper_case>
</#if>

<#assign fromMultipart = true />
<#include "textbox/textbox.ftl"><#t>
<#assign cssClass="">
<#if parameters.descdataattribute??>
    <input type="text" value="${parameters.desDataValue!''}"<#rt/>
       <#lt> id="${parameters.id}_DESC" <#rt>
       <#lt> dataattribute="${parameters.descdataattribute}" <#rt>
    <#if ((parameters.required!false) && !(parameters.readonly!false))>
        <#lt> required="required" <#rt/>
        <#assign cssClass="validate[required]">
    </#if>
       <#lt> readonly="true"<#rt/>
    <#if (parameters.readonly?? && parameters.readonly)>
       <#lt> class="form_td_multipart_readonly"
    <#else>
       <#lt> class="form_td_multipart ${cssClass}"
    <#--onBlur="inputOnBlur(this)" onChange="inputOnChange(this)" changed="0" <#rt/>-->
    </#if>
    <#t/>/><#t/>

    <#if ("QUERY" == inputmode)>
    <script type="text/javascript">
        var idValue = $("#" + "${parameters.id}").val();
        if ("" != idValue) {
            WebClientBean.getRelationshipAttrValue("${parameters.jboname}", "${parameters.descdataattribute}", idValue, function (data) {
                $("#" + "${parameters.id}_DESC").val(data);
            });
        } else {
            $("#" + "${parameters.id}_DESC").val("");
        }
    </script>
    </#if>
</#if>

<#if (parameters.lookup??) && !(parameters.readonly!false)>
    <#assign width = parameters.lookupWidth!0>
    <#assign height = parameters.lookupHeight!0>
<img src="${base}/skin/${skinName}/images/lookup.png" class="iconbutton"
     onclick="lookupAction('${parameters.lookup}','${parameters.id}', ${width}, ${height})"/>
<img src="${base}/skin/${skinName}/images/lookup-clear.png" class="iconbutton"
     onclick="lookupClear(this, event)"/>
</#if>
<#t/>
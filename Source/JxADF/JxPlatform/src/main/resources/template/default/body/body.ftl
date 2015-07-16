<#--
/**
$id:body$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<!-- body.ftl begin -->
<body <#rt>
<#if (parameters.cssStyle??)>
    <#lt> style="${parameters.cssStyle?html}" <#rt/>
<#elseif (parameters.dynamicAttributes.style??)>
    <#lt> style="${parameters.dynamicAttributes.style}" <#rt/>
</#if>
<#if parameters.cssClass?exists>
    <#lt> class="${parameters.cssClass?html}" <#rt/>
</#if>
<#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
        ><#t>
<script type='text/javascript'>
    <#if parameters.appName??>
    var jx_appName = "${parameters.appName}";
    var jx_appType = "${parameters.appType!'MAIN'}";
    var jx_appNameType = jx_appName + "." + jx_appType;
    var comtopUrl = '${parameters.comtopUrl!''}';
    var reportUrl = '${parameters.reportUrl!''}';
    var contextPath = "${base}";
        <#if (parameters.msg??)>
        if ("lookup" != jx_appType) {
            showToolbarMsg("${parameters.msg}");
        }
        </#if>
    </#if>
    var userLangeCode = "${parameters.lang!''}";
    $(function () {
        loadLanguage('${parameters.lang!''}');
    });
</script>

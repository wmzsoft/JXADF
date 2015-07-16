<#--
/**
$id:sectioncol$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<div <#t>
<#if parameters.cssStyle?exists>
        style="${parameters.cssStyle?html}"<#t>
</#if>
<#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
        ><#t>
<#if parameters.type??>
    <#if parameters.type=="BTN_GROUP">
        <div class="btn-group form-group"><#t>
    </#if>
</#if>
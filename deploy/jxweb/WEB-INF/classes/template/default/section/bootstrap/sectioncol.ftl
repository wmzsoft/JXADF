<#--
/**
$id:sectioncol$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<div <#t>
<#if parameters.cssClass??>
	<#lt> class="${parameters.cssClass?html}"<#rt/>
<#elseif parameters.dynamicAttributes.cssClass??>
	<#lt> class="${parameters.dynamicAttributes.cssClass?html}"<#rt/>
<#elseif parameters.type?? >
	<#if parameters.type=='HEAD_LEFT'>
		<#lt> class="table_head_td_left"<#rt/>
	<#elseif parameters.type=='HEAD_RIGHT' >
		<#lt> class="table_head_td_right"<#rt/>
	</#if>
</#if>
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
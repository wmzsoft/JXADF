<#--
/**
$id:section$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->

<table <#rt>
<#if parameters.cssClass??>
	class="${parameters.cssClass?html}"<#rt/>
<#elseif parameters.type??>
	class="table_${parameters.type?lower_case}"<#rt/>
<#else>
	class="table_edit_list"<#rt/>
</#if>
<#if parameters.cssStyle?exists>
 	style="${parameters.cssStyle?html}"<#rt/>
</#if>
<#if parameters.id??>
	id="${parameters.id}" 
</#if>
<#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
>
<#t/>
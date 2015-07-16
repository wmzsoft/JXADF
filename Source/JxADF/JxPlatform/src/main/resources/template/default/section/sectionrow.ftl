<#--
/**
$id:null$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<tr<#t/>
<#if parameters.id??>
	id="${parameters.id}" <#rt/>
</#if>
<#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
><#t/>
<#--
/**
$id:link$
$author:wmzsoft@gmail.com
#date:2014.11
**/
-->
<#t/>
<#if ( (parameters.title??) || (parameters.titleDataValue??) )>
	<td class="form_td_label"><#t>
	<#if (parameters.titleDataValue??)>
		${parameters.titleDataValue}<#t>
	<#else>
		${parameters.title}<#t>
	</#if>
	</td><#t>
	<td class="form_td_content" <#rt>
    <#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
    <#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
    <#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
    ><#t>
</#if>
<#if ((parameters.urlDataValue??) || (parameters.url??) || (parameters.txtDataValue??) || (parameters.txt??) || (parameters.mxevent??)) >
	<a id='${parameters.id}' <#rt>
	<#if  (parameters.mxevent??)>
		onclick='${parameters.mxevent}(this,event)' <#t>
		<#if (parameters.urlDataValue??)>
			url='${parameters.urlDataValue}' <#t>
		<#elseif (parameters.url??)>
			url='${parameters.url}' <#t>
		</#if>
	<#elseif (parameters.urlDataValue??)>
		href='${parameters.urlDataValue}' <#t>
	<#elseif (parameters.url??)>
		href='${parameters.url}' <#t>
	<#else>
		href='javascript:void(0)' <#t>
	</#if>
	><#t>
	<#if (parameters.txtDataValue??)>
		${parameters.txtDataValue}<#t>
	<#elseif (parameters.txt??)>
		${parameters.txt}<#t>
	</#if>
	</a><#t>
</#if>
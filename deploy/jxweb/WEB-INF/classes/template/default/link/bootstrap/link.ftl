<#--
/**
$id:link$
$author:wmzsoft@gmail.com
#date:2014.11
**/
-->
<#t/>
<#if ( (parameters.title??) || (parameters.titleDataValue??) )>
	<div class="form-group"><label class="col-sm-2 control-label"><#t>
	<#if (parameters.titleDataValue??)>
		${parameters.titleDataValue}<#t>
	<#else>
		${parameters.title}<#t>
	</#if>
	</label><#t>
    <div class="col-sm-10">
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
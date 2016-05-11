<#--
/**
$id:selectList$
$author:wmzsoft@gmail.com
#date:2013.10
**/
-->
<#t/>
<ul id="${parameters.id}" class="${parameters.ulClass!'connectedSortable'}" <#rt/>
<#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
<#t>><#t>
<#if (parameters.jboset??) && (parameters.displayValue??) && (parameters.displayName??) >
	<#list parameters.jboset.jbolist as jbo>
		<#if jbo.data??>
			<li class="${parameters.liClass!'listLi'}" value="${jbo.getString(parameters.displayValue)!''}">${jbo.getString(parameters.displayName)!''}</li>
		</#if>
	</#list>
</#if>
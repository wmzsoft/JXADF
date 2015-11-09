<#--
/**
$id:textbox$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<!--textbox.ftl begin-->
<#assign inputmode=(parameters.inputmode!'edit')>
<#if ((parameters.render!"") != "HIDDEN")>
    <#if (parameters.mystyle!'TD')!='NONE'>
        <#include "textbox-label.ftl"><#t>
    </#if>
</#if>
<#if (inputmode=='QUERY') || (inputmode=='QUERYIMMEDIATELY')>
	<#include "textbox-query.ftl"><#t>
<#elseif ((inputmode=='READONLY') || (parameters.readonly!false))>
    <#include "textbox-readonly.ftl"><#t>
<#else>
	<#include "textbox-input.ftl"><#t>
</#if>

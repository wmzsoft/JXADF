<#--
/**
$id:checkbox$
$author:sl
#date:2013.08.22
**/
-->
<#if (parameters.mystyle!'TD')=='TD'>
    <td class="checkbox_td_label <#rt>
    <#if (parameters.readonly!false)>
        <#lt> readonly<#rt>
    </#if>"<#t>
    <#if (parameters.labeltip??)>
        <#lt> title='${parameters.labeltip}' <#rt>
    </#if>
    ><#t>
    <#include "../common/input-label.ftl"/><#t>
    </td><td class="checkbox_td_content" colspan="${parameters.colspan!'1'}" <#rt>
    <#if (parameters.valuetip??)>
        <#lt> title='${parameters.valuetip}' <#rt>
    </#if>
    ><#t>
</#if>
<#if ((parameters.inputmode!'edit')=='QUERY') || ((parameters.inputmode!'edit')=='QUERYIMMEDIATELY')>
	<#if (parameters.dataattribute??)>
        <#include "checkbox-query.ftl"/>
	</#if>
<#else>
	<#if (parameters.dataattribute??) || (parameters.dataValue??)>
    	<#include "checkbox-input.ftl"/>
    </#if>
</#if>

<#--
/**
$id:label$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if (parameters.type!'x') != 'PROP'>
    <span <#rt/>
    <#if (parameters.type??)>
    	<#if (parameters.type=="TITLE")>
    	   <#lt> class="label_title"<#rt/>
    	<#else>
    	   <#lt> class="label_content" <#rt/>
    	</#if>
    </#if>
    <#if (parameters.id??)>
    	<#lt> id="${parameters.id}" <#rt>
    </#if>
    ><#t/>
</#if>

<#if (parameters.dataattribute??)>
    ${parameters.dataValue!''}<#t>
<#else>
    ${parameters.value!''}<#t>
</#if>

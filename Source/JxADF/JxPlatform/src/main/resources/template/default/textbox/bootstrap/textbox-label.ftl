<#--
/**
$id:textbox$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if (parameters.required!false==true)>
    <#assign icss=" required">
</#if>
<label class="control-label ${icss!}">
<#if (parameters.label??)>
    <#assign plabel=parameters.label>
<#elseif (parameters.dynamicAttributes.label??)>
    <#assign plabel=parameters.dynamicAttributes.label>
<#elseif (parameters.columnAttribute??)>
    <#assign plabel=parameters.columnAttribute.title!parameters.tagbundle['label.null']!>
</#if>
<#if (((parameters.rows!1)?number) == 1)>
    <#if ((parameters.dataValue!'x')?lower_case)?starts_with("http://") || ((parameters.dataValue!'x')?lower_case)?starts_with("https://")>
        <a href="${parameters.dataValue!'javascript:void(0)'}" target="_blank">${plabel!''}</a><#t>
    <#elseif (((parameters.render!'x')?upper_case)=="LINKLABEL")>
        <a href="${parameters.renderExtends!''}${parameters.dataValue!''}" target="_blank">${plabel!''}</a><#t>
    <#else>
        ${plabel!''}<#t>
    </#if>
<#elseif (((parameters.render!'x')?upper_case)=="LINKLABEL")>
    <a href="${parameters.renderExtends!''}" target="_blank">${plabel!''}</a><#t>
<#else>
    ${plabel!''}<#t>
</#if>
</label><#t>
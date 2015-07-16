<#--
/**
$id:select$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if (parameters.mystyle??) && (parameters.mystyle=="COL")>
    <#--  如果是col类型的，啥也不做-->
<#else>
    <#assign avisible=(parameters.visible!true)>
    <#if (avisible)>
        <#if ((parameters.inputmode!'')=='QUERYIMMEDIATELY') && (parameters.jboset??)>
            <#if ((parameters.jboset.jbolist?size)<=1) >
                <#assign avisible=false>
            </#if>
        </#if>
    </#if>
    <#if (parameters.mystyle!'TD')=='TD'>
        <#lt><td class="form_td_label <#rt>
	    <#if ((parameters.inputmode!'edit')=='QUERY') ||((parameters.inputmode!'edit')=='QUERYIMMEDIATELY')>
	        <#-- 查询框样式补充 -->
	        <#assign aSelected=0>
	    <#else>
	        <#-- 输入框样式补充 -->
	        <#if (parameters.readonly!false)>
	            <#lt> readonly<#rt>
	        <#elseif (parameters.required!false)>
	            <#lt> required<#t/>
	            <#assign cssClass = "required">
	        </#if>
	    </#if>
	    <#lt>"><#rt>
    </#if>
    <#if (parameters.appBundle?? && parameters.appBundle.containsKey('${parameters.dataattribute}'))>
        <#assign labelvalue=parameters.appBundle.getString(parameters.dataattribute)>
    <#elseif (parameters.label??) && (avisible==true) >
        <#assign labelvalue=parameters.label>
    </#if>
    <#if ((parameters.render!'x')?upper_case=="LINKLABEL")>
        <a href="${parameters.renderExtends!''}<#t>
        <#if (parameters.selectedJbo??)>
            <#if  (parameters.renderParam??)>
                ${parameters.selectedJbo.getString(parameters.renderParam)!''}<#t>
            <#else>
                ${parameters.selectedJbo.uidValue!'0'}<#t>
            </#if>
        <#else>
            ${parameters.selected!}<#t>
        </#if>
        " target="_blank">${labelvalue!}</a><#t>
    <#else>
        ${labelvalue!}<#t>
    </#if>
    <#if (parameters.mystyle!'TD')=='TD'>
        </td><#t>
        <td class="form_td_content<#rt>
        <#if !(parameters.readonly!false) >
            <#lt> form_td_content_select<#rt>
        </#if>
        "<#t>
        <#if parameters.colspan??>
            <#lt> colspan="${parameters.colspan?html}" <#rt/>
        </#if>
        <#if parameters.rowspan??>
            <#lt>　rowspan="${parameters.rowspan?html}" valign="middle"　<#rt/>
        </#if>
         ><#t>
        <#if !(parameters.readonly!false)>
            <#lt><span class="form_td_select" <#rt>
            <#if parameters.width??>
                <#lt> style="width:${parameters.width}px" <#rt>
            </#if>
            ><#t>
        </#if>
    <#else>
        <#if !(parameters.readonly!false)>
            <#lt><span class="form_select_disable" <#rt>
            <#if parameters.width??>
                <#lt> style="width:${parameters.width}px" <#rt>
            </#if>
            ><#t>
        </#if>
    </#if>    
    <#include "select-options.ftl">    
</#if>

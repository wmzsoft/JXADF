<#--
/**
$id:attachment$
$author:wmzsoft@gmail.com
#date:2013.12
**/
-->
<#assign extcss="">
<#if (parameters.required!false)>
        <#assign extcss=" required">
</#if>
<#if (parameters.mystyle!'TD')=='TD'>
    <td class="form_td_label${extcss}">
    <#if parameters.label??>
        ${parameters.label}
    <#else>
        ${parameters.tagbundle['ATTACHMENT_NAME']!'附件'}
    </#if>
    </td><td<#rt>
    <#if parameters.cols??>
    	colspan=${parameters.cols?number - 1}
    </#if>
    <#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
    <#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
    <#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
    ><#t>
</#if>
<img class='iconbutton' id="${parameters.id}" <#rt>
    <#if (parameters.required!false)>
        <#lt> required="true" <#rt>
        <#if ((parameters.acount)>0)>
             <#lt> value="${parameters.acount}" <#rt>
        </#if>
    </#if>
    <#if ((parameters.acount)>0)>
        <#lt> src='${baseSkin}/images/attachmentok.png' <#rt>
    <#else>
        <#lt> src='${baseSkin}/images/attachment.png' <#rt>
    </#if>
    <#lt> title="${parameters.acount}" onclick="attachment(this,event)" <#rt>
    <#lt> type="${parameters.type!''}" cols="${parameters.cols!0}" <#rt>
    <#lt> imgwidth="${parameters.imgwidth!''}" imgheight="${parameters.imgheight!''}" imgtype="${parameters.imgtype!''}"<#rt>
    <#lt> vfolder="${parameters.vfolder!''}"<#rt>
    <#if parameters.jbo??>
        <#lt> uid='${parameters.jbo.uidValue!0}' <#rt>
        <#lt> upath='${base}/app.action?fromUid=${parameters.jbo.uidValue!0}&code=${parameters.datasrc!(parameters.jbo.jboName)}&readonly=${(parameters.jbo.readonly)?string}&pagesize=10' /><#rt>
    </#if>

     </td>
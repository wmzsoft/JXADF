<#--
/**
$id:pushbutton$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#t/>
<#if (parameters.ignorepermission!false==true) || (parameters.permission!false==true)>
    <#if (parameters.visible!false==true)>
        <#assign cssClass="pushbutton">
        <#if parameters.cssClass??>
            <#if ((parameters.cssClass!"")!="")>
                <#assign cssClass="pushbutton ${parameters.cssClass}">
            </#if>
        </#if>
        <#if ((parameters.menutype!'unknow')=='LIA') >
            <#lt>
        <li>
            <#lt><a <#rt/>
        <#elseif ((parameters.menutype!'unknow')=='INPUT') >
            <#lt><input value="${parameters.label!'按钮'}" class="${cssClass}" <#rt/>
            <#lt> type="${parameters.type!'button'}" <#rt>
        <#else>
            <#lt> <span class="${cssClass}" <#rt>
        </#if>
        <#lt> id="${parameters.id}" <#rt/>
        <#if (parameters.mxevent??)>
            <#lt> onclick="${parameters.mxevent}(this,event)" <#rt/>
            <#lt> mxevent="${parameters.mxevent}" <#rt/>
        </#if>
        <#if (parameters.appType??)>
            <#lt> apptype="${parameters.appType?lower_case}"<#rt/>
        </#if>
        <#if (parameters.appName??)>
            <#lt> appname="${parameters.appName}"<#rt/>
        </#if>
        <#if (parameters.targetid??)>
            <#lt> targetid="${parameters.targetid}"<#rt/>
        </#if>
        <#if (parameters.readonly!false)>
            <#lt> readonly="readonly"<#rt/>
        </#if>
        <#if (parameters.disabled!false)>
            <#lt> disabled="disabled" <#rt/>
        </#if>
        <#lt> <#include "/${parameters.templateDir}/simple/scripting-events.ftl" /> <#rt/>
        <#lt> <#include "/${parameters.templateDir}/simple/common-attributes.ftl" /> <#rt/>
        <#lt> <#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /> <#rt/>
        <#if ((parameters.menutype!'unknow')=='LIA') >
                ><#t>
        <#elseif ((parameters.menutype!'unknow')=='INPUT') >
            /><#t/>
        <#else>
            ><#t/>
        </#if>
    </#if>
</#if> 
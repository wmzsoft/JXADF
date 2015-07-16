<#--
/**
$id:pushbutton$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if (parameters.ignorepermission!false==true) || (parameters.permission!false==true)>
    <#if (parameters.visible!false==true)>
        <#if ((parameters.menutype!'unknow')=='LIA') >
        <li role="presentation " id="${parameters.id}_li"><#t>
            <a href="javascript:void(0);" <#t>
        <#elseif ((parameters.menutype!'unknow')=='INPUT') >
            <input value="${parameters.label!'按钮'}" class="pushbutton" <#t>
                   type="${parameters.type!'button'}" <#t>
        <#else>
                <button class="btn btn-default" <#t>
                        type="button" <#t>
        </#if>
                        id="${parameters.id}" <#t>
        <#if (parameters.mxevent??)>
                        onclick="${parameters.mxevent}(this,event)" <#t>
                        mxevent="${parameters.mxevent}" <#t>
        </#if>
        <#if (parameters.appType??)>
                        apptype="${parameters.appType?lower_case}" <#t>
        </#if>
        <#if (parameters.appName??)>
                        appname="${parameters.appName}"<#t>
        </#if>
        <#if (parameters.targetid??)>
                        targetid="${parameters.targetid}"<#t>
        </#if>
        <#if (parameters.readonly!false)>
                        readonly="readonly"<#t>
        </#if>
        <#if (parameters.disabled!false)>
                        disabled="disabled" <#t>
        </#if>
        <#lt> <#include "/${parameters.templateDir}/simple/scripting-events.ftl" /> <#rt/>
        <#lt> <#include "/${parameters.templateDir}/simple/common-attributes.ftl" /> <#rt/>
        <#lt> <#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /> <#rt/>
            ><#t>
    <#-- 显示图标 -->
        <#if (parameters.mxevent??)>
            <#if ((parameters.mxevent?lower_case)=='add')>
                <span class="glyphicon glyphicon-plus"></span>
            <#elseif ((parameters.mxevent?lower_case)=='del')>
                <span class="glyphicon glyphicon-trash"></span>
            <#elseif ((parameters.mxevent?lower_case)=='save')>
                <span class="glyphicon glyphicon-floppy-disk"></span>
            <#elseif ((parameters.mxevent?lower_case)=='search')>
                <span class="glyphicon glyphicon-search"></span>
            <#elseif ((parameters.mxevent?lower_case)=='close')>
                <span class="glyphicon glyphicon-off"></span>
            </#if>
        </#if>
    </#if>
</#if> 
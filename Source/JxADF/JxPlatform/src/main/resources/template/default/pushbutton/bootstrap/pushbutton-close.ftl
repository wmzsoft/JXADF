<#--
/**
$id:pushbutton-close$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if ((parameters.ignorepermission!false==true) ||  (parameters.permission!false==true))>
    <#if (parameters.visible!false==true)>
        <#if ((parameters.menutype!'unknow')=='LIA') >
        ${parameters.label!'按钮'}</a></li><#t>
        <#elseif ((parameters.menutype!'unknow')=='INPUT') >
        <#else>
        ${parameters.label!'按钮'}</button><#t>
        </#if>
    </#if>
</#if>

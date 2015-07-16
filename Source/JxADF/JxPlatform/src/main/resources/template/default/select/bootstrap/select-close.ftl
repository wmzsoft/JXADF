<#--
/**
$id:select-close$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if (parameters.mystyle??) && (parameters.mystyle=="COL")>
<#--  如果是col类型的，啥也不做-->
<#else>
    <#assign selectid = parameters.id>
    <#assign params = parameters>
    <#include '../select-script.ftl'>
</#if>
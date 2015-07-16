<#--
/**
$id:table-close$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if ((parameters.render!'x')?lower_case=='json')>
    <#include "json/table-close.ftl">
<#else>
    <#include "table-close-html.ftl">
</#if>
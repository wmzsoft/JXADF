<#--
/**
$id:table$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if ((parameters.render!'x')?lower_case=='json')>
    <#include "json/table.ftl">
<#else>
    <#include "table-html.ftl">
</#if>
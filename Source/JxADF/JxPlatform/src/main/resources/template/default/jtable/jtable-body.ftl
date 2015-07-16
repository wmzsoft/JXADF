<#--
/**
$id:jtable$
$author:wmzsoft@gmail.com
#date:2014.11
**/
-->
<#t/>
<#if (parameters.mapData??) >
    <#include "jtable-body-map.ftl">
<#else>
    <#include "jtable-body-json.ftl">
</#if>
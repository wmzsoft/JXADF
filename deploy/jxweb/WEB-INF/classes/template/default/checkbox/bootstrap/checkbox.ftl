<#--
/**
$id:checkbox$
$author:sl
#date:2013.08.22
**/
-->
<div class="container-fluid">
    <div class="form-group row">
    <#if (parameters.dataattribute??)>
        <#if ((parameters.inputmode!'edit')=='QUERY') || ((parameters.inputmode!'edit')=='QUERYIMMEDIATELY')>
            <#include "checkbox-query.ftl"/>
        <#else>
            <#include "checkbox-input.ftl"/>
        </#if>
    </#if>
</div>
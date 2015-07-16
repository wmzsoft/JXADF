<#if ((parameters.render!'x')?lower_case=='json')>
<div>To do json/table.ftl</div>
<#else>
    <#include "table-html.ftl">
</#if>
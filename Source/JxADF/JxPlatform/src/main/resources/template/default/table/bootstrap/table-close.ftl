<#if ((parameters.render!'x')?lower_case=='json')>
	<#include "../json/table-close.ftl">
<#else>
    <#include "table-html.ftl">
</#if>
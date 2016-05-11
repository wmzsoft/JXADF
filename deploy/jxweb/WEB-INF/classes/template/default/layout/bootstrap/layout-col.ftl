<#assign cssClass="ui-layout-col">
<#if (parameters.flex??)>
    <#assign cssClass="ui-layout-col ui-layout-col-flex-${parameters.flex}">
</#if>
<div class="${cssClass}">

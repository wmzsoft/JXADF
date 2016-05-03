<#assign cssClass="ui-layout-container">
<#if (parameters.cssClass??)>
    <#assign cssClass="${cssClass} ${parameters.cssClass}">
</#if>
<div id="${parameters.id}" <#t>
<#if (parameters.displayMode??)>
     displayMode="${parameters.displayMode}" <#t>
    <#assign cssClass="${cssClass} ${parameters.displayMode}">
</#if>
<#if (parameters.width??)>
    <#if (parameters.width != "")>
     width="${parameters.width}" <#t>
    </#if>
</#if>
<#if (parameters.height??)>
    <#if (parameters.height != "")>
     height="${parameters.height}" <#t>
    </#if>
</#if>
<#if (parameters.minWidth??)>
    <#if (parameters.minWidth != "")>
     minwidth="${parameters.minWidth}" <#t>
    </#if>
</#if>
<#if (parameters.minHeight??)>
    <#if (parameters.minHeight != "")>
     minheight="${parameters.minHeight}" <#t>
    </#if>
</#if>
<#if (parameters.maxWidth??)>
    <#if (parameters.maxWidth != "")>
     maxwidth="${parameters.maxWidth}" <#t>
    </#if>
</#if>
<#if (parameters.maxHeight??)>
    <#if (parameters.maxHeight != "")>
     maxheight="${parameters.maxHeight}" <#t>
    </#if>
</#if>
     class="${cssClass}" <#t>
        ><#t>
<#if (parameters.displayMode == "panel")>
    <script>
        $(function(){
            createLayout("${parameters.id}");
        });
    </script>
</#if>

<#assign cssClass="ui-layout-pane">
<#if (parameters.cssClass??)>
    <#assign cssClass="ui-layout-pane ${parameters.cssClass}">
</#if>
<div class="ui-layout-${parameters.region} ${cssClass} ${parameters.scrollable}" <#t>
     region="${parameters.region}" <#t>
     closable="${parameters.closable}" <#t>
     resizable="${parameters.resizable}" <#t>
     resizable="${parameters.status}" <#t>
     space="${parameters.space}" <#t>
<#if (parameters.panelSize??)>
    <#if (parameters.panelSize != "" )>
     size="${parameters.panelSize}" <#t>
    </#if>
</#if>
<#if (parameters.minSize??)>
    <#if (parameters.minSize != "")>
     minsize="${parameters.minSize}" <#t>
    </#if>
</#if>
<#if (parameters.maxSize??)>
    <#if (parameters.maxSize != "")>
     maxsize="${parameters.maxSize}" <#t>
    </#if>
</#if>
<#if (parameters.space??)>
    <#if (parameters.space != "")>
     space="${parameters.space}" <#t>
    </#if>
</#if>
        ><#t>

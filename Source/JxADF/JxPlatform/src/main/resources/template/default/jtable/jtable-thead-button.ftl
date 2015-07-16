<#--
/**
$table-page-head-button$
$author:wmzsoft@gmail.com
#date:2013.10
**/
-->
<#if parameters.buttons?? >
    <#list parameters.buttons as tb>
            <#lt><span class="toolbar_btn" id="${tb.parameters.id}" <#rt>
            <#if (tb.parameters.mxevent??) >
                <#lt> mxevent="${tb.parameters.mxevent}" <#rt>
                <#lt> onclick="${tb.parameters.mxevent}(this, event)" <#rt>
            </#if>
            <#assign dynamicAttrs =tb.parameters.dynamicAttributes>
            <#include "../common/jx-dynamic-attributes.ftl">
             ><#t>
            <#if (tb.parameters.icon??)>
                <img src="${base}/skin/${skinName}/images/${tb.parameters.icon}" class="appbar-menu-toolbar-icon"/><#t>
            </#if>
            ${tb.parameters.label!''}<#t>
            </span><#t>
    </#list>
</#if>
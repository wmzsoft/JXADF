<#--
/**
$table-page-head-button$
$author:wmzsoft@gmail.com
#date:2013.10
**/
-->
<!--table-thead-title-button.ftl begin-->
<#if parameters.tableButtons?? >
    <#list parameters.tableButtons as tb>
        <#if (tb.parameters.permission!false==true)>
            <#lt><span class="toolbar_btn" id="${tb.parameters.id}" <#rt>
            <#if (tb.parameters.mxevent??) >
                <#lt>   mxevent="${tb.parameters.mxevent}" <#rt>
            </#if>
            <#if tb.parameters.lookup??>
                <#lt> lookup="${tb.parameters.lookup}" lookupType="table"<#rt>
                       onclick="lookupAction('${tb.parameters.lookup}','${parameters.id}',${tb.parameters.lookupWidth!0}, ${tb.parameters.lookupHeight!0})"
            <#else>
                       onclick="${tb.parameters.mxevent}(this, event)" tag="tablebutton"
            </#if>
            <#if (tb.parameters.params??)>
                <#lt> params="${tb.parameters.params}" <#rt>
            </#if>
            <#if (tb.parameters.url??)>
                <#lt> url="${tb.parameters.url}" <#rt>
            </#if>
            <#assign dynamicAttrs =tb.parameters.dynamicAttributes>
            <#include "../../common/jx-dynamic-attributes.ftl">
                ><#t>
            <#if (tb.parameters.icon??)>
                <img src="${base}/skin/${skinName}/images/${tb.parameters.icon}.png" class="appbar-menu-toolbar-icon"/><#t>
            </#if>
        ${tb.parameters.label!''}<#t>
            </span><#t>
        </#if>
    </#list>
</#if>
<!--table-thead-title-button.ftl end-->
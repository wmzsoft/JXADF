<#include "../../common/ftl-head.ftl"/>
<div id="${parameters.id?html}"<#rt>
<#if parameters.url??>
     url="${parameters.url?html}"<#rt>
<#else>
     url="${parameters.url!'app.action'}"<#rt>
</#if>
     type="${parameters.type!'list-table'}"<#rt>
     jboname="${parameters.jboname!''}" relationship="${parameters.relationship!''}"<#rt>
<#if parameters.cssClass??>
     class="${parameters.cssClass?html}"<#rt/>
</#if>
<#if parameters.cssStyle??>
     style="${parameters.cssStyle?html}"<#rt/>
</#if>
<#if parameters.inputmode??>
     inputmode="${parameters.inputmode!''}"<#rt/>
</#if>
<#if parameters.rootparent??>
     rootparent="${parameters.rootparent!''}"<#rt/>
</#if>
<#if parameters.expandtype??>
     expandtype="${parameters.expandtype}"<#rt/>
</#if>
<#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
        ><#t/>
<#if (parameters.columns??)><#t>
    <#if ((parameters.tabletype!'')=='IMGLIST')>
        <div class="text-center alert alert-danger">todo imglist表格</div>
        <#include "imglist/table.ftl"><#t>
    <#else>
        <#include "list/table.ftl"><#t>
    </#if>
<#else>
    <div class="text-center alert alert-danger">${parameters.tagbundle['table-tbody.table_config_error']}</div>
</#if>
</div><#t>

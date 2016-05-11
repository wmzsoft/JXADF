<#--
/**
$id:select$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#t/>
<#if ((selected??) && (params.selected??) )>
     <#assign wc>
         <#if (params.wherecause??)>
             <#lt>${params.wherecause} and <#rt>
         </#if>
         <#lt> ${params.displayvalue}=?<#rt>
     </#assign>
     <#assign p=[selected]>
     <#assign colDataValue=(jbo.findDataAttributeValue(params.jboname!,wc!,p,params.displayname))!selected>
    <#if (col.parameters.mxevent??)>
        <#include "../../list/table-tbody-col-mxevent.ftl">
    <#else>
        ${colDataValue!selected}<#t>
    </#if>
</#if>  
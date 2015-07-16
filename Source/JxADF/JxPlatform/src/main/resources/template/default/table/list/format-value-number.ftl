<#--
/**
格式化数字信息
$id:table-tbody-col-number$
$author:wmzsoft@gmail.com
#date:2014.11
**/
-->
<#if ((jbo.isNumeric(col.dataattribute)!false)==true) >
    <#if colDataValue !=''>
        <#if col.parameters.format??>
            <#assign format=col.parameters.format!>
            <#assign colDataValue=jbo.getString(col.dataattribute,1,format)!>            
        </#if>
    </#if>
 </#if>

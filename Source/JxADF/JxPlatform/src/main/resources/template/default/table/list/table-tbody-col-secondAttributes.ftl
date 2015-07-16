<#--
/**
输出 tablecol中定义的secondAttributes属性
$id:table-tbody-col-secondAttributes$
$author:wmzsoft@gmail.com
#date:2015.05
**/
-->
<#if (col.parameters.secondAttributes??)>
     <#lt><span style="display:none" <#rt>
     <#list ((col.parameters.secondAttributes)?split(",")) as key>
        <#if ( (key?index_of("="))>=0)>
            <#lt> ${key} <#rt>
        <#elseif (key??)>
            <#lt> ${key}="${jbo.getString(key)!}" <#rt>
        </#if>
     </#list>
     ></span><#t>
</#if>
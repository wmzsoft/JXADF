<#--
/**
将控件的动态属性原样添加到生成的HTML代码中。
其它页面需要调用示例：
            <#assign dynamicAttrs =tb.parameters.dynamicAttributes> 
            <#include "../common/jx-dynamic-attributes.ftl">
如果不使用此页面，则请使用：
    <#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /> 
$id:flt-head$
$author:wmzsoft@gmail.com
#date:2014.11
**/
--><#t/>
<#if (dynamicAttrs?? && dynamicAttrs?size > 0)>
    <#assign aKeys = dynamicAttrs.keySet()>
    <#list aKeys as aKey>
        <#assign keyValue = dynamicAttrs[aKey]/>
        <#if keyValue?is_string>
            <#assign value = struts.translateVariables(keyValue)!keyValue/>
        <#else>
            <#assign value = keyValue?string/>
        </#if>
        <#lt> ${aKey}="${value?html}" <#rt/>
    </#list>
</#if> 
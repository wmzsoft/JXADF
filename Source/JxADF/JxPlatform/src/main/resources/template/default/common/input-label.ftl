<#--
/**
Freemarker 输入框的Label
其它页面需要引入<#include "common/input-label.ftl"/>
$id:input-label$
$author:wmzsoft@gmail.com
#date:2014.01
**/
-->
<#if (parameters.label??)>
    ${parameters.label}<#t/>
<#elseif (parameters.dynamicAttributes.label??)>
    ${parameters.dynamicAttributes.label}<#t/>
<#elseif (parameters.columnAttribute??)>
    ${parameters.columnAttribute.title!(parameters.tagbundle['label.null'])}<#t>
<#elseif (parameters.jxattribute??)>
    ${parameters.jxattribute.title!(parameters.tagbundle['label.null'])}<#t>
<#else>
    ${parameters.dataattribute!(parameters.tagbundle['label.null'])}<#t>
</#if>

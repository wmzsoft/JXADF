<#--
/**
格式化 display属性的内容，转义
$id:format-value-display$
$author:wmzsoft@gmail.com
#date:2014.11
**/
-->
 <#if col.parameters.dataDisplay??>
     <#assign displayValue=col.parameters.dataDisplay?eval />
     <#list displayValue?keys as key>
         <#if key==colDataValue>
             <#assign dv = (displayValue[key]?lower_case)>
             <#if (dv?ends_with('.gif')) || (dv?ends_with('.png')) || (dv?ends_with('.jpg')) >
                 <#assign dishtml=true> <#--这里会造成一个dishtml永远为true的bug-->
                 <#assign colDataValue="<img class='iconbutton' src='" + (baseSkin) + "/images/" + (displayValue[key]) + "'>" >
             <#else>
                 <#assign colDataValue=displayValue[key] >
             </#if>
             <#break>
         </#if>
     </#list>
 </#if>
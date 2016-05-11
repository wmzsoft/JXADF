<#--
/**
$id:jtable$
$author:wmzsoft@gmail.com
#date:2014.11
**/
-->
<#t/>
<#-- 显示表格列头标题 -->
<#if (parameters.cols??) && (parameters.mapData??) >
  <#list parameters.mapData as data>
    <#if (data_index == 0)>
        <#-- 这是头部数据 -->
    </#else>
      <#list parameters.cols as col>
        <#if ((col.parameters.visible!true)==true)>
          <#-- 是否显示<TD>标记 Begin.13 -->
          <#if ((col.parameters.visibleHead!true)==true)>  
             <td <#rt> <#-- Begin TD.13 -->
                <#assign dynamicAttrs =col.parameters.dynamicAttributes>
                <#include "../common/jx-dynamic-attributes.ftl">
             ><#t> <#-- End TD.13 -->
          </#if>
          <#-- 是否显示<TD>标记 End.13 -->
          
          <#-- 显示内容 Begin--><#t>
          ${col.parameters.startHtml!''}<#t>
          <#if (col.parameters.attribute??) >
            ${data[col.parameters.attribute]!''}<#t>
          </#if>
          ${col.parameters.html!''}<#t>
          <#-- 显示内容 End--><#t>
          
          <#-- 是否显示</TD>标记 Begin -->
          <#if ((col.parameters.visibleHead!true)==true)>
            </td><#t>
          <#else>    
             <#if col_has_next>
                <#if ((parameters.cols[col_index+1].parameters.visibleHead!true)==true)>
                    <#t></td><#t>
                </#if>
             <#else>
                <#t></td><#t>
             </#if>
          </#if>
          <#-- 是否显示</TD>标记 END -->
        </#if>
      </#list>
  </#list> 
</#if>
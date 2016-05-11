<#--
/**
$id:jtable$
$author:wmzsoft@gmail.com
#date:2014.11
**/
-->
<#t/>
<#-- 整个表格的标题以及按钮 -->
<#if parameters.title??>
  <tr> 
    <td colspan="100" width="100%" align="left"><#t>
        <span class='table_list_thead_table_title'>${parameters.title!''}</span>
        <span class='table_list_thead_table_button1'  nowrap='true'>
            <#include "jtable-thead-button.ftl"><#t>
        </span>
        <span class="table_list_thead_clear"></span>
    </td><#t>
  </tr>
</#if>
<#-- 显示表格列头标题 -->
<#if parameters.cols??>
  <#list parameters.cols as col>
    <#if ((col.parameters.visible!true)==true)>
      <#if ((col.parameters.visibleHead!true)==true)>
         <th <#rt> <#-- Begin TH.26 ~29 -->
            <#assign dynamicAttrs =col.parameters.dynamicAttributes>
            <#include "../common/jx-dynamic-attributes.ftl">
         ><#t> <#-- End TH.26 ~29 -->
         ${col.parameters.label!''}<#t>
         <#if col_has_next>
            <#if ((parameters.cols[col_index+1].parameters.visibleHead!true)==true)>
                <#t></th><#t>
            </#if>
         <#else>
            <#t></th><#t>
         </#if>
      </#if>
    </#if>
  </#list>
</#if>
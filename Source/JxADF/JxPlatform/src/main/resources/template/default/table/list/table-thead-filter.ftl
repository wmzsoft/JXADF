<#--
/**
$id:table-close$
$author:smellok@126.com
#date:2014.05
**/
-->
<#-- 整个表格的列头过滤器信息 -->
<#if ((parameters.visibleHead!true)==true)>

  <tr><#t>
  <#if (parameters.expandtype??)>
    <th></th><#t>
  </#if>

  <#if (parameters.selectmode??)>
    <th></th><#t>
  </#if>

  <#list parameters.columns as col>
    <#if ((col.parameters.visible!true)==true)>
      <#if ((col.parameters.visibleHead!true)==true)>
        <th <#rt>
            <#if (col.parameters.width??)>
                <#lt> width="${col.parameters.width}" nowrap="nowrap" <#rt>
            </#if>
            <#if (col.parameters.height??)>
                <#lt> height="${col.parameters.height}" <#rt>
            </#if>
            <#if (col.dataattribute??) >
                  <#lt> dataattribute="${col.dataattribute?upper_case}" sortname="${col.parameters.sortname!''}" tid="${parameters.id}" <#rt>
            </#if>
        ><#t>
        <#--快速搜索框 begin-->
		<#if (col.dataattribute?? && col.dataattribute?index_of(".") < 0) >
					<#assign attribute=parameters.jboset.getJxAttribute(col.dataattribute!'')>
					<#assign maxtype=attribute.maxType!'un'>
					<#assign dateid = "dateid_" + col_index>
					
					<input type="text" onkeydown="tableFilterQuickSearch(this, event)"
					<#if (maxtype?upper_case=='DATE')>
						cause = " = to_date(?, 'yyyy-mm-dd')" datatype="date" 
					<#elseif (maxtype?upper_case=='DATETIME')>
						cause = " = to_date(?,'yyyy-mm-dd hh:mm:ss')" datatype="date" 
					<#else>
						cause = " like ?" onchange = "inputOnChange(this, event)"
					</#if>
					
					<#--遍历条件,赋值-->
					<#assign causeParams = parameters.jboset.queryInfo.params>
					<#assign keys = causeParams?keys>
					<#list keys as key>
						<#if (key?index_of(col.dataattribute?upper_case) >= 0)>
							value = "${causeParams[key]}" 
						</#if>
					</#list>
					
					<#lt> dataattribute="${col.dataattribute}" changed="0" fragmentid="${parameters.id!'listtable'}" inputmode="QUERYIMMEDIATELY" id="${dateid}" onblur="inputQueryOnBlur(this, event)" /> <#rt>
					<#include "../../common/btnDate.ftl">
				</#if> 
			<#--快速搜索框 end -->    
        </th><#t>
      </#if>
    </#if>
  </#list>
  </tr><#t>
</#if>

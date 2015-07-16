<#--
/**
$id:table-close$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#include "../common/ftl-head.ftl"/>
<#if (parameters.columns??)><#t>
    <#if ((parameters.tabletype!'')=='IMGLIST')>
        <#include "imglist/table.ftl"><#t>
    <#else>
        <#include "list/table.ftl"><#t>
    </#if>
<#else>
    <tr><td>表格配置有误，请与管理员联系</td></tr>
</#if>
</table><#t>
<div class="bottom">
	<#if ((parameters.tabletype!'')=='IMGLIST')>
    	<#include "imglist/table-tfoot-page.ftl"><#t>
    <#else>
        <#include "list/table-tfoot-page.ftl"><#t>
    </#if>

	<div class="table_list_foot_tip">
	<#if parameters.bottomtipvalue??>
    	<span style="color:${parameters.bottomtipcolor!'red'}">${parameters.bottomtipvalue}</span>
    </#if>
    </div>
</div>
<!--table.ftl end-->
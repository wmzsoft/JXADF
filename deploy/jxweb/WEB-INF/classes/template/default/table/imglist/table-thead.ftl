<#--
/**
$id:table-close$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if parameters.title??>
    <tr> <#-- 整个表格的标题以及按钮 -->
        <td colspan="${(parameters.imgcols!1)+1}" width="100%" align="left">
		      <#include "table-thead-title.ftl">
		</td>
	</tr>
</#if>


<#--
/**
$id:attachment$
$author:wmzsoft@gmail.com
#date:2013.12
**/
-->
<#include "../common/ftl-head.ftl"/>
<#if (!parameters.jbo.toBeAdd)>
	<#if parameters.display?? && parameters.display == "table">
		<#include "attachment-table.ftl" />
	<#else>
		<#include "attachment-icon.ftl" />
	</#if>
<#else>
    <td colspan="${parameters.colspan!'1'}">
	    <span class="noattachment_holder" />
    </td>
	<script type="text/javascript">
		$(function(){
			$(".noattachment_holder").closest("tr").hide();
		});
	</script>
</#if>
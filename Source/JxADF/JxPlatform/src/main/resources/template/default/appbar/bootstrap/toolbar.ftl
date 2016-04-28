<#--
/** 应用程序菜单之下拉菜单
$id:appbar-appmenu$
$author:wmzsoft@gmail.com
#date:2014.09
**/
-->
<#if (parameters.mobileMenu > 0) >
	<#if (parameters.mobileToolbar??) >
		<#assign msize=(parameters.mobileToolbar?size)>
	    <#if (msize > 0) >
	    	<div class="btn-group btn-group-justified" role="group" aria-label="...">
	    	<#assign btnclass="btn btn-default">
	        <#list parameters.mobileToolbar as toolbar>
	        	<#assign button = (toolbar.data)>
	            <#if ((msize-toolbar_index)<=startidx) >
	            	<div class="btn-group" role="group">
					<#include "button.ftl">
					</div><#t>
	            </#if>
	        </#list>
			</div>
	    </#if>
	</#if>
</#if>
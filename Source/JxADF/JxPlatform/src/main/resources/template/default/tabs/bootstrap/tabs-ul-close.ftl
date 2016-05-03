<#--
/**
$id:tab$
$author:wmzsoft@gmail.com
#date:2014.12
**/
-->
<#if (parameters.tabli??)>
	<#assign tsize=(parameters.tabli?size)>
	<#list parameters.tabli as tabli>
		<#if (tabli_index == 0 )>
			<li  class="active">
		<#elseif ((tabli_index == 2) && (tsize>3) )>
			<li class="dropdown">
				<a href="#" id="${parameters.tabsid}_d" class="dropdown-toggle"  data-toggle="dropdown">
					${parameters.tagbundle['more']!'...'}<b class="caret"></b>
				</a>
			<ul class="dropdown-menu" role="menu" aria-labelledby="${parameters.tabsid}_d">
			<li>
		<#else>
			<li>
		</#if>
		<#include "a.ftl"></li>
	</#list>
	<#if ((tsize) > 3) >
			</ul>
		</li>
	</#if>
</#if>
</ul>
<div id="${parameters.tabsid}" class="tab-content"><#t>    


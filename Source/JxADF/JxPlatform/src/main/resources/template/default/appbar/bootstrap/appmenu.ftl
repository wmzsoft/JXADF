<#--
/** 应用程序菜单之下拉菜单
$id:appbar-appmenu$
$author:wmzsoft@gmail.com
#date:2014.09
**/
-->
<#if (parameters.mobileMenu > startidx) >
	<div class="input-group-btn">
    	<div class="btn-group" role="group">
	        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
	        	<span class="glyphicon glyphicon-menu-hamburger"></span>
	        </button>
          	<ul class="dropdown-menu">
			<#if (parameters.mobileToolbar??) >
				<#assign msize=(parameters.mobileToolbar?size)>
			    <#if (msize > 0) >
			        <#list parameters.mobileToolbar as toolbar>
			        	<#assign button = (toolbar.data)>
			            <#if ((msize-toolbar_index)>startidx) >
			            	<li><#include "button.ftl"></li><#t>
			            </#if>
			        </#list>
			    </#if>
			</#if>
			<#if (parameters.mobileList??) >
				<#if ((parameters.mobileList?size) > 0) >
					<li role="separator" class="divider"></li>
					<#list parameters.mobileList as menu>
						<#assign submenus=menu.getJboSet().getMenus(menu.data['APP'],menu.data['APPTYPE'],'LIST',menu.data['MENU']) >
						<#if (submenus??)>
							<#if (submenus?size>0)>
								<#list submenus as subm>
									<#assign button = (subm.data)>
									<li><#include "button.ftl"></li><#t>
								</#list>
								<li role="separator" class="divider"></li>
							</#if>
						<#else>
							<#assign button = (menu.data)>
							<li><#include "button.ftl"></li><#t>
						</#if>
					</#list>
				</#if>
			</#if>
			</ul>
		</div>
	</div>
</#if>


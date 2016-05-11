<#--
/** 应用程序菜单之下拉菜单
$id:appbar-appmenu$
$author:wmzsoft@gmail.com
#date:2014.09
**/
-->
<#if (button??)>
	<span class="${btnclass!}"<#t>
	   onclick="${toolbar.data['MENU']?lower_case}(this,event)" <#t>
	   mxevent="${toolbar.data['MENU']?lower_case}" ${button['EXTENDS']!''} ><#t>
	    <#if (button['IMAGE']??)>
	        <img src="${base}/skin/${skinName}/images/${button['IMAGE']}" <#t>
	             class="appbar-menu-toolbar-icon"/> <#t>
	    </#if>
	    <#assign buttonValue = button['DESCRIPTION']>
	    <#if lang??>
	        <#assign buttonValue = lang["maxmenu.${parameters.appNameType!''}.${button['MENU']}"]>
	        <#if (buttonValue == "")>
	            <#assign buttonValue = lang["maxmenu.${button['MENU']}"]>
	            <#if (buttonValue == "")>
	                <#assign buttonValue = lang["maxmenu.${button['DESCRIPTION']}"]>
	                <#if (buttonValue == "")>
	                    <#assign buttonValue = button['DESCRIPTION']>
	                </#if>
	            </#if>
	        </#if>
	    </#if>
	${buttonValue!'　'}</span><#t>
</#if>
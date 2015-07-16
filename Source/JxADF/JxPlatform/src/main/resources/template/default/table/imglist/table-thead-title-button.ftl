<#--
/**
$table-page-head-button$
$author:wmzsoft@gmail.com
#date:2013.10
**/
-->
<#if parameters.tableButtons?? >
	<#list parameters.tableButtons as tb>
		<#if (tb.parameters.mxevent??)>
			<#if ((tb.parameters.mxtype!'b')=='ICON') || (!(tb.parameters.label??))>
				<#t><img src="${baseSkin}/images/${tb.parameters.mxevent}.png" class="iconbutton" <#rt>
				<#lt> title="${tb.parameters.label!''}" mxevent="${tb.parameters.mxevent}" <#rt>
				<#lt> id="${parameters.id!'x'}_${tb.parameters.mxevent}" <#rt>
				<#lt> onClick="${tb.parameters.mxevent}(this,event)" fragmentid="${parameters.id}"<#rt>
				<#lt> params="${tb.parameters.params!''}" <#rt>
				/><#t>
			<#else>
				<#t><input type="button" id="${tb.parameters.id!'x'}_${tb.parameters.mxevent}" mxevent="${tb.parameters.mxevent}" <#rt>
				<#lt> onClick="${tb.parameters.mxevent}(this,event)" <#rt>
				<#lt> value="${tb.parameters.label!'按钮'}" class="btn_href" fragmentid="${parameters.id}"
				<#lt> params="${tb.parameters.params!''}" <#rt>
				/><#t>
			</#if>
		</#if>
	</#list>
</#if>
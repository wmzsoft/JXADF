<#--
/**
$id:textbox$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#-- 第一个查询输入框 BEGIN -->
<input type="text" <#rt>
 	<#lt> dataattribute="${parameters.dataattribute!''}" <#rt>
	<#lt> id="${parameters.id!'x'}" <#rt/>
	<#lt> onBlur="inputQueryOnBlur(this)" onChange="inputOnChange(this)"<#rt/>
	<#lt> onKeyup="inputOnChange(this)" changed="0" <#rt/>
	<#lt> value="${parameters.queryValue!''}"<#rt/>
	<#lt> inputmode="${parameters.inputmode!'edit'}"<#rt/>
<#if !(parameters.dynamicAttributes.size??) && (parameters.cols??)>
	<#lt> size = "${parameters.cols}" <#rt/>
</#if>
<#if !(parameters.dynamicAttributes.maxlength??) && (parameters.columnAttribute??)>
	<#lt> maxlength="${(parameters.columnAttribute.length!20)?c}" <#rt>
</#if>
<#if (parameters.cause??)>
    <#lt> cause="${parameters.cause}" <#rt>
</#if>
<#if (parameters.columnAttribute??) >
    <#if ((parameters.columnAttribute.numeric!false)==true)>
        <#assign icss='form_td_number'>
    <#elseif ((parameters.columnAttribute.dateType!false)==true)>
        <#assign icss='form_td_date'>
    </#if>
</#if>
<#if fromMultipart??>
    <#lt> class="form_td_multipart_first" <#rt/>
<#else>
    <#lt> class="${icss!'form_query'}" <#rt>
</#if> 
<#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
><#t/>
<#if (parameters.columnAttribute??)>
	<#assign maxtype=parameters.columnAttribute.maxType!'un'>
	<#assign dateid=parameters.id>
</#if>
<#include "../common/btnDate.ftl"><#t>
<#-- 第一个查询输入框 END -->

<#if (parameters.columnAttribute??)>	
	<#-- 第二个查询输入框 BEGIN -->
	<#if (parameters.columnAttribute.maxType??)>
		<#if ((parameters.columnAttribute.numOrDateTime!false)==true)>
			<#assign dateid=(parameters.id)+"_1">
			&nbsp;&nbsp;${parameters.tagbundle['textbox-query.date_to']}&nbsp;&nbsp;<#t/>
			<input type="text" class="${icss!'form_td_100'}"<#rt>
			 	<#lt> dataattribute="${parameters.dataattribute}" <#rt>
				<#lt> id="${parameters.id!'x'}_1" <#rt/>
				<#lt> onBlur="inputQueryOnBlur(this)" onChange="inputOnChange(this)"<#rt/>
				<#lt> onKeyup="inputOnChange(this)" changed="0""<#rt/>
				<#lt> value="${parameters.queryValue2!''}"<#rt/>
				<#lt> inputmode="${parameters.inputmode!'edit'}"<#rt/>
			<#if !(parameters.dynamicAttributes.size??) && (parameters.cols??)>
				<#lt> size = "${parameters.cols}" <#rt/>
			</#if>
			<#if !(parameters.dynamicAttributes.maxlength??)>
				<#lt> maxlength="${(parameters.columnAttribute.length!20)?c}" <#rt>
			</#if>			
			<#if parameters.cause2??>
				<#lt> cause="${parameters.cause2}" <#rt>
			</#if>
			<#assign dateid=(parameters.id)+"_1">
			<#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
			<#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
			<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
			><#t/>
			<#include "../common/btnDate.ftl"><#t>		
		</#if>	
	</#if>
	<#-- 第二个查询输入框 END -->
</#if>

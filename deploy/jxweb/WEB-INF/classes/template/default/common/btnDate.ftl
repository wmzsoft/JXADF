<#--
/**
$id:btnDate$
解析日期选择按钮，在引用之前需要先赋值 <#assign maxtype='DATE'> <#assign dateid=parameters.id>
DATE：选择日期
DATETIME：选择时间
TIME：选择日期和时间
其它页面引用方法：<#include "common/btnDate.ftl"><#t>
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#t/>
<#include "ftl-head.ftl"/>
<#if (maxtype??)>
	<#if (maxtype?upper_case)=='DATE'>
		<img src='${baseSkin}/images/datePicker.gif' class="iconbutton"  title="${parameters.tagbundle['datapicker.title']}" onClick='showDate(this, event)' tid="${dateid!(parameters.id!'x')}" /><#t>
	<#elseif (maxtype?upper_case)=='DATETIME'>
		<img src='${baseSkin}/images/datetime.png' class="iconbutton"  onClick='showDateTime(this,event)' tid="${dateid!(parameters.id!'x')}" /><#t>
	<#elseif (maxtype?upper_case)=='TIME'>
		<img src='${baseSkin}/images/time.png' class="iconbutton" onClick='showTime(this,event)' tid="${dateid!(parameters.id!'x')}" /><#t>
	</#if>
</#if>


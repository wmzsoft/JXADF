<#--
/**
$id:attachment$
$author:smellok
#date:2014.3.18
**/
-->

<#assign url = "${base}/app.action?app=attachment&type=list&attid=${parameters.id}">
<#if parameters.jboname??>
	<#--jboname不为空，不是主表附件-->
	<#assign url = url + "&code=${parameters.jboname}" />
<#else>
	<#--主表附件-->
	<#assign url = url + "&code=${parameters.jbo.jboSet.jboname}&fromUid=${parameters.jbo.uidValue!0}" />
</#if>
<#if parameters.needed?? && ("true" == parameters.needed)>
	<#assign url = url + "&isNeeded=true">
</#if>

<#if parameters.readonly?? && ("true" == parameters.readonly)>
	<#assign url = url + "&readonly=true">
<#else>
	<#assign url = url + "&readonly=false">
</#if>

<#if parameters.vfolder??>
	<#assign url = url + "&vfolder=${parameters.vfolder}">
</#if>
<#if parameters.label??>
	<#assign url = url + "&label=${parameters.label}">
</#if>
<#if parameters.operate??>
	<#assign url = url + "&operate=${parameters.operate}">
</#if>
<#--TODO 2.3.16不支持.now。需要升级至2.3.17-->
<#assign objectConstructor = "freemarker.template.utility.ObjectConstructor"?new()>
<#assign clock = objectConstructor("java.util.GregorianCalendar")>
<#assign mmddyy = objectConstructor("java.text.SimpleDateFormat","yyyyMMDDhhmmss")>
<#assign date = clock.getTime()>
<#assign now = mmddyy.format(date)>
<iframe id="iframeAtt_${parameters.id}" name="iframeAtt_${parameters.id}" src="${url}&r_timestamp=${now}"
		width="100%" border="0" frameborder="0"
		scrolling="yes" marginheight="0" marginwidth="0"> </iframe>

<#--
/**
$id:fullCalendar$
$author:wmzsoft@gmail.com
#date:2014.04
**/
-->
<#if (parameters.loadjs!true)>
    <link rel='stylesheet' type='text/css' href='${base}/javascript/fullcalendar/fullcalendar.min.css'/>
    <script type='text/javascript' src='${base}/javascript/lib/moment.min.js'></script>    
    <script type='text/javascript' src='${base}/javascript/fullcalendar/fullcalendar.min.js'></script>
    <script type='text/javascript' src='${base}/javascript/fullcalendar/lang-all.js'></script>
</#if>
<div id="${parameters.id!'calendar'}" class="fullCalendar" <#rt/>
 <#lt> calendarEvent="${parameters.calendarEvent!''}" <#rt>
 <#lt> <#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
 <#lt> <#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
 <#lt> <#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
 <#if (parameters.dynamicAttributes.month??)>
 <#else>
    <#lt> month="${parameters.month!'2015-01'}" <#rt>
 </#if>
 ></div><#t>
<script type='text/javascript'>
	$(document).ready(function() {
	   myFullCalendar("${parameters.id!'calendar'}");
	});
</script>

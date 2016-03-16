<#--
/**
$id:select-close$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if (parameters.mystyle!'TD')=='TD'>
</td><#t>
</#if>
<#if (parameters.mystyle??) && (parameters.mystyle=="COL")>
<#--  如果是col类型的，啥也不做-->
<#else>
    <#assign selectid = parameters.id>
    <#assign params = parameters>
    <#if ((params.readonly!false)==false) >
      <#if ((params.ajax??) || ((params.optionsCount!0) &gt; 10) || (params.partialTriggers??))>
    	<script type="text/javascript">$(function () {<#t>
        <#if (params.ajax??)>
        	select2AjaxSelectTag('${selectid}', '${params.displayvalue!""}', '${params.dataattribute!""}', '${params.displayname!""}', '${params.ajax!""}', '${params.selectedDisplay!""}', 'Input to Choose Option');<#t>
        <#elseif ((params.optionsCount!0) &gt; 10) >
        	select2CustomSelectTag('${selectid}', 'Select an option', true);
        </#if>
        <#if (params.partialTriggers??)>
        	doPartialTriggers($('#${selectid}'));<#t>
        </#if>
        });</script><#t>
      </#if>  
    </#if>
</#if>
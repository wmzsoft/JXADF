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
        <#if (params.ajax??)>
        <script type="text/javascript">
            $(function () {
                select2AjaxSelectTag('${selectid}', '${params.displayvalue!""}', '${params.dataattribute!""}', '${params.displayname!""}', '${params.ajax!""}', '${params.selectedDisplay!""}', 'Input to Choose Option');
            });
        </script>
        <#elseif ((params.optionsCount!0) &gt; 10) >
        <script type="text/javascript">
            $(function () {
                select2CustomSelectTag('${selectid}', 'Select an option', true);
            });
        </script>
        </#if>
    </#if>
</#if>
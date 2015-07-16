<#if ((params.readonly!false)==false) >
    <#if (params.ajax??)>
    <script type="text/javascript">
        $(function () {
            select2AjaxSelectTag('${selectid}', '${params.displayvalue!""}','${params.dataattribute!""}', '${params.displayname!""}', '${params.ajax!""}', '${params.selectedDisplay!""}')
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
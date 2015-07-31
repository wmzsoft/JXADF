</div>
<#if (parameters.displayMode == "panel")>
    <script>
        $(function(){
            createLayout("${parameters.id}");
        });
    </script>
</#if>
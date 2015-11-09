<#--
/**
$id:tab$
$author:wmzsoft@gmail.com
#date:2014.12
**/
-->
</div>
<script type="text/javascript"><#t>
$(function () {
    var ${parameters.id}_tabs = $("#${parameters.id}").tabs({<#t>
        create: ${parameters.tabCreateEvent!'tabCreate'},<#t>
        beforeActivate: ${parameters.beforeActivateEvent!'beforeActivate'},<#t>
    <#if (parameters.beforeLoadeEvent??)>
        beforeLoad: ${parameters.beforeLoadeEvent},<#t>
    </#if>
        activate: ${parameters.activateEvent!'tabActivate'}<#t>
    });<#t>
});
</script><#t>
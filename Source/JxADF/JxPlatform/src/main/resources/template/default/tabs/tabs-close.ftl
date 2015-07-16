<#--
/**
$id:tab$
$author:wmzsoft@gmail.com
#date:2014.12
**/
-->
</div>
<script type="text/javascript">
    $(function () {
        var ${parameters.id}_tabs = $("#${parameters.id}").tabs({
            create: tabCreate,
            beforeActivate: beforeActivate
        });
    });
</script>
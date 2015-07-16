<#--
/**
$id:highChart$
$author:wmzsoft@gmail.com
#date:2014.02
**/
-->
<div id="${parameters.id}" style='width:${parameters.width!'100%'}; height:${parameters.height!'500px'};'></div>
<script type="text/javascript">
$('#${parameters.id}').highcharts({
    <#if (parameters.chart??)>
        chart:${parameters.chart},
    </#if>
    <#if (parameters.colors??)>
        colors:${parameters.colors},
    </#if>
    <#if (parameters.title??)>
        title:${parameters.title},
    </#if>    
    <#if (parameters.subtitle??)>
        subtitle:${parameters.subtitle},
    </#if>
    <#if (parameters.xAxis??)>
        xAxis:${parameters.xAxis},
    </#if>
    <#if (parameters.yAxis??)>
        yAxis:${parameters.yAxis},
    </#if>
    <#if (parameters.tooltip??)>
        tooltip:${parameters.tooltip},
    </#if>
    <#if (parameters.plotOptions??)>
        plotOptions:${parameters.plotOptions},
    </#if>
    <#if (parameters.legend??)>
        legend:${parameters.legend},
    </#if>
    <#if (parameters.credits??)>
        credits:${parameters.credits},
    </#if>
    <#if (parameters.functions??)>
        function:${parameters.functions},
    </#if>
    <#if (parameters.exporting??)>
        exporting:${parameters.exporting},
    </#if>                                    
    <#if (parameters.labels??)>
        labels:${parameters.labels},
    </#if>                                    
    <#if (parameters.loading??)>
        loading:${parameters.loading},
    </#if>                                    
    <#if (parameters.navigation??)>
        navigation:${parameters.navigation},
    </#if>                                    
    <#if (parameters.pane??)>
        pane:${parameters.pane},
    </#if>                                    
    series:${parameters.series!'[]'} 
}); 
</script>
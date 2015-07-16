<#--
/**
$id:tab$
$author:cxm
#date:2014.03.21
**/
-->
<ul>
<#assign urlParameters = (request_query_string!'')>
<#if parameters.tabs??>
    <#list parameters.tabs as tab>
        <#assign url="#nothinghere">
        <#if tab.parameters.page??>
            <#if (request_query_string??) >
                <#if (tab.parameters.page?index_of("?") >= 0)>
                    <#assign url = tab.parameters.page + "&" + urlParameters>
                <#else>
                    <#assign url = tab.parameters.page + "?" + urlParameters>
                </#if>
            <#else>
                <#assign url = tab.parameters.page>
            </#if>
        </#if>
        <li <#t>
                <#if (tab.parameters.refreshonclick!'FALSE') == "TRUE">
                    <#lt> refreshonclick = 'true' <#rt>
                </#if>
                ><#t>
            <#lt> <a id="${tab.parameters.id!''}" href="${url}">${tab.parameters.title!'未定义title'}</a><#rt>
        </li><#t>
    </#list>
</#if>
</ul>
</div>
<script type="text/javascript">
    $(function () {
        $.ajaxSetup({cache:false});
        var ${parameters.id}_tabgroup = $("#${parameters.id}").tabs({
            beforeActivate : beforeActivate,
            beforeLoad: beforeTabLoad,
            load: tabLoaded
        });
    });
</script>
<!--tabgroup.ftl end-->
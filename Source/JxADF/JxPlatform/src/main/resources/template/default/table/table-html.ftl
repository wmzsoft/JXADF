<#--
/**
$id:table$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<!--table.ftl begin-->
<#include "../common/ftl-head.ftl"/>
<table id="${parameters.id?html}"<#rt>
<#if parameters.url??>
       url="${parameters.url?html}"<#rt>
<#else>
       url="${parameters.url!'app.action'}"<#rt>
</#if>
       type="${parameters.type!'list-table'}"<#rt>
       jboname="${parameters.jboname!''}" relationship="${parameters.relationship!''}"<#rt>
<#if parameters.cssClass??>
       class="${parameters.cssClass?html}"<#rt/>
<#else>
       class="table_list display"<#rt/>
</#if>
<#if parameters.cssStyle??>
       style="${parameters.cssStyle?html}"<#rt/>
</#if>
<#if parameters.inputmode??>
       inputmode="${parameters.inputmode!''}"<#rt/>
</#if>
<#if parameters.rootparent??>
       rootparent="${parameters.rootparent!''}"<#rt/>
</#if>
<#if parameters.expandtype??>
       expandtype="${parameters.expandtype}"<#rt/>
</#if>
<#if parameters.height??>
       sheight="${parameters.height}"<#rt/>
</#if>
<#if parameters.fixedWidth??>
       fixedWidth="${parameters.fixedWidth}"<#rt/>
</#if>
<#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
        ><#t/>
<#if ((parameters.ignoreDataTable!'false') != 'true')>
    <script type="text/javascript">
        $(function () {
            var tableId = "${parameters.id?html}";
        <#if parameters.height??>
            loadDataTable(tableId, {"height": "${parameters.height}"});
        <#else>
            loadDataTable(tableId);
        </#if>
            /*$("body").resize(function () {
                if ($.fn.DataTable.fnIsDataTable($("#" + tableId)[0])) {
                    $("#" + tableId).dataTable().fnDraw();
                }
            });*/
        });
    </script>
</#if>
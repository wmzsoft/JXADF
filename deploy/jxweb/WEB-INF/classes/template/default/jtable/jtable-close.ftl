<#--
/**
$id:jtable$
$author:wmzsoft@gmail.com
#date:2014.11
**/
-->
<#t/>
<#include "../common/ftl-head.ftl"/>
<thead>
    <#include "jtable-head.ftl">
</thead>
<tbody>
    <#include "jtable-body.ftl">
</tbody>
<tfoot>
    <#include "jtable-foot.ftl">
</tfoot>
</table>
<script type="text/javascript">
$(function () {
    <#if parameters.jsonData??>
        <#lt>var jsonData="${parameters.jsonData}";<#rt>
    </#if>
    loadJsonToTable("${parameters.id}",jsonData);     
});
</script>
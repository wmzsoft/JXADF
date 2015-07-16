<#--
/**
$id:fragment$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->

<#--<tr>
<td id="lockedTD" align="center" vAlign="top">-->
<div id="div_${parameters.id!test}" fragment="${parameters.id!test}"<#rt/>
<#lt> url="${parameters.url!'app.action'}"<#rt/>
<#lt> type="${parameters.type!''}"<#rt/>
<#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
        ><#t>
    <div class="alert text-center">
        <span class="glyphicon glyphicon-repeat rotating"></span>
    </div>
</div>
<#if (parameters.lazyload != 'TRUE')>
<script type='text/javascript'>
    $(function () {
        getTableData("div_${parameters.id!test}", null, function () {
            afterFragmentLoad('${parameters.id!test}')
        }, null, 3);
    });
</script>
</#if>
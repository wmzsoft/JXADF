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
<#lt> displaymode="${parameters.displayMode!'none'}"<#rt>
<#lt> class="fragment-mode fragment-mode-${parameters.displayMode!'none'}"<#rt>
<#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
        ><#t>

<#if (parameters.lazyload == 'TRUE')>
<#else>
    <script type='text/javascript'>
        $(function () {
            getTableData("div_${parameters.id!test}", null, function () {
                afterFragmentLoad('${parameters.id!test}')
            }, null, 3);
        });
    </script>
</#if>
</div>
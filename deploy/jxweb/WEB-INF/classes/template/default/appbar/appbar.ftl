<#--
/**
$id:appbar$
$author:wmzsoft@gmail.com
#date:2014.09
**/
-->
<!-- appbar begin -->
<#t/>
<#include "../common/ftl-head.ftl">
<table class="appbar" <#rt>
<#lt> <#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
<#lt> <#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
<#lt> <#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" />><#rt/>
    <tr>
        <td class="td_bg"></td>
        <td class="appbar-quicksearch">
        </td>
        <td class="appbar-menu">
        <#if ((parameters.hideSearch!'false') != 'true')>
            <#include "appbar-quicksearh.ftl" />
        </#if>
        <#include "appmenu.ftl" />
        </td>
        <td class="appbar-toolbar"><#include "toolbar.ftl" /></td>
        <td class="appbar-extends">
		

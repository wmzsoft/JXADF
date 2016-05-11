<#--
/**
$id:checkbox$
$author:sl
#date:2013.08.22
**/
-->
<div class="col-xs-12">
    <label>
        <input type="checkbox" id="${parameters.id!'x'}" <#rt>
        <#lt> dataattribute="${parameters.dataattribute!''}" <#rt>
        <#lt> changed="0" <#rt/>
        <#if (parameters.dataValue!'0') == (parameters.checked!'1')>
            <#lt> checked="checked" <#rt>
        </#if>
        <#lt> value="${parameters.dataValue!(parameters.notChecked!'0')}" <#rt>
        <#lt> chk="${parameters.checked!'1'}" notchk="${parameters.notChecked!'0'}" <#rt>
        <#if !(parameters.readonly!false)>
            <#lt> onBlur="inputOnBlur(this)" onClick="checkOnChange(this)" <#rt/>
            <#lt> onKeyup="checkOnChange(this)" <#rt>
        <#else>
            <#lt> disabled <#rt>
        </#if>
        <#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
        <#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
        <#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
                /> <#t>
        <#include "../../common/input-label.ftl"/>
    </label>
</div>
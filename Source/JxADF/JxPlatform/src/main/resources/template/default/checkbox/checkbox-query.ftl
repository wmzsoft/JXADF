<#--
/**
$id:checkbox$
$author:sl
#date:2013.08.22
**/
-->
<#if (parameters.queryType!"SELECT" == "CHECKBOX")>
   <input type="checkbox" id="${parameters.id!'x'}"
    <#lt> dataattribute="${parameters.dataattribute!''}" cause="${parameters.cause!''}"<#rt>
    <#lt> class="checkbox_td_query" changed="0" inputmode="${parameters.inputmode!''}" <#rt/>
    <#if (parameters.queryValue!'0') == (parameters.checked!'1')>
        <#lt> checked="checked" <#rt>
    </#if>
    <#lt> value="${parameters.queryValue!(parameters.notChecked!'0')}" <#rt>
    <#lt> chk="${parameters.checked!'1'}" notchk="${parameters.notChecked!'0'}" <#rt>
    <#if !(parameters.readonly!false)>
        <#lt> onchange="checkOnChange(this, event)" <#rt>
    <#else>
        <#lt> disabled="disabled" <#rt>
    </#if>
    <#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
    <#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
    <#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
        /><#t>
<#else>
    <select onChange="selectChange(this,event)" id="${parameters.id}"  <#rt>
        <#lt> inputmode="${parameters.inputmode}" changed="0" dataattribute="${parameters.dataattribute!}" <#rt>
        <#lt> cause="=?" class="checkbox_td_query"><#t>
        <option value="">ALL</option>
        <option value="${parameters.checked!'1'}">True</option>
        <option value="${parameters.notChecked!'0'}">False</option>
    </select>
</#if>
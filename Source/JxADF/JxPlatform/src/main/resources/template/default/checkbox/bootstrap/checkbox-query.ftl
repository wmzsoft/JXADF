<#--
/**
$id:checkbox$
$author:sl
#date:2013.08.22
**/
-->
<div class="col-xs-12">
    <label><#t>
    <#if parameters.queryType!"CHECKBOX" == "CHECKBOX">
        <input type="checkbox" id="${parameters.id!'x'}"<#t>
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
                /> <#t>
    <#else>
        <select onChange="selectChange(this,event)" id="${parameters.id}"  <#t>
            <#lt> inputmode="${parameters.inputmode}" changed="0" dataattribute="${parameters.dataattribute}" <#rt>
            <#lt> cause="=?" class="checkbox_td_query"><#t>
            <option value="">${parameters.tagbundle['select.options']!}</option>
            <option value="${parameters.checked!'1'}">${parameters.checked!'1'}</option>
            <option value="${parameters.notChecked!'0'}">${parameters.notChecked!'0'}</option>
        </select> <#t>
    </#if>
    <#include "../../common/input-label.ftl"/>
    </label>
</div>
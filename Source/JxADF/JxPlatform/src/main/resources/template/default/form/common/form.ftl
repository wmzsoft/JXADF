<#--
/**
$id:form$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<!-- form.ftl begin -->
<#t/>
<#include "../../common/ftl-head.ftl" parse=true encoding="UTF-8"/>
<#t/>
<form <#rt/>
<#if (parameters.id??)>
    <#lt> id="${parameters.id}"<#rt/>
<#else>
    <@s.if test="app != null">
        <#lt> id="<@s.property value='app'/>"<#rt/>
    </@s.if>
</#if>
<#if parameters.relationship??>
    <#lt> relationship="${parameters.relationship}"<#rt/>
</#if>
<#if (parameters.name??)>
    <#lt> name="${parameters.name}"<#rt/>
<#else>
    <@s.if test="app != null">
        <#lt> name="<@s.property value='app'/>" <#rt>
    </@s.if>
</#if>
<#lt> method="post" <#rt>
<#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
        ><#t>
<#if parameters.fromid??>
    <input type="hidden" id="fromid" value="${parameters.fromid}"/>
<#else>
    <@s.if test="fromid!=null">
        <input type="hidden" id="fromid" value="<@s.property value='fromid'/>"/>
    </@s.if>
</#if>
    <input type="hidden" id="uid" value="${parameters.uid!''}"/>
    <input type="hidden" id="fromApp" value="${parameters.fromApp!''}"/>
    <input type="hidden" id="fromAppType" value="${parameters.fromAppType!''}"/>
    <input type="hidden" id="fromJboname" value="${parameters.fromJboname!''}"/>
    <input type="hidden" id="fromUid" value="${parameters.fromUid!''}"/>
    <input type="hidden" id="jboname" value="${parameters.jboname!''}"/>
    <input type="hidden" id="instanceid" value="${parameters.instanceid!''}"/>
<#if (parameters.instancestatus??)>
    <input type="hidden" id="instancestatus" value="${parameters.instancestatus!''}"/>
</#if>

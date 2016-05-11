<#--
/**
$id:jtable$
$author:wmzsoft@gmail.com
#date:2014.11
**/
-->
<#t/>
<#include "../common/ftl-head.ftl"/>
<table id="${parameters.id?html}" <#rt>
    <#lt> jsonUrl="${parameters.jsonUrl!''}"<#rt>
 <#lt> <#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
 <#lt> <#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
 <#lt> <#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
><#t>
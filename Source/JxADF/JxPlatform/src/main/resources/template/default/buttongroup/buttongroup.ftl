<#--
/**
$id:buttongroup$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if (parameters.type??)>
<script type="text/javascript">
$(function(){
    $('#' + '${parameters.id}').dcMegaMenu({
        rowItems: '3',
        speed: 'fast',
        effect: 'fade'
    });
  });
</script>
    <div class="${parameters.type}" <#rt>
        <#lt> <#include "/${parameters.templateDir}/simple/scripting-events.ftl" /> <#rt/>
        <#lt> <#include "/${parameters.templateDir}/simple/common-attributes.ftl" /> <#rt/>
        <#lt> <#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /> <#rt/>
        ><#t>
        <ul id="${parameters.id}" class="mega-menu">
</#if>
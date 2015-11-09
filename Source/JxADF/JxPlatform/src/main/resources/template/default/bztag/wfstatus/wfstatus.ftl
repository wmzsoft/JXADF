<#--
/**
$id:depttree$
$author:wmzsoft@gmail.com
#date:2015/8
**/
-->
<#if (parameters.label??)>
    <#assign plabel=parameters.label>
<#elseif (parameters.dynamicAttributes.label??)>
    <#assign plabel=parameters.dynamicAttributes.label>
</#if>

<td class="form_td_label readonly">${plabel!}</td>
<td class="form_td_content">
  <#if (parameters.bpmengine?? && ((parameters.bpmengine?lower_case)=='jxbpm'))>
    <a href="${base}/jxbpm/wfdetail.action?appname=${parameters.app!}&amp;uid=${parameters.ownerid!}&amp;instanceid=${parameters.instanceid}" id="${parameters.id!}&amp;engine=${parameters.bpmengine!}" target="_blank" title="${parameters.statusValue!}" class="form_td_100"><#t>
    ${parameters.statusDesc!'_'}</a><#t>
  <#elseif ((parameters.bpmengine?? ) || (parameters.instanceid??)) >
    <a href="${base}/app.action?app=workflow&amp;type=workflowinfo&amp;appname=${parameters.app!}&amp;uid=${parameters.ownerid!}&amp;instanceid=${parameters.instanceid!}&amp;engine=${parameters.bpmengine!}" id="${parameters.id!}" target="_blank" title="${parameters.statusValue!}" class="form_td_100"><#t>
    ${parameters.statusDesc!'_'}</a><#t>
  <#else>
    ${parameters.statusDesc!'_'}<#t>
  </#if>
  
    

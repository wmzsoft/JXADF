<#--
/**
$id:panel$
$author:wmzsoft@gmail.com
#date:2014.10
**/
-->
<#-- 转换样式 -->
<#assign cssname="default">
<#if (parameters.type??)>
    <#if (parameters.type?lower_case=='info')>
        <#assign cssname="info">
    <#elseif (parameters.type?lower_case=='tip')>
        <#assign cssname="primary">
    <#elseif (parameters.type?lower_case=='note')>
        <#assign cssname="primary">
    <#elseif (parameters.type?lower_case=='warning')>
        <#assign cssname="warning">
    <#elseif (parameters.type?lower_case=='error')>
        <#assign cssname="danger">
    <#elseif (parameters.type?lower_case=='gray')>
        <#assign cssname="default">
    <#elseif (parameters.type?lower_case=='blue')>
        <#assign cssname="primary">
    <#elseif (parameters.type?lower_case=='green')>
        <#assign cssname="success">
    <#elseif (parameters.type?lower_case=='yellow')>
        <#assign cssname="warning">
    </#if>
</#if>
<div class="panel panel-${cssname!'default'}" id='${parameters.id}'>
  <div class="panel-heading" id='${parameters.id}_title'>${parameters.title!''}</div>
  <div class="panel-body" id='${parameters.id}_content'>
    ${parameters.content!''}


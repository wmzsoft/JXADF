<#--
/**
$id:appbar$
$author:wmzsoft@gmail.com
#date:2014.09
**/
-->
<!-- appbar begin -->
<#t/>
<#include "../../common/ftl-head.ftl">
<nav class="navbar navbar-default navbar-fixed-top" id="${parameters.id}">
  <div class="input-group">
  <#assign currentidx=0>
  <#if ((parameters.hideSearch!'false') != 'true')>
  	<#assign startidx=0>
  	<#include "appmenu.ftl" />  	
  	<#include "appbar-quicksearh.ftl" />
  <#else>
  	<#assign startidx=4>
  	<#include "appmenu.ftl" />
  	<#include "toolbar.ftl" />
  </#if>


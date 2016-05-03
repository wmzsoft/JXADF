<#--
/**
$id:textbox$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if (parameters.mystyle!'TD')=='TD'>
	<div class="container-fluid">
	    <div class="form-group row">
	        <div class="col-sm-2 col-xs-12">
	            <#include "textbox-label.ftl">
	        </div>
	        <div class="col-sm-10 col-xs-12">
	            <#include "textbox-input.ftl">
	        </div>
	    </div>
	</div>
<#else>
	<#include "textbox-input.ftl">
</#if>
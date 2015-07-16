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
<#assign isSearchShown= ("${parameters.hideSearch!'false'}" != 'true')>
<#assign menuItemsLength= 0>
<#assign toolbarItemLength=0>
<#if (parameters.menusList??) >
    <#assign menuItemsLength = parameters.menusList?size>
</#if>
<#if (parameters.menusToolbar??) >
    <#assign toolbarItemLength = parameters.menusToolbar?size>
</#if>
<#assign cssClass="navbar navbar-default navbar-fixed-top">
<#if !isSearchShown>
    <#assign cssClass="search-hidden ${cssClass}">
</#if>
<#if (menuItemsLength + toolbarItemLength) == 0>
    <#assign cssClass="button-hidden ${cssClass}">
</#if>

<nav class="${cssClass}" id="${parameters.id}">
    <div class="container-fluid">
        <div class="navbar-header">
            <div class="input-group">
                <#include "appbar-quicksearh.ftl" />
                <span class="input-group-btn">
                    <button type="button" class="btn btn-default navbar-toggle collapsed" data-toggle="collapse"
                            data-target="#${parameters.id}_dt">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                </span>
            </div>
        </div>
        <div class="collapse navbar-collapse" id="${parameters.id}_dt">
            <ul class="nav navbar-nav">
            <#include "appmenu.ftl" />
            <#include "toolbar.ftl" />
            </ul>
		

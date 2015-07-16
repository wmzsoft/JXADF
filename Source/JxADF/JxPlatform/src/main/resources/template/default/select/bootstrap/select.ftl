<#--
/**
$id:select$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if ((parameters.parentName!'x') != 'TABLECOL')>
    <div class="container-fluid">
        <div class="form-group row">
            <div class="col-sm-2 col-xs-12">
                <label  class="control-label <#rt>
                    <#if parameters.required!false>
                        required<#t>
                    </#if>
                ">${parameters.label!}</label><#t>
            </div>
            <div class="col-sm-10 col-xs-12">
                <#include "select-options.ftl">
            </div>
        </div>
    </div>
</#if> 

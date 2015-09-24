<#--
/**
$id:select$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#t/>
<#assign avisible=(params.visible!true)>
<#if (params.visible??)>
    <#if ((params.inputmode!'')=='QUERYIMMEDIATELY') && (params.jboset??)>
        <#if ((params.jboset.jbolist?size)<=1) >
            <#assign avisible=false>
        </#if>
    </#if>
</#if>
<td  <#t>
<#if params.colspan??>
    <#lt> colspan="${params.colspan?html}" align="left"<#rt/>
</#if>
<#if params.rowspan??>
    <#lt> rowspan="${params.rowspan?html}" valign="middle"<#rt/>
</#if>
        >
<#assign selectid = "${params.id!'st'}_${jbo_index}_select" >
    <select onChange="tableSelectChange(this,event);" id="${selectid}" changed="0"<#rt/>
    <#if (avisible==false) >
        <#lt> style="display:none" <#t>
    <#elseif params.width??>
        <#lt> style="width:${params.width?number + 20}px" <#rt/>
    </#if>
    <#if ((params.readonly!false)!= false)>
        <#lt> disabled="disabled" <#rt>
    </#if>
    <#if (params.multiple!false)>
        <#lt> multiple="multiple" <#rt>
    </#if>
    <#if (params.inputmode??)>
        <#lt> inputmode="${params.inputmode!''}"<#rt/>
    </#if>
    <#if (params.dataattribute??)>
        <#lt> dataattribute="${params.dataattribute!''}" <#rt/>
    </#if>
    <#if (params.jboname??)>
        <#lt> jboname="${params.jboname!''}" <#rt>
    </#if>
    <#if (params.relationship??)>
        <#lt> relationship="${params.relationship!''}" <#rt>
    </#if>
    <#if (params.partialTriggers??)>
        <#lt> partialTriggers="${params.partialTriggers!''}" <#rt>
    </#if>
    <#if (params.partialCause??)>
        <#lt> partialCause="${params.partialCause!''}" <#rt>
    </#if>
    <#if (params.wherecause??)>
        <#lt> wherecause="${params.wherecause!''}"<#rt>
    </#if>
    <#if (params.orderby??)>
        <#lt> orderby="${params.orderby!''}" <#rt>
    </#if>
    <#if (params.displayvalue??)>
        <#lt> displayvalue="${params.displayvalue!''}" <#rt>
    </#if>
    <#if (params.displayname??)>
        <#lt> displayname="${params.displayname!''}" <#rt>
    </#if>
            options="${params.options!''}" <#rt>
    <#if (params.name??)>
            name="${params.name}" <#rt>
    </#if>
    <#if (params.inputmode??)>
        <#if (params.inputmode=='QUERY') || (params.inputmode=='QUERYIMMEDIATELY')>
            <#lt> cause="${params.cause!'=?'}" <#rt/>
        <#elseif ((params.inputmode=="READONLY") || (params.readonly!false))>
            <#lt> disabled <#rt>
        <#elseif (jbo.isReadonly()) || jbo.isReadonly(params.dataattribute)>
            <#lt> disabled <#rt>
        </#if>
    <#else>
        <#if (params.readonly!false)>
            <#lt> disabled <#rt>
        <#elseif (jbo.isReadonly()) || jbo.isReadonly(params.dataattribute)>
            <#lt> disabled <#rt>
        </#if>
    </#if>
    <#if (parameters.required!false) >
        <#lt> required="required" class="validate[required]" <#rt/>
    </#if>
    <#lt> <#include "/${params.templateDir}/simple/scripting-events.ftl" /><#rt/>
    <#lt> <#include "/${params.templateDir}/simple/common-attributes.ftl" /><#rt/>
    <#lt> <#include "/${params.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
            ><#t/>
    <#assign aSelected=0>
    <#if (params.optionsList??) >
        <#list params.optionsList as item>
            <#if item.key=='null'>
                <option value="" <#rt>
            <#else>
                    <option value="${item.key}" <#rt>
            </#if>
            <#if ((selected!'')==item.key)>
                <#lt>  selected="selected"<#t>
                <#assign aSelected=1>
            </#if>
            <#t>>${item.value}</option><#rt>
        </#list>
    </#if>
    <#if (params.jboset??) && (params.displayvalue??)>
        <#list params.jboset.jbolist as jbo>
            <#if jbo.data??>
                <#assign colDataValue=jbo.getString(params.displayvalue)!''>
                <option value="${colDataValue}" <#rt>
                    <#if ((selected!'')==colDataValue)>
                        <#lt> selected="selected"<#t>
                        <#assign aSelected=1>
                    </#if>
                        ><#t>
			<#if (params.displayname??)>
                ${jbo.getString(params.displayname)}<#t>
                <#else>
                ${colDataValue}<#t>
                </#if>
                </option><#t>
            </#if>
        </#list>
    </#if>
    <#-- 支持多选和预置 -->
    <#if (aSelected==0)>
        <#assign selectedList = (selected?split(",")) >
        <#list selectedList as sl>
            <option value="${sl}" selected>${sl}</option><#t>
            <#assign aSelected=1>
        </#list>
    </#if>
    <#if ((params.inputmode!'edit')=='QUERY') ||((params.inputmode!'edit')=='QUERYIMMEDIATELY')>
    <#else>
        <#if (aSelected==0)>
            <option value="" selected>${parameters.tagbundle['select.options']}</option><#t>
        </#if>
    </#if>
    </select>
    <#if (params.ajax??)>
        <script type="text/javascript">
            $(function () {
                select2AjaxSelectTag('${selectid}', '${params.displayvalue!""}', '${params.dataattribute!""}', '${params.displayname!""}', '${params.ajax!""}', '${params.selectedDisplay!""}', 'Input to Choose Option');
            });
        </script>
    <#elseif ((params.optionsCount!0) &gt; 10) >
        <script type="text/javascript">
            $(function () {
                select2CustomSelectTag('${selectid}', 'Select an option', true);
            });
        </script>
    </#if>
</td>
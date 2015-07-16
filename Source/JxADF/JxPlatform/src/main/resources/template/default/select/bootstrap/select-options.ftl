<#--
/**
$id:select$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if (parameters.readonly!false)>  
    <#-- 显示只读信息 -->
    ${parameters.selectedDisplay!''}<#t>
<#else>
    <#assign cssClass="form-control" >
    <#-- 真正开始Select标签生成了 -->
    <select onChange="selectChange(this,event);" id="${parameters.id!'st'}" changed="0" <#rt>
    style="width:100%"<#rt/>
    <#if (parameters.inputmode??)>
        <#lt> inputmode="${parameters.inputmode}" <#rt/>
    </#if>
    <#if (parameters.dataattribute??)>
        <#lt> dataattribute="${parameters.dataattribute!''}" <#rt/>
    </#if>
    <#if (parameters.jboname??)>
        <#lt> jboname="${parameters.jboname}" <#rt>
    </#if>
    <#if (parameters.relationship??)>
        <#lt> relationship="${parameters.relationship!''}" <#rt>
    </#if>
    <#if (parameters.partialTriggers??)>
        <#lt> partialTriggers="${parameters.partialTriggers!''}" <#rt>
    </#if>
    <#if (parameters.partialCause??)>
        <#lt> partialCause="${parameters.partialCause!''}" <#rt>
    </#if>
    <#if (parameters.wherecause??)>
        <#lt> wherecause="${parameters.wherecause!''}" <#rt>
    </#if>
    <#if (parameters.orderby??)>
        <#lt> orderby="${parameters.orderby!''}" <#rt>
    </#if>
    <#if (parameters.displayvalue??)>
        <#lt> displayvalue="${parameters.displayvalue!''}" <#rt>
    </#if>
    <#if (parameters.displayname??)>
        <#lt> displayname="${parameters.displayname!''}" <#rt>
    </#if>
    <#if (parameters.name??)>
        <#lt> name="${parameters.name}" <#rt>
    </#if>
    <#if (parameters.inputmode??)>
        <#if (parameters.inputmode=='QUERY') || (parameters.inputmode=='QUERYIMMEDIATELY')>
            <#lt> cause="${parameters.cause!'=?'}" <#rt/>
        <#elseif ((parameters.inputmode=="READONLY") || (parameters.readonly!false))>
            <#lt> disabled <#rt>
        </#if>
    <#else>
        <#if (parameters.readonly!false)>
            <#lt> disabled <#rt>
        </#if>
    </#if>
    <#if (parameters.required!false) >
        <#lt> required="required" <#rt/>
        <#assign cssClass="validate[required] "+cssClass >
    </#if>
    <#lt> class="${cssClass}" <#rt/>
    <#lt> <#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
    <#lt> <#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
    <#lt> <#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
    ><#t/>
    <#if (parameters.ajax??)>
        <#-- 异步查询需要添加的Options -->
        <#if (parameters.selected??)>
            <option value="${parameters.selected}" selected><#t>
            ${parameters.selectedDisplay!''}</option><#t>
        <#else>
            <option value="" selected>${parameters.tagbundle['select.options']}</option><#t>
        </#if>        
    <#else>    
        <#-- 结束Select标签,开始Options标签 -->
        <#assign aSelected=0>
        <#if (parameters.optionsList??) >
            <#list parameters.optionsList as item>
                <#lt><option value="<#rt>
                <#if (item.key != 'null')>
                    ${item.key}<#t>
                </#if>
                "<#t>
                <#if ((parameters.selected!'')==item.key)>
                    <#lt>  selected<#t>
                    <#assign aSelected=1>
                </#if>
                <#t>>${item.value}</option><#rt>
            </#list>
        </#if>
        <#if (parameters.jboset??) && (parameters.displayvalue??)>
            <#list parameters.jboset.jbolist as jbo>
                <#if jbo.data??>
                    <#assign colDataValue=jbo.getString(parameters.displayvalue)!''>
                    <option value="${colDataValue}" <#rt>
                        <#if (parameters.selected??)>
                            <#if (parameters.selected==colDataValue)>
                                <#lt> selected<#t>
                                <#assign aSelected=1>
                            </#if>
                        </#if>
                        ><#t>
    				<#if (parameters.displayname??)>
                        ${jbo.getString(parameters.displayname)}<#t>
                    <#else>
                        ${colDataValue}<#t>
                    </#if>
                    </option><#t>
                </#if>
            </#list>
        </#if>
        <#if (aSelected==0)>
            <option value="" selected>${parameters.tagbundle['select.options']}</option><#t>
        </#if>
    </#if>        
    </select></span><#t>
</#if>
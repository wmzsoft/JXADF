<#--
/**
$id:table-close$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if ((parameters.jboset??) && (parameters.jboset.jbolist??)) >
    <#if ((parameters.jboset.jbolist?size) > 0) >
    <ul><#t>
        <#list parameters.jboset.jbolist as jbo>
            <#if jbo.data??>
                <li class="list-group-item table-list-item"><#t>
                    <#list parameters.columns as col>
                        <#if ((col.parameters.visible!true)==true)>
                            <#assign cssClass="">
                            <#if col.dataattribute??>
                                <#if  col.dataattribute == 'ASSETNUM'>
                                    <#assign cssClass= "table-list-item-title">
                                <#elseif col.dataattribute == "JXASSETOWNERID.DISPLAYNAME">
                                    <#assign cssClass= "table-list-item-title">
                                <#elseif col.dataattribute == "SPEC">
                                    <#assign cssClass= "table-list-item-title">
                                <#elseif col.dataattribute == "ADDONE">
                                    <#assign cssClass= "table-list-item-title">
                                </#if>
                            </#if>
                            <div class="row ${cssClass}"><#t>
                            <#-- 显示标签 -->
                                <div class="col-xs-6 col-sm-3">
                                    <strong><#t>
                                    ${col.parameters.label!''}<#t>
                                    </strong><#t>
                                </div>
                            <#-- 得到要显示的真实的值 Begin 95-->
                                <#if col.descdataattribute??>
                                    <#assign colDescDataValue=jbo.getString(col.descdataattribute)!''>
                                </#if>
                                <#if (col.dataattribute??)>
                                    <#if col.dataattribute == 'ROWNUM'>
                                        <#assign colDataValue="${jbo_index + 1}">
                                    <#else>
                                        <#assign colDataValue=jbo.getString(col.dataattribute)!''>
                                        <#include "../list/format-value-number.ftl">
                                        <#include "../list/format-value-display.ftl">
                                    </#if>
                                </#if>
                            <#-- 得到要显示的真实的值 End 95-->
                            <#--拆分、拼凑Request传参-->
                                <#assign requestParams>
                                    <#if (col.parameters.allUrlParams??)>
                                        <#list col.parameters.allUrlParams as item>
                                        ${item.key}=${(jbo.getURLString(item.value!'')!'')?url}<#t>
                                            <#lt><#if item_has_next>&</#if><#rt>
                                        </#list>
                                    <#else>
                                        <#lt>${jbo.getURLString(col.parameters.urlParamValue!'')!''}<#rt>
                                    </#if>
                                </#assign>
                            <#--拆分、拼凑Request传参 END-->
                            <#-- 显示值 Begin 108 -->
                                <div class="col-xs-6 col-sm-9">
                                    <#assign maxtype='un'>
                                    <#if col.dataattribute??>
                                        <#assign attribute=(jbo.getJxAttribute(col.dataattribute!''))!''>
                                        <#if attribute != ''>
                                            <#assign maxtype=attribute.maxType!'un'>
                                        </#if>
                                    </#if>
                                    <span<#t>
                                        <#if (maxtype?upper_case) == "DATETIME">
                                            <#t> class="datetime"<#t>
                                        </#if>
                                            ><#t>
                                    <#if (col.parameters.mxevent??) >
                                        <#include "table-tbody-col-mxevent.ftl">
                                    <#else>
                                    ${colDataValue!}<#t>
                                    </#if>
                                </span><#t>
                                </div>
                            <#-- 显示值 End 108 -->
                            </div>
                        </#if>
                    </#list>
                </li><#t>
            <#-- 行列数据定义 Begin 64-->
            </#if>
        </#list>
        <#lt></ul>
    <#else>
    ${parameters.tagbundle['table-tbody.norecords']!'Not found'}<#t>
    </#if>
</#if>

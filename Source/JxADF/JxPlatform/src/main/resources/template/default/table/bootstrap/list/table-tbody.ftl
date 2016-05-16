<ul class="list-group m-table-list">
<#if ((parameters.jboset??) && (parameters.jboset.jbolist??)) >
    <#if ((parameters.jboset.jbolist?size) > 0) >
        <#list parameters.jboset.jbolist as jbo>
            <#if jbo.data??>
                <li class="list-group-item" id="tr_${parameters.id}_${jbo_index}" uid='${jbo.uidValue!0}'
                    <#lt> onclick="selectTableTr(this,event)" <#rt>
                    <#if jbo.toBeAdd?? && jbo.toBeAdd>
                    	<#lt> toBeAdd="true" <#t>
                    </#if>
                    <#if jbo.toBeDel?? && jbo.toBeDel>
                    	<#lt> toBeDel="true" <#t>
                    </#if>
                <#--todo  根据条件设置字体颜色及背景颜色-->
                        ><#t>
                <#--todo 扩展列表-->
                    <#if (parameters.selectmode??)>
                        <#if (parameters.selectmode != "NONE")>
                            <#assign type="checkbox">
                            <#if (parameters.selectmode == "SINGLE")>
                                <#assign  type="radio">
                            </#if>
                            <div class="hidden">
                                <input type="${type}"
                                       name="ck_${parameters.id}"
                                       index="${jbo_index}"
                                       id="${jbo.uidName!jbo_index}_${jbo.uidValue!0}"
                                       value="${jbo.uidValue!0}"
                                       onclick="ckOneSelect(this,event,'${jbo_index}')"/><#t>
                            </div>
                            <#t>
                        </#if>
                    </#if>
                    <table>
                    <#list parameters.columns as col>
                        <#if ((col.parameters.visible!true)==true)>
                            <#if col.descdataattribute??>
                                <#assign colDescDataValue=jbo.getString(col.descdataattribute)!''>
                            </#if>
                            <#if (col.dataattribute??)>
                                <#if col.dataattribute == 'ROWNUM'>
                                    <#assign colDataValue="${jbo_index + 1}">
                                <#else>
                                    <#assign colDataValue=jbo.getString(col.dataattribute)!''>
                                    <#include "../../list/format-value-number.ftl">
                                    <#include "../../list/format-value-display.ftl">
                                </#if>
                            </#if>
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
                            <#if (col.parameters.mxevent?? || ((colDataValue!'') != '')) >
                                <tr>
                                    <td class="key">${col.parameters.label!''}</td><#t>
                                    <td class="value">
                                        <#assign maxtype='un'>
                                        <#if col.dataattribute??>
                                            <#assign attribute=(jbo.getJxAttribute(col.dataattribute!''))!''>
                                            <#if attribute != ''>
                                                <#assign maxtype=attribute.maxType!'un'>
                                            </#if>
                                        </#if>
                                        <#if (col.parameters.mxevent??) >
                                            <#include "../../list/table-tbody-col-mxevent.ftl">
                                        <#elseif (maxtype=='YORN')>
                                        	<#include "table-tbody-col-yorn.ftl">
                                        <#elseif (col.sec??)>
                                        	<#assign params = col.sec.parameters>
                            				<#assign selected = jbo.getString(col.sec.parameters.dataattribute)!''>
                                        	<#include "table-tbody-col-select.ftl">
                                        <#else>
                                            ${colDataValue!}
                                        </#if>
                                    </td>
                                </tr>
                            </#if>
                        </#if>
                    </#list>
                    </table>
                </li>
            </#if>
        </#list>
    <#else>
        <li class="list-group-item text-center">
        ${parameters.tagbundle['table-tbody.norecords']!'Not found'}<#t>
        </li>
    </#if>
</#if>
</ul>
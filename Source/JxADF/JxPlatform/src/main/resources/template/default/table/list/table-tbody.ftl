<#--
/**
$id:table-close$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<!--table-tbody.ftl begin-->
<#if ((parameters.jboset??) && (parameters.jboset.jbolist??)) >
    <#if ((parameters.jboset.jbolist?size) > 0) >
        <#list parameters.jboset.jbolist as jbo>
            <#if jbo.data??>
            <#-- 在IE兼容模式下，如果有这个label，DataTables插件无法使用 label for="${jbo.uidName!jbo_index}_${jbo.uidValue!0}" -->
            <#-- 标记 <tr> begin .14 -->
            <tr id="tr_${parameters.id}_${jbo_index}" uid='${jbo.uidValue!0}' <#t>
                <#if ((parameters.rowSelectable!'f') == "true")>
                    <#lt> onclick="selectTableTr(this,event)" <#rt>
                </#if>
                <#if (jbo.toBeAdd?? && jbo.toBeAdd)>
                    <#lt>  toBeAdd="true" <#rt>
                </#if>
                <#if (jbo.toBeDel?? && jbo.toBeDel)>
                    <#lt>  toBeDel="true" <#rt>
                </#if>
            <#-- 根据条件设置字体颜色及背景颜色 -->
                <#if ((parameters.trwhereattr??) && (parameters.trwhere??) && (parameters.trwherevalue??) ) >
                    <#if parameters.trwhere == "==">
                        <#if (jbo.data[parameters.trwhereattr]??) && jbo.data[parameters.trwhereattr] == parameters.trwherevalue>
                            <#lt>
                style="color:${parameters.trfontcolor!''};background-color:${parameters.trbgcolor!''};" <#rt>
                        </#if>
                    <#elseif (parameters.trwhere == ">")>
                        <#if ((jbo.data[parameters.trwhereattr]??) && (jbo.data[parameters.trwhereattr] > parameters.trwherevalue))>
                            <#lt>
                style="color:${parameters.trfontcolor!''};background-color:${parameters.trbgcolor!''};" <#rt>
                        </#if>
                    <#elseif (parameters.trwhere == "<")>
                        <#if ((jbo.data[parameters.trwhereattr]??) && (jbo.data[parameters.trwhereattr] < parameters.trwherevalue))>
                            <#lt>
                style="color:${parameters.trfontcolor!''};background-color:${parameters.trbgcolor!''};" <#rt>
                        </#if>
                    </#if>
                </#if>
                    ><#t/>
            <#-- 标记 <tr> end .14 -->

            <#--扩展列表显示界面-->
                <#if (parameters.expandtype??)>
                    <td align="center" class="btn_expand_tr"><#t/>
                        <span onclick="expandRow(this,'${parameters.expandtype}')" class="btn_expand"></span>
                    </td><#t>
                </#if>

                <#if (parameters.selectmode??)>
                    <#if (parameters.selectmode != "NONE")>
                        <td class="table_td_${parameters.selectmode?lower_case}"><#t>
                            <#if (parameters.selectmode == "MULTIPLE")>
                                <input type="checkbox" name="ck_${parameters.id}" index="${jbo_index}" <#rt>
                                    <#lt> id="${jbo.uidName!jbo_index}_${jbo.uidValue!0}"
                                       value="${jbo.uidValue!0}" <#rt>
                                    <#lt> onclick="ckOneSelect(this,event,'${jbo_index}')"/><#rt>
                            <#elseif (parameters.selectmode == "SINGLE")>
                                <input type="radio" name="ck_${parameters.id}" index="${jbo_index}"  <#rt>
                                    <#lt> id="${jbo.uidName!jbo_index}_${jbo.uidValue!0}"
                                       value="${jbo.uidValue!0}"  <#rt>
                                    <#lt> onclick="ckOneSelect(this,event,'${jbo_index}')"/> <#rt>
                            </#if>
                        </td><#t>
                    </#if>
                </#if>
            <#-- 行列数据定义 Begin 64-->
                <#list parameters.columns as col>
                    <#if ((col.parameters.visible!true)==true)>
                    <#--先要赋值给false，以防止页面使用了dataDisplay属性造成了永久为true的BUG-->
                        <#assign dishtml=false>
                        <#if (col.sec??)>
                        <#--将参数传到table-tbody-col-select页面-->
                            <#assign params = col.sec.parameters>
                            <#assign selected = jbo.getString(col.sec.parameters.dataattribute)!''>
                            <#include "table-tbody-col-select.ftl">
                        <#else>
                        <#-- 得到要显示的真实的值 Begin 95-->
                            <#if col.descdataattribute??>
                                <#assign colDescDataValue=jbo.getString(col.descdataattribute)!''>
                            </#if>
                            <#if (col.dataattribute??)>
                                <#if col.dataattribute == 'ROWNUM'>
                                    <#assign colDataValue="${jbo_index + 1}">
                                <#else>
                                    <#assign colDataValue=jbo.getString(col.dataattribute)!''>
                                    <#include "format-value-display.ftl">
                                    <#include "format-value-number.ftl">
                                </#if>
                            </#if>
                        <#-- 得到要显示的真实的值 End 95-->

                        <#-- 显示 <TD> Begin 75. -->
                            <#if ((col.parameters.visibleHead!true)==true)>
                            <td  <#t>
                                <#if (col.parameters.width??) >
                                    <#if col.parameters.lookup??>

                                    <#else>
                                        <#lt> width="${col.parameters.width}" <#rt>
                                    </#if>

                                </#if>
                                <#if (col.parameters.height??) >
                                    <#lt> height="${col.parameters.height}" <#rt>
                                </#if>
                            <#--
                            <#if (col.dataattribute?? && col.dataattribute != 'ROWNUM') && ((col.dataattribute??) && ( (parameters.inputmode!'')=='EDIT') && (!jbo.readonly) && (!jbo.isReadonly(col.dataattribute!'')) && ((col.dataattribute!'')?index_of('.')<0) && ((col.dataattribute!'') != jbo.getUidName()))>
                            <#else>
                                <#lt> readonly="true" <#rt>
                            </#if>
                            -->
                                <#if (col.parameters.align??) >
                                    <#lt> align="${col.parameters.align}" <#rt>
                                <#else>
                                    <#if ((jbo.isNumeric(col.dataattribute)!false)==true) >
                                        <#lt> align="${col.parameters.align!'right'}" <#rt>
                                    <#else>
                                    <#-- <#lt> align="${col.parameters.align!'left'}" <#rt> -->
                                    </#if>
                                </#if>
                                <#if (col.parameters.lookup??)>
                                    <#lt> lookup="${col.parameters.lookup!''}" <#rt>
                                </#if>
                            <#-- <#lt> title="${col.parameters.title!(colDataValue!'')}" <#rt> -->
                                <#lt> dataattribute="${col.parameters.dataattribute!''}" <#rt>
                                    ><#t>
                            </#if>
                        <#-- 显示 <TD> END 75. -->

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
                        ${col.parameters.startHtml!''}<#t>
                            <#include "table-tbody-col-secondAttributes.ftl"><#t>
                            <#if (col.parameters.mxevent??) >
                                <#include "table-tbody-col-mxevent.ftl"><#t>
                            <#elseif ((col.parameters.render!'TEXT')=='LABELS')>
                                <#include "table-tbody-col-label.ftl"><#t>
                            <#else>
                                <#include "table-tbody-col-text.ftl"><#t>
                            </#if>
                        ${col.parameters.html!''}<#t>
                        <#-- 显示值 End 108 -->

                            <#if ((col.parameters.visibleHead!true)==true)>
                                <#if col_has_next>
                                    <#if ((parameters.columns[col_index+1].parameters.visibleHead!true)==true)>
                                        <#t></td><#t>
                                    </#if>
                                </#if>
                            </#if>
                            <#if (!col_has_next)>
                                    <#t></td><#t>
                            </#if>
                        </#if>
                    </#if>
                </#list>
            <#-- 行列数据定义 Begin 64-->

                <#t/></tr>
            </#if>
        </#list>
    </#if>
</#if>

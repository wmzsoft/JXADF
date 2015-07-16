<#--
/**
$id:table-close$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<!--table-thead.ftl begin-->
<#-- 整个表格的标题以及按钮 -->
<#if parameters.title??>
<#--放在表头，只有第一行才能确定宽度-->
<#if ((parameters.ignoreLayoutFixed!'false') != 'true')>
    <tr class="fix-width-row" style="height: 0;visibility:hidden;"><#t>
        <#if (parameters.selectmode??)>
            <#if (parameters.selectmode != "NONE")>
                <td style="height: 0;border-top-width: 0;border-bottom-width: 0;" class="table_head_${parameters.selectmode?lower_case}"></td>
            </#if>
        </#if>
        <#list parameters.columns as col>
            <#if ((col.parameters.visibleHead!true) == true)>
                <td style="height: 0;border-top-width: 0;border-bottom-width: 0;" <#t>
                    <#if (col.parameters.width??)>
                        <#lt> width="${col.parameters.width}" <#rt>
                    </#if>
                        ></td><#t>
            </#if>
        </#list>
    </tr><#t>
</#if>
<#assign colscount=0>
<#list parameters.columns as col>
    <#if ((col.parameters.visibleHead!true) == true)>
        <#assign colscount = colscount+1>
    </#if>
</#list>
<#if (parameters.selectmode??)>
    <#if (parameters.selectmode?upper_case != "NONE")>
        <#assign colscount=colscount+1>
    </#if>
</#if>
<tr>
    <td colspan="${colscount}" align="left"><#t>
  		<#include "table-thead-title.ftl"><#t>
    </td><#t>
</tr>
</#if>

<#--快速过滤-->
<#if (parameters.jboset?? && parameters.filterable??)>
    <#if parameters.filterable == "true">
        <#include "table-thead-filter.ftl"><#t>
    </#if>
</#if>

<#-- 整个表格的列头信息 -->
<#if ((parameters.visibleHead!true)==true)>
<tr>

<#-- 是否可以扩展子表格 -->
    <#if (parameters.expandtype??)>
        <th class="table_head_expand"></th>
    </#if>

<#-- 选择列 -->
    <#if (parameters.selectmode??)>
        <#if (parameters.selectmode != "NONE")>
        <th class="table_head_${parameters.selectmode?lower_case}">
            <#if (parameters.selectmode == "MULTIPLE")>
                <input type="checkbox" name="allbox" onClick="ckPageSelectHandler(this,'ck_${parameters.id}');"/> <#t>
            <#elseif (parameters.selectmode == "SINGLE")>
            ${parameters.tagbundle['table-thead.choose']} <#t>
            </#if>
        </th>
        </#if>
    </#if>

<#-- 具体定义的各个列头 -->
    <#list parameters.columns as col>
        <#if ((col.parameters.visible!true)==true)>
            <#if ((col.parameters.visibleHead!true)==true)>
            <th <#rt> <#-- Begin TH.54 -->
                <#if (col.parameters.width??)>
                    <#lt> width="${col.parameters.width}" <#rt>
                </#if>
                <#if (col.parameters.height??)>
                    <#lt> height="${col.parameters.height}" <#rt>
                </#if>
                <#if (col.dataattribute??) >
                    <#lt> dataattribute="${col.dataattribute?upper_case}" sortname="${col.parameters.sortname!''}" <#rt>
                    <#lt> tid="${parameters.id}" <#rt>
                    <#if ((col.parameters.sortable!true)==true)>
                        <#lt> onclick="pageSort(this,event)" class="table_thsort"<#rt/>
                    </#if>
                </#if>
                <#lt> nowrap="true"><#rt> <#-- End TH.54 -->

                <#assign mytitle=col.parameters.label!''>
            <#--国际化-->
                <#if (parameters.appBundle?? && parameters.appBundle.containsKey('${col.dataattribute!}'))>
                    <#assign mytitle = parameters.appBundle.getString('${col.dataattribute!}')>
                </#if>
                <#if (mytitle=='') && col.parameters.jxattribute??>
                    <#assign mytitle=col.parameters.jxattribute.title!''>
                </#if>
                <#if (mytitle=='') >
                    <#if (col.dataattribute??) && (parameters.jboset??) && (parameters.jboset.jxAttributes??)>
                        <#if parameters.jboset.jxAttributes[col.dataattribute!]??>
                            <#assign mytitle=parameters.jboset.jxAttributes[col.dataattribute!].title!''>
                        </#if>
                    </#if>
                </#if>
                <#if (mytitle=='') >
                    <#assign mytitle=col.dataattribute!'未知标题'>
                </#if>
                <#t>${mytitle}<#t>
                <#if (col.parameters.note??)>
                    <#lt><br/>${col.parameters.note}<#rt>
                </#if>
                <#if (col.parameters.required!false)>
                    <#lt> <span class="table_th_required"></span> <#rt>
                </#if>

                <#if ((col.parameters.sortable!true)==true)>
                    <#if ((parameters.orderby!'unknow')==((col.dataattribute!'')+" desc") || (parameters.orderby!'unknow')==((col.parameters.sortname!'')+" desc"))>
                        <img src="${baseSkin}/images/arrow_down.png" class="icons"/><#t>
                    <#elseif ((parameters.orderby!'unknow')==((col.dataattribute!'')+" asc")  || (parameters.orderby!'unknow')==((col.parameters.sortname!'')+" asc"))>
                        <img src="${baseSkin}/images/arrow_up.png" class="icons"/><#t>
                    </#if>
                </#if>
                <#if col_has_next>
                    <#if ((parameters.columns[col_index+1].parameters.visibleHead!true)==true)>
                        <#t></th><#t>
                    </#if>
                <#else>
                        <#t></th><#t>
                </#if>
            </#if>
        </#if>
    </#list>
</tr>
</#if>
<!--table-thead.ftl end-->

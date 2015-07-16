<#--
/**
$id:null$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
 <#if (col_index == 0 && parameters.rootparent??)>
    <span class="treeclose" treeStatus="close" onclick="openTreeTable(this,event)" <#rt>
       <#lt> title="${parameters.tagbundle['table-thead.expend.title']}" parent="root"></span>
 </#if>

 <#if (col.dataattribute??) && ( (parameters.inputmode!'')=='EDIT') && (!jbo.readonly) && (!jbo.isReadonly(col.dataattribute!'')) && ((col.dataattribute!'')?index_of('.')<0) && ((col.dataattribute!'') != jbo.getUidName()) && (jbo.getJxAttribute(col.dataattribute)??)>
     <#assign inputValue = colDataValue>
     <#if col.descdataattribute??>
         <#assign inputDescValue=colDescDataValue>
     </#if>
     <#assign cssClass = (col.parameters.dynamicAttributes.cssClass!'text-input') />
     <#include "table-tbody-col-input.ftl"><#t>
 <#else>
     <#assign ml=(col.parameters.maxlength!200)?number>
     <#if (((dishtml!false)==true) || (col.parameters.render!'TEXT')=="HTML" )>
        <#-- 按HTML风格显示 -->
        <#t>${colDataValue}<#t>
     <#else>
        <#-- 显示文字，对HTML进行转义 -->
        <#escape colDataValue as colDataValue?html>
             <#if ((colDataValue?length) > ml) >
                <#if col.descdataattribute??>
                    ${(colDescDataValue?substring(0,ml))}...<#t>
                <#else>
                    ${(colDataValue?substring(0,ml))}...<#t>
                </#if>
             <#else>
                <#if ((col.startHtml!'') == '')>
                    <#if col.descdataattribute??>
                        <span title="${colDescDataValue}">${colDescDataValue}</span><#t>
                    <#else>
                        <span title="${colDataValue}">${colDataValue}</span><#t>
                    </#if>
                <#else >
                    <#if col.descdataattribute??>
                        ${colDescDataValue}<#t>
                    <#else>
                        ${colDataValue}<#t>
                    </#if>
                </#if>
             </#if>
         </#escape>
     </#if>
 </#if>
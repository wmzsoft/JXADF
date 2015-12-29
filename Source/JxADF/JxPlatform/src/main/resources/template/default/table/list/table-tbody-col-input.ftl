<#--
/**
$table-col-input$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#assign class = "">
<#assign seq = (jbo_index + 1)>
<#if col.dataattribute == 'ROWNUM'>
    <#assign pagesize = parameters.pagesize!20>
    <#assign pagenum = (parameters.pagenum!1)>
${seq + (pagesize?number) * (pagenum?number - 1)}
<#else>
    <#assign tdidpre=(parameters.id)+"_"+(jbo_index)+"_">
    <#assign dateid=tdidpre+(col.dataattribute!'')>
    <#if (cssClass??)>
        <#assign cssClass=cssClass?replace('dataattribute=',tdidpre)>
    </#if>
    <#assign attribute=jbo.getJxAttribute(col.dataattribute!'')>
    <#assign maxtype=attribute.maxType!'un'>
<#--如果maxtype的为YORN则生成checkbox-->
    <#if (maxtype=='YORN')>
    <input type="checkbox" id="${dateid}" <#rt>
        <#if (jbo.getString(col.dataattribute)??)>
            <#if ((jbo.getString(col.dataattribute)!'0')=='1')>
                <#lt> checked="checked" <#rt>
            </#if>
        </#if>
        <#lt> dataattribute="${attribute.attributeName!''}" <#rt>
        <#lt> onBlur="tableInputOnBlur(this,event)" onChange="inputOnChange(this)" <#rt>
        <#lt> class="tdcheck validate[${cssClass!}]"<#rt>
            /><#t>
    <#elseif (col.parameters.readonly??)&&(col.parameters.readonly == "true")>
        <#lt> ${jbo.getString(col.dataattribute)!''}<#rt>
    <#else>
        <input type="text" id="${dateid}" name="${dateid}" <#rt>
        <#lt> value='${jbo.getHtmlInputValue(col.dataattribute)!''}'<#rt>
        <#lt> dataattribute="${attribute.attributeName!''}" <#rt>
        <#lt> onBlur="tableInputOnBlur(this,event)" onChange="inputOnChange(this)" <#rt>
        <#if (jbo.isRequired("${attribute.attributeName!''}")!false)>
            <#lt> required="required" <#rt>
            <#assign cssClass = "required," + cssClass>
        </#if>
        <#if (col.parameters.lookup??) && (col.parameters.lookupWrite??) && (col.parameters.lookupWrite == "FALSE")>
            <#lt> readonly="true" <#rt>
            <#if (col.parameters.descdataattribute??)>
                <#lt> style="display:none" <#rt>
            </#if>
        </#if>
        <#if (col.parameters.width??)>
                  <#lt> style="width:${col.parameters.width}px" <#rt>
        </#if>
    <#--行编辑中的日期只能选择-->
        <#if (maxtype?upper_case)=='DATE'>
            <#lt> readonly="true" datatype="date" <#rt>
            <#assign cssClass = "custom[date]," + cssClass>
        <#elseif (maxtype?upper_case)=='DATETIME'>
            <#lt> readonly="true" datatype="date" <#rt>
			<#assign cssClass = "custom[dateTime]," + cssClass>
		<#elseif (maxtype?upper_case)=='TIME'>
			<#lt> readonly="true" datatype="date" <#rt>
            <#assign cssClass = "custom[time]," + cssClass>
        </#if>

        <#if ((jbo.isNumeric(col.dataattribute)!false)==true) >
            <#lt>class="tdinputnum validate[${cssClass}]" <#rt>
        <#elseif ((attribute.isDateType()!false)==true)>
            <#lt>class="tdinputdate validate[${cssClass}]" <#rt>
        <#else>
            <#lt>class="tdinput validate[${cssClass}]" <#rt>
        </#if>
    /><#t>
        <#if col.parameters.descdataattribute??>
        <input type="text" value="${inputDescValue}"<#rt/>
            <#lt> id="${dateid}_DESC" <#rt>
            <#lt> name="${dateid}_DESC" <#rt>
            <#lt> dataattribute="${col.parameters.descdataattribute}" <#rt>
            <#if ((jbo.isRequired("${col.parameters.descdataattribute}")!false) && !(parameters.readonly!false))>
                <#lt> required="required" <#rt/>
            </#if>
            <#lt> readonly="true"<#rt/>
            <#if (parameters.readonly?? && parameters.readonly)>
                <#lt> class="form_td_multipart_readonly"
            <#else>
                <#lt> class="form_td_multipart ${cssClass}"
            </#if>
            <#t/>/><#t/>
        </#if>
    </#if>
</#if>
<#if (col.parameters.readonly??)&&(col.parameters.readonly == "true")>

<#else>
    <#if (col.parameters.lookup??)>
    <img class="lookupicon ${cssClass}" src="${base}/skin/${skinName}/images/lookup.png"
        <#lt>
         onclick="lookupAction('${col.parameters.lookup}','${dateid}', ${col.parameters.lookupWidth!0}, ${col.parameters.lookupHeight!0})"/>
    </#if>
    <#include "../../common/btnDate.ftl">
</#if>
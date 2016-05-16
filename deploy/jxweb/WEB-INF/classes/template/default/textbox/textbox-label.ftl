<#--
/**
$id:textbox$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#assign extcss="">
<#if (inputmode=='QUERY') || (inputmode=='QUERYIMMEDIATELY')>
    <#-- 查询框样式补充 -->
<#else>
    <#-- 输入框样式补充 -->
    <#if (parameters.readonly!false)>
        <#assign extcss=" readonly">
    <#elseif (parameters.required!false)>
        <#assign extcss=" required">
    </#if>
</#if>

<#if (parameters.mystyle!'TD')=='TD'>
    <#lt><td class="form_td_label${extcss}" <#rt>
    <#if (parameters.labeltip??)>
        <#lt> title='${parameters.labeltip!}' <#rt>
    </#if>
    ><#t>
</#if>
<#if (parameters.label??)>
    <#assign plabel=parameters.label>
<#elseif (parameters.dynamicAttributes.label??)>
    <#assign plabel=parameters.dynamicAttributes.label>
<#elseif (parameters.columnAttribute??)>
    <#assign plabel=parameters.columnAttribute.title!(parameters.tagbundle['label.null']!)>
</#if>
<#if (((parameters.rows!1)?number) == 1)>
    <#if ((parameters.dataValue!'x')?lower_case)?starts_with("http://") || ((parameters.dataValue!'x')?lower_case)?starts_with("https://")>
        <a href="${parameters.dataValue!'javascript:void(0)'}" target="_blank">${plabel!''}</a><#t>
    <#elseif (((parameters.render!'x')?upper_case)=="LINKLABEL")>
        <a href="${parameters.renderExtends!''}${parameters.dataValue!''}" target="_blank">${plabel!''}</a><#t>
    <#else>
        ${plabel!''}<#t>
    </#if>
<#elseif (((parameters.render!'x')?upper_case)=="LINKLABEL")>
    <a href="${parameters.renderExtends!''}${parameters.urlParamValue!}" target="${parameters.urlTarget!'_self'}">${plabel!''}</a><#t>
<#else>
    ${plabel!''}<#t>
</#if>
<#if (inputmode=='QUERY') || (inputmode=='QUERYIMMEDIATELY') || (inputmode=='READONLY') || ((parameters.render!'x')=='CKEDITOR') >
    <#-- readonly情况不处理 -->
<#elseif (parameters.columnAttribute??)>
    <#if (parameters.columnAttribute.length??)>
        <#assign inputsize=(parameters.columnAttribute.length)>
        <#if (parameters.dataValue??)>
            <#assign remainderLen=inputsize - parameters.dataValue?length>
        <#else>
            <#assign remainderLen=inputsize />
        </#if>
        <#if parameters.rows?? && !(parameters.readonly!false)>
            <br/>&nbsp;<font color="Blue">${parameters.tagbundle['textbox.inputcount.pre']!}<#t/>
            <input type="text" tabIndex="-1000" id="len${parameters.id!'x'}" <#rt/>
            <#lt> readonly class="form_label_tip" <#rt/>
            <#lt> value="${remainderLen}" maxvalue="${inputsize?c}">${parameters.tagbundle['textbox.inputcount.end']!}</font><#t/>
        </#if>
    </#if>
</#if>
<#if (parameters.mystyle!'TD')=='TD'>
    </td><td class="form_td_content" <#rt/>
    <#if (parameters.valuetip??)>
        <#lt> title='${parameters.valuetip!}' <#rt/>
    </#if>
    <#if parameters.colspan??>
        <#lt> colspan="${parameters.colspan?html}" align="left" <#rt>
    </#if>
    <#if parameters.rowspan??>
        <#lt> rowspan="${parameters.rowspan?html}" valign="middle" <#rt>
    </#if>
	><#t>
</#if>
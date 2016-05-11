<#--
/**
$id:textbox$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#assign cssClass = (parameters.dynamicAttributes.cssClass!'text-input') />
<#assign icss = ''>
<#if (parameters.isInput!false)>
    <input length="${(inputsize!20)?c}"  <#rt/>
    <#if (parameters.render!"") == "PASSWORD">
        <#lt/>  type="PASSWORD"  <#rt/>
    <#elseif ((parameters.render!"") == "HIDDEN")>
        <#lt/>  type="hidden"  <#rt/>        
    <#else>
        <#lt/>  type="text"  <#rt/>
    </#if>
    <#assign icss = "form_td_100">
    <#if (parameters.columnAttribute??) >
        <#if ((parameters.columnAttribute.numeric!false)==true)>
            <#assign icss='form_td_number'>
            <#assign cssClass = "custom[number]," + cssClass>
        <#elseif ((parameters.columnAttribute.dateType!false)==true)>
            <#assign icss='form_td_date'>
        </#if>
    </#if>
    <#if parameters.cols??>
        <#lt> size="${parameters.cols}" <#rt/>
    </#if>
    <#if fromMultipart??>
        <#lt> class="form_td_multipart_first" <#rt/>
    </#if>
<#else>
    <textarea rows="${parameters.rows!1}" <#rt/>
    <#if parameters.cols??>
        <#lt> cols="${parameters.cols!20}" <#rt/>
    </#if>
    <#lt> length="${(inputsize!20)?c}" <#rt>
    <#assign icss = "form_td_textarea">
</#if>
<#lt> id="${parameters.id!'x'}" <#rt/>
<#if (parameters.required!false)>
    <#lt> required="required" <#rt/>
    <#assign cssClass = "required," + cssClass>
</#if>
<#if (parameters.name??)>
    <#lt> name="${parameters.name}" <#rt>
</#if>
<#if (parameters.dataattribute??)>
    <#lt> dataattribute="${parameters.dataattribute!''}" <#rt>
    <#if (parameters.jbo?? ) >
        <#lt> onBlur="inputOnBlur(this,event)" onfocus="inputOnFocus(this,event)" onChange="inputOnChange(this,event)" onKeyup="inputOnChange(this,event)" changed="0" <#rt/>
    </#if>
</#if>
<#if (parameters.render??)>
    <#lt> render="${parameters.render!''}" <#rt>
</#if>
<#if (parameters.renderExtends??)>
    <#lt> renderExtends="${parameters.renderExtends!''}"<#rt/>
</#if>
<#include "/${parameters.templateDir}/simple/scripting-events.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" /><#rt/>
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" /><#rt/>
<#if ((parameters.isInput!false)==false)>
    <#lt> class="${icss +' validate[' + cssClass + ']'}" <#rt>
    <#lt> >${parameters.dataValue!''}</textarea>
    <#if ((parameters.render!'x')=='CKEDITOR')>
        <script type="text/javascript"><#t>
            $(function () {<#t>
                renderCKEditor('${parameters.id!"x"}');<#t>
            });<#t>
        </script><#t>
    </#if>
<#else>
    <#assign maxtype="">
    <#--判断是否为日期类型-->
    <#if (parameters.dynamicAttributes.maxtype??)>
        <#assign maxtype=parameters.dynamicAttributes.maxtype!'un'>
    <#elseif (parameters.columnAttribute??) >
        <#assign maxtype=parameters.columnAttribute.maxType!'un'>
    </#if>
    <#if (maxtype?upper_case)=='DATE'>
        <#assign cssClass = "custom[date]," + cssClass>
    <#elseif (maxtype?upper_case)=='DATETIME'>
        <#assign cssClass = "custom[dateTime]," + cssClass>
    <#elseif (maxtype?upper_case)=='TIME'>
        <#assign cssClass = "custom[dateTime]," + cssClass>
    </#if>
    <#if ((jbo??) && (parameters.dataattribute??))>
        <#assign textDataValue=jbo.getHtmlInputValue(parameters.dataattribute)!''>
        <#--数字格式化-->
        <#include "format-value-number.ftl">
        <#lt> value='${textDataValue}' <#rt>
    <#else>
        <#lt> value='${parameters.nameValue!}' <#rt>
    </#if>
    <#lt> class="${icss + ' validate[' + cssClass + ']'}" /><#rt/>
    <#if (false==(parameters.readonly!false))>
        <#assign dateid=parameters.id!'x'>
        <#include "../common/btnDate.ftl">        
    </#if>
</#if>
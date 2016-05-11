<#--
/**
$id:textbox$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#assign cssClass = (parameters.dynamicAttributes.cssClass!'text-input') />
<#assign icss = ''>
<#if ((parameters.rows!1)?number) &gt; 1>
        <textarea rows="${parameters.rows}" style="width: 100%;"<#rt/>
    <#if (parameters.name??)>
        <#lt> name="${parameters.name}" <#rt>
    </#if>
    <#lt> length="${(inputsize!20)?c}" <#rt>
    <#if ((parameters.required!false) && !(parameters.readonly!false))>
        <#lt> required="required" <#rt/>
        <#assign cssClass = 'required, ' + cssClass>
    </#if>
    <#assign icss = "form_td_textarea">
<#else>
    <#if (parameters.buttonType??) && !(parameters.readonly!false)>
    <div class="input-group">
    </#if>
        <input length="${(inputsize!20)?c}"  <#rt/>
    <#if (parameters.render!"") == "PASSWORD">
        <#lt/> type="PASSWORD"<#rt/>
    <#else>
        <#lt/> type="text"<#rt/>
    </#if>
    <#assign icss = "form-control">
    <#if (parameters.name??)>
        <#lt> name="${parameters.name!'x'}" <#rt>
    </#if>
    <#if (parameters.columnAttribute??) >
        <#if ((parameters.columnAttribute.numeric!false)==true)>
            <#assign cssClass = "custom[number]," + cssClass>
        </#if>
    </#if>
    <#if parameters.cols??>
        <#lt> size="${parameters.cols}" <#rt/>
    </#if>
    <#if ((parameters.required!false) && !(parameters.readonly!false))>
        <#lt> required="required" <#rt/>
        <#assign cssClass = "required," + cssClass>
    </#if>
    <#if (parameters.readonly!false) || (parameters.buttonType??)>
        <#lt> readonly="readonly" <#rt>
    </#if>
</#if>
<#lt> id="${parameters.id!'x'}" <#rt/>
<#if (parameters.dataattribute??)>
    <#lt> dataattribute="${parameters.dataattribute!''}" <#rt>
    <#if (parameters.jbo?? ) >
        <#lt> onBlur="inputOnBlur(this)" onChange="inputOnChange(this)" onKeyup="inputOnChange(this)" changed="0" <#rt/>
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
<#if ((parameters.rows!1)?number) &gt; 1>
    <#lt> class="${icss +' validate[' + cssClass + ']'}" <#rt>
    <#lt> >${parameters.dataValue!''}</textarea>
    <#if ((parameters.render!'x')=='CKEDITOR')>
        <script type="text/javascript">
            $(function () {
                renderCKEditor('${parameters.id!"x"}');
            });
        </script>
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
        <#assign textDataValue=jbo.getString(parameters.dataattribute)!''>
    <#--数字格式化-->
        <#include "../format-value-number.ftl">
        <#lt> value="${textDataValue!}" <#rt>
    <#else>
        <#lt> value="${parameters.nameValue!}" <#rt>
    </#if>
    <#lt> class="${icss + ' validate[' + cssClass + ']'}" /><#rt/>
    <#if (false==(parameters.readonly!false))>
        <#assign dateid=parameters.id!'x'>
        <span class="input-group-btn">
            <#include "../../common/bootstrap/btnDate.ftl">
        </span>
    </#if>
    <#if (parameters.buttonType??) &&  !(parameters.readonly!false)>
    </div>
    </#if>
</#if>
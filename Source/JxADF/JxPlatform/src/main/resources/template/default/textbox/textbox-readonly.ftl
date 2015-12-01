<#--
/**
$id:textbox$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if ((parameters.render!'x') == "LINKVALUE")>
    <a href="${parameters.renderExtends!'#'}${parameters.urlParamValue!}" id='${parameters.id!"x"}' <#rt>
       <#lt> target="${parameters.urlTarget!'_self'}" <#rt>
        <#if (parameters.name??)>
            <#lt> name="${parameters.name}" <#rt>
        </#if>
       <#lt> dataattribute="${parameters.dataattribute!''}" <#rt>
        <#if fromMultipart?? && fromMultipart>
            <#lt> class="form_td_multipart_first" <#rt>
        <#else>
            <#lt> class="${icss!'form_td_100'}" <#rt>
        </#if>><#t>
    ${parameters.dataValue!''}<#t>
    </a><#t>
<#elseif ((parameters.render!'x') == "MXEVENT")>
    <a href="javascript:void(0);" onclick="${parameters.renderExtends!'void'}(this,event)" id='${parameters.id!"x"}' <#rt>
       <#lt> target="${parameters.urlTarget!'_self'}" <#rt>
        <#if (parameters.name??)>
            <#lt> name="${parameters.name}" <#rt>
        </#if>
       <#lt> urlvalue="${parameters.urlParamValue!}"<#rt>
       <#lt> dataattribute="${parameters.dataattribute!''}" <#rt>
        <#if fromMultipart?? && fromMultipart>
            <#lt> class="form_td_multipart_first" <#rt>
        <#else>
            <#lt> class="${icss!'form_td_100'}" <#rt>
        </#if>><#t>
    ${parameters.dataValue!''}<#t>
    </a><#t>
<#elseif ((parameters.isInput!false)==false)>
    <textarea readonly="readonly" class="form_td_textarea readonly" id='${parameters.id!"x"}' <#rt>
          <#lt> dataattribute="${parameters.dataattribute!''}" rows='${parameters.rows!"1"}' <#rt>
        >${parameters.dataValue!''}</textarea><#t>
    <#if ((parameters.render!'x')=='CKEDITOR')>
    <script type="text/javascript"><#t>
        $(function () {renderCKEditor('${parameters.id!"x"}');});<#t>
    </script><#t>
    </#if>
<#else>
    <#if (parameters.columnAttribute??) >
        <#if ((parameters.columnAttribute.numeric!false)==true)>
            <#assign icss='form_td_number'>
        <#elseif ((parameters.columnAttribute.dateType!false)==true)>
            <#assign icss='form_td_date'>
        </#if>
    </#if>
    <input id='${parameters.id!"x"}'  <#rt>
        <#if ((parameters.render!"") == "HIDDEN")>
            <#lt/>  type="hidden"  <#rt/>        
        <#else>
            <#lt/> type="text" readonly="readonly"  <#rt/>        
        </#if>
        <#if (parameters.name??)>
            <#lt> name="${parameters.name}" <#rt>
        </#if>
        <#if fromMultipart?? && fromMultipart>
           <#lt> class="form_td_multipart_first" <#rt>
        <#else>
           <#lt> class="${icss!'form_td_100'}" <#rt>
        </#if>
        <#if ((jbo??) && (parameters.dataattribute??))>
            <#assign textDataValue=jbo.getString(parameters.dataattribute)!''>
            <#--数字格式化-->
            <#include "format-value-number.ftl">
            <#lt>  value="${textDataValue!}" <#rt>
        <#else>
            <#lt> value="${parameters.nameValue!}" <#rt>
        </#if>
        <#lt> dataattribute="${parameters.dataattribute!''}"/> <#rt>
</#if>

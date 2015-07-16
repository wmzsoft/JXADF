<#--
/**
$id:table-close$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if ((parameters.jboset??) && (parameters.jboset.jbolist??)) >
    <#if !(parameters.columns??) || (parameters.columns?size<1)>
        <tr><td colspan="${parameters.imgcols!10}" class="noresultset">本列表配置有误，请与管理员联系</td></tr>
    </#if>
    <#assign cnt=(parameters.jboset.jbolist?size)>
    <#if (cnt<=0) >
        <tr><td colspan="${parameters.imgcols!10}" class="noresultset">本列表暂无记录</td></tr>
    <#else>
        
        <#list parameters.jboset.jbolist as jbo>
            <#if jbo.data??>
                <#if (jbo_index == 0)>
                    <tr>
                    <#assign haslist=true>
                <#else>
                    <#assign ix=(jbo_index % (parameters.imgcols!1))>
                    <#if (ix==0)>
                        </tr><tr>
                    </#if>
                </#if>
                <td>
                <#list parameters.columns as col>
                    <#--拆分、拼凑Request传参-->
                    <#assign requestParams>
                        <#if (col.parameters.allUrlParams??)>
                            <#list col.parameters.allUrlParams as item>
                                ${item.key}=${(jbo.getString(item.value!'')!'')?url}<#t>
                                <#lt><#if item_has_next>&</#if><#rt>
                            </#list>
                        <#else>
                            <#lt>${jbo.getString(col.parameters.urlParamValue!'')!''}<#rt>
                        </#if>
                    </#assign>
                    <#--拆分、拼凑Request传参 END-->
                    <#if (col.dataattribute??)>
                        <#assign colDataValue=(jbo.getString(col.dataattribute)!'')>
                        <#if (col_index > 0)>
                            <span class="imglisttitle"><#t>
                            <#if (col.parameters.label??)>
                                ${col.parameters.label}：<#t>
                            <#elseif (col.parameters.jxattribute??)>
                                ${col.parameters.jxattribute.title!''}：<#t>
                            </#if>
                            </span><#t>
                            <span class="imglistcontent"><#t>
                        </#if>
                        <#if (col.parameters.urlType??)>
                            <#assign ml=(col.parameters.maxlength!200)?number>
                            <#if col.parameters.urlType=='me' >
                                <#assign myurlprex=base>
                            <#else>
                                <#assign myurlprex=col.parameters.urlType>
                            </#if>
                            <#if ((col.parameters.mxevent!'x')!="LINK") >
                            	<#if (col.parameters.mxevent??)>
                            		<a href="javascript:void(0)" onclick="${col.parameters.mxevent}(this,event);">
                            	<#else>
                            		<a href="javascript:void(0)">
                            	</#if>
                            <#else>
                            	<a href="${myurlprex}/${col.parameters.urlParamName!'uid'}${requestParams}"  target="${col.parameters.urlTarget!'_self'}">
                            </#if>
                            <#if  (col_index == 0)> 
                                <img src="${base}/${col.parameters.urlParamName!'uid'}${requestParams}&imgtype=${parameters.imgtype!''}" class="imglist" width="${parameters.imgwidth!'100%'}" height="${parameters.imgheight!'100%'}"/>
                            <#else>
                                <#if ((colDataValue?length) > ml) >
                                    <#t>${colDataValue?substring(0,ml)}...<#t>
                                <#else>
                                    <#t>${colDataValue}<#t>
                                </#if>
                            </#if>
                            </a><br /><#t>
                        <#else>                        
                            ${jbo.getString(col.dataattribute)!''}<br /><#t>
                            <#if (col_index > 0)>
                                </span><#t>
                            </#if>
                        </#if>
                    </#if>
                    <#if ((col_index == 0) && !(parameters.readonly!false))>
                        <#if (parameters.selectmode == "MULTIPLE")>
                            <input type="checkbox" name="ck_${parameters.id}" index="${jbo_index}" id="${jbo.uidName!jbo_index}_${jbo.uidValue!0}"  value="${jbo.uidValue!0}"  onclick="ckOneSelectHandler(this,'${jbo_index}')" class="imglistcheck"/><#t>
                        </#if>
                    </#if>
                </#list>
                </td>       
            </#if>
        </#list>
        <#if (haslist!false)>
            </tr>
        </#if>
    </#if>    
</#if>

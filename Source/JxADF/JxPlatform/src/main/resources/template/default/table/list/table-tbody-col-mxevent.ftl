<#--
/**
$id:table-tbody-col-mxevent$
$author:wmzsoft@gmail.com
#date:2014.11
**/
-->
<#if (col.parameters.mxevent??)>
    <#if (col.parameters.mxevent=="SELECTRECORD") >
        <#t><a href="${base}/app.action?app=${parameters.appName!''}&uid=${jbo.uidValue!0}" <#rt>
        <#lt> target="${col.parameters.urlTarget!'_self'}">${colDataValue}</a><#rt>
    <#elseif (col.parameters.mxevent=="DELROW")>
        <#t><img class='iconbutton' src='${baseSkin}/images/delrow.png' <#rt>
        <#lt> id="del_${parameters.id}_${jbo_index}" onclick="delrow(this,event)" <#rt>
        <#lt> flag="0" mxevent='delrow'/><#rt>
    <#elseif (col.parameters.mxevent=='LOOKUP')>
        <#t><a onclick="lookup(this,event)" tid='${colDataValue}' <#rt>
        <#lt>  mxevent='lookup'>${colDataValue}</a><#rt>
    <#elseif (col.parameters.mxevent=='WORKFLOW')>
        <#if (col.dataattribute??) >
            <#t><a onclick="showFlowInfo(this,event)" <#rt>
            <#lt> wfpath='${col.parameters.mxevent_desc!0}' <#rt>
            <#lt> uid='${(jbo.getUidValue())!0}' wfid='${jbo.getWorkflowId()!0}'> <#rt>
            <#assign ml=(col.parameters.maxlength!200)?number>
            <#if ((colDataValue?length) > ml) >
                <#t>${colDataValue?substring(0,ml)}...<#t>
            <#elseif ((colDataValue?length) < 1) >
                <#t>${parameters.tagbundle['tablecol.wf.nostart']}<#t>
            <#else>
                <#t>${colDataValue!''}<#t>
            </#if>
            <#t></a><#t>
        <#else>
            <#t><img class='iconbutton' src='${baseSkin}/images/workflow.png' <#rt>
            <#lt> onclick="showFlowInfo(this,event)" <#rt>
            <#lt> wfpath='${col.parameters.mxevent_desc!0}' <#rt>
            <#lt> uid='${(jbo.getUidValue())!0}' <#rt>
            <#lt> wfid='${jbo.getWorkflowId()!0}'/><#rt>
        </#if>
    <#elseif (col.parameters.mxevent=='ATTACHMENT')>
        <#assign attcount=jbo.getAttachmentCount("")>
        <#if (attcount>0) >
            <#assign img='ok'>
        <#else>
            <#assign img=''>
        </#if>
        <#t><img class='iconbutton' src='${baseSkin}/images/attachment${img!''}.png' <#rt>
        <#lt> title="${attcount}" onclick="attachment(this,event)" <#rt>
        <#lt> uid='${jbo.uidValue!0}' <#rt>
        <#lt>
                 upath='app.action?fromUid=${jbo.uidValue!0}&code=${col.parameters.mxevent_desc}&readonly=${(jbo.readonly)?string}&pagesize=10'/><#t>
    <#elseif (col.parameters.mxevent=='LINK')>
        <#if (col.parameters.urlType??)>
            <#assign ml=(col.parameters.maxlength!200)?number>

            <#if col.parameters.urlType=='me' >
                <#assign urlParam = col.parameters.urlParamName!'uid'>
                <#assign urlParam = jbo.getElValue(urlParam)>
                <#t><a href="${base}/${urlParam!'uid'}${requestParams}" <#rt>
                <#lt>  target="${col.parameters.urlTarget!'_self'}"> <#rt>
                <#if ((colDataValue?length) > ml) >
                    <#t>${colDataValue?substring(0,ml)}...<#t>
                <#else>
                    <#t>${colDataValue}<#t>
                </#if>
            </a><#t>
            <#else>
                <#t><a <#rt>
                <#lt> href="${col.parameters.urlType}${col.parameters.urlParamName!'uid'}${requestParams}" <#lt>
                <#lt> target="${col.parameters.urlTarget!'_self'}"> <#rt>
                <#if ((colDataValue?length) > ml) >
                    <#t>${colDataValue?substring(0,ml)}...<#t>
                <#else>
                    <#t>${colDataValue}<#t>
                </#if>
            </a><#t>
            </#if>
        <#else>
            <#if (col.parameters.mxevent_icon??) >
            <#--当是图片的时候，按照图片处理-->
                <#t><img class='iconbutton' <#rt>
                <#lt> src='${baseSkin}/images/${col.parameters.mxevent_icon}.png' <#rt>
                <#lt> title="${jbo.uidValue!0}" <#rt>
                <#lt> onclick="${col.parameters.type!'void'}(this,event,'${jbo.uidValue!0}')"/><#rt>
            <#else>
                <#assign ml=(col.parameters.maxlength!200)?number>
            <#-- 当非图片的时候，按照文字处理，具体实现，看满足功能要求-->
            <a href="javascript:${col.parameters.type!'void'}(this,event,'${jbo.uidValue!0}');">
                <#if ((colDataValue?length) > ml) >
                    <#t>${colDataValue?substring(0,ml)}...<#t>
                <#else>
                    <#t>${colDataValue}<#t>
                </#if>
            </a><#t>
            </#if>
        </#if>
    <#else>
        <#assign ml=(col.parameters.maxlength!200)?number>
        <#if (col.parameters.mxevent_icon??) >
        <#--当是图片的时候，按照图片处理-->
        <span class="toolbar_btn" onclick="${col.parameters.mxevent}(this,event,'${jbo.uidValue!0}')"
              mxevent="${col.parameters.mxevent}">
            <#t><img class="appbar-menu-toolbar-icon" <#rt>
            <#lt> src='${baseSkin}/images/${col.parameters.mxevent_icon}.png' <#rt>
            <#lt> title="${jbo.uidValue!0}"/><#rt>
            <#if ((colDataValue?length) > ml) >
                <#t>${colDataValue?substring(0,ml)}...<#t>
            <#else>
                <#t>${colDataValue}<#t>
            </#if>
            </span><#t>
        <#else>
            <#if ((col.parameters.type!'x')=='LINK')>
            <a href="javascript:${col.parameters.mxevent}(this,event,'${jbo.uidValue!0}');">
                <#if ((colDataValue?length) > ml) >
                    <#t>${colDataValue?substring(0,ml)}...<#t>
                <#else>
                    <#t>${colDataValue}<#t>
                </#if>
            </a><#t>
            <#else>
                <#if ((col.parameters.mxevent_render!'button') == "SWITCH")>
                <#assign switchId='slideSwitch_${jbo_index}_${col_index}'>
                <#assign switchCssClass="">
                <div class="slide-switch">
                    <input type="checkbox" id="${switchId}" value="<#t>
                        <#if ((colDataValue?length) > ml) >
                            <#t>${colDataValue?substring(0,ml)}...<#t>
                        <#else>
                            <#t>${colDataValue}<#t>
                        </#if>"<#t>
                        <#if colDataKey??>
                            <#if (colDataKey== '1')>
                                <#assign switchCssClass="open" />
                                checked
                            </#if>
                        </#if>
                        <#if (col.parameters.mxevent_disabled??)>
                           <#assign disabled=action.getJboElValue(jbo,'${col.parameters.mxevent_disabled}')>
                        <#if (disabled=='disabled' || disabled=='true')>
                            <#lt> disabled="disabled" <#rt>
                           <#assign switchCssClass="disable">
                        </#if>
                    </#if>  onchange="${col.parameters.mxevent}(this, event ,'${jbo.uidValue!0}')"/><#t>
                    <label class="${switchCssClass}" for="${switchId}">
                        <span class="btn-switch"></span>
                    </label>
                </div>
                <#else>
                <#-- 当非图片的时候，按照文字处理，具体实现，看满足功能要求-->
                <input type="button" onclick="${col.parameters.mxevent}(this, event ,'${jbo.uidValue!0}')"
                       class="btn_column"  <#lt>
                       value="<#t>
                <#if ((colDataValue?length) > ml) >
                    <#t>${colDataValue?substring(0,ml)}...<#t>
                <#else>
                    <#t>${colDataValue}<#t>
                </#if>
                "<#t>
                    <#if (col.parameters.mxevent_disabled??)>
                       <#assign disabled=action.getJboElValue(jbo,'${col.parameters.mxevent_disabled}')>;
                        <#if (disabled=='disabled' || disabled=='true')>
                            <#lt> disabled="disabled" <#rt>
                        </#if>
                    </#if>
                        /><#t>
                </#if>
            </#if>
        </#if>
    </#if>
</#if>
<#--
/**
$id:img$
$author:wmzsoft@gmail.com
#date:2014.01
**/
-->

    <#if ((parameters.jboset??) && (parameters.jboset.jbolist??)) >
        <#assign isize=(parameters.jboset.jbolist?size)>
        <#if ((isize!0) <= 0) >
            <#-- 没有查询到结果集 -->
            <div>没有查询到结果</div>
        <#else>
            <#-- 有查询到结果集 -->
            <div id="${parameters.id}" style="text-align:left" class="imgplayer" isize="${isize}" iwidth="${parameters.width!'100%'}" iheight="${parameters.height!'100%'}" idirection="${parameters.direction!'fix'}">                   
            <#list parameters.jboset.jbolist as jbo>
                <#if (parameters.imgSrc??)>
                    <#assign imgsrc=jbo.getString(parameters.imgSrc)!''>
                    <#if (parameters.imgTitle??)>
                        <#assign imgTitle=jbo.getString(parameters.imgTitle)!''>
                    </#if>          
                    <#if (imgsrc??)>
                        <#if (parameters.imgHref??)>                    
                            <#assign imgHref=jbo.getString(parameters.imgHref)!''>
                            <#if (imgHref=='')>
                                <a href="javascript:void(0)"><#t>
                            <#else>
                                <a href="${parameters.imgBase!''}${imgHref}" target="${parameters.id}">
                            </#if>
                        <#else>
                            <a href="javascript:void(0)"><#t>
                        </#if>
                        <img src="${parameters.imgBase!''}${imgsrc}" title="${imgTitle!''}"/></a><#t>
                    </#if>
                </#if>            
            </#list>
            </div>
             <script type="text/javascript"> ${parameters.imgEvent!'imgplayer'}($('#${parameters.id}'),event);</script>   
        </#if>
    </#if>


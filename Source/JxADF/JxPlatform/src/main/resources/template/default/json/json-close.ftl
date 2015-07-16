<#--
/**
$id:json$
$author:wmzsoft@gmail.com
#date:2015.03
**/
-->
<#if ((parameters.jboset??) && (parameters.jboset.jbolist??)) >
    <#if ((parameters.jboset.jbolist?size) > 0) >
        ${parameters.startStr!""}<#t>
        <#list parameters.jsonHead as jhead>
            <#if (jhead.attributeLabel??)>
                "${jhead.attributeLabel}"<#t>
            </#if>
            <#if (jhead.attribute??)>
                :"${parameters.jboset.invokeGetMethod(jhead.attribute)}"<#t>
            </#if>
            ${jhead.fixText!""}<#t>
            <#if (jhead_has_next)>
                ,<#t>
            </#if>
        </#list>
        <#list parameters.jboset.jbolist as jbo>
            {<#t>
            <#list parameters.jsonCol as col>
                <#if (col.dataname??)>
                    "${col.dataname}"<#t>
                <#else>
                    "${(col.dataattribute!)?replace('.','_')}"
                </#if>
                :"${(jbo.getString(col.dataattribute)!)?js_string?replace("\\'", "\'")?replace("\\>",">")}"<#t>
                <#if (col_has_next)>
                    ,<#t>
                </#if>
            </#list>
            }<#t>
            <#if (jbo_has_next)>
                ,<#t>
            </#if>            
        </#list>
        <#list parameters.jsonFoot as jfoot>
            <#if (jfoot.attributeLabel??)>
                "${jfoot.attributeLabel}"<#t>
            </#if>
            <#if (jfoot.attribute??)>
                :"${parameters.jboset[jfoot.attribute]}"<#t>
            </#if>
            ${(jfoot.fixText)!""}<#t>
            <#if (jfoot_has_next)>
                ,<#t>
            </#if>
        </#list>
        ${parameters.endStr!""}<#t>
    </#if>
</#if>
<#--
/**
$id:table-close$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if ((parameters.jboset??) && (parameters.jboset.jbolist??)) >
    <#if ((parameters.jboset.jbolist?size) >= 0) >
        ${parameters.startStr!""}<#t>
        <#if (parameters.jsonHead??)>
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
        </#if>
        [<#t>
        <#list parameters.jboset.jbolist as jbo>
            {<#t>
            <#list parameters.columns as col>
                <#if (col.dataname??)>
                    "${col.dataname}"<#t>
                <#else>
                    "${((col.dataattribute!)?replace('.','_'))?lower_case}"
                </#if>
                :"${jbo.getJsonString(col.dataattribute)!}"<#t>
                <#if (col.parameters.allUrlParams??)>
                    ,<#t>
                    <#list col.parameters.allUrlParams as item>
                        "${item.key}":"${jbo.getJsonString(item.value!'')!''}"<#t>
                        <#if item_has_next>,<#t></#if>
                    </#list>
                <#elseif (col.parameters.urlParamValue??)>
                    ,"${col.parameters.urlParamValue}":"${jbo.getJsonString(col.parameters.urlParamValue)!}"<#t>
                </#if>
                <#if (col_has_next)>
                    ,<#t>
                </#if>
            </#list>
            }<#t>
            <#if (jbo_has_next)>
                ,<#t>
            </#if>            
        </#list>
        ]<#t>
        <#if (parameters.jsonFoot??)>
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
        </#if>
        ${parameters.endStr!""}<#t>
    </#if>
</#if>
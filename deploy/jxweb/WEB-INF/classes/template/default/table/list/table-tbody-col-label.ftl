<#--
/**
$id:table-tbody-col-label$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if (colDataValue??) >
    <#list colDataValue?split(' ') as cdv>
        <#if ((cdv?trim)!='')>
            <span onclick="labelsQuery(this,event)" <#rt>
                <#lt> class="table_td_labels" <#rt>
                <#lt> cause=" like ? "  <#rt>
                <#lt> dataattribute='${col.parameters.dataattribute}'><#rt>
                ${cdv}<#t>
            </span><#t>
        </#if>
    </#list>
</#if>
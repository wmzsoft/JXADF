<input type="checkbox"   disabled="disabled"<#rt>
<#if (jbo.getString(col.dataattribute)??)>
    <#if ((jbo.getString(col.dataattribute)!'0')=='1')>
        <#lt> checked="checked" <#rt>
    </#if>
</#if>
/><#t>

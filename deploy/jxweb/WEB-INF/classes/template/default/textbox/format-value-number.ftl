<#--
/**
格式化数字信息
$id:textbox-number-format$
$author:jpvfnwxf@gmail.com
#date:2015-01-14 10:12:51
**/
-->
<#if ((jbo.isNumeric(parameters.dataattribute)!false)==true) >
    <#if parameters.format??>
        <#lt> format = "${parameters.format}" <#rt>
        <#if textDataValue !=''>
            <#assign format=parameters.format?lower_case>
            <#if format==',###.##'>
                <#assign textDataValue=jbo.getDouble(parameters.dataattribute)?string(",###.##")>
            <#elseif format==',###.00'>
                <#assign textDataValue=jbo.getDouble(parameters.dataattribute)?string(",###.00")>
            <#elseif format==',##0.00'>
                <#assign textDataValue=jbo.getDouble(parameters.dataattribute)?string(",##0.00")>
            <#elseif format==',###'>
                <#assign textDataValue=jbo.getLong(parameters.dataattribute)?string(",###")>
            <#elseif format==',###.#'>
                <#assign textDataValue=jbo.getDouble(parameters.dataattribute)?string(",###.#")>
            <#elseif format=='$'>
                <#assign textDataValue=jbo.getDouble(parameters.dataattribute)?string("$,##0.00")>
            <#elseif format=='￥' || format=='rmb' >
                <#assign textDataValue=jbo.getDouble(parameters.dataattribute)?string("￥,##0.00")>
            <#elseif format=='%' || format=='％'  || format=='percent'>
                <#assign textDataValue=jbo.getDouble(parameters.dataattribute)?string.percent>
            <#elseif format=='currency'>
                <#assign textDataValue=jbo.getDouble(parameters.dataattribute)?string.currency>
            </#if>
        </#if>
    </#if>

</#if>

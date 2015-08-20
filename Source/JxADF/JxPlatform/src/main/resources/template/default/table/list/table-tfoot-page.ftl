<#--
/**
$id:null$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<!--table-toot.ftl begin-->
<#if (parameters.footVisible!true)>
<div style="float:left" id="${parameters.id!'test'}_info" role="status" aria-live="polite">
${parameters.tagbundle['table-tfoot.totalpre']}&nbsp;${parameters.count!0}
    &nbsp;${parameters.tagbundle['table-tfoot.totaltail']}
    <input type='text' class='pageSelected' id='${parameters.id}_pagesize' name='pagesize'
           value='${parameters.pagesize!20}'
           SIZE='3'
           maxlength='3'
           onpaste="return pagePaste()"
           onblur="pageBlur(this,'${parameters.id}')"
           originValue='${parameters.pagesize!20}'
           onkeypress="return pageKeypress(this, event)">
    &nbsp;(
    <#lt><span <#rt>
    <#if ((parameters.pagesize!'')=='20')>
        <#lt> class="pagesize-select" <#rt>
    </#if>
    <#lt> onclick="spanSetPageSize('${parameters.id}',20);">20  </span>,<#rt>   
    <#lt><span <#rt>
    <#if ((parameters.pagesize!'')=='50')>
        <#lt> class="pagesize-select" <#rt>
    </#if>
    <#lt> onclick="spanSetPageSize('${parameters.id}',50);">50</span>,<#rt> 
    <#lt><span <#rt>
    <#if ((parameters.pagesize!'')=='80')>
        <#lt> class="pagesize-select" <#rt>
    </#if>
    <#lt> onclick="spanSetPageSize('${parameters.id}',80);">80</span>,<#rt> 
    <#lt><span <#rt>
    <#if ((parameters.pagesize!'')=='100')>
        <#lt> class="pagesize-select" <#rt>
    </#if>
    <#lt> onclick="spanSetPageSize('${parameters.id}',100);">100</span><#rt>
    )&nbsp;${parameters.tagbundle['table-tfoot.items']}<#t>
</div>

<div id="${parameters.id!'test'}_paginate" style="float:right">
  ${parameters.pagenum!0}&nbsp;/&nbsp;${parameters.pagecount!0}${parameters.tagbundle['table-tfoot.cpagetail']}
    <#if ((parameters.pagenum!1)>1)>
        <a href='javascript:pageFirst("${parameters.id}")' class="table-tfoot-page-first"></a>
        <a href='javascript:pagePre("${parameters.id}")' class="table-tfoot-page-pre"></a>
    </#if><#rt>
    <#if ((parameters.pagenum!1) < (parameters.pagecount!1))>
        <a href='javascript:pageNext("${parameters.id}")' class="table-tfoot-page-next"></a>
        <a href='javascript:pageLast("${parameters.id}")' class="table-tfoot-page-last"></a>
    </#if>
${parameters.tagbundle['table-tfoot.goto']}
    <INPUT TYPE='text' id='${parameters.id}_topage' NAME='gotoPage'
           class="gotoPage" value='1' onpaste="return pagePaste()"
           onkeypress='pageGotoKeypress("${parameters.id}", event)' SIZE='3'
           maxlength='5'>${parameters.tagbundle['table-tfoot.cpagetail']}

    <input type='button' value='GO' onclick='pageGoto("${parameters.id}")' class='btn_href_GO'>

    <a href="javascript:exportExcel('${parameters.id!x}');" title="导出Excel" class="expexcel">
        <img border='0' src="${baseSkin}/images/excel.png"/>
    </a>
    <a href="javascript:exportWord('${parameters.id!x}');" title="导出Word" class="expword">
        <img border='0' src="${baseSkin}/images/word.png"/>
    </a>
    <a href="javascript:custcolumn('${parameters.id!x}');" title="自定义列" class="custcolumn">
        <img border='0' src="${baseSkin}/images/custom.jpg"/>
    </a>
    <a href="javascript:tableFullScreen('${parameters.id!x}');" title="全屏" class="detach">
        <img border='0' src="${baseSkin}/images/full.png"/>
    </a>


    <input type="hidden" id="${parameters.id}_pagenum" name="pagenum" value="${parameters.pagenum!1}">
    <input type="hidden" id="${parameters.id}_pagecount" name="pagecount" value="${parameters.pagecount!0}">
    <input type="hidden" id="${parameters.id}_pagesort" name="orderby" value="${parameters.orderby!''}">
</div>
</#if>

<!--table-toot.ftl end-->
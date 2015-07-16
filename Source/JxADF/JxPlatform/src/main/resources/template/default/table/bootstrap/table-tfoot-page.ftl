<#if ((parameters.footVisible!true) && ((parameters.count!0) &gt; 0))>
<nav><#t>
    <ul class="pager">
        <li><#t>
        ${parameters.tagbundle['table-tfoot.totalpre']}&nbsp;${parameters.count!0}</li><#t>
        <#if ((parameters.pagenum!1)>1)>
            <li>
                <button class="btn btn-default" type="button" onclick='pageFirst("${parameters.id}")'><#t>
            ${parameters.tagbundle['table-tfoot.fpage']!'First'}<#t>
                </button>
            </li><#t>
            <li>
                <button class="btn btn-default" type="button" onclick='pageFirst("${parameters.id}")'><#t>
                ${parameters.tagbundle['table-tfoot.bpage']!'Previous'}<#t>
                    <span class="badge">${parameters.pagenum-1}</span><#t>
                </button>
            </li><#t>
        </#if>
        <#if ((parameters.pagenum!1) < (parameters.pagecount!1))>
            <li>
                <button class="btn btn-default" type="button" onclick='pageNext("${parameters.id}")'><#t>
                ${parameters.tagbundle['table-tfoot.npage']!'Next'}<#t>
                    <span class="badge">${parameters.pagenum-(-1)}</span><#t>
                </button>
            </li><#t>
            <li>
                <button class="btn btn-default" type="button" onclick='pageLast("${parameters.id}")'><#t>
                ${parameters.tagbundle['table-tfoot.lpage']!'Last'}<#t>
                    <span class="badge">${parameters.pagecount!0}</span><#t>
                </button>
            </li><#t>
        </#if>
    </ul>
    <input type="hidden" id="${parameters.id}_pagenum" name="pagenum" value="${parameters.pagenum!1}"/>
    <input type="hidden" id="${parameters.id}_topage" name="topage" value="${parameters.pagenum!1}"/>
    <input type="hidden" id="${parameters.id}_pagecount" name="pagecount" value="${parameters.pagecount!0}"/>
    <input type="hidden" id="${parameters.id}_pagesort" name="orderby" value="${parameters.orderby!''}"/>
</nav><#t>
</#if>
<#--
/**
$id:null$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#if (parameters.footVisible!true)>

<tr>
    <div class="dataTables_info" id="${parameters.id!'test'}_info" role="status" aria-live="polite">
    	共有${parameters.count!0}记录&nbsp;每页显示&nbsp;
    	  <input type='text' class='pageSelected' id='${parameters.id}_pagesize' name='pagesize'
                 value='${parameters.pagesize!20}' SIZE='3' maxlength='3'
                 onpaste="return pagePaste()" onblur="pageBlur('${parameters.id}')" onkeypress='return pageKeypress()'
                 onkeydown="pageKeydown('${parameters.id}')">
    	  &nbsp;&nbsp;(
    	  <span <#if (parameters.pagesize=='20')>class="pagesize-select" </#if><#rt>
                onclick="spanSetPageSize('${parameters.id}',20);">20</span>,&nbsp;
    	  <span <#if (parameters.pagesize=='50')>class="pagesize-select" </#if><#rt>
                onclick="spanSetPageSize('${parameters.id}',50);">50</span>,&nbsp;
    	  <span <#if (parameters.pagesize=='80')>class="pagesize-select" </#if><#rt>
                onclick="spanSetPageSize('${parameters.id}',80);">80</span>,&nbsp;
    	  <span <#if (parameters.pagesize=='100')>class="pagesize-select"</#if><#rt>
                onclick="spanSetPageSize('${parameters.id}',100);">100</span>&nbsp;
    	  )&nbsp;条
      </div>

		<div id="${parameters.id!'test'}_paginate" class="dataTables_paginate">
    		当前第${parameters.pagenum!1}页，
    		共${parameters.pagecount!0}页 &nbsp;
                <#if ((parameters.pagenum!1)>1)>
                    <a href='javascript:pageFirst("${parameters.id}")'>首页</a>&nbsp;
    			<a href='javascript:pagePre("${parameters.id}")'>上页</a>&nbsp;
                </#if><#rt>
                <#if ((parameters.pagenum!1) < (parameters.pagecount!1))>
                    <a href='javascript:pageNext("${parameters.id}")'>下页</a>&nbsp;
    			<a href='javascript:pageLast("${parameters.id}")'>末页</a>&nbsp;
                </#if>
                转到 <INPUT TYPE='text' id='${parameters.id}_topage' NAME='gotoPage' style='text-align:right' value='1'
                          onpaste="return pagePaste()"
                          onkeypress='pageGotoKeypress("${parameters.id}")' SIZE='3' maxlength='5'> 页
    	&nbsp;<input type='button' value='GO' onclick='pageGoto("${parameters.id}")' class='btn_href'>
    	&nbsp;<a href="javascript:tableFullScreen('${parameters.id!x}');" title="全屏" class="detach"><img border='0'
                                                                                                         src="${baseSkin}/images/full.png"/></a>

    	<input type="hidden" id="${parameters.id}_pagenum" name="pagenum" value="${parameters.pagenum!1}">
    	<input type="hidden" id="${parameters.id}_pagecount" name="pagecount" value="${parameters.pagecount!0}">
    	<input type="hidden" id="${parameters.id}_pagesort" name="orderby" value="${parameters.orderby!''}">
    <div>

</#if>    	
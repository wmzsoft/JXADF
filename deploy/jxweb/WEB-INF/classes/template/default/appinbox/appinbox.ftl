<#--
/**
$id:appinbox$
$author:wmzsoft@gmail.com
#date:2013.09
**/
-->
<#t/>
<#if (parameters.fragmentId??)>
	${parameters.statusLabel!'任务状态'}<#t><span class="form_select" >
	<select id="s_${parameters.id}" onChange="selectAppInboxStatus(this,event);"  <#rt/>
		fragmentId="div_${parameters.fragmentId}" statusColumn="${parameters.statusColumn!'audit_status'}" <#rt>
		backflagColumn="${parameters.backflagColumn!'WFT_BACKFLAG'}" <#rt>
		creatorColumn="${parameters.creatorColumn!''}" transactorColumn="${parameters.transactorColumn!'WFT_TRANSACTOR_ID'}"><#rt/>		
		<option value="0">全部</option><#t>
		<option value="1" <#if ((parameters.status!'')=='1') >selected</#if>>我的待办</option><#t>
		<option value="2" <#if ((parameters.status!'')=='2') >selected</#if>>我的已办待结</option><#t>
		<option value="3" <#if ((parameters.status!'')=='3') >selected</#if>>我的审批已完成</option><#t>
		<option value="4" <#if ((parameters.status!'')=='4') >selected</#if>>所有审批已完成</option><#t>
		</select></span><#t>
    <#if (parameters.nodeList??) && ((parameters.disnode!true)==false)>
	    <span id="l_s_${parameters.id}">&nbsp;&nbsp;${parameters.nodeLabel!'任务'}<#t></span>
	    <span class="form_select" ><#t>
	    <select id="n_s_${parameters.id}" onChange="selectAppInboxNode(this,event);" <#rt/>
	    fragmentId="div_${parameters.fragmentId}" statusColumn="${parameters.statusColumn!'audit_status'}" <#rt/>
		firstActTaskId="${parameters.firstActTaskId!''}" <#rt>
		creatorColumn="${parameters.creatorColumn!''}" transactorColumn="${parameters.transactorColumn!'WFT_TRANSACTOR_ID'}"><#rt/>		
	    <option value="">全部</option><#t><#t>
	    <#list parameters.nodeList as jbo>
	    	<#if (jbo.data??) >
	    		<option value="${jbo.getString('actid')!''}" <#rt>
	    		<#if (parameters.nodeId!'' == jbo.getString('actid')!'1') >
	    			selected<#rt>
	    		</#if>
	    		>${jbo.getString('actname')!'未知'}</option><#t>
	    	</#if>
	    </#list>
	    </select></span><#t>
	</#if>
	<#if ((parameters.disroute!false)==true)>
		&nbsp;&nbsp;<input value="发送工作流" id = "r_n_s_${parameters.id}"  type="button" class="btn_href" onclick="routeDialog(this,event)" fragmentid="${parameters.fragmentId}" <#rt>
		<#if ((parameters.status!'')!='1') >
			<#lt> style="display:none" <#t>			
		</#if>
		/><#t>	
	</#if>
</#if>
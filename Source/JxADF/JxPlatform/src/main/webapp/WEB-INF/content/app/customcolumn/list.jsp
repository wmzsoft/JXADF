<%@ include file="/WEB-INF/content/common/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<jxui:head title="自己定义显示列">
<script type='text/javascript'>
  $(function() {
  	//加载已经选中的
  	var tbid=$('#fromid').val();
  	var ptable = $('#'+tbid,parent.document);
  	var pthead = $(ptable).find('th');
  	var len = pthead.length;
  	for (var i=0;i<len;i++){  
  		var attr = $(pthead[i]).attr('dataattribute');
  		var txt = $(pthead[i]).text();
  		if (txt=='' || attr =='' || attr==null || txt==null){	
  		}else{
  			$("li.ui-state-default[value='"+attr+"']").css("display","none"); 
  			$('#selectedId').append("<li class='ui-state-highlight' value='"+attr+"'>"+txt+"</li>");
  		}
  	}
  	//加载列表
    $("#allId, #selectedId").sortable({
      connectWith: ".connectedSortable",
      opacity: 0.6,
      revert: true,
      out: function(event, ui) {
      		if ($('#selectedId').children().length==0){
      			$("#allId, #selectedId").sortable('cancel');
      		}
      	}
    }).disableSelection();
    
  });
  
  function ok(me,e,init){
  	var dnames = $('#selectedId').children();
  	var count = dnames.length;
  	var dvalues = "";
  	if (init==false){
	  	for (var i=0;i<count;i++){
	  		var v = $(dnames[i]).attr('value');
	  		if (v !='undefined'){
	  			dvalues = dvalues + v +",";
	  		}
	  	}
  	}else{
  		dvalues="init";
  	}
  	//重新加载数据
  	
  	var tbid=$('#fromid').val();
  	getTableData('div_'+tbid,e,closewindow,dvalues);
  }
  
  function closewindow(){  	
  	var ld =window.parent.$('#'+$('#fromid').val()+'div');
  	ld.dialog('close');
    ld.remove();
    //window.close();
  }
</script>
</jxui:head>
<c:if test="${param.relationship==null||param.relationship==\"\"}">
    <c:set var="sql" value="objectname='${param.objectname}'"/>
</c:if>
<c:if test="${param.relationship!=null&&param.relationship!=\"\"}">
    <c:set var="sql" value="objectname in (select child from maxrelationship where parent='${param.objectname}'  and name='${param.relationship}')"/>
</c:if>
<jxui:body appName="customcolumn" appType="list">
    <jxui:form id="customcolumn">
    	
    	<div style="float: left;width:300px;height:390px;overflow:auto;overflow-y:scroll;">
    			可选项&nbsp;<input type="button" value="标准" class="btn_href" onClick="ok(this,event,true)"/><BR>
    			<jxui:selectlist id="allId" 
                    	jboname="maxattribute" 
                    	wherecause="${sql}"
                    	orderby="attributeno"
                    	displayName="title"
                    	displayValue="attributeName"
                    	liClass="ui-state-default"/>
    	</div>
    	<div style="float: left;width:300px;height:390px;overflow:auto;">
    			需要显示的列
    			&nbsp;<input type="button" value="确定" class="btn_href" onClick="ok(this,event,false)"/><br>
    			<jxui:selectlist id="selectedId"/>
    	</div>
    </jxui:form>
    <jxui:footer/>
</jxui:body>




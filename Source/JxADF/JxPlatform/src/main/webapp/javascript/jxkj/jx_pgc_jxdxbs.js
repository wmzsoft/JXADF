/**
**注释 by wmzsoft 2015.01.07
$(function(){
		var authority=getUrlParamVal("authority");
		if("NO"==authority){
			$("#btnSave").hide();
		}
		var uid=getUrlParamVal("uid");
		$("#txtMainId").val(uid);
		$("#txtProjId").val(getUrlParamVal("proId"));
		$("#txtTypeId").val(getUrlParamVal("typeId"));
		
		
		init();
		setTimeout(staticSave,2000);
		//点击保存的方法
		$("#btnSave").click(function(){
			var $ensemble=$(".ensemble");
			var expList=new Array();
			//查询表单中的元素
		 	$ensemble.find("textarea").each(function(){
		 		expList.push(addExpTb($(this)));
			}).end().find("input:text").each(function(){
				expList.push(addExpTb($(this)));
			});
 	 		cpms.ajax("/CommonAction!ajaxExecute.action", {
					applogic : "beanPgcjxdxbsApplogic",
					operateName : "editUnitExp",
					data:$.toJSON(expList)
				}, function(o) {
					alert(o.message);
					init();
			});
		});
		
		//初始化
		function init(){
			if(uid!=""||uid!=null){
				$.ajax({
			        url: cpms.ctx + "/CommonAction!ajaxExecute.action",
			        type: 'POST',
			        success: function (result, request) {
			            var o = eval("(" + result + ")");
			            if (o.success) {
			            	for(var i=0;i<o.message.length;i++){
								var exp=new SupExpTb();
								exp.toExp(o.message[i]);
								var $control=$("#"+exp.controls_id);
								if($control.length!=0){
									if("INPUT"==$control.get(0).nodeName.toUpperCase()){
										$control.val(exp.controls_value);
									}
									if("TEXTAREA"==$control.get(0).nodeName.toUpperCase()){
										$control.html(exp.controls_value);
									}
									$control.attr("dataId",exp.list_id);
									$control.attr("dataType",exp.controls_type);
								}
							}
			            }
			            else {
			                alert(o.header.fullMsg);
			            }
			        },
			        error: function (result, request) {
			            alert(result);
			        },
			        data: {
						applogic : "beanPgcjxdxbsApplogic",
						operateName : "getDataById",
						uid:uid
					},complete:function(){
						var fileName=getUrlParamVal("fileName");
						var fileNo=getUrlParamVal("fileNo");
						if($("#txtTypeId").val()=="A-03"){
							setDefVal($("#TXT_A03_001"),fileName);
							setDefVal($("#TXT_A03_002"),fileNo);
						}
						if($("#txtTypeId").val()=="A-04"){
							setDefVal($("#TXT_A04_001"),fileName);
							setDefVal($("#TXT_A04_002"),fileNo);
						}
						if($("#txtTypeId").val()=="A-05"){
							setDefVal($("#TXT_A05_001"),fileName);
							setDefVal($("#TXT_A05_002"),fileNo);
						}
						if($("#txtTypeId").val()=="A-08"){
							setDefVal($("#TXT_A08_001"),fileName);
							setDefVal($("#TXT_A08_002"),fileNo);
						}
						if($("#txtTypeId").val()=="A-13"){
							setDefVal($("#TXT_A13_001"),fileName);
							setDefVal($("#TXT_A13_002"),fileNo);
						}
						if($("#txtTypeId").val()=="A-14"){
							setDefVal($("#TXT_A14_001"),fileName);
							setDefVal($("#TXT_A14_002"),fileNo);
						}
						getFlowInfo();
						if(!$("#btnSave").is(":hidden")){
							authorityEle();
						}else{
							var $ensemble=$(".ensemble");
							//查询表单中的元素
						 	$ensemble.find("textarea").each(function(){
						 		$(this).attr("readonly","readonly");
							}).end().find("input:text").each(function(){
								$(this).attr("readonly","readonly");
							});
						}
						
					}
			    });
				
			}
		}
		
		//获得工作流信息
		function getFlowInfo(){
			$("input:hidden").each(function(){
				var actcode=$(this).attr("actcode");
				var targetId=$(this).attr("targetId");
				var targetType=$(this).attr("targetType");
				if(actcode!=undefined && targetType!=undefined && targetId!=undefined){
					cpms.ajax("/CommonAction!ajaxExecute.action", {
						applogic : "beanPgcjxdxbsApplogic",
						operateName : "getWorkFlowInfo",
						uid:uid,
						actcode:actcode
					}, function(o) {
						var $target=$("#"+targetId);
						if($target.val()==""){
							if("TRANSACTOR"==targetType){
								$target.val(o.message.transactor);
							}
							if("TRANSDATE"==targetType){
								$target.val(o.message.transdate);
							}
						}
					});
				}
			 });
		}
		
		//权限控制
		function authorityEle(){
			cpms.ajax("/CommonAction!ajaxExecute.action", {
				applogic : "beanPgcjxdxbsApplogic",
				operateName : "getCurrCodeByAuthority",
				uid:uid,
				typeid:getUrlParamVal("typeId")
			}, function(o) {
				var len=o.message.FIELDS.length;
				var $ensemble=$(".ensemble");
				var expList=new Array();
				//查询表单中的元素
			 	$ensemble.find("textarea").each(function(){
			 		$(this).attr("readonly","readonly");
				}).end().find("input:text").each(function(){
					$(this).attr("readonly","readonly");
				});
				for(var i=0;i<len;i++){
					var newField=$("#"+o.message.FIELDS[i]);
					newField.removeAttr("readonly");
					newField.change(function(){
						if(this.value!=""){
							$(this).removeClass("validate_field");
						}else{
							$(this).addClass("validate_field");
						}
					});
					newField.change();
				}
			});
		}
		
		
		//静态保存
		function staticSave(){
			var $ensemble=$(".ensemble");
			var expList=new Array();
			//查询表单中的元素
		 	$ensemble.find("textarea").each(function(){
		 		expList.push(addExpTb($(this)));
			}).end().find("input:text").each(function(){
				expList.push(addExpTb($(this)));
			});
			cpms.ajax("/CommonAction!ajaxExecute.action", {
					applogic : "beanPgcjxdxbsApplogic",
					operateName : "editUnitExp",
					data:$.toJSON(expList)
				}, function(o) {
					init();
			});
		}
		
		//添加封装的对象
		function addExpTb(ele){
			var exp=new SupExpTb();
			exp.controls_id=ele.attr("id");
			exp.controls_type=ele.attr("dataType");
			exp.list_id=ele.attr("dataId");
			exp.main_id=$("#txtMainId").val();
			exp.project_id=$("#txtProjId").val();
			exp.type_id=$("#txtTypeId").val();
			exp.valid_status=$("#txtValidStatus").val();
			exp.remark=$("#txtRemark").val();
			//根据不同的类型进行封装
			if(ele.get(0).nodeName.toUpperCase()=="TEXTAREA"){
				exp.controls_value=ele.html().replace(/\r?\n/gi, "<br />");
			}else if(ele.get(0).nodeName.toUpperCase()=="INPUT"){
				exp.controls_value=ele.val();
			}
			return exp;
		}
		//监理表单式对象
		var SupExpTb=function(){
			//封装普通的属性
			this.list_id;
			this.project_id;
			this.main_id;
			this.type_id;
			this.controls_id;
			this.controls_type;
			this.controls_value;
			this.valid_status;
			this.remark;
			this.creator_id;
			this.creator;
			this.create_date;
			this.modifier_id;
			this.modifier;
			this.modify_date;
			//解析json对象
			this.toExp=function(e){
				this.list_id=e.list_id;
				this.project_id=e.project_id;
				this.main_id=e.main_id;
				this.type_id=e.type_id;
				this.controls_id=e.controls_id;
				this.controls_type=e.controls_type;
				this.controls_value=e.controls_value;
			};
			return this;
		};
	});

**/

//获得URL的参数
function getUrlParamVal(name) {
	var result = location.search.match(new RegExp("[\?\&]" + name+ "=([^\&]+)","i"));
     if(result == null || result.length < 1){
         return "";
     }
     return result[1];
}

function setDefVal(e,v){
	if(e.val()==""){
		e.val(v);
	}
}
<#--
/**
$id:depttree$
$author:wmzsoft@gmail.com
#date:2015/8
**/
-->
<#assign treeid=parameters.id!'tree'>
<SCRIPT type="text/javascript">
    var z${treeid} = null;
    var setting_${treeid} = {
      <#if (parameters.checked??)>
        check: {
            enable: true,
            chkStyle: "checkbox"
        },        
      </#if>
        data: {
            key: {
              <#if (parameters.checked??)>
                checked: "${parameters.checked}",
              </#if>
                name: "name"
            },
            simpleData: {
                enable: true,
                idKey: "department_id",
                pIdKey: "super_department_id",
                rootPId: "0"
            },
            keep: {
                parent: true
            }
        },
        callback: {
            beforeExpand: onBeforeExpand_${treeid},
            onAsyncSuccess: onAsyncSuccess_${treeid},
            onAsyncError: onAsyncError_${treeid},
            <#if (parameters.checkedDisable==false)>
                onCheck: onCheck_${treeid},
            </#if>
            onClick: onClick_${treeid}
        }
    };

    var treeNodes_${treeid} = [${parameters.jsondata!''}];
    //点击数展开
    function onBeforeExpand_${treeid}(treeId, treeNode) {
        z${treeid}.reAsyncChildNodes(treeNode, "refresh", false);
    }

    function onAsyncSuccess_${treeid}(event, treeId, treeNode, msg) {
        if (treeNode) {
            if (treeNode.children.length == 0) {
                treeNode.isParent = false;
                var zTreex = $.fn.zTree.getZTreeObj(treeId);
                zTreex.updateNode(treeNode);
            }
        }
    }

    function onAsyncError_${treeid}(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
    }
    
    //请重载 please overrider        
    <#if (parameters.fragmentid??)>
        function onClick_${treeid}(event, treeId, treeNode){
            dwr.engine.setAsync(false);
            $("#div_${parameters.fragmentid}").html(treeNode.name + " Loading...");
            WebClientBean.queryJboSetData(jx_appNameType, "${parameters.fragmentRName!}",
                    "${parameters.fragmentRColumn!'department_id'} = '" + treeNode.department_id + "'", function(data) {
                        getTableData("div_${parameters.fragmentid}", event, function(data) {
                            $("#div_${parameters.fragmentid} span.table_list_thead_table_title").text(treeNode.name);
                        });
                        try{
                            eval("onClick_${treeid}_callback(event, treeId, treeNode)");
                        }catch(exception){}  
                    });
            dwr.engine.setAsync(true);   
        }
    </#if>

    $(document).ready(function () {
        z${treeid} = $.fn.zTree.init($("#${treeid}"), setting_${treeid}, treeNodes_${treeid});
        var node_${treeid} = z${treeid}.getNodeByTId("${treeid}_1");
        z${treeid}.expandNode(node_${treeid}, true, false);
        afterTreeInit(z${treeid});
        <#if (parameters.checkedDisable)>
          z${treeid}.setChkDisabled(node_${treeid}, true,true,true);
        </#if>
    });
</SCRIPT>

<div id="${parameters.id!'tree'}" class="ztree"></div>
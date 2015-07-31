<#--
/**
$id:apptree$
$author:xueliang@jxtech.net wmzsoft@gmail.com
#date:2015/4/23.
**/
-->
<!-- apptree begin -->
<#t/>
<#assign treeid=parameters.id!'tree'>
<SCRIPT type="text/javascript">
    var z${treeid} = null;
    var setting_${treeid} = {
        data: {
            key: {
                name: "description"
            },
            simpleData: {
                enable: true,
                idKey: "app",
                pIdKey: "parent",
                rootPId: "null"
            },
            keep: {
                parent: true
            }
        },
        callback: {
            beforeExpand: onBeforeExpand_${treeid},
            onAsyncSuccess: onAsyncSuccess_${treeid},
            onAsyncError: onAsyncError_${treeid},
            onClick: function (event, treeId, treeNode) {
                onClick_${treeid}(event, treeId, treeNode);           
            }
        }
    };

    var treeNodes_${treeid} = ${parameters.jsondata!'[]'};
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
    
    function onClick_${treeid}(event, treeId, treeNode){
        //请重载 please overrider
        <#if (parameters.fragmentid??)>
            dwr.engine.setAsync(false);
            $("#div_${parameters.fragmentid}").html(treeNode.description + " Loading...");
            WebClientBean.queryJboSetData(jx_appNameType, "${parameters.fragmentRName!}",
                    "APP = '" + treeNode.app + "'", function(data) {
                        getTableData("div_${parameters.fragmentid}", event, function(data) {
                            $("#div_${parameters.fragmentid} span.table_list_thead_table_title").text(treeNode.description);
                        });
                        try{
                            eval("onClick_${treeid}_callback(event, treeId, treeNode)");
                        }catch(exception){}  
                    });
            dwr.engine.setAsync(true);   
        </#if>
    }

    $(document).ready(function () {
        z${treeid} = $.fn.zTree.init($("#${treeid}"), setting_${treeid}, treeNodes_${treeid});
        var node_${treeid} = z${treeid}.getNodeByTId("${treeid}_1");
        z${treeid}.expandNode(node_${treeid}, true, false);
        afterTreeInit(z${treeid});
    });
</SCRIPT>

<div id="${parameters.id!'tree'}" class="ztree"></div>
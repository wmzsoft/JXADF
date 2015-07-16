<#--
/**
$id:apptree$
$author:xueliang@jxtech.net
#date:2015/4/23.
**/
-->
<!-- apptree begin -->
<#t/>
<#include "../../common/ftl-head.ftl">
<#assign jxui=JspTaglibs["/WEB-INF/tlds/jxui.tld"] />
<#if (parameters.label??) >
    <#if ((parameters.label!'')!='') >
        <span class="label_title">${parameters.label}</span><#t>
    </#if>
</#if><#t>

<div id="div_${parameters.id!'apptree'}"
     fragmentid="${parameters.fragmentid!''}" >
    <@jxui.tree id="${parameters.id!'apptree'}" jboname="maxapps" treeNodeKey="app" treeNodeParentKey="parent"
        whereCause="${parameters.whereCause!''}" treeNodeName="description"
        orderby="orderid" async="false" hasChildName="" /><#t>
   <!-- <br><button id="btntest" onclick="testClick()">testClick</button> -->
</div>

<script type="text/javascript">

    function apptreeClick(event, treeId, treeNode){
        debug("[apptreeClick]app=" + treeNode.description);
        if ("${parameters.fragmentid!''}"!="") {
            dwr.engine.setAsync(false); // 设置成同步
            WebClientBean.getJbo(jx_appNameType, "MAXAPPS", "app", treeNode.app, true, "", {
                callback: function (jbo) {
                    getTableData("div_${parameters.fragmentid!''}", null, null, null, 1);
                },
                errorHandler: errorHandler,
                exceptionHandler: exceptionHandler
            });
            dwr.engine.setAsync(true); // 设置成同步
        }
    }

    $(function(){
        $(".ztree").css("margin-top", "-1px");
        if("${parameters.height!''}"==""){
            $("#div_${parameters.id!'apptree'}").css("height",$(window).height()-60);
        }
        appTree = $.fn.zTree.getZTreeObj("${parameters.id!'apptree'}");
        nodes = appTree.getNodes();
        node = null;
        if (nodes.length>0) {
            node = nodes[0];
            appTree.selectNode(node,false);
        }
        if(null != node) apptreeClick(null,"#div_${parameters.id!'apptree'}",node);
    });

    window.onresize=function(){
        if("${parameters.height!''}"==""){
            $("#div_${parameters.id!'apptree'}").css("height",$(window).height()-60);
        }
    };

    setting_${parameters.id!'tree'}.callback.onClick = apptreeClick;

</script>

<style type="text/css">
    #div_${parameters.id!'apptree'}{
        width:${parameters.width!'300px'};
        overflow:auto;
        border:${parameters.border!'1px'} solid #bbbbbb;
    }
</style>
<#--
/**
$id:tree$
$author:wmzsoft@gmail.com
#date:2013.08
**/
-->
<#t/>
<SCRIPT type="text/javascript">
    var zTree = null;
    var zTreeUrl;

    var setting_${parameters.id!'tree'} = {
    <#if ((parameters.async!'false')=='true') >
        <#assign shipparam = ''>
        <#if (parameters.relationship??)>
            <#assign shipparam = '&relationshop=${parameters.relationship}}'>
        </#if>

        async: {
            enable: true,
            url: getAsyncUrl,
            autoParam: ["${parameters.treeNodeKey!'id'}"],
            dataFilter: null
        },
    </#if>
        view: {
        <#-- 如果是自定义root节点，需要做一些清理工作（1，不能有收起图标；2，不能有双击功能）-->
            dblClickExpand: function (treeId, treeNode) {
            <#if (parameters.root??)>
                return treeNode.level != 0;
            </#if>
            }
        },
        data: {
            key: {
            <#if (parameters.treeNodeName?? && parameters.treeNodeName?contains(":"))>
                name: "${parameters.treeNodeName?substring(0, parameters.treeNodeName?index_of(":"))}"
            <#else>
                name: "${parameters.treeNodeName!'name'}"
            </#if>

            },
            simpleData: {
                enable: true,
                idKey: "${parameters.treeNodeKey!'id'}",
                pIdKey: "${parameters.treeNodeParentKey!'pid'}",
                rootPId: "0"
            },
            keep: {
                parent: true
            }
        },
        callback: {
            beforeExpand: beforeExpand,
            onAsyncSuccess: onAsyncSuccess,
            onAsyncError: onAsyncError,
            onClick: function (event, treeId, treeNode) {
            <#--先到后台设置一下当前的JBO-->
                if (zTreeOnClick && typeof(zTreeOnClick) == "function") {
                    return function () {
                    <#if parameters.mode?? && parameters.mode == "ALONE">
                        zTreeOnClick(event, treeId, treeNode);
                    <#else>
                        WebClientBean.getJbo(jx_appNameType, "${parameters.jboname}", "${parameters.treeNodeKey!'id'}", treeNode.${parameters.treeNodeKey!'id'}, true, "${parameters.relationship!''}", {
                            callback: function (jbo) {
                                zTreeOnClick(event, treeId, treeNode, jbo);
                            }
                        });
                    </#if>
                    }();

                } else {
                    return function () {
                    <#if parameters.mode?? && parameters.mode == "ALONE">
                        WebClientBean.getJbo(jx_appNameType, "${parameters.jboname}", treeNode.${parameters.treeNodeKey!'id'}, true, {
                            callback: function (jbo) {
                                debug("没有自定义zTreeOnClik方法");
                            }
                        });
                    <#else>
                        debug("没有自定义zTreeOnClik方法");
                    </#if>
                    }();
                }
            }
        }
    };

    <#if ((parameters.async!'false')=='true') >
    function getAsyncUrl() {
        if (undefined == zTreeUrl || null == zTreeUrl) {
            return "${base}/tree.action?jboname=${parameters.jboname!''}&idKey=${parameters.treeNodeKey!'id'}&pidKey=${parameters.treeNodeParentKey!'pid'}&name=${parameters.treeNodeName!'name'}&hasChildName=${parameters.hasChildName!''}&orderby=${parameters.orderby!''}&cause=${parameters.restrictions!''}&leafDisplay=${parameters.leafDisplay!''}${shipparam}&i18n=${parameters.i18n!''}";
        }

        return zTreeUrl;
    }
    </#if>

    var treeNodes_${parameters.id!'tree'} = [${parameters.treeNodes!''}];
    //点击数展开
    function beforeExpand(treeId, treeNode) {
        //var zTree =$.fn.zTree.getZTreeObj(treeId);
        zTree.reAsyncChildNodes(treeNode, "refresh", false);
    }

    function onAsyncSuccess(event, treeId, treeNode, msg) {
        if (treeNode) {
            if (treeNode.children.length == 0) {
                treeNode.isParent = false;
                var zTree = $.fn.zTree.getZTreeObj(treeId);
                zTree.updateNode(treeNode);
            }
        }
    }

    function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
    }

    $(document).ready(function () {
        zTree = $.fn.zTree.init($("#${parameters.id!'tree'}"), setting_${parameters.id!'tree'}, treeNodes_${parameters.id!'tree'});
        var node = zTree.getNodeByTId("${parameters.id!'tree'}_1");
        zTree.expandNode(node, true, false);
    <#-- 如果是自定义root节点，需要做一些清理工作（1，不能有收起图标；2，不能有双击功能）-->
    <#if (parameters.root??)>
        $(".root_open").remove();
    </#if>
        afterTreeInit(zTree);
    });
</SCRIPT>

<div id="${parameters.id!'tree'}" class="ztree"></div>
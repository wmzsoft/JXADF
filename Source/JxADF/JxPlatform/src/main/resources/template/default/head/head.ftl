<!doctype html>
<html>
<#include "../common/ftl-head.ftl"/>
<head>
    <meta name="Server" content="${server}"/>    
    <#-- <meta http-equiv="cache-control" content="no-cache, must-revalidate"/> -->
    <#if ((parameters.headtype!'')=="TOPMENU")>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="JxPlatform,健新科技">
        <meta http-equiv="description" content="广州健新自动化科技有限公司，JxPlatform">
    </#if>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>${parameters.title!''}</title>

    <link rel="icon" href="${base}/skin/default/images/favicon.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="${base}/skin/default/images/favicon.ico" type="image/x-icon" />

    <#--加载jQuery相关的资源-->
    <script type='text/javascript' src='${base}/javascript/jquery-ui-1.10.3/jquery-1.9.1.min.js'></script>
    <script type='text/javascript' src='${base}/javascript/jquery-ui-1.10.3/ui/minified/jquery-ui.min.js'></script>
    <link rel="stylesheet" type="text/css" href="${base}/javascript/jquery-ui-1.10.3/themes/base/jquery.ui.all.css"/>
    <script type='text/javascript' src='${base}/javascript/jquery-ui-1.10.3/jquery.accounting.min.js'></script>
    <link rel='stylesheet' type='text/css' href="${base}/javascript/select2/css/select2.min.css" />
    <script type="text/javascript" src="${base}/javascript/select2/js/select2.full.min.js"></script>
    
    <#if ((parameters.headtype!'')=="TOPMENU")>
        <script type='text/javascript' src='${base}/javascript/jxkj/jxMenu.js'></script>
        <script type='text/javascript' src='${base}/javascript/jxkj/skinswitch.js'></script>
    <#else>
        <script type='text/javascript' src='${base}/javascript/jquery-ui-1.10.3/jquery-migrate-1.2.1.min.js'></script>
        <script type='text/javascript' src='${base}/javascript/jquery-ui-1.10.3/jquery.hoverIntent.minified.js'></script>
        <script type='text/javascript' src='${base}/javascript/jquery-ui-1.10.3/jquery.dcmegamenu.1.2.js'></script>

        <#-- 加载datatable -->
        <script type='text/javascript' src='${base}/javascript/dataTables1.9.4/js/jquery.dataTables.min.js'></script>
        <link rel='stylesheet' type='text/css' href='${base}/javascript/dataTables1.9.4/css/jquery.dataTables.css'/>
        <link rel='stylesheet' type='text/css'  href='${base}/javascript/dataTables1.9.4/css/jquery.dataTables_themeroller.css'/>
        <script type='text/javascript' src='${base}/javascript/dataTables1.9.4/plugins/scroller/js/dataTables.scroller.js'></script>
        <link rel='stylesheet' type='text/css'  href='${base}/javascript/dataTables1.9.4/plugins/scroller/css/dataTables.scroller.css'/>
        <script type='text/javascript' src='${base}/javascript/jxkj/jxtable.js'></script>

        <script type='text/javascript' src='${base}/javascript/jxkj/jquery.ba-resize.js'></script>
    </#if>

    <#--加载DWR相关的资源-->
    <script type='text/javascript' src='${base}/dwr/engine.js'></script>
    <script type='text/javascript' src='${base}/dwr/util.js'></script>
    <script type='text/javascript' src='${base}/dwr/interface/WebClientBean.js'></script>

    <#--加载jQuery.i18n.properties插件-->
    <script type='text/javascript' src='${base}/javascript/jquery.i18n.properties/jquery.i18n.properties.js'></script>
    <script type='text/javascript' src='${base}/javascript/jxkj/jx.i18n.js'></script>
    <#--加载jquery.layout-->
    <link rel="stylesheet" href="${base}/javascript/layout/layout-default.css"/>
    <script type='text/javascript' src='${base}/javascript/layout/jquery.layout.js'></script>
    <#--加载自定义js相关的资源-->
    <script type='text/javascript' src='${base}/javascript/jxkj/jxcommon.js'></script>
    <script type='text/javascript' src='${base}/javascript/jxkj/jx.inputtexthelper.js'></script>

    <#-- 加载自定义的CSS样式相关的资源,必须放到最后以方便覆盖前面的预定义的CSS -->
    <link rel="stylesheet" type="text/css" href="${base}/skin/${skinName}/css/default.css" title="skin"/>

<#t/>
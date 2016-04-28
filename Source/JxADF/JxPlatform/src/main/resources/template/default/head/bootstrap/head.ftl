<!DOCTYPE html>
<html>
<#include "../../common/ftl-head.ftl"/>
<head>
    <meta charset="utf-8" />
    <#if ((parameters.headtype!'')=="TOP")>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="JXADF,健新科技">
        <meta http-equiv="description" content="广州健新自动化科技有限公司，JXADF">
    </#if>
    <meta name="format-detection" content="telephone=no" />
    <meta name="msapplication-tap-highlight" content="no" />
    <meta name="viewport" content="user-scalable=no, initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width" />
    <title>${parameters.title!''}</title>
    <link rel="icon" href="${base}/skin/default/images/favicon.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="${base}/skin/default/images/favicon.ico" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="${base}/javascript/bootstrap/bootstrap/3.3.4/css/bootstrap.min.css"/>
    <#--加载jQuery相关的资源-->
    <script type='text/javascript' src='${base}/javascript/jquery-ui-1.10.3/jquery-1.9.1.min.js'></script>
    <script type='text/javascript' src='${base}/javascript/jquery-ui-1.10.3/ui/minified/jquery-ui.min.js'></script>
    <script type='text/javascript' src='${base}/javascript/jquery-ui-1.10.3/jquery.accounting.min.js'></script>
    <script type="text/javascript" src="${base}/javascript/bootstrap/bootstrap/3.3.4/js/bootstrap.min.js"></script>

    <#if ((parameters.headtype!'')=="TOPMENU")>
        <script type='text/javascript' src='${base}/javascript/jxkj/jxMenu.js'></script>
        <script type='text/javascript' src='${base}/javascript/jxkj/skinswitch.js'></script>
    <#else>
        <script type='text/javascript' src='${base}/javascript/jquery-ui-1.10.3/jquery-migrate-1.2.1.min.js'></script>
        <script type='text/javascript' src='${base}/javascript/jquery-ui-1.10.3/jquery.hoverIntent.minified.js'></script>
        <script type='text/javascript' src='${base}/javascript/jquery-ui-1.10.3/jquery.dcmegamenu.1.2.js'></script>
        <script type='text/javascript' src='${base}/javascript/jxkj/jquery.ba-resize.js'></script>
    </#if>

    <#--加载DWR相关的资源-->
    <script type='text/javascript' src='${base}/dwr/engine.js'></script>
    <script type='text/javascript' src='${base}/dwr/util.js'></script>
    <script type='text/javascript' src='${base}/dwr/interface/WebClientBean.js'></script>

    <#--加载jQuery.i18n.properties插件-->
    <script type='text/javascript' src='${base}/javascript/jquery.i18n.properties/jquery.i18n.properties.js'></script>
    <script type='text/javascript' src='${base}/javascript/jxkj/jx.i18n.js'></script>


    <#--加载自定义js相关的资源-->
    <script type='text/javascript' src='${base}/javascript/jxkj/jxcommon.js'></script>
	<script type='text/javascript' src='${base}/javascript/jxkj/jxtable.js'></script>
    <script type='text/javascript' src='${base}/javascript/jxkj/bootstrap/jxcommon-override.js'></script>
	<script type='text/javascript' src='${base}/javascript/jxkj/bootstrap/jxtable-override.js'></script>
	
    <#-- 加载自定义的CSS样式相关的资源,必须放到最后以方便覆盖前面的预定义的CSS -->
    <link rel="stylesheet" type="text/css" href="${base}/skin/${skinName}/css/bootstrap/default.css" title="skin"/>
<#t/>
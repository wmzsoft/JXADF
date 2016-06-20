<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/WEB-INF/tlds/struts-tags.tld" %>
<%@ page import="com.jxtech.app.usermetadata.MetaData,com.jxtech.common.JxResource" %>
<%@ page import="com.jxtech.jbo.auth.JxSession,com.jxtech.jbo.base.JxUserInfo" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

    JxUserInfo userInfo = JxSession.getJxUserInfo();
    String loginId = userInfo.getLoginid();
    String displayName = userInfo.getDisplayname();
    String langCode = userInfo.getLangcode();
    String jsLangCode = JxSession.getUserLang();
%>

<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">

    <title>JxPlatform</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="JXADF,健新科技,http://osgi.help">
    <meta http-equiv="description" content="广州健新自动化科技有限公司，JXADF,http://osgi.help">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge"/>

    <link rel="icon" href="/jxweb/skin/default/images/favicon.ico" type="image/x-icon"/>
    <link rel="shortcut icon" href="/jxweb/skin/default/images/favicon.ico" type="image/x-icon"/>

    <!-- 加载jquery和jquery.layout文件及样式 -->
    <script type="text/javascript" src="<%=path%>/javascript/jquery-ui-1.10.3/jquery-1.9.1.min.js"></script>
    <script type="text/javascript"
            src="<%=path%>/javascript/jquery-ui-1.10.3/ui/minified/jquery-ui.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="<%=path%>/javascript/jquery-ui-1.10.3/themes/base/jquery.ui.all.css"/>

    <!-- 加载DWR -->
    <script type='text/javascript' src='<%=path%>/dwr/engine.js'></script>
    <script type='text/javascript' src='<%=path%>/dwr/util.js'></script>
    <script type='text/javascript' src='<%=path%>/dwr/interface/WebClientBean.js'></script>
    <script type='text/javascript' src='<%=path%>/javascript/jxkj/jxmenu-v-icon.js'></script>
    <script type='text/javascript' src='<%=path%>/javascript/jxkj/jxcommon.js'></script>
    <script type='text/javascript' src='<%=path%>/javascript/jxkj/jxtable.js'></script>
    <!--加载jQuery.i18n.properties插件-->
    <script type='text/javascript'
            src='<%=path%>/javascript/jquery.i18n.properties/jquery.i18n.properties.js'></script>
    <script type='text/javascript' src='<%=path%>/javascript/jxkj/jx.i18n.js'></script>
    <script type='text/javascript' src='<%=path%>/javascript/jxkj/skinswitch.js'></script>
    <!-- 加载dataTable 用于重设列表宽度 -->
    <script type="text/javascript" src="<%=path%>/javascript/dataTables1.9.4/js/jquery.dataTables.min.js"></script>
    <!-- 加载Layout -->
    <script type="text/javascript" src="<%=path%>/javascript/layout/jquery.layout.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=path%>/javascript/layout/layout-default.css"/>

    <!-- 加载首页样式表 -->
    <link rel="stylesheet" id="skin-css" type="text/css"
          href="<%=path%>/skin/<s:property value='skinName'/>/css/skinClassic.css" title="skin"/>

    <script type="text/javascript">
        var contextPath = '<%=path%>';
        var panelLayout;
        var langCode = "<%=langCode%>";

        function loadApp(title,url) {
            $("#centerIframe").attr("src", url);
            // 设置title
            $("#appName").text(title);
            window.dataTableCollection = {};
        }

        function logout() {
            window.location.href = '<%=path%><%=MetaData.getUserMetadata("LOGOUT")%>';
        }

        function initUILang(lang) {
            $("#appName").html(getLangString("home.home"));
            $("#welcomeInfo").html(getLangString("home.welcomeInfo"));
            $("#home_link").html(getLangString("home.homeLink"));
            $("#exit_link").html(getLangString("home.exitLink"));
        }

        //iframe 加载完成事件
        function afterCenterFrameLoad() {

        }
        /*主页按钮跳转,部署的时候，需要将此处的src值替换为下面iframe相同的src*/
        function goHome() {
            loadApp("<%=path%><%=MetaData.getHomePageApp()%>");
            $("#appName").text(getLangString("home.home"));
        }
        function getUrlParam(href, name) {
            var value = "";
            if (href && href.length > 0) {
                var hrefs = href.split("&");
                for (var i = 0; i < hrefs.length; i++) {
                    var tempValue = hrefs[i];
                    if (tempValue.indexOf(name + "=") >= 0) {
                        var values = tempValue.split("=");
                        value = values[1];
                        break;
                    }
                }
            }
            return value;
        }

        $(function () {
            //加载布局
            loadLanguage('<%=jsLangCode%>');
            panelLayout = $('#homebody').layout({
                applyDefaultStyles: false,
                fxName: 'slide',
                fxSpeed: 'fast',
                north: {
                    size: "59",
                    spacing_open: 0,
                    closable: false,
                    resizable: false,
                    slidable: false
                },
                west: {
                    size: "263",
                    spacing_open: 15,
                    spacing_closed: 15,
                    resizable: false,
                    slidable: false
                },
                center__childOptions: {
                    center__paneSelector: ".middle-center",
                    north: {
                        paneSelector: ".middle-north",
                        size: 30,
                        spacing_open: 0,
                        closable: false
                    }
                },
                onopen_end: function () {
                    $.each(window.dataTableCollection, function (index, item) {
                        if (item[0].is(":visible")) {
                            resizeDataTable(item[0]);
                        } else {
                            delete window.dataTableCollection[index];
                        }
                    });
                },
                onclose_end: function () {
                    $.each(window.dataTableCollection, function (index, item) {
                        if (item[0].is(":visible")) {
                            resizeDataTable(item[0]);
                        } else {
                            delete window.dataTableCollection[index];
                        }
                    });
                }
            });
            loadApp("Home","<%=path%><%=MetaData.getHomePageApp()%>");
            //样式修复
            $("#header").css({
                right: 0,
                left: 0
            });

            //搜索最近浏览输入框
            $('.input-txt').focus(function () {
                $(this).val('');
            }).blur(function () {
                if ($(this).val() == '') {
                    $(this).val(getLangString("home.search"));
                }
            });

            //加载菜单数据
            WebClientBean.getMaxAppMenu(function (result) {
                if (result) {
                    var jxMenu = new JxMenu({
                        menuData: result,
                        iconBase: "<%=path%>/skin/<s:property value='skinName'/>/images/"
                    }, $("#mainMenu"));
                    jxMenu.onMenuClick = onMenuClick;
                }
            });
        })
    </script>
</head>

<body id="homebody">
<div class="ui-layout-center" id="contentPanel">
    <div class="middle-north" id="appName"></div>
    <iframe class="middle-center" id="centerIframe" name="centerIframe"
            width="100%" marginwidth="0" marginheight="0"
            frameborder="0" scrolling="auto">
    </iframe>
</div>

<div class="ui-layout-north" id="header">
    <div id="headerBg" class="headerBg">
        <div id="headerImage"></div>
        <!-- logo -->
        <div id="headerLogo" class="headerLogo">
            <%--      <img src="/jxweb/skin/default/images/logo.png" width="177" height="49" alt="LOGO">--%>
        </div>
        <div class="date">
            <p></p>
            <span id="welcomeInfo"></span>
            ,
            <strong><%=displayName%>
            </strong>
            <span id="appMsg" class="appMsg"></span>
        </div>

        <div id="headerAction" class="headerAction">
                    <span class="action" id="homeAction">
                        <a href="javascript:void(0);" target="_self" onClick="goHome();">
                            <span class="action_home" id="home_link">主页</span>
                        </a>
                    </span>
                    <span class="action" id="skinAction">
                        <span class="action_skin" id="skinswitch">样式</span>
                        <div id="skins" class="skins">
                            <div class="skins-cont">
                                <div class="skins-cont-top"></div>
                                <ul class="skins-cont-bottom skins-list">
                                    <%String ss = JxResource.skins2String();%>
                                    <%=ss%>
                                </ul>
                            </div>
                        </div>
                    </span>
                    <span class="action" id="exitAction">
                        <a href="javascript:void(0);" onClick="logout()" target="_self">
                            <span class="action_exit" id="exit_link">退出</span>
                        </a>
                    </span>
                    <span class="action" id="aboutAction">
                        <a href="http://osgi.help" target="osgi">
                            <span style="font-size: small;" id="exit_link">About</span>
                        </a>
                    </span>
        </div>
        <div class="skin-header" style="display:none">
            <ul>
                <li class="skin-blue on" skin="default"></li>
                <li class="skin-green" skin="comtop">
                    <a href="###"></a>
                </li>
                <li class="skin-yellow" skin="gray">
                    <a href="###"></a>
                </li>
                <li class="skin-red" skin="blue">
                    <a href="###"></a>
                </li>
            </ul>
        </div>
    </div>
    <div class="header-bottom"></div>

</div>

<div class="ui-layout-west" style="overflow: visible;" id="westPanel">
    <div id="mainMenu">
        <div id="menuAppBar" class="menuAppBar">
            <div class="menuAppBarLeftCorner"></div>
            <div class="menuAppBarContent">
                <div class="visitHisBtn">
                    <span class="triangleLeft"></span>
                </div>
                <div class="searchInput">
                    <input type="text" class="input-txt" value="搜索应用"/>
                    <input type="button" class="search-btn"/>
                </div>
            </div>

        </div>
    </div>

    <div class="popupWin"></div>
    <%--<div id="subMenu"></div>--%>
</div>
<%String hc = JxResource.getHomeContent();%>
<%=hc%>
</body>
</html>

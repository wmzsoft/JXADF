<%@ page language="java" import="com.jxtech.jbo.auth.JxSession" pageEncoding="UTF-8" %>
<%@ page
        import="com.jxtech.jbo.base.JxUserInfo" %>
<%
    JxUserInfo userInfo = JxSession.getJxUserInfo();
    String loginId = userInfo.getLoginid();
    String displayName = userInfo.getDisplayname();
%><head>
    <script type='text/javascript' src='javascript/jxkj/skinswitch.js'></script>
</head>
<!-- header segment-->


<div id="headerBg">
    <div id="headerImage"></div>
    <!-- logo -->
    <div id="headerLogo">
<%--        <img src="/jxweb/skin/default/images/logo.png" width="177" height="49" alt="LOGO">--%>
    </div>
   <div id="date">
        <p><strong>管理员,</strong>欢迎来到本系统<span>今天是2014-10-08 星期二 09：30</span></p>
    </div>
    <div id="headerAppInfo">
        <span id="appName"></span>
        <span id="appMsg"></span>
    </div>
    

    <!-- <div id="headerInfo">
        <label id="welcomeInfo"></label>
        <span class="userId" style="display: none"><%=loginId%></span>
        <span class="userName"><%=displayName%></span>
    </div>-->
    <div id="headerAction">
		<span class="action">
			<a href="javascript:void(0);" target="_self" onclick="goHome();">
                <span class="action_home" id="home_link">主页</span>
            </a>
		</span>

		<span class="action">
			<a href="javascript:void(0);" onclick="logout()" target="_self">
                <span class="action_exit" id="exit_link">退出</span>
            </a>
		</span>
    </div>
    <div class="skin-header">
        <ul>
            <li class="skin-blue on" skin="default"></li>
            <li class="skin-green" skin="blue"><a href="###"></a></li>
            <li class="skin-yellow" skin="comtop"><a href="###"></a></li>
            <li class="skin-red" skin="blue"><a href="###"></a></li>
        </ul>
    </div>
</div>
<div class="header-bottom"></div>



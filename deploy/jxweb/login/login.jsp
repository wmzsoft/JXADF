<%@ page language="java" pageEncoding="UTF-8" %>
<%
    String oldurl = request.getParameter("oldurl");
    String loginurl = request.getParameter("loginurl");
    String path = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
    <title>登陆</title>
    <link href="../skin/default/css/basic.css" rel="stylesheet" type="text/css"/>
    <link href="../skin/default/css/login.css" rel="stylesheet" type="text/css"/>

    <link rel="icon" href="../skin/default/images/favicon.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="../skin/default/images/favicon.ico" type="image/x-icon" />

    <script type='text/javascript' src='<%=path%>/javascript/jquery-ui-1.10.3/jquery-1.9.1.js'></script>
    <script type='text/javascript' src='<%=path%>/javascript/jquery-cookie/jquery.cookie.js'></script>
    <script type='text/javascript' src='<%=path%>/javascript/jquery-ui-1.10.3/ui/minified/jquery-ui.min.js'></script>
    <!--加载jQuery.i18n.properties插件-->
    <script type='text/javascript' src='<%=path%>/javascript/jquery.i18n.properties/jquery.i18n.properties.js'></script>
    <script type='text/javascript' src='<%=path%>/javascript/jxkj/jx.i18n.js'></script>
    <script type='text/javascript' src='<%=path%>/dwr/engine.js'></script>
    <script type='text/javascript' src='<%=path%>/dwr/util.js'></script>
    <script type='text/javascript' src='<%=path%>/dwr/interface/WebClientBean.js'></script>

    <script type="text/javascript">
        function login() {
            var userid = $("#username").val();
            var pass = $("#password").val();
            var lang = $("#langSelector").val();
            $(".error").hide();
            if ("" == userid || undefined == userid) {
                $(".error").show().text(getLangString("login.needUserName"));
                $("#username").focus();
                return;
            }

            if ("" == pass || undefined == pass) {
                $(".error").show().text(getLangString("login.needUserPass"));
                $("#password").focus();
                return;
            }

            var remeber = $("#remeberme").attr("checked");
            if (remeber) {
                $.cookie("username", userid);
                $.cookie("password", pass);
                $.cookie("remeberme", remeber);
            } else {
                $.removeCookie("username");
                $.removeCookie("password");
                $.removeCookie("remeberme");
            }

            try {
                dwr.engine.setAsync(false); //设置成同步
                WebClientBean.login(userid, pass, lang, function (result) {
                    if (result) {
                        if ("null" == "<%=oldurl%>") {
                            window.location.href = "<%=path%>/index.jsp";
                        } else {
                            window.location.href = '<%=oldurl%>';
                        }

                    } else {
                        $(".error").show().html(getLangString("login.errorTip") + getLangString("login.fail"));
                    }
                });

                dwr.engine.setAsync(true); //重新设置成异步
            } catch (e) {
                $(".error").show().html(getLangString("login.errorTip") + e);
            }
        }

        //选择语言
        function changeLanguage(newval) {
					loadLanguage(newval);
        }

        function remeberChange(me) {
            var $_this = $(me);
            if ($_this.attr("checked")) {
                $_this.removeAttr("checked");
            } else {
                $_this.attr("checked", "checked");
            }
        }

        //初始化页面语言
        function initUILang(lang) {
            $("option[value='" + lang.substring(0, 2) + "']").attr("selected", "true");
            document.title = getLangString("login.title");
            $("#rememberLabel").text(getLangString("login.remeber"));

            $("#userlabel").text(getLangString("login.username"));
            $("#passlabel").text(getLangString("login.userpass"));

            $("#loginBtn").val(getLangString("login.loginbtn"));
        }

        $(function () {
            if (window != top) {
                var tip = top.getLangString("login.timeout");
                if (tip != null) {
                    alert(tip);
                }
                top.location.href = "<%=path%>" + "/login/logout.jsp";
            }
            var browserLang = $.i18n.browserLang();
            changeLanguage(browserLang);
            loadLanguage(browserLang);

            var cookieUser = $.cookie("username");
            if (cookieUser) {
                $("#remeberme").attr("checked", "checked");
                $("#username").val(cookieUser);
                $("#password").val($.cookie("password"));
            }else{
                $("#remeberme").attr("checked", "");
                $("#username").val("");
                $("#password").val("");
            }

            $("#username").focus();

            $("body").bind("keydown", function (e) {
                if (e.keyCode == 13) {
                    login();
                }
            });
            var clientWidth = document.body.clientWidth;
            $("#bgImage").attr("width", clientWidth);
            $(".bg").css("min-width", clientWidth - 10);
            $(".bg").css("max-width", clientWidth - 10);
        });
    </script>
</head>

<body class="bd_bg">
    <div class="login">
    <p class="logo">
        <img src="../skin/default/images/login/logo.png"/>
    </p>
    <div class="user">
        <div style="height:30px;">
            <p class="error" style="display:none;"></p>
        </div>
        <p><label class="userlabel" id="userlabel" >用户：</label>
        	 <input class="input-txt" type="input" id="username" value=""/>

        <p class="password">
        	<label class="passlabel" id="passlabel" >密码：</label>
        	<input type="password" id="password" class="input-txt" value=""/>

        <p style="margin-top:10px; margin-left:75px;">
            <input  class="remeberme" type="checkbox" id="remeberme" onChange="remeberChange(this)" style="vertical-align:middle;">
            <label class="rememberLabel" for="remeberme" style="color :rgb(3, 154, 245); vertical-align:middle;" id="rememberLabel">记住我的用户名</label>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <select id="langSelector" onChange="changeLanguage(this.value)">
                <option value="zh-CN" selected="true">中文</option>
                <option value="en">English</option>
            </select>
        </p>
        <p class="btn-login"><input id="loginBtn" type="button" value="" onClick="login()"/></p>
    </div>
</div>


</body>
</html>

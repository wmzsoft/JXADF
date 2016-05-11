<%@ include file="/WEB-INF/content/common/header.jsp" %>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=7,IE=8,IE=9,IE=EDGE">
    <link rel="stylesheet" type="text/css" href="skin/comtop/css/default.css"/>
    <script type='text/javascript' src='javascript/jquery-ui-1.10.3/jquery-1.9.1.min.js'></script>
    <script type='text/javascript' src='dwr/engine.js'></script>
    <script type='text/javascript' src='dwr/util.js'></script>
    <script type='text/javascript' src='dwr/interface/WebClientBean.js'></script>
    <script type='text/javascript' src='javascript/jxkj/jxMenu.js'></script>

    <script>
        function onMenuClick(e) {
            var frameContentWindow = document.getElementById('centerIframe').contentWindow;
            frameContentWindow.location = "/jxweb/" + $(e.target).attr("appUrl");
            console.info(e.target.attributes["appUrl"]);
            //设置title
            $("#appName").text(e.target.text);
        }

        $(document).ready(function () {
            WebClientBean.getMaxAppMenu(function (result) {
                if (result) {
                    var jxMenu = new JxMenu({menuData: result, iconBase: "skin/comtop/images/menu"}, $("#mainMenu"));
                    jxMenu.onMenuClick = onMenuClick;
                }
            });
        })
    </script>
</head>

<htmL>
<body>
<div id="mainMenu"></div>
<div id="subMenu"></div>
</body>
</htmL>

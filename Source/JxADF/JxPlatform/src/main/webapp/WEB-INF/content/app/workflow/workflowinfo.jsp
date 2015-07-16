<%@ page import="com.jxtech.jbo.JboIFace" %>
<%@ page import="com.jxtech.jbo.util.JboUtil" %>
<%@ page import="java.net.URLDecoder" %>
<%
    String appname = request.getParameter("appname");
    String instanceid = request.getParameter("instanceid");
    String uid = request.getParameter("uid");

    request.setAttribute("appname", appname);
    request.setAttribute("instanceid", URLDecoder.decode(instanceid, "UTF-8"));
    request.setAttribute("uid", uid);

    String engine = "";
    try {
        JboIFace maxappswfinfoJbo = JboUtil.getJbo("MAXAPPSWFINFO", "APP", appname.toUpperCase());
        if (null != maxappswfinfoJbo) {
            engine = maxappswfinfoJbo.getString("ENGINE").toLowerCase();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    String path = request.getServletContext().getContextPath();
%>
<!DOCTYPE HTML>
<html>
<head>

    <title>工作流信息</title>
    <script type='text/javascript' src='<%=path%>/javascript/jquery-ui-1.10.3/jquery-1.9.1.min.js'></script>
    <script type="text/javascript">
        $(function () {
            var engine = "<%=engine%>";
            if ("" != engine) {
                window.location.href = "<%=path%>/<%=engine%>/wfdetail.action?appname=${appname}&instanceid=${instanceid}&uid=${uid}";
            } else {
                $("body").append("无法获取当前应用的工作流引擎！");
            }
        });
    </script>
</head>
<body></body>
</html>


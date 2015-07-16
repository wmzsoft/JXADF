<%--
  Created by IntelliJ IDEA.
  User: cxm
  Date: 15/4/20
  Time: 21:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="javascript/jquery-ui-1.10.3/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        function testig() {
            $.ajax({
                url: "/jxweb/intergrationServices.action",
                method: "post",
                data: {intergrationData: "{'jboname': 'PUB_USER','jbos': [{'action': 'C', 'datas': { 'USER_ID': 'CXM', 'LOGIN_ID': 'CXM', 'FIRST_NAME': 'CHEN', 'LAST_NAME': 'XIAOMIN'}},{'action': 'C', 'datas': {'USER_ID': 'CXM2', 'LOGIN_ID': 'CXM2', 'FIRST_NAME': 'CHEN2', 'LAST_NAME': 'XIAOMIN2'}}]}"},
                success: function (data) {
                    alert(data);
                }

            });
        }
    </script>
</head>
<body>
<input type="button" value="testIG" onclick="testig()"/>
</body>
</html>

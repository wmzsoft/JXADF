<%@ page import="com.jxtech.util.StrUtil" %>
<%@ include file="/WEB-INF/content/common/header.jsp" %>
<%
    String fromid = request.getParameter("fromid");
    if (StrUtil.isNull(fromid)) {
        fromid = "DEPARTMENT_ID";
    }
%>
<link rel='stylesheet' type='text/css' href='/jxweb/javascript/zTree/css/zTreeStyle/zTreeStyle.css'/>
<script type='text/javascript' src='/jxweb/javascript/zTree/js/jquery.ztree.all-3.5.min.js'></script>

<style type="text/css">
    .ztree {
        width: 250px;
        height: 200px;
        margin: 0 2px 0 0;

        background: none repeat scroll 0 0 #fff;
        border: 1px solid #bbb;
        border-radius: 3px;
        box-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1) inset;

        overflow: auto;
    }

    .ztree li span.button.add {
        background-position: -144px 0;
        margin-left: 2px;
        margin-right: -1px;
        vertical-align: top;
    }
</style>

<script type="text/javascript">
    function zTreeOnClick(event, treeId, treeNode, jbo) {
    }

    var afterTreeInit = JxUtil.extend(afterTreeInit, function (zTree) {
        zTree.setting.view.addHoverDom = function (treeId, treeNode) {
            var sObj = $("#" + treeNode.tId + "_span");
            var addBtnStr = "#addBtn_" + treeNode.tId;
            if (treeNode.editNameFlag || $(addBtnStr).length > 0) return;

            //配置 新增，编辑，删除3个按钮
            var addStr = "<span class='button add' id='addBtn_" + treeNode.tId +
                    "' title='确定' onfocus='this.blur();'></span>";

            sObj.after(addStr);

            //确定
            var addbtn = $(addBtnStr);
            if (addbtn) addbtn.bind("click", function () {
                toggleDepartmentVisible();

                var departmentIdInput = $("input[dataattribute='<%=fromid%>']");
                debug(departmentIdInput);
                departmentIdInput.focus();
                departmentIdInput.val(treeNode.department_id);
                departmentIdInput.attr("changed", 1);
                departmentIdInput.blur();

                return false;
            });
        }

        //移除鼠标后的树视图
        zTree.setting.view.removeHoverDom = function (treeId, treeNode) {
            $("#addBtn_" + treeNode.tId).unbind().remove();
        };
    });
</script>

<jxui:tree id="tree" jboname="PUB_DEPARTMENT" treeNodeKey="DEPARTMENT_ID" mode="ALONE"
           treeNodeParentKey="SUPER_DEPARTMENT_ID" treeNodeName="NAME" leafDisplay="true"
           whereCause="" orderby="DEPARTMENT_ID" async="true"/>
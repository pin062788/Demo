<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript">
        var vehicleIds = "";
        $(document).ready(function () {
            var getTimestamp = new Date().getTime();
            $("#referenceDetail").jqGrid({
                url: "${pageContext.request.contextPath}/reference/detail.do?_timer_=" + getTimestamp + "&id=${id}",
                datatype: "json",
                mytype: 'GET',
                colNames: ["ID", "顺序", "Code Key", "Code Desc", "Group Name", "Parent Id", "Activate", "修改时间", "修改人"],
                colModel: [
                    {name: "id", hidden: true},
                    {name: "orderKey", align: 'right'},
                    {name: "code", align: 'right'},
                    {name: "codeDesc", align: 'right'},
                    {name: "groupName", align: 'right'},
                    {name: "parentId", align: 'right'},
                    {name: "activate", align: 'right'},
                    {name: "editDate", align: 'right'},
                    {name: "editUser", align: 'right'}
                ],
                viewrecords: true,
                height: "auto",
                //width: "auto",
                autowidth: true,
                gridview: true,
                autoencode: true
            });

            $("#subPage").dialog({
                autoOpen: false,
                pisition: {my: "center", at: "center", of: window},
                width: 800,
                height: 250,
                modal: true,
                resizable: true,
                title: "节点信息"
            });
        });
        function afterSave() {
            $("#subPage").dialog("close");
            $("#referenceDetail").jqGrid().trigger("reloadGrid");
            asyncNode();
        }

        function asyncNode() {
            var tree = $.fn.zTree.getZTreeObj("referenceTreeMenu");//reAsyncChildNodes(currentNode,'refresh');
            var node = tree.getNodeByTId(currentParentNode);
            tree.reAsyncChildNodes(node, "refresh");
        }
    </script>
</head>
<body>
<div id="query" class="ui-widget">
    <div class="ui-corner-right">
        <table id="referenceDetail"></table>
    </div>
</div>
<div id="subPage"></div>
</body>
</html>
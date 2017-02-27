<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript">
        var resourceTreeVar;
        $(document).ready(function () {
            $("#saveBtn").button({
                icons: {primary: "ui-icon-disk"}
            }).next()
                    .button({
                        icons: {primary: "ui-icon-close"}
                    }).next()
                    .button({
                        icons: {primary: "ui-icon-check"}
                    }).next()
                    .button({
                        icons: {primary: "ui-icon-delete"}
                    });
            function showIconForTree(treeId, treeNode) {
                return !treeNode.isParent;
            };
            var setting = {
                check: {
                    enable: true,
                    chkboxType: {"Y": "p"}
                },
                view: {
                    showIcon: false,
                    selectedMulti: false
                },
                async: {
                    enable: true,
                    url: "${pageContext.request.contextPath}/resource/getResourceTree.do?"
                    //autoParam:["moduleId","parentId"],
                    //dataFilter: filter
                },
                callback: {
                    onAsyncSuccess: zTreeOnAsyncSuccess
                },
                data: {
                    keep: {
                        parent: true,
                        leaf: true
                    },
                    key: {
                        name: "resourceName",
                        resourceCode: "resourceCode"
                    },
                    simpleData: {
                        enable: true,
                        idKey: "resourceCode",
                        pIdKey: "parentResourceCode",
                        rootPId: "-1"
                    }
                }
            };
            resourceTreeVar = $.fn.zTree.init($("#resourceTree"), setting);
        });

        function zTreeOnAsyncSuccess() {
            getPermission();
        }
        function getPermission() {
            var _url = "${pageContext.request.contextPath}/role/getPermission.do?_timeStamp=" + (new Date()).getTime();
            var _param = {roleId: "${roleId}"};
            $.getJSON(
                    _url,
                    _param,
                    function (data) {
                        if (data) {
                            $.each(data, function (key, value) {

                                var nodes = resourceTreeVar.getNodesByParam("resourceCode", value.resourceCode);

                                if (nodes && nodes[0]) {
                                    resourceTreeVar.checkNode(nodes[0], true, true);
                                }
                            });
                            resourceTreeVar.expandAll(true);
                        }
                    }
            );

        }
        function saveAuth() {
            var params = new Array();
            var nodes = resourceTreeVar.getCheckedNodes(true);
            var len = nodes.length;
            for (var i = 0; i < len; i++) {
                var resourceCode = nodes[i].resourceCode;
                params.push(resourceCode);
            }
            var getTimestamp = new Date().getTime();
            var _url = "${pageContext.request.contextPath}/role/savePermission.do?_timer_=" + getTimestamp;
            var _param = {resources: params.join(","), roleId: "${roleId}"};
            $.postAjax(_url, _param, null, function (data) {
                data = $.parseJSON(data);
                if (data.status != "1") {
                    alert(data.message);
                } else {
                    alert("设置成功");
                    $("#resourceDialog").dialog("close");
                }
            });
        }
        function selectAll(flag) {
            resourceTreeVar.checkAllNodes(flag);
        }
        function closePop() {
            $("#resourceDialog").dialog("close");
            $("#resourceDialog").html("");
            $("#roleList").jqGrid().trigger("reloadGrid");
        }
    </script>
</head>
<body>
<div class="ui-widget">
    <div id="toolbar" class="ui-widget-header ui-state-default">
        <div id="btns">
            <button id="saveBtn" onclick="saveAuth()">保存</button>
            <button id="closeBtn" onclick="closePop();">关闭</button>
            <button id="selectAllBtn" onclick="selectAll(true)">选择全部</button>
            <button id="clearAllBtn" onclick="selectAll(false)">清除全部</button>
        </div>
    </div>
    <div id="resourceTreePane">
        <ul id='resourceTree' class='ztree'></ul>
    </div>
</div>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:include page="/common/common.jsp" />
    <script type="text/javascript">
        $(function () {
            $("#resourceMenu").menu().hide();

            var setting = {
                async: {
                    enable: true,
                    url: "${ctx }/resource/getMenu.do",
                    autoParam: ["resourceCode", "parentResourceCode"],
                    type: "post",
                    dataFilter: function (treeId, parentNode, childNodes) {
                        if (!childNodes) return null;
                        for (var i = 0, l = childNodes.length; i < l; i++) {
                            if (!childNodes[i].isParent) {
                                childNodes[i].isParent = true;
                            }
                        }
                        return childNodes;
                    }
                },
                data: {
                    key: {
                        name: "resourceName"
                    }
                },
                simpleData: {
                    enable: true,
                    idKey: "resourceCode",
                    pIdKey: "parentResourceCode",
                    rootPId: null,
                },
                view: {
                    showLine: true
                },
                callback: {
                    onClick: function (event, treeId, treeNode) {

                    },
                    onRightClick: function (e, treeId, treeNode) {
                        if (treeNode) {
                            var nodes = resourceTree.getSelectedNodes();
                            if (nodes && nodes.length > 0) {
                                for (var i = 0, j = nodes.length; i < j; i++) {
                                    if (nodes[i].resourceId == treeNode.resourceId) {
                                        $("#resourceMenu").show();
                                        $("#resourceMenu").position({my: "left top", at: "left bottom", of: e});
                                        break;
                                    }
                                }
                            }
                        }
                    },
                    onDblClick: function (e, treeId, treeNode) {
                        if (treeNode.resourceId) {
                            $("#resource_form_div").load("${ctx}/resource/edit.do?resourceId=" + treeNode.resourceId);
                        }
                    }
                }

            };
            resourceTree = $.fn.zTree.init($("#resourceTree_ul"), setting);

        });
        function addResource() {
            var nodes = resourceTree.getSelectedNodes();
            if (nodes && nodes.length > 1) {
                alert("只能选择一个节点。");
                return;
            }
            if (nodes.length == 1) {
                var pCode = nodes[0].resourceCode;
                $.loadDiv("resource_form_div", "${ctx}/resource/add.do", {parentCode: pCode}, "post");
            }
            else {
                alert("请选择一个节点。");
            }
        }
        function editResource() {
            var nodes = resourceTree.getSelectedNodes();
            if (nodes && nodes.length > 1) {
                alert("只能选择一个节点。");
                return;
            }
            if (nodes.length == 1) {
                $.loadDiv("resource_form_div", "${ctx}/resource/edit.do", {resourceId: nodes[0].resourceId}, "post");
            }
            else {
                alert("请选择一个节点。");
            }
        }
        function delResource() {
            var nodes = resourceTree.getSelectedNodes();
            if (nodes && nodes.length > 1) {
                alert("只能选择一个节点.");
                return;
            }
            if (nodes && nodes.length == 1) {
                var confirmMessage = '';
                if (nodes[0].isParent)
                    confirmMessage = "确认删除该节点以及其子节点？";
                else
                    confirmMessage = "确认删除该节点？";
                if (confirm(confirmMessage)) {
                    var url = "${ctx}/resource/delete.do?resourceCode=" + nodes[0].resourceCode;
                    $.postAjax(url, null, null, function (data) {
                        var result = jQuery.parseJSON(data);
                        if (result.status == '1') {
                            alert("删除成功");
                            refreshNode(nodes[0]);
                            $("#resource_form_div").html("");
                        }
                    });
                }
            }
            else {
                alert("请选择一个节点。");
            }
        }
        function refreshNode(_resource) {
            if (_resource) {
                var pNnode = resourceTree.getNodeByParam("resourceCode", _resource.parentResourceCode, null);
                pNnode.isParent = true;
                resourceTree.updateNode(pNnode);
                resourceTree.reAsyncChildNodes(pNnode, "refresh");
            }
        }


    </script>
    <style type="text/css">
        .ui-menu {
            width: 80px;
        }

    </style>
</head>
<body id="loading">
<table cellspacing="5" cellpadding="5" width="100%" border="0">
    <tr>
        <td width="40%">
            <div class="ui-widget-content">
                <div class="ui-widget-header ui-state-default"><span class="ui-widget">所有资源</span></div>
                <div style="margin:5px 0;overflow:auto;height:550px;width:100%;background:#fff;position:">
                    <ul id="resourceTree_ul" class="ztree"></ul>
                </div>
            </div>
        </td>
        <td width="60%">
            <div class="ui-widget-content">
                <div class="ui-widget-header">资源详细</div>
                <div id="resource_form_div" style="margin:5px 0;overflow:auto;height:550px;width:100%;background:#fff;">

                </div>
            </div>
        </td>
    </tr>
</table>
<ul id="resourceMenu" class="context_menu">
    <li id="mAddMenu" onclick="addResource()"><span class="ui-icon ui-icon-plus"></span><span>添加</span></li>
    <li id="mEditMenu" onclick="editResource()"><span class="ui-icon ui-icon-pencil"></span>修改</li>
    <li id="mDelMenu" onclick="delResource()"><span class="ui-icon ui-icon-trash"></span>删除</li>
</ul>
</body>
</html>
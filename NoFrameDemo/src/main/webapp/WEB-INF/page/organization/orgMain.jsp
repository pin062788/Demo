<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <script type="text/javascript">
        $(function () {
            $("#orgMenu").menu().hide();

            var setting = {
                async: {
                    enable: true,
                    url: "${ctx }/organization/getOrgTree.do",
                    autoParam: ["id", "pId"],
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
                        name: "text"
                    }
                },
                simpleData: {
                    enable: true,
                    idKey: "id",
                    pIdKey: "pId",
                    rootPId: null,
                },
                view: {
                    showLine: true
                },
                callback: {
                    onClick: function (event, treeId, treeNode) {
                        if (treeNode.id) {
                            if (treeNode.id == 0) {
                                return;
                            }
                            $("#org_form_div").load("${ctx}/organization/edit.do?orgId=" + treeNode.id);
                        }
                    },
                    onRightClick: function (e, treeId, treeNode) {
                        if (treeNode) {
                            var nodes = orgTree.getSelectedNodes();
                            if (nodes && nodes.length > 0) {
                                for (var i = 0, j = nodes.length; i < j; i++) {
                                    if (nodes[i].id == treeNode.id) {
                                        $("#orgMenu").show();
                                        $("#orgMenu").position({my: "left top", at: "left bottom", of: e});
                                        break;
                                    }
                                }
                            }
                        }
                    },
                    onDblClick: function (e, treeId, treeNode) {
                    }
                }

            };
            orgTree = $.fn.zTree.init($("#orgTree_ul"), setting);

        });
        function addOrg() {
            var nodes = orgTree.getSelectedNodes();
            if (nodes && nodes.length > 1) {
                alert("只能选择一个节点。");
                return;
            }
            if (nodes.length == 1) {
                var parentOrgId = nodes[0].id;
                var parentOrgName = nodes[0].text;
                $.loadDiv("org_form_div", "${ctx}/organization/add.do", {
                    parentOrgId: parentOrgId,
                    parentOrgName: parentOrgName
                }, "post");
            }
            else {
                alert("请选择一个节点。");
            }
        }
        function editOrg() {
            var nodes = orgTree.getSelectedNodes();
            if (nodes && nodes.length > 1) {
                alert("只能选择一个节点。");
                return;
            }
            if (nodes.length == 1) {
                $.loadDiv("org_form_div", "${ctx}/resource/edit.do", {resourceId: nodes[0].resourceId}, "post");
            }
            else {
                alert("请选择一个节点。");
            }
        }
        function delOrg() {
            var nodes = orgTree.getSelectedNodes();
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
                    var url = "${ctx}/organization/delete.do?orgId=" + nodes[0].id;
                    $.postAjax(url, null, null, function (data) {
                        var result = jQuery.parseJSON(data);
                        if (result.status == '1') {
                            alert("删除成功");
                            orgTree.removeNode(nodes[0]);
                            $("#org_form_div").html("");
                        }
                    });
                }
            }
            else {
                alert("请选择一个节点。");
            }
        }
        function refreshOrgNode(org) {
            if (org) {
                var pNnode = orgTree.getNodeByParam("id", org.orgId, null);
                if (!pNnode) {
                    pNnode = orgTree.getNodeByParam("id", org.parentOrgId, null);
                    pNnode.isParent = true;
                } else {
                    pNnode.text = org.orgName;
                }
                if (pNnode) {
                    orgTree.updateNode(pNnode);
                    orgTree.reAsyncChildNodes(pNnode, "refresh");
                    var newNode = orgTree.getNodeByParam("id", org.orgId, null);
                    if (newNode) {
                        orgTree.checkNode(newNode, true);
                    }

                }

            }
        }


    </script>
    <style type="text/css">
        .ui-menu {
            width: 80px;
        }

    </style>
</head>
<body>
<table cellspacing="5" cellpadding="5" width="100%" border="0">
    <tr>
        <td width="20%">
            <div>

                <div style="margin:5px 0;overflow:auto;height:550px;width:100%;background:#fff;position:">
                    <ul id="orgTree_ul" class="ztree"></ul>
                </div>
            </div>
        </td>
        <td width="60%">
            <div class="ui-widget-content">

                <div id="org_form_div" style="margin:5px 0;overflow:auto;height:550px;width:100%;background:#fff;">

                </div>
            </div>
        </td>
    </tr>
</table>
<ul id="orgMenu" class="context_menu">
    <li id="mAddMenu" onclick="addOrg()"><span class="ui-icon ui-icon-plus"></span><span>添加</span></li>
    <li id="mEditMenu" onclick="editOrg()"><span class="ui-icon ui-icon-pencil"></span>修改</li>
    <li id="mDelMenu" onclick="delOrg()"><span class="ui-icon ui-icon-trash"></span>删除</li>
</ul>
</body>
</html>
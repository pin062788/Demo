<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:include page="/common/common.jsp" />
    <script type="text/javascript">
        var currentParentNode;
        var setting = {
            view: {
                selectedMulti: false
            },
            edit: {
                enable: true,
                showRemoveBtn: false,
                showRenameBtn: false
            },
            data: {
                keep: {
                    parent: true,
                    leaf: true
                },
                key: {
                    name: "organizationName"
                },
                simpleData: {
                    enable: true,
                    idKey: "organizationNumber",
                    pIdKey: "leadOrganizationId",
                    rootPId: null
                }
            },
            async: {
                enable: true,
                url: '${pageContext.request.contextPath}/organization/getOrganizationTree.do?',
                autoParam: ["organizationNumber"],
                dataFilter: filter
            },
            callback: {
                beforeClick: returnCode,
                onAsyncSuccess: orgSyncSuccess
            }
        };

        function filter(treeId, parentNode, childNodes) {
            if (!childNodes) return null;
            for (var i = 0, l = childNodes.length; i < l; i++) {
                if (childNodes[i].organizationName)
                    childNodes[i].organizationName = childNodes[i].organizationName.replace(/\.n/g, '.');
                if (childNodes[i].leadOrganizationId)
                    childNodes[i].leadOrganizationId = $.trim(childNodes[i].leadOrganizationId);//childNodes[i].leadOrganizationId.trim();
            }
            return childNodes;
        }


        $(document).ready(function () {

            $("#closeBtnTree").button({
                icons: {primary: "ui-icon-close"}
            }).click(function () {
                $("#orgDialog").hide();
            });
            $.fn.zTree.init($("#treeMenu"), setting);
        });

        function orgSyncSuccess() {
            var treeObj = $.fn.zTree.getZTreeObj("treeMenu");
            var node = treeObj.getNodeByParam("organizationNumber", $("#department").val());
            treeObj.selectNode(node, false);
        }
        function returnCode(treeId, treeNode) {

            if (treeNode.isParent) return;


            $("#departmentName").val(treeNode.organizationName);
            $("#department").val(treeNode.organizationNumber);

            /* $("#departmentName").attr("value",treeNode.organizationName);
             $("#department").attr("value",treeNode.organizationNumber); */
            $("#orgDialog").hide();
            if (typeof afterSelectOrg != 'undefined' && afterSelectOrg instanceof Function) {
                afterSelectOrg();
            }
        }


    </script>
</head>
<body>

<div style="display: block;overflow:auto;width:200px; height:250px; ">
    <div class="right">
        <button id="closeBtnTree">关闭</button>
    </div>
    <ul id="treeMenu" class="ztree"></ul>
</div>

</body>
</html>
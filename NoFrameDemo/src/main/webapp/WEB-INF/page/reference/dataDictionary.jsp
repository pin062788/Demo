<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
                    name: "name"
                },
                simpleData: {
                    enable: true,
                    idKey: "id",
                    pIdKey: "pId",
                    rootPId: null
                }
            },
            async: {
                enable: true,
                url: '${pageContext.request.contextPath}/reference/showTree.do?',
                autoParam: ["id"],
                dataFilter: filter
            },
            callback: {
                beforeClick: openDetail
            }
        };

        function filter(treeId, parentNode, childNodes) {
            if (!childNodes) return null;
            for (var i = 0, l = childNodes.length; i < l; i++) {
                if (childNodes[i].name)
                    childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
            }
            return childNodes;
        }

        function getTime() {
            var now = new Date(),
                    h = now.getHours(),
                    m = now.getMinutes(),
                    s = now.getSeconds(),
                    ms = now.getMilliseconds();
            return (h + ":" + m + ":" + s + " " + ms);
        }

        var newCount = 1;
        function add(e) {
            showLoading();
            var zTree = $.fn.zTree.getZTreeObj("referenceTreeMenu"),
                    isParent = e.data.isParent,
                    nodes = zTree.getSelectedNodes(),
                    treeNode = nodes[0];
            if (nodes.length == 0) {
                alert("请先选择一个节点");
                hideLoading();
                return;
            }
            var ifParent = isParent ? "1" : "0";
            var parentNode = treeNode.isParent;
            var getTimestamp = new Date().getTime(); //时间戳
            if (parentNode) {
                currentParentNode = treeNode.tId;
                $("#subPage").dialog("open").load("${pageContext.request.contextPath}/reference/addNode.do?parentId=" + treeNode.id + "&isParent=" + ifParent + "&__timer__=" + getTimestamp);
                hideLoading();
            }
            else {
                currentParentNode = treeNode.parentTId;
                $("#subPage").dialog("open").load("${pageContext.request.contextPath}/reference/addNode.do?parentId=" + treeNode.pId + "&isParent=" + ifParent + "&__timer__=" + getTimestamp);
                hideLoading();
            }

        }
        ;
        function edit() {
            showLoading();
            var zTree = $.fn.zTree.getZTreeObj("referenceTreeMenu"),
                    nodes = zTree.getSelectedNodes(),
                    treeNode = nodes[0];
            var getTimestamp = new Date().getTime(); //时间戳
            if (nodes.length == 0) {
                alert("请先选择一个节点");
                hideLoading();
                return;
            }
            currentParentNode = nodes[0].parentTId;
            $("#subPage").dialog("open").load("${pageContext.request.contextPath}/reference/edit.do?id=" + nodes[0].id + "&__timer__=" + getTimestamp);
            hideLoading();
        }
        ;
        function remove(e) {
            var zTree = $.fn.zTree.getZTreeObj("referenceTreeMenu"),
                    nodes = zTree.getSelectedNodes(),
                    treeNode = nodes[0];
            if (nodes.length == 0) {
                alert("请先选择一个节点");
                hideLoading();
                return;
            }

            currentParentNode = nodes[0].parentTId;
            /* $.post("
            ${pageContext.request.contextPath}/reference/delete.do?",{id:nodes[0].id},function(data){
             var node = $.fn.zTree.getZTreeObj("referenceTreeMenu").getNodeByTId(currentParentNode);
             zTree.reAsyncChildNodes(node,"refresh");
             });*/
            var getTimestamp = new Date().getTime();
            var url = "${pageContext.request.contextPath}/reference/delete.do?_timer_=" + getTimestamp;
            $.post(url, {id: nodes[0].id}, function (data) {
                var node = $.fn.zTree.getZTreeObj("referenceTreeMenu").getNodeByTId(currentParentNode);
                zTree.reAsyncChildNodes(node, "refresh");
            });
        }
        ;

        $(document).ready(function () {
            $("#addParent").bind("click", {isParent: true}, add);
            $("#addLeaf").bind("click", {isParent: false}, add);
            $("#edit").bind("click", edit);
            $("#remove").bind("click", remove);
            $.fn.zTree.init($("#referenceTreeMenu"), setting);
        });

        function openDetail(treeId, treeNode) {

            //$.post("${pageContext.request.contextPath}/reference/detail.do?",{id:treeNode.id})
            var getTimestamp = new Date().getTime();
            /*$("#referenceDetail").jqGrid('setGridParam',{
             url: "
            ${pageContext.request.contextPath}/reference/detail.do?id=" + treeNode.id+"&_timer_="+getTimestamp ,
             mytype: 'GET',
             page:1
             }).trigger("reloadGrid");*/
            $("#reference_form_div").load("${pageContext.request.contextPath}/reference/showDetail.do?id=" + treeNode.id + "&_timer_=" + getTimestamp);
        }
        function clearCache() {
            /*$.post("
            ${pageContext.request.contextPath}/reference/clearCache.do",function(result){
             if(result == 200)
             alert("已清除缓存");
             });*/
            var getTimestamp = new Date().getTime();
            var url = "${pageContext.request.contextPath}/reference/clearCache.do?_timer_=" + getTimestamp;
            $.post(url, null, function (result) {
                if (result == 200)
                    alert("已清除缓存");
            });
        }
    </script>
</head>
<body>

<table cellspacing="5" cellpadding="5" width="100%" border="0">
    <tr>
        <td width="40%">
            <div class="ui-widget-content">
                <div class="ui-widget-header ui-state-default"><span class="ui-widget">所有资源</span></div>
                <div style="margin:5px 0;overflow:auto;height:550px;width:100%;background:#fff;position:">
                    <div class="right">
                        [ <a id="addParent" href="#" title="增加父节点" onclick="return false;">增加父节点</a> ]
                        [ <a id="edit" href="#" title="编辑名称" onclick="return false;">编辑节点</a> ]<br/>
                        [ <a id="addLeaf" href="#" title="增加叶子节点" onclick="return false;">增加子节点</a> ]
                        [ <a id="remove" href="#" title="删除节点" onclick="return false;">删除节点</a> ]<br/>
                        [ <a id="clear" href="#" title="清除缓存" onclick="clearCache()">清除缓存</a> ]
                    </div>
                    <ul id="referenceTreeMenu" class="ztree"></ul>
                </div>
            </div>
            </div>
        </td>
        <td width="60%">
            <div class="ui-widget-content">
                <div class="ui-widget-header">节点明细</div>
                <div id="reference_form_div"
                     style="margin:5px 0;overflow:auto;height:550px;width:100%;background:#fff;">

                </div>
            </div>
        </td>
    </tr>
</table>
</body>
</html>
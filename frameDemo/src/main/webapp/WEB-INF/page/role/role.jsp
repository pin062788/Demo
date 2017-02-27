<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:include page="/common/common.jsp" />
    <script type="text/javascript">

        function deleteRoles() {
            var rowIds = $("#roleList").jqGrid('getGridParam', 'selarrrow');
            var count = rowIds.length;
            if (count == 0)
                alert("请选择一行数据");
            else {
                var r = confirm("是否确认删除，删除后相应用户的权限也将被删除，且无法恢复？");
                if (r == true) {
                    var roleIds = new Array(rowIds.length);
                    for (var i = 0; i < rowIds.length; i++) {
                        var rowData = $("#roleList").getRowData(rowIds[i]);
                        roleIds[i] = rowData.roleId;
                    }
                    $.ajax({
                                type: "POST",
                                url: "${pageContext.request.contextPath}/role/delete/" + roleIds + ".do?",
                                dataType: "json"
                            })
                            .done(function (data) {
                                if (data.status == 1) {
                                    alert('删除成功');
                                    $("#roleList").jqGrid().trigger("reloadGrid");
                                } else {
                                    alert(data.message);
                                }
                            })
                            .fail(function () {
                                alert('删除失败！');
                            })
                    ;
                }
            }
        }

        function openResourcesTree() {
            var row = selectRows("roleList", false, false);
            if (row) {
                $.ajax({
                    type: "POST",
                    url: "${pageContext.request.contextPath}/resource/openResourceTree/" + row[0].roleId + ".do?",
                    dataType: "html",
                    success: function (data) {
                        $("#resourceDialog").html(data).dialog("open");
                    }
                }).fail(function () {
                    alert("加载失败");
                });
            }
        }

        function showDataConfig() {
            var row = selectRows("roleList", false, false);
            if (row) {
                var url = "${ctx}/role/showDataConfig.do?roleId="+row[0].roleId;
                $.post(_url,"",function(data){
                    $("#dataConfigDialog").dialog("open").html(data);
                }).fail(function(){
                    alert("加载失败!");
                });
                /*$.loadDiv("dataConfigDialog", url, {roleId: row[0].roleId});
                $("#dataConfigDialog").dialog("open").dialog("option", "title", "数据权限设置");*/
            }

        }

        $(document).ready(function () {
            $("#resourceDialog").dialog({
                title: "角色管理 ->权限设置",
                autoOpen: false,
                pisition: {my: "center", at: "center", of: window},
                width: "40%",
                height: 400
            });

            $("#roleDialog").dialog({
                title:"权限管理",
                autoOpen: false,
                position: {my: "top", at: "top+50", of: window},
                modal:true,
                resizable: true,
                width:"800",
                height:250
            });
            $("#dataConfigDialog").dialog({
                title:"数据权限设置",
                autoOpen: false,
                position: {my: "top", at: "top+50", of: window},
                modal:true,
                resizable: true,
                width:"800",
                height:500
            });

            $("#searchBtn").button({//searchBtn
                icons: {primary: "ui-icon-search"}
            })
            .click(function () {
                var _number = encodeURI($("#roleCode").val());
                var _name = encodeURI($("#roleName").val());
                $("#roleList").jqGrid('setGridParam', {
                    url: "${pageContext.request.contextPath}/role/getList.do?roleCode=" + _number + "&roleName=" + _name
                }).trigger("reloadGrid");

            });
            $("#addBtn").click(function () {
                var url = "${pageContext.request.contextPath}/role/add.do";
                $.post(url,"",function(data){
                    $("#roleDialog").dialog("open").html(data);
                }).fail(function(){
                    alert("加载失败!");
                });
            })
            .button({
                icons: {primary: "ui-icon-plus"}
            });
            $("#editBtn").button({
                icons: {primary: "ui-icon-pencil"}
            })//editBtn
            .click(function () {
                var row = selectRows("roleList", false, false);
                if (row) {
                    var url = "${pageContext.request.contextPath}/role/edit.do?roleId=" + row[0].roleId;
                    $.post(url,"",function(data){
                        $("#roleDialog").dialog("open").html(data);
                    }).fail(function(){
                        alert("加载失败!");
                    });
                }
            });
            $("#deleteBtn") //deleteBtn
            .button({
                icons: {primary: "ui-icon-trash"}
            })
            .click(function () {
                deleteRoles();
            });
            $("#setResourceBtn") //setResourceBtn
            .button({
                icons: {primary: "ui-icon-gear"}
            })
            .click(function () {
                openResourcesTree();
            });
            $("#setDataFilterBtn")
            .button({
                icons: {primary: "ui-icon-gear"}
            })
            .click(function () {
                showDataConfig();
            });  //.parent().buttonset()
            $("#roleList").jqGrid({
                url: "${pageContext.request.contextPath}/role/getList.do",
                datatype: "json",
                mytype: 'POST',
                multiselect: true,
                colNames: [
                    'ID',
                    '角色编码',
                    '角色名称',
                    '角色描述'
                ],
                colModel: [
                    {name: "roleId", hidden: true},
                    {name: "roleCode"},
                    {name: "roleName"},
                    {name: "remark"}
                ],
                rowNum: 10,
                page: 1,
                total: 15,
                rowList: [10, 20, 30],
                pager: "#rolePager",
                viewrecords: true,
                height: "auto",
                autowidth: true,
                gridview: true,
                autoencode: true,
                subGrid: false
            }).navGrid("#rolePager", {
                refresh: false,
                edit: false,
                add: false,
                del: false,
                search: false
            });
        });
    </script>
</head>
<body id="loading">
<div class="ui-widget-content">
    <div id="toolbar" class="ui-widget-header ui-state-default">
        <div id="btns">
            <shiro:hasPermission name="role:view">
                <button id="searchBtn">查询</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="role:add">
                <button id="addBtn">增加</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="role:edit">
                <button id="editBtn">修改</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="role:delete">
                <button id="deleteBtn">删除</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="role:addOperateFilter">
                <button id="setResourceBtn">操作权限设置</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="role:addDataFilter">
                <button id="setDataFilterBtn">数据权限设置</button>
            </shiro:hasPermission>
        </div>
    </div>
    <div id="criterion">
        <table cellspacing="5" cellpadding="5" border="0">
            <tr>
                <td>角色编码：</td>
                <td><input type="text" id="roleCode"/></td>
                <td>角色名称：</td>
                <td><input type="text" id="roleName"/></td>
            </tr>
        </table>
    </div>
    <table id="roleList"></table>
    <div id="rolePager"></div>
    <div id="roleDialog"></div>
    <div id="resourceDialog"></div>
    <div id="dataConfigDialog"></div>
</div>
</body>
</html>
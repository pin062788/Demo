<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:include page="/common/common.jsp" />
    <script type="text/javascript">
        $(document).ready(function () {
            $("#findUserGroupBtn").button({
                icons: {primary: "ui-icon-search"}
            }).click(function () {
                var userGroupName = encodeURI($("#userGroup_name").val());
                $("#userGroups").jqGrid('setGridParam', {
                    url: "${pageContext.request.contextPath}/userGroup/getList.do?groupName=" + userGroupName,
                    mytype: 'GET',
                    page: 1
                }).trigger("reloadGrid");//重新载入
            });
            $("#addUserGroupBtn").button({
                icons: {primary: "ui-icon-plus"}
            }).click(function () {
                var getTimestamp = new Date().getTime();  //时间戳
                var _url = "${pageContext.request.contextPath}/userGroup/add.do?userGroupId=0&_timer_=" + getTimestamp;
                $.post(_url,"",function(data){
                    $("#detailDialog").dialog("open").html(data);
                }).fail(function(){
                    alert("加载失败!");
                });
            });
            $("#modifyUserGroupBtn").button({
                icons: {primary: "ui-icon-pencil"}
            }).click(function () {
                var rowData = jQuery('#userGroups').jqGrid('getGridParam', 'selarrrow');
                if (rowData.length != 1) {
                    alert("请选择一条记录");
                    $("#userGroups").jqGrid().trigger("reloadGrid");
                    hideLoading();
                    return;
                }
                var groupId = $('#userGroups').jqGrid('getCell', rowData[0], 'groupId');
                var getTimestamp = new Date().getTime();  //时间戳
                var _url = "${pageContext.request.contextPath}/userGroup/edit.do?userGroupId=" + groupId + "&_timer_=" + getTimestamp;
                $.post(_url,"",function(data){
                    $("#detailDialog").dialog("open").html(data);
                }).fail(function(){
                    alert("加载失败!");
                });
            });
            $("#delUserGroupBtn").button({
                icons: {primary: "ui-icon-trash"}
            }).click(function () {
                var rowData = jQuery('#userGroups').jqGrid('getGridParam',
                        'selarrrow');
                var groupIds = new Array();
                if (rowData.length > 0) {
                    if (confirm("确认删除数据吗？")) {
                        var groupId;
                        for (var i = 0; i < rowData.length; i++) {
                            groupId = $('#userGroups').jqGrid('getCell', rowData[i], 'groupId');
                            groupIds.push(groupId);
                        }
                        var getTimestamp = new Date().getTime();
                        var url = "${pageContext.request.contextPath}/userGroup/deleteGroups.do?groupIds="
                                + groupIds + "&_timer_=" + getTimestamp;
                        $.postAjax (url, null, null, function (data) {
                            var result = jQuery.parseJSON(data);
                            if (result.status == '1') {
                                alert("删除成功");
                                $("#userGroups").jqGrid().trigger("reloadGrid");
                            } else {
                                alert(result.message);
                            }
                        });
                    }
                } else {
                    alert("请选择要删除的记录");
                    return;
                }
            }).attr('enabled', 'true');
            var getTimestamp = new Date().getTime();
            $("#userGroups").jqGrid({
                url: "${pageContext.request.contextPath}/userGroup/getList.do?_timer_=" + getTimestamp,
                datatype: "json",
                mytype: 'GET',
                multiselect: false,
                colNames: ["ID", "用户组", "角色", "用户"],
                colModel: [
                    {name: "groupId", hidden: true},
                    {name: "groupName", align: 'center'},
                    {name: "roleNames", align: 'center', sortable: false},
                    {name: "userNames", align: 'center', sortable: false},
                ],
                rowNum: 10,
                page: 1,
                total: 15,
                rowList: [10, 20, 30],
                pager: "#userGroupsPager",
                viewrecords: true,
                height: "auto",
                loadComplete: function() {
                    var grid = $("#userGroups");
                    var ids = grid.getDataIDs();
                    for (var i = 0; i < ids.length; i++) {
                        grid.setRowData ( ids[i], false, {height: 35+i*2} );
                    }
                },
                caption:"用户组管理",
                autowidth: true,
                gridview: true,
                autoencode: true,
                multiselect: true
            }).navGrid("#userGroupsPager", {
                refresh: false,
                edit: false,
                add: false,
                del: false,
                search: false
            });

            $("#detailDialog").dialog({
                title:"用户组管理",
                autoOpen: false,
                position: {my: "top", at: "top+50", of: window},
                modal:true,
                resizable: true,
                width:"800",
                height:450
            });
        });
    </script>
</head>
<body id="loading">
<div id="query" class="ui-widget-content">
    <div id="toolbar" class="ui-widget-header ui-state-default">
        <div>
            <shiro:hasPermission name="usergroup:add">
                <button id="addUserGroupBtn">新建</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="usergroup:edit">
                <button id="modifyUserGroupBtn">更新</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="usergroup:delete">
                <button id="delUserGroupBtn">删除</button>
            </shiro:hasPermission>
        </div>
    </div>
    <div id="criterion">
        <form id="userGroupQueryForm">
            <table >
                <tr><td >
                    <div class="criteria_float_div">
                    <ul>
                        <li ><label>用户组名：</label><input type="text" id="userGroup_name"></li>
                        <li class="queryButton">
                            <button id="findUserGroupBtn">查询</button>
                        </li>
                    </ul>
                    </div>
                </td></tr>
            </table>
        </form>
    </div>
    <table id="userGroups"></table>
    <div id="userGroupsPager"></div>
    <div id="detailDialog"></div>
</div>
</body>
</html>
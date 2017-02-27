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
            $("#detailDialog").dialog({
                title:"用户管理",
                autoOpen: false,
                position: {my: "top", at: "top+10", of: window},
                modal:true,
                resizable: true,
                width:"800",
                height:550
            });

            $("#findUserBtn").button({
                icons: {primary: "ui-icon-search"}
            }).click(function () {
                var userName = encodeURI($("#user_name").val());
                var roleCode = encodeURI($("#roleCode").val());
                var rName = encodeURI($("#rName").val());

                $("#userManagement").jqGrid('setGridParam', {
                    url: "${pageContext.request.contextPath}/user/getList.do?userName=" + userName + "&roleCode=" + roleCode + "&rName=" + rName,
                    mytype: 'GET',
                    page: 1
                }).trigger("reloadGrid");//重新载入

            });
            $("#addUserBtn").button({
                icons: {primary: "ui-icon-plus"}
            }).click(function () {
                var getTimestamp = new Date().getTime();  //时间戳
                var _url = "${pageContext.request.contextPath}/user/add.do?userId=0&_timer_=" + getTimestamp;
                $.post(_url,"",function(data){
                    $("#detailDialog").dialog("open").html(data);
                }).fail(function(){
                    alert("加载失败!");
                });
            });
            $("#modifyUserBtn").button({
                icons: {primary: "ui-icon-pencil"}
            }).click(function () {
                var rowData = $('#userManagement').jqGrid('getGridParam', 'selarrrow');
                if (rowData.length != 1) {
                    alert("请选择一条记录");
                    $("#userManagement").jqGrid().trigger("reloadGrid");
                    return;
                }
                var userId = $('#userManagement').jqGrid('getCell', rowData[0], 'userId');
                var _url = "${pageContext.request.contextPath}/user/edit.do?userId=" + userId;
                $.post(_url,"",function(data){
                    $("#detailDialog").dialog("open").html(data);
                }).fail(function(){
                    alert("加载失败!");
                });
            });
            $("#delUserBtn").button({
                icons: {primary: "ui-icon-trash"}
            }).click(function () {
                rowData = jQuery('#userManagement').jqGrid('getGridParam', 'selarrrow');
                var userIds = new Array();
                if (rowData.length > 0) {
                    if (confirm("确认删除数据吗？")) {
                        for (var i = 0; i < rowData.length; i++) {
                            userId = jQuery('#userManagement').jqGrid('getCell',
                                    rowData[i], 'userId');//name是colModel中的一属性
                            userIds.push(userId);
                        }
                        var url = "${pageContext.request.contextPath}/user/del.do?userIds=" + userIds;
                        $.postAjax(url, null, null, function (result) {
                            if (result == '1') {
                                alert("删除成功");
                                closeAndReload("", "userManagement");
                            }
                        });
                    }
                } else {
                    alert("请选择要删除的记录");
                    return;
                }
            });
            $("#userManagement").jqGrid({
                url: "${pageContext.request.contextPath}/user/getList.do",
                datatype: "json",
                mytype: 'GET',
                multiselect: false,
                colNames: ["ID", "用户名", "名称", "类型", "角色", "所属用户组"],
                colModel: [
                    {name: "userId", hidden: true},
                    {name: "userName", align: 'center'},
                    {name: "relatedName", align: 'center'},
                    {
                        name: "relatedType", align: 'center', formatter: function (cellvalue) {
                        return getReferenceDesc(jQuery.parseJSON('${relatedTypes}'), cellvalue);
                    }
                    },
                    {name: "roleNames", align: 'center', sortable: false},
                    {name: "groupNames", align: 'center', sortable: false},
                ],
                rowNum: 10,
                page: 1,
                total: 15,
                rowList: [10, 20, 30],
                pager: "#userPager",
                viewrecords: true,
                height: "auto",
                //width: "auto",
                autowidth: true,
                gridview: true,
                autoencode: true,
                multiselect: true,
                multiboxonly: true
            }).navGrid("#userPager", {
                refresh: false,
                edit: false,
                add: false,
                del: false,
                search: false
            });

        });
        function afterSave() {
            $("#detailDialog").dialog("close");
            $("#userManagement").jqGrid().trigger("reloadGrid");
        }
    </script>
</head>
<body id="loading">
<div id="query" class="ui-widget-content">
    <div id="toolbar" class="ui-widget-header ui-state-default">
        <div>
            <shiro:hasPermission name="user:view">
                <button id="findUserBtn">查询</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="user:add">
                <button id="addUserBtn">增加</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="user:edit">
                <button id="modifyUserBtn">修改</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="user:delete">
                <button id="delUserBtn">删除</button>
            </shiro:hasPermission>
        </div>
    </div>
    <div id="criterion">
        <form:form commandName="role">
            <table cellspacing="5" cellpadding="5">
                <tr>
                    <td align="right"><label>用户名：</label></td>
                    <td><input type="text" id="user_name"></td>
                    <td align="right"><label>名称：</label></td>
                    <td><input type="text" id="rName" name="rName"></td>
                </tr>
            </table>
        </form:form>
    </div>
    <table id="userManagement"></table>
    <div id="userPager"></div>
    <div id="detailDialog"></div>
</div>
</body>
</html>
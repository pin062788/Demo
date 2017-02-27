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
            $("#findRestrictTableBtn").button({
                icons: {primary: "ui-icon-search"}
            }).click(function () {
                var tableName = encodeURI($("#table_name").val());
                $("#tableRestricts").jqGrid('setGridParam', {
                    url: "${pageContext.request.contextPath}/restrictTable/getList.do?tableName=" + tableName,
                    mytype: 'GET',
                    page: 1
                }).trigger("reloadGrid");//重新载入

            });
            $("#addRestrictTableBtn").button({
                icons: {primary: "ui-icon-plus"}
            }).click(function () {
                var _url = "${pageContext.request.contextPath}/restrictTable/add.do?tableId=0";
                $.post(_url,"",function(data){
                    $("#tableDetailDialog").dialog("open").html(data);
                }).fail(function(){
                    alert("加载失败!");
                });
            });
            $("#modifyRestrictTableBtn").button({
                icons: {primary: "ui-icon-pencil"}
            }).click(function () {
                var rowData = jQuery('#tableRestricts').jqGrid('getGridParam', 'selrow');
                if (rowData.length != 1) {
                    alert("请选择一条记录");
                    $("#tableRestricts").jqGrid().trigger("reloadGrid");
                    return;
                }
                var tableId = $('#tableRestricts').jqGrid('getCell', rowData[0], 'tableId');
                var getTimestamp = new Date().getTime();  //时间戳
                var _url = "${pageContext.request.contextPath}/restrictTable/edit.do?tableId=" + tableId + "&_timer_=" + getTimestamp;
                $.post(_url,"",function(data){
                    $("#tableDetailDialog").dialog("open").html(data);
                }).fail(function(){
                    alert("加载失败!");
                });
            });
            $("#delRestrictTableBtn").button({
                icons: {primary: "ui-icon-trash"}
            }).click(function () {
                var rowData = jQuery('#tableRestricts').jqGrid('getGridParam',
                        'selrow');
                var tableIds = new Array();
                if (rowData.length > 0) {
                    if (confirm("确认删除数据吗？")) {
                        var tableId;
                        for (var i = 0; i < rowData.length; i++) {
                            tableId = $('#tableRestricts').jqGrid('getCell', rowData[i], 'tableId');
                            tableIds.push(tableId);
                        }
                        var getTimestamp = new Date().getTime();
                        var url = "${pageContext.request.contextPath}/restrictTable/delete.do?tableIds="
                                + tableIds + "&_timer_=" + getTimestamp;
                        $.postAjax (url, null, null, function (data) {
                            var result = jQuery.parseJSON(data);
                            if (result.status == '1') {
                                alert("删除成功");
                                closeAndReload("", "tableRestricts");
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
            $("#tableRestricts").jqGrid({
                url: "${pageContext.request.contextPath}/restrictTable/getList.do?_timer_=" + getTimestamp,
                datatype: "json",
                mytype: 'GET',
                multiselect: false,
                colNames: ["ID", "表名", "描述"],
                colModel: [
                    {name: "tableId", hidden: true},
                    {name: "tableName", align: 'center'},
                    {name: "tableDesc", align: 'center'}
                ],
                rowNum: 10,
                page: 1,
                total: 15,
                rowList: [10, 20, 30],
                pager: "#tablePager",
                viewrecords: true,
                height: "auto",
                //width: "auto",
                autowidth: true,
                gridview: true,
                onSelectRow: loadColumns,
                autoencode: true,
                multiselect: false
            }).navGrid("#tablePager", {
                refresh: false,
                edit: false,
                add: false,
                del: false,
                search: false
            });

            $("#tableDetailDialog").dialog({
                title:"过滤表管理",
                autoOpen: false,
                position: {my: "top", at: "top+50", of: window},
                modal:true,
                resizable: true,
                width:"700",
                height:150
            });
        });
        function loadColumns() {
            var rowData = jQuery('#tableRestricts').jqGrid('getGridParam', 'selrow');
            if (rowData) {
                var tableId = $('#tableRestricts').jqGrid('getCell', rowData[0], 'tableId');
                $("#columnDIV").load("${pageContext.request.contextPath}/restrictColumn/list.do?tableId=" + tableId);
            }
        }
        function afterSave() {
            $("#tableDetailDialog").dialog("close");
            $("#tableRestricts").jqGrid().trigger("reloadGrid");
        }
    </script>
</head>
<body id="loading">
<div id="query" class="ui-widget-content">
    <div id="toolbar" class="ui-widget-header ui-state-default">
        <div>
            <shiro:hasPermission name="restrictTable:view">
                <button id="findRestrictTableBtn">查询</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="restrictTable:add">
                <button id="addRestrictTableBtn">增加</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="restrictTable:edit">
                <button id="modifyRestrictTableBtn">修改</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="restrictTable:delete">
                <button id="delRestrictTableBtn">删除</button>
            </shiro:hasPermission>
        </div>
    </div>
    <div id="criterion">
        <form:form commandName="tableRestrict">
            <table cellspacing="5" cellpadding="5">
                <tr>
                    <td align="right"><label>表名：</label></td>
                    <td><input type="text" id="table_name"></td>
                </tr>
            </table>
        </form:form>
    </div>
    <table id="tableRestricts"></table>
    <div id="tablePager"></div>
    <div id="tableDetailDialog"></div>
</div>
</body>
</html>
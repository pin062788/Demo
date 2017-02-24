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
            $("#addRestrictColumnBtn").button({
                icons: {primary: "ui-icon-plus"}
            }).click(function () {
                var _url = "${pageContext.request.contextPath}/restrictColumn/add.do?tableId=${tableId}";
                $.post(_url,"",function(data){
                    $("#columnDetailDialog").dialog("open").html(data);
                }).fail(function(){
                    alert("加载失败!");
                });
            });
            $("#modifyRestrictColumnBtn").button({
                icons: {primary: "ui-icon-pencil"}
            }).click(function () {
                var rowData = jQuery('#restrictColumns').jqGrid('getGridParam', 'selarrrow');
                if (rowData.length != 1) {
                    alert("请选择一条记录");
                    $("#restrictColumns").jqGrid().trigger("reloadGrid");
                    return;
                }
                var columnId = $('#restrictColumns').jqGrid('getCell', rowData[0], 'columnId');
                var _url = "${pageContext.request.contextPath}/restrictColumn/edit.do?columnId=" + columnId + "&_timer_=" + getTimestamp;
                $.post(_url,"",function(data){
                    $("#columnDetailDialog").dialog("open").html(data);
                }).fail(function(){
                    alert("加载失败!");
                });
            });
            $("#delRestrictColumnBtn").button({
                icons: {primary: "ui-icon-trash"}
            }).click(function () {
                var rowData = jQuery('#restrictColumns').jqGrid('getGridParam',
                        'selarrrow');
                var columnIds = new Array();
                if (rowData.length > 0) {
                    if (confirm("确认删除数据吗？")) {
                        var columnId;
                        for (var i = 0; i < rowData.length; i++) {
                            columnId = $('#restrictColumns').jqGrid('getCell', rowData[i], 'columnId');
                            columnIds.push(columnId);
                        }
                        var getTimestamp = new Date().getTime();
                        var url = "${pageContext.request.contextPath}/restrictColumn/delete.do?columnIds="
                                + columnIds + "&_timer_=" + getTimestamp;
                        $.postAjax (url, null, null, function (data) {
                            var result = jQuery.parseJSON(data);
                            if (result.status == '1') {
                                alert("删除成功");
                                $("#restrictColumns").jqGrid().trigger("reloadGrid");
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
            $("#restrictColumns").jqGrid({
                url: "${pageContext.request.contextPath}/restrictColumn/getList.do?tableId=${tableId}&_timer_=" + getTimestamp,
                datatype: "json",
                mytype: 'GET',
                multiselect: false,
                colNames: ["ID", "字段名", "字段描述", "字段类型", "数据源"],
                colModel: [
                    {name: "columnId", hidden: true},
                    {name: "columnName", align: 'center'},
                    {name: "columnDesc", align: 'center'},
                    {name: "columnType", align: 'center'},
                    {name: "datasouce", align: 'center'}
                ],
                rowNum: 10,
                page: 1,
                total: 15,
                rowList: [10, 20, 30],
                pager: "#restrictColumnsPager",
                viewrecords: true,
                height: "auto",
                //width: "auto",
                autowidth: true,
                gridview: true,
                autoencode: true,
                multiselect: true
            }).navGrid("#restrictColumnsPager", {
                refresh: false,
                edit: false,
                add: false,
                del: false,
                search: false
            });

            $("#columnDetailDialog").dialog({
                title:"过滤字段",
                autoOpen: false,
                position: {my: "top", at: "top+50", of: window},
                modal:true,
                resizable: true,
                width:"700",
                height:350
            });
        });
        function afterSave() {
            $("#columnDetailDialog").dialog("close");
            $("#restrictColumns").jqGrid().trigger("reloadGrid");
        }
    </script>
</head>
<body>
<div id="query" class="ui-widget">
    <div id="toolbar" class="ui-widget-header ui-state-default">
        <div>
            <button id="addRestrictColumnBtn">增加</button>
            <shiro:hasPermission name="restrictColumn:add">
            </shiro:hasPermission>
            <shiro:hasPermission name="restrictColumn:edit">
                <button id="modifyRestrictColumnBtn">修改</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="restrictColumn:delete">
                <button id="delRestrictColumnBtn">删除</button>
            </shiro:hasPermission>
        </div>
    </div>
    <table id="restrictColumns"></table>
    <div id="restrictColumnsPager"></div>
    <div id="columnDetailDialog"></div>
</div>
</body>
</html>
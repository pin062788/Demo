<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:include page="/common/common.jsp" />
    <script type="text/javascript">
        $(document).ready(function () {
            $("#logFileListSearchBtn").button({
                icons: {primary: "ui-icon-search"}
            })
            .click(function () {
                $("#logFileListGrid").jqGrid('setGridParam').trigger("reloadGrid");
            });
            $("#logFileListDownloadBtn").button({
                icons: {primary: "ui-icon-document-b"}
            })
            .click(function () {
                var rowId = $("#logFileListGrid").jqGrid('getGridParam', 'selrow');
                if (rowId && rowId != '') {
                    var rowData = $("#logFileListGrid").jqGrid('getRowData', rowId);
                    var fileName = encodeURI(rowData.fileName);
                    var filePath = encodeURI(rowData.filePath);
                    window.location.href = "${ctx}/log/downloadLogFile.do?fileName=" + fileName + "&filePath=" + filePath;
                } else {
                    alert('请选择一条记录');
                }
            });

            $("#logFileListGrid").jqGrid({
                url: "${ctx}/log/getLogFileList.do",
                datatype: "json",
                mytype: 'GET',
                colNames: ['文件名', '文件路径'],
                colModel: [
                    {name: "fileName", sortable: false},
                    {name: "filePath", hidden: true}
                ],
                rownumbers: true,
                rowNum: 10,
                page: 1,
                total: 15,
                rowList: [10, 20, 30],
                pager: "#logFileListGridPager",
                viewrecords: true,
                height: "auto",
                autowidth: true,
                gridview: true,
                autoencode: true,
                caption: "系统日志"
            }).navGrid("#logFileListGridPager", {
                refresh: true,
                edit: false,
                add: false,
                del: false,
                search: false
            });
        });
    </script>
</head>
<body id="loading">
<div class="ui-widget">
    <div id="logFileListToolbar" class="ui-widget-header ui-state-default">
        <button id="logFileListSearchBtn">查询</button>
        <shiro:hasPermission name="logFileList:download">
            <button id="logFileListDownloadBtn">下载</button>
        </shiro:hasPermission>
    </div>
    <div>
        <div id="criterion">
            <table cellspacing="5" cellpadding="5" border="0">
                <tr>
                    <td colspan="6">
                        <div id="logFileListErrormsg" class="errorCls" style="text-align: center;"></div>
                    </td>
                </tr>
            </table>
            <form id="logFileListDownloadForm" action="${ctx}/log/downloadLogFile.do"></form>
        </div>
    </div>
    <table id="logFileListGrid"></table>
    <div id="logFileListGridPager"></div>
</div>
</body>
</html>
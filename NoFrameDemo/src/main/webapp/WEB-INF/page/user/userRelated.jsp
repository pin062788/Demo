<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript">
        var relatedType;
        $(document).ready(function () {
            relatedType = "${relatedType}";
            $("#selectTypeBtn").button({
                        icons: {primary: "ui-icon-check"}
                    })
                    .click(function () {
                        selectItem();
                    });
            $("#closeTypeBtn").button({
                icons: {primary: "ui-icon-close"}
            });

            var _colModel;
            var _gridUrl = "${pageContext.request.contextPath}/user/getUserRelatedList.do?relatedType=${relatedType}";

            var _colNames = [
                '编码',
                '名称'];
            if ("customer" == relatedType) {
                _colModel = [
                    {name: "customerCode"},
                    {name: "customerName"}];
            }
            else if ("supplier" == relatedType) {
                _colModel = [
                    {name: "enterpriseCode"},
                    {name: "enterpriseName"}];
            }
            else if ("employee" == relatedType) {
                _colModel = [
                    {name: "employeeCode"},
                    {name: "name"}];
            }
            initGrid("userRelatedList", "userRelatedPager", _gridUrl, _colNames, _colModel, "用户类型");

            initOrgWidget("departmentName", "orgDialog");
        });

        function afterSelectOrg() {
            changeOrg();
        }

        function changeOrg() {
            var orgNum = $("#department").val();
            if (orgNum) {
                var _gridUrl = "${pageContext.request.contextPath}/user/getUserRelatedList.do?relatedType=${relatedType}&organizationNumber=" + orgNum;
                $("#userRelatedList").jqGrid('setGridParam', {
                    url: _gridUrl,
                    mytype: 'GET',
                    page: 1
                }).trigger("reloadGrid");
            }
        }
        function selectItem() {
            var selectedId = $("#userRelatedList").jqGrid("getGridParam", "selrow");
            if (selectedId == null) {
                alert("没有选择记录。");
                return;
            }
            var rowData = $("#userRelatedList").jqGrid("getRowData", selectedId);
            var rId;
            var rName;

            if (relatedType == "customer") {
                rId = rowData.customerCode;
                rName = rowData.customerName;
            }
            else if (relatedType == "supplier") {
                rId = rowData.enterpriseCode;
                rName = rowData.enterpriseName;
            }
            else if (relatedType == "employee") {
                rId = rowData.employeeCode;
                rName = rowData.name;
            }

            $("#relatedId").val(rId);
            $("#relatedName").val(rName);
            $("#userRelatedName").val(rName);
            $("#userRelatedDialog").dialog("close");
        }

        function closeType() {
            $("#userRelatedDialog").dialog("close");
        }
    </script>
</head>
<body>
<div id="query" class="ui-widget">
    <div id="toolbar" class="ui-widget-header ui-state-default">
        <div>
            <button id="selectTypeBtn">选择</button>
            <button id="closeTypeBtn" onclick="closeType();">关闭</button>
        </div>
    </div>
    <c:if test="${relatedType == 'employee'}">
        <div id="criterion">
            <table cellspacing="5" cellpadding="5">
                <tr>
                    <td align="right"><label>组织部门：</label></td>
                    <td>
                        <input type="hidden" id="department" onchange="changeOrg()">
                        <input type="text" id="departmentName" readonly>
                    </td>
                </tr>
            </table>
        </div>
    </c:if>
</div>
<table id="userRelatedList"></table>
<div id="userRelatedPager"></div>
<div id="orgDialog" style="display: none;">
    <jsp:include page="../organization/orgTree.jsp"></jsp:include>
</div>
</body>
</html>
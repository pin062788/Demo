<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:include page="/common/common.jsp" />
    <title>Insert title here</title>
    <script type="text/javascript">
        $(document).ready(function () {
            //需要验证的字验证的提示消息
            var messages = {
                tableDesc: {required: "描述不能为空"}
            };
            //需要验证的字段名称和验证规则
            var rules = {
                tableDesc: {required: true}
            };
            //封装成MAP类型的对象
            var validateMap = {
                "formId": "restrictTableDetail",
                "submitMethod": saveForm,
                "showErrorId": "errorMsg",
                "rules": rules,
                "messages": messages
            };
            //开启验证规则
            validateForm(validateMap);
            $("#restrictTableDetail").submit(function (event) {
                event.preventDefault();
            });
            $("#saveBtn").button({
                        icons: {primary: "ui-icon-disk"}
                    })
                    .next()
                    .button({
                        icons: {primary: "ui-icon-close"}
                    });

        });

        function checkDouble(tableName, tableId) {
            var isDouble = false;
            $.ajax({
                type: "post",
                url: "${pageContext.request.contextPath}/restrictTable/isDuplicated.do?tableName=" + tableName + "&tableId=" + tableId,
                datatype: "json",
                async: false,
                success: function (result) {
                    if (result == 'true') {
                        isDouble = true;
                    }
                }
            });
            return isDouble;
        }

        function saveForm() {
            var tableName = encodeURI($("#tableName").val());
            var tableId = encodeURI($("#tableId").val());
            if (checkDouble(tableName, tableId)) {
                $("#errorMsg").html("*该表已经建立！");
            } else {
                save("#restrictTableDetail", {"tableName": tableName}, function () {
                    closeAndReload("tableDetailDialog", "tableRestricts");
                });
            }
        }


    </script>
</head>
<body>
<div id="restrictTableDetailDiv">
    <form:form commandName="restrictTableDetail" action="${pageContext.request.contextPath}/restrictTable/save.do">
        <div id="toolbar" class="ui-widget-header ui-state-default">
            <button id="saveBtn" type="submit">保存</button>
            <button id="closeBtn" type="button" onclick="closePop()">关闭</button>
        </div>
        <form:hidden path="tableId" id="tableId"/>
        <table cellspacing="5" cellpadding="5" width="100%" border="0">
            <tr>
                <td colspan="4">
                    <div id="errorMsg" class="errorCls" style="text-align: center;"></div>
                </td>
            </tr>
            <tr>
                <td align="right">表名:</td>
                <td>
                    <form:select path="tableName" id="tableName" items="${allUserTables}"
                                 disabled="${not empty restrictTableDetail.tableId}">
                    </form:select> <font color="red">*</font>

                </td>
                <td align="right">描述:</td>
                <td>
                    <form:input id="tableDesc" path="tableDesc" maxlength="100"/>
                    <font color="red">*</font>
                </td>
            </tr>
        </table>
    </form:form>
</div>
</body>
</html>
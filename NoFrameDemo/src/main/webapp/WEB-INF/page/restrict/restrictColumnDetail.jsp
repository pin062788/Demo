<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <jsp:include page="../common/common.js.jsp"></jsp:include>
    <script type="text/javascript">
        $(document).ready(function () {
            //需要验证的字验证的提示消息
            var messages = {
                columnDesc: {required: "描述不能为空"},
                columnType: {required: "字段类型不能为空"}
            };
            //需要验证的字段名称和验证规则
            var rules = {
                columnDesc: {required: true},
                columnType: {required: true}
            };
            //封装成MAP类型的对象
            var validateMap = {
                "formId": "restrictColumn",
                "submitMethod": saveForm,
                "showErrorId": "errorMsg",
                "rules": rules,
                "messages": messages
            };
            //开启验证规则
            validateForm(validateMap);
            $("#restrictColumn").submit(function (event) {
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

        function checkDouble(tableId, columnId, columnName) {
            var isDouble = false;
            $.ajax({
                type: "post",
                url: "${pageContext.request.contextPath}/restrictColumn/isDuplicated.do?",
                data: {"tableId": tableId, "columnId": columnId, "columnName": columnName},
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
            var tableId = encodeURI($("#tableId").val());
            var columnId = encodeURI($("#columnId").val());
            var columnName = encodeURI($("#columnName").val());
            if (checkDouble(tableId, columnId, columnName)) {
                $("#errorMsg").html("*该字段已经建立！");
            } else {
                save("#restrictColumn", {"columnName": columnName}, function () {
                    closeAndReload("columnDetailDialog", "restrictColumns");
                });
            }
        }


    </script>
</head>
<body>
<div id="restrictColumnDiv">
    <form:form commandName="restrictColumn" action="${pageContext.request.contextPath}/restrictColumn/save.do">
        <div id="toolbar" class="ui-widget-header ui-state-default">
            <button id="saveBtn" type="submit">保存</button>
            <button id="closeBtn" type="button" onclick="closePop()">关闭</button>
        </div>
        <form:hidden path="tableId" id="tableId"/>
        <form:hidden path="columnId" id="columnId"/>
        <table cellspacing="5" cellpadding="5" width="100%" border="0">
            <tr>
                <td colspan="4">
                    <div id="errorMsg" class="errorCls" style="text-align: center;"></div>
                </td>
            </tr>
            <tr>
                <td align="right">字段名:</td>
                <td>
                    <form:select path="columnName" id="columnName" items="${columns}"
                                 disabled="${not empty restrictColumn.columnId}">
                    </form:select> <font color="red">*</font>

                </td>
                <td align="right">描述:</td>
                <td>
                    <form:input id="columnDesc" path="columnDesc" maxlength="100"/>
                    <font color="red">*</font>
                </td>
            </tr>
            <tr>
                <td align="right">字段类型:</td>
                <td>
                    <form:select path="columnType" id="columnType" items="${columnTypes}">
                    </form:select>
                    <font color="red">*</font>
                </td>
                <td align="right">数据源:</td>
                <td>
                    <form:input id="dataSource" path="dataSource" maxlength="100"/>
                </td>
            </tr>
        </table>
    </form:form>
</div>
</body>
</html>
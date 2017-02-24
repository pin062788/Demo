<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <script type="text/javascript">
        $(function () {
            $("#saveBtn").button({
                icons: {primary: "ui-icon-disk"}
            }).click(function () {
                saveForm();
            });
        });

        function saveForm() {
            var _form = $("#reference");
            $.ajax({
                type: "POST",
                url: _form[0].action,
                data: _form.serialize(),
                dataType: "json"
            }).done(function (data) {
                afterSave(data);
            }).fail(function () {
                alert('保存失败！');
            });
        }
    </script>
</head>
<body>
<div id="nodeDetailDiv">
    <form:form commandName="reference" action="${pageContext.request.contextPath}/reference/save.do">
        <form:hidden path="id"/>
        <form:hidden path="parentId"/>
        <form:hidden path="isParent"/>
        <table cellspacing="5" cellpadding="5" width="100%" border="0">
            <tr>
                <td>Code：</td>
                <td>
                    <form:input path="code" disabled="false"/>
                </td>
                <td>Code Desc：</td>
                <td>
                    <form:input path="codeDesc" disabled="false"/>
                </td>
            </tr>
            <tr>
                <td>Parent Node：</td>
                <td>
                        ${parentDesc}
                </td>
                <td>激活：</td>
                <td>
                    <form:select path="activate" disabled="false">
                        <form:option value="${reference.activate}"></form:option>
                        <c:forEach items="${activate }" var="a">
                            <form:option value="${a.key }">${a.value }</form:option>
                        </c:forEach>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td>Group Name：</td>
                <td>
                    <form:input path="groupName"/>
                </td>
                <td>顺序：</td>
                <td>
                    <form:input path="orderKey"/>
                </td>
            </tr>
        </table>
    </form:form>
    <table cellspacing="0" cellpadding="0" width="100%" border="0">
        <tr>
            <td align="center">
                <button id="saveBtn">保存</button>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
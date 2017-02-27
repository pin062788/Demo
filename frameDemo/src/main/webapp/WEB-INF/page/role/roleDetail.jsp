<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <script type="text/javascript">
        $(function () {
            //需要验证的字验证的提示消息
            var messages = {
                roleCode: {required: "角色编码不能为空"},
                roleName: {required: "角色名称不能为空"},
                remark: {maxlength: "角色描述长度不能超过100个字符!"}
            };
            //需要验证的字段名称和验证规则
            var rules = {
                roleCode: {required: true},
                roleName: {required: true},
                remark: {maxlength: 100}
            };
            //封装成MAP类型的对象
            var validateMap = {
                "formId": "roleDetail",
                "submitMethod": saveRole,
                "showErrorId": "erroradd",
                "rules": rules,
                "messages": messages
            };
            //开启验证规则
            validateForm(validateMap);
            $("#roleDetail").submit(function (event) {
                event.preventDefault();
            });
            $("#saveBtn").button({
                icons: {primary: "ui-icon-disk"}
            });
            $("#closedBtn") .button({
                icons: {primary: "ui-icon-close"}
            });
        });

        function closePop() {
            closeAndReload("roleDialog", "roleList");
        }
        function saveRole() {
            save("#roleDetail", null, function (data) {
                closeAndReload("roleDialog", "roleList");
            });
        }
    </script>
</head>
<body>
<div id="roleDetailDiv">
    <form:form commandName="roleDetail" action="${pageContext.request.contextPath}/role/save.do">
        <form:hidden path="roleId"/>
        <div id="erroradd" class="errorCls" style="text-align: center;"></div>
        <table cellspacing="5" cellpadding="5" width="100%" border="0">
            <tr>
                <td>角色编码：</td>
                <td>
                <c:if test="${role.roleId == '' or role.roleId == null }">
                    <form:input path="roleCode" maxlength="16"/>
                    <font color="red">*</font>
                </c:if>
                <c:if test="${role.roleId  != '' and role.roleId != null }">
                    <form:input path="roleCode" disabled="true"/>
                    <font color="red">*</font>
                </c:if>
                </td>
                <td>角色名称：</td>
                <td>
                    <form:input id="roleName" path="roleName" maxlength="50"/><font color="red">*</font>
                </td>
            </tr>
            <tr>
                <td>角色描述：</td>
                <td colspan="3">
                    <form:textarea id="remark" path="remark" rows="5" cols="50"/>
                </td>
            </tr>
            <tr>
                <td colspan="4">
                    <div id="toolbar" style="float:right;">
                        <button id="saveBtn" type="submit">保存</button>
                        <button id="closedBtn" onclick="closePop();" type="button">关闭</button>
                    </div>
                </td>
            </tr>
        </table>
    </form:form>
</div>
</body>
</html>
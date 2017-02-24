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
        $(document).ready(function () {
            initLeftRight("availableRoles", "selectedRoles", "disSelectRoleBtn", "selectRoleBtn", "disSelectAllRoleBtn", "selectAllRoleBtn");
            initLeftRight("availableUsers", "selectedUsers", "disSelectUserBtn", "selectUserBtn", "disSelectAllUserBtn", "selectAllUserBtn");
            //需要验证的字验证的提示消息
            var messages = {
                groupName: {required: "组名不能为空"}
            };
            //需要验证的字段名称和验证规则
            var rules = {
                groupName: {required: true}
            };
            //封装成MAP类型的对象
            var validateMap = {
                "formId": "userGroupDetail",
                "submitMethod": saveForm,
                "showErrorId": "errorMsg",
                "rules": rules,
                "messages": messages
            };
            //开启验证规则
            validateForm(validateMap);
            $("#userGroupDetail").submit(function (event) {
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

        function checkDouble(groupId, groupName) {
            var isDouble = false;
            $.ajax({
                type: "post",
                url: "${pageContext.request.contextPath}/userGroup/isDuplicated.do?groupName=" + groupName + "&groupId=" + groupId,
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
            var index = 0;
            $.each(
                    $('#selectedRoles option'), function () {
                        var input = $('<input>');
                        input.attr("name", "roles[" + index + "].roleId");
                        input.attr("type", "hidden");
                        input.attr("value", $(this).val());
                        $('#selectedRoles').append(input);
                        index++;
                    }
            );
            index = 0;
            $.each(
                    $('#selectedUsers option'), function () {
                        var input = $('<input>');
                        input.attr("name", "users[" + index + "].userId");
                        input.attr("type", "hidden");
                        input.attr("value", $(this).val());
                        $('#selectedUsers').append(input);
                        index++;
                    }
            );
            var groupId = encodeURI($("#groupId").val());
            var groupName = encodeURI($("#groupName").val());
            if (checkDouble(groupId, groupName)) {
                $("#errorMsg").html("*组名已被占用！");
            } else {
                save("#userGroupDetail", null, function () {
                    closeAndReload("detailDialog", "userGroups");
                });
            }
        }


    </script>
</head>
<body>
<div id="userGroupDetailDiv">
    <form:form commandName="userGroupDetail" action="${pageContext.request.contextPath}/userGroup/save.do">
        <div id="toolbar" class="ui-widget-header ui-state-default">
            <button id="saveBtn" type="submit">保存</button>
            <button id="closeBtn" type="button" onclick="closePop()">关闭</button>
        </div>
        <form:hidden path="groupId" id="groupId"/>
        <table cellspacing="5" cellpadding="5" width="100%" border="0">
            <tr>
                <td colspan="4">
                    <div id="errorMsg" class="errorCls" style="text-align: center;"></div>
                </td>
            </tr>
            <tr>
                <td colspan="3">
                    <fieldset>
                        <legend>用户组信息</legend>
                        <table cellspacing="5" cellpadding="5" width="100%" border="0">
                            <tr>
                                <td align="right">组名:</td>
                                <td>
                                    <form:input size="30" path="groupName" id="groupName" maxlength="30"/>
                                    <font color="red">*</font>
                                </td>
                                <td align="right">描述:</td>
                                <td>
                                    <form:input id="groupDesc" path="groupDesc" maxlength="100"/>

                                </td>
                            </tr>
                        </table>
                    </fieldset>
                </td>
            </tr>
            <tr>
                <td align="left" width="45%">
                    <fieldset>
                        <legend>已选角色</legend>
                        <select id="selectedRoles" name="selectedRoles" multiple="multiple"
                                style="height: 100px; width: 100%">
                            <c:forEach items="${selectedRoles }" var="selectedRole">
                                <option value="${selectedRole.roleId }">${selectedRole.roleName }</option>
                            </c:forEach>
                        </select>
                    </fieldset>
                </td>
                <td align="center">
                    <div id="btns">
                        <button id="selectAllRoleBtn">全选</button>
                        <br>
                        <button id="selectRoleBtn">选择</button>
                        <br>
                        <button id="disSelectRoleBtn">取消选择</button>
                        <br>
                        <button id="disSelectAllRoleBtn">全部取消</button>
                    </div>
                </td>
                <td align="left" width="45%">
                    <fieldset>
                        <legend>所有角色</legend>
                        <select id="availableRoles" name="availableRoles" multiple="multiple"
                                style="height: 100px;width: 100%">
                            <c:forEach items="${availableRoles }" var="availableRole">
                                <option value="${availableRole.roleId }">${availableRole.roleName }</option>
                            </c:forEach>
                        </select>
                    </fieldset>
                </td>
            </tr>
            <tr>
                <td align="left" width="45%">
                    <fieldset>
                        <legend>已选用户</legend>
                        <select id="selectedUsers" name="selectedUsers" multiple="multiple"
                                style="height: 100px; width: 100%">
                            <c:forEach items="${selectedUsers }" var="selectedUser">
                                <option value="${selectedUser.userId }">${selectedUser.userName }</option>
                            </c:forEach>
                        </select>
                    </fieldset>
                </td>
                <td align="center">
                    <div id="btns">
                        <button id="selectAllUserBtn">全选</button>
                        <br>
                        <button id="selectUserBtn">选择</button>
                        <br>
                        <button id="disSelectUserBtn">取消选择</button>
                        <br>
                        <button id="disSelectAllUserBtn">全部取消</button>
                    </div>
                </td>
                <td align="left" width="45%">
                    <fieldset>
                        <legend>所有用户</legend>
                        <select id="availableUsers" name="availableUsers" multiple="multiple"
                                style="height: 100px;width: 100%">
                            <c:forEach items="${availableUsers }" var="availableUser">
                                <option value="${availableUser.userId }">${availableUser.userName }</option>
                            </c:forEach>
                        </select>
                    </fieldset>
                </td>
            </tr>
        </table>
    </form:form>
</div>
</body>
</html>
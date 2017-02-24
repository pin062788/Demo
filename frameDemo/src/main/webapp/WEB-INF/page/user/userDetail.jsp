<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <script type="text/javascript">
        $(function () {
            initLeftRight("availableRoles", "selectedRoles", "disSelectRoleBtn", "selectRoleBtn", "disSelectAllRoleBtn", "selectAllRoleBtn");
            initLeftRight("availableUserGroups", "selectedUserGroups", "disSelectUserGroupBtn", "selectUserGroupBtn", "disSelectAllUserGroupBtn", "selectAllUserGroupBtn");
            //需要验证的字验证的提示消息
            var messages = {
                userName: {required: "用户名不能为空"},
                password: {required: "密码不能为空"},
                relatedId: {required: "密码不能为空"}
                /* 	,relatedType:{
                 required:"类型不能为空"
                 },
                 userRelatedName:{required:"名称不能为空"} */
            };
            //需要验证的字段名称和验证规则
            var rules = {
                userName: {required: true},
                password: {required: true},
                relatedId: {required: true}
                /*  ,relatedType:{
                 required:true
                 },*/
                //userRelatedName:{required:true}
            };
            //封装成MAP类型的对象
            var validateMap = {
                "formId": "userDetail",
                "submitMethod": saveUser,
                "showErrorId": "errorMsg",
                "rules": rules,
                "messages": messages
            };
            //开启验证规则
            validateForm(validateMap);
            $("#userDetail").submit(function (event) {
                event.preventDefault();
            });

            $("#saveUserBtn").button({
                icons: {primary: "ui-icon-disk"}
            });

            $("#resetUserBtn").button({
                icons: {primary: "ui-icon-arrowreturnthick-1-w"}
            });

        });

        function saveUser() {
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
                    $('#selectedUserGroups option'), function () {
                        var input = $('<input>');
                        input.attr("name", "groups[" + index + "].groupId");
                        input.attr("type", "hidden");
                        input.attr("value", $(this).val());
                        $('#selectedUserGroups').append(input);
                        index++;
                    }
            );
            save("#userDetail", null, function (data) {
                closeAndReload("detailDialog", "userManagement");
            });
        }

    </script>
</head>
<body>
<div id="customerDetailDiv">
    <form:form commandName="userDetail" action="${ctx}/user/save.do">
        <form:hidden path="userId" id="userId"/>
        <table cellspacing="5" cellpadding="5" width="100%" border="0">
            <tr>
                <td colspan="4">
                    <div style="text-align: left;">
                        <button id="saveUserBtn" type="submit">保存</button>
                        <button id="resetUserBtn">重置</button>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="4">
                    <div id="errorMsg" class="errorCls" style="text-align: center;"></div>
                </td>
            </tr>
            <tr>
                <td colspan="3">
                    <fieldset>
                        <legend>账号信息</legend>
                        <table cellspacing="5" cellpadding="5" width="100%" border="0">
                            <tr>
                                <td align="right">用户名:</td>
                                <td>
                                    <form:input size="30" path="userName" id="userName" maxlength="30"
                                                disabled="${userDetail.userId !=null}"/>
                                    <font color="red">*</font>
                                </td>
                                <td align="right">密码:</td>
                                <td>
                                    <form:input id="password" path="password" maxlength="30"/>
                                    <font color="red">*</font>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">类型:</td>
                                <td>
                                    <form:select path="relatedType" id="relatedType">
                                        <form:option value="employee">内部账户</form:option>
                                        <%-- <form:options items="${relatedTypes }" itemValue="code" itemLabel="codeDesc"/> --%>
                                    </form:select>
                                    <font color="red">*</font>
                                </td>
                                <td align="right">
                                    <label id="userRelatedNameLab">
                                            <%--  <c:if test="${userDetail.relatedType == 'employee'}">
                                                 组织机构：
                                             </c:if>

                                             <c:if test="${userDetail.relatedType != 'employee'}">
                                                 名称:
                                             </c:if> --%>
                                        名称:
                                    </label>
                                </td>
                                <td>
                                    <form:select path="relatedId" id="relatedId">
                                        <form:option value="">=请选择=</form:option>
                                        <form:options items="${employeeList }" itemValue="employeeId"
                                                      itemLabel="employeeMc"/>
                                    </form:select><span style="color:red;">*</span>
                                        <%--  <form:input path="relatedName" id="relatedName" /> --%>
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
                        <legend>已选用户组</legend>
                        <select id="selectedUserGroups" name="selectedUserGroups" multiple="multiple"
                                style="height: 100px; width: 100%">
                            <c:forEach items="${selectedUserGroups }" var="selectedUserGroup">
                                <option value="${selectedUserGroup.groupId }">${selectedUserGroup.groupName }</option>
                            </c:forEach>
                        </select>
                    </fieldset>
                </td>
                <td align="center">
                    <div id="btns">
                        <button id="selectAllUserGroupBtn">全选</button>
                        <br>
                        <button id="selectUserGroupBtn">选择</button>
                        <br>
                        <button id="disSelectUserGroupBtn">取消选择</button>
                        <br>
                        <button id="disSelectAllUserGroupBtn">全部取消</button>
                    </div>
                </td>
                <td align="left" width="45%">
                    <fieldset>
                        <legend>所有用户组</legend>
                        <select id="availableUserGroups" name="availableUserGroups" multiple="multiple"
                                style="height: 100px;width: 100%">
                            <c:forEach items="${availableUserGroups }" var="availableUserGroup">
                                <option value="${availableUserGroup.groupId }">${availableUserGroup.groupName }</option>
                            </c:forEach>
                        </select>
                    </fieldset>
                </td>
            </tr>

        </table>
    </form:form>
</div>
<%-- <div id="userRelatedDialog"></div>
<div id="orgDialog" style="display: none;">
    <jsp:include page="../organization/orgTree.jsp" ></jsp:include>
</div> --%>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:include page="/common/common.jsp"/>
    <script type="text/javascript">
        $(document).ready(function () {
            var messages = {
                orgCode: {required: "组织行业统一编码不能为空", maxlength: "组织行业统一编码长度不能超过20"},
                //orgType:{required:"组织机构类别不能为空"},
                orgName: {required: "组织机构名称不能为空", maxlength: "组织机构名称长度不能超过20"},
                placType: {required: "组织机构名称不能为空"},
                orgAddress: {maxlength: "组织机构地址长度不能超过100"},
                remark: {maxlength: "描述长度不能超过300"},
                leadOrgName: {required: "上级组织机构不能为空"}
            };
            //需要验证的字段名称和验证规则
            var rules = {
                orgCode: {required: true, maxlength: 20},
                //orgType:{required:true},
                orgName: {required: true, maxlength: 20},
                placType: {required: true},
                orgAddress: {maxlength: 100},
                remark: {maxlength: 300},
                leadOrgName: {required: true}
            };
            //封装成MAP类型的对象
            var validateMap = {
                "formId": "organizationDetail",
                "submitMethod": saveForm,
                "showErrorId": "organization_error",
                "rules": rules,
                "messages": messages
            };
            //开启验证规则
            validateForm(validateMap);
            $("#organization_SaveBtn").button({
                        icons: {primary: "ui-icon-disk"}
                    })
                    .next()
                    .button({
                        icons: {primary: "ui-icon-close"}
                    });
        });


        function saveForm() {
            var _form = $("#organizationDetail");
            $.ajax({
                type: "POST",
                url: "${ctx}/organization/save.do",
                data: _form.serialize(),
                dataType: "json"
            }).done(function (data) {
                if (data.status == "1") {
                    alert("保存成功");
                    refreshOrgNode(data.data);
                } else {
                    alert("保存失败，错误原因：" + data.message);
                }
            }).fail(function () {
                alert("请求超时");
            });
        }
    </script>
</head>
<body>
<div>
    <form:form commandName="organizationDetail" id="organizationDetail" name="organizationDetail">
        <div id="toolbar" class="ui-widget-header ui-state-default">
            <button id="organization_SaveBtn" type="submit">保存</button>
        </div>
        <form:hidden path="orgId"/>
        <table cellspacing="5" cellpadding="5" border="0">
            <tr>
                <td colspan="4">
                    <div id="organization_error" class="errorCls"></div>
                </td>
            <tr>
            <tr>
                <td nowrap><label>组织行业统一编码：</label></td>
                <td>
                    <form:input id="orgCode" path="orgCode"/><font color="red">*</font>
                </td>
                <td nowrap><label> 组织机构名称：</label></tdnowrap>
                <td id="orgNameTD">
                    <form:input path="orgName"/><font color="red">*</font>
                </td>

            </tr>
            <tr>
                <td nowrap><label>组织机构地址：</label></td>
                <td><form:input path="orgAddress"/></td>
                <td nowrap><label>上级组织机构：</label></td>
                <td>
                    <form:input path="parentOrg.orgName" readonly="true"/>
                    <form:hidden path="parentOrgId"/>
                </td>
            </tr>
            <tr>
                <td nowrap><label>组织机构行政类别：</label></td>
                <td>
                    <form:select path="orgType" class="selcls">
                        <form:option value="" label="=请选择="></form:option>
                        <form:options items="${orgTypeList}" itemValue="code" itemLabel="codeDesc"/>
                    </form:select>
                    <font color="red">*</font>
                </td>
                <td nowrap><label>组织机构职能类别：</label></td>
                <td>
                    <form:select path="orgFunctionTypeCode" class="selcls">
                        <form:option value="" label="=请选择="></form:option>
                        <form:options items="${orgFunctionList}" itemValue="code" itemLabel="codeDesc"/>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td nowrap>组织机构状态：</td>
                <td colspan="3">
                    <form:select path="expire" class="selcls">
                        <form:option value="1" label="可用"></form:option>
                        <form:option value="0" label="不可用"></form:option>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td nowrap>描述：</td>
                <td colspan="3"><form:textarea path="orgDesc" cols="55" rows="3"/></td>
            </tr>
        </table>
    </form:form>
</div>
</body>
</html>
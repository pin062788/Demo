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
            $("#saveResourceBtn").button({
                        icons: {primary: "ui-icon-disk"}
                    })
                    .click(function (event) {
                        event.preventDefault();
                        save("#resourceDetail", {_timer_: new Date().getTime()}, function (data) {
                            refreshNode(data);
                        });
                    });
            $("#resetResourceBtn").button({
                icons: {primary: "ui-icon-arrowreturnthick-1-w"}
            }).click(function (event) {
                event.preventDefault();
            });

        });


    </script>
</head>
<body>
<div id="resourceDetailDiv" class="ui-widget-content">
    <form:form commandName="resourceDetail" action="${ctx}/resource/save.do">
        <form:hidden path="resourceId"/>
        <form:hidden path="parentResourceCode"/>
        <form:hidden path="parentModuleCode"/>
        <table cellspacing="5" cellpadding="5" width="100%" border="0">
            <tr>
                <td width="80px;">资源编码：</td>
                <td>
                    <form:input path="resourceCode" maxlength="32"/>
                </td>
                <td width="80px;">父资源：</td>
                <td>
                    <form:input path="parentResourceName" maxlength="32" readonly="true"/>
                </td>
            </tr>
            <tr>
                <td>资源名称：</td>
                <td>
                    <form:input path="resourceName" maxlength="20"/><font color="red">*</font>
                </td>
                <td>资源类型：</td>
                <td>
                    <form:select path="resourceType" style="width:140px;" id="resourceType">
                        <form:option value="">=请选择=</form:option>
                        <form:options itemValue="code" itemLabel="codeDesc" items="${resourceTypes}"/>
                    </form:select><font color="red">*</font>
                </td>
            </tr>
            <tr>
                <td>资源URL：</td>
                <td>
                    <form:input path="resourceUrl" style="width:190px;"/>
                </td>
                <td>资源标识：</td>
                <td>
                    <form:input path="resourceSn" maxlength="50"/>
                </td>
            </tr>
            <tr>
                <td>资源顺序：</td>
                <td colspan="3">
                    <form:input path="orderNumber" maxlength="22"/>
                </td>
            </tr>

            <tr>
                <td>资源描述：</td>
                <td colspan="3">
                    <form:textarea path="resourceDesc" rows="5" cols="50"/>
                </td>
            </tr>
            <tr>
                <td colspan="4">
                    <div style="float:right;">
                        <button id="saveResourceBtn">保存</button>
                        <button id="resetResourceBtn">重置</button>
                    </div>
                </td>
            </tr>
        </table>
    </form:form>

</div>
</body>
</html>
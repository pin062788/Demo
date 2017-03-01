<%@ page import="com.ocellus.platform.utils.PageConstants" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript" src="${ctx}/script/common.js"/>
    <script type="text/javascript">
        $(function(){
            $("#saveBtn_${modelName}").button({
                icons : {primary : "ui-icon-disk"}
            }).click(function(){
                $("#editForm_${modelName}").submit();
            });

            $("#closeBtn_${modelName}").button({
                icons : {primary : "ui-icon-close"}
            }).click(function(){
                if(confirm("确定要关闭窗口吗?")){
                    $("#editDialog_${modelName}").dialog("close");
                }
            });
            var messages = {
                name: {required: "名称不能为空"},
                url: {required: "测试不能为空"}
            };
            var rules = {
                name:{required:true},
                url:{required:true}
            };
            var validateMap = {
                "formId": "editForm_${modelName}",
                "submitMethod": saveModel,
                "showErrorId": "editErrorMsg_${modelName}",
                "rules":rules,
                "messages": messages
            };
            validateForm(validateMap);
        });
        function saveModel(){
            var model = $("#editForm_${modelName}").serializeJson();
            $.ajax({
                type 		: 	"POST",
                url			:	 "${pageContext.request.contextPath}/${modelName}/saveBase.do",
                contentType	:"application/json",
                data		:	  JSON.stringify(model),
                dataType	:	 "json"
            }).done(function(data){
                alert(data.message);
                if(data.status == "1"){
                    $("#editDialog_${modelName}").dialog("close");
                    $("#jqGrid_${modelName}").jqGrid('setGridParam',{
                        page:1
                    }).trigger("reloadGrid");
                }
            });
        }
    </script>
</head>
<body>
<div id="toolbar" class="ui-widget-header ui-state-default">
    <button id="saveBtn_${modelName}" type="button"><%=PageConstants.BUTTON_SAVE_TEXT%></button>
    <button id="closeBtn_${modelName}"  type="button"><%=PageConstants.BUTTON_CLOSE_TEXT%></button>
</div>
<div id="editErrorMsg_${modelName}" class="errorCls"></div>
<div id="editMainDiv_${modelName}" class="ui-widget">
    <form:form commandName="model" id="editForm_${modelName}" name="editForm_${modelName}">
        <form:hidden path="id"/>
        <fieldset class="ui-widget">
            <legend>测试</legend>
            <table cellspacing="5" cellpadding="5" border="0">
                <tr>
                    <td nowrap align="right">名称<span style="color:red;">*</span></td>
                    <td >
                        <form:input path="name" />
                    </td>
                </tr>
                <tr>
                    <td nowrap align="right">测试<span style="color:red;">*</span></td>
                    <td>
                        <form:input path="url" />
                    </td>
                </tr>
            </table>
        </fieldset>
    </form:form>
</div>
</body>
</html>
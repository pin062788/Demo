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
                if(confirm("Confirm closing dialog ?")){
                    $("#editDialog_${modelName}").dialog("close");
                }
            });

            var rules = {
                counterName:{required:true},
                nextKeyValue:{required:true,number:true}
            };
            var validateMap = {
                "formId": "editForm_${modelName}",
                "submitMethod": save,
                "showErrorId": "editErrorMsg_${modelName}",
                "rules":rules
            };
            validateForm(validateMap);
        });
        function save(){
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
    <form:form commandName="counter" id="editForm_${modelName}" name="editForm_${modelName}">
        <form:hidden path="id"/>
        <fieldset class="ui-widget">
            <legend>Counter</legend>
            <table cellspacing="5" cellpadding="5" border="0">
                <tr>
                    <td nowrap align="right">Counter Name<span style="color:red;">*</span></td>
                    <td >
                        <form:input path="counterName" size="30" maxlength="30" />
                    </td>
                </tr>
                <tr>
                    <td nowrap align="right">Next Key Value<span style="color:red;">*</span></td>
                    <td>
                        <form:input path="nextKeyValue" />
                    </td>
                </tr>
                <tr>
                    <td nowrap align="right">Value1</td>
                    <td>
                        <form:input path="value1" size="30" maxlength="30"/>
                    </td>
                </tr>
                <tr>
                    <td nowrap align="right">Value2</td>
                    <td>
                        <form:input path="value2" size="30" maxlength="30"/>
                    </td>
                </tr>
                <tr>
                    <td nowrap align="right">Version</td>
                    <td>
                        <form:input path="version" size="30" maxlength="30"/>
                    </td>
                </tr>
            </table>
        </fieldset>
    </form:form>
</div>
</body>
</html>
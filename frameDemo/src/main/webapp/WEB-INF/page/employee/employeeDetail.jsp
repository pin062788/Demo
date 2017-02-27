<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	$(document).ready(function(){
		$(".datepicker").datepicker();
		$("#employee_SaveBtn").button({
			icons : {primary : "ui-icon-disk"}
		}).click(function(){
			$("#employeeAddForm").submit();
		});
		
		$("#employee_CloseBtn").button({
			icons : {primary : "ui-icon-close"}
		});
		
		//需要验证的字验证的提示消息
		var messages = {
			employeeMc:{required:"人员名称不能为空"},
			employeeZzbmMc:{required:"组织部门不能为空"},
			employeeSjh:{ number:"手机号只能输入数字", maxlength: "手机号码不能超过11位"}
		};
		//需要验证的字段名称和验证规则
		var rules = {
			employeeMc:{required:true},
			employeeZzbmMc:{required:true},
			employeeSjh:{number:true, maxlength:11}
		};
		//封装成MAP类型的对象
		var validateMap = {
		   "formId": "employeeAddForm",
		   "submitMethod": saveEmployee,
		   "showErrorId": "employeeAddErrorMsg",
		   "rules":rules,
		   "messages":messages
	   	};
		//开启验证规则
		validateForm(validateMap);
		initOrgWidget("employeeZzbmMc","orgDialog");
	});
	function closePop() {
		closeAndReload("rywhDialog", "renYuanWeiHuGridList");
	}

	function saveEmployee(){
		$.post("${ctx}/employee/saveOrUpdate.do", $("#employeeAddForm").serialize(), function(data){
			var result = $.parseJSON(data);
			if(result.status == "1"){
				if(!checkIsNull(result.message))
					alert(result.message);
				else
					alert("保存成功");
				closeAndReload("rywhDialog", "renYuanWeiHuGridList");
			}else
				alert("保存失败，错误原因：" + result.message);
		});
	}
	
	function afterSelectOrg(){
	   $("#orgDialog").hide();
	   var ssdwSelect = $("#employeeZzbmMc");
	   ssdwSelect.empty();
	   $("#employeeZzbmMc").val("");
	   $("#employeeZzbmMc").val($("#relatedName").val());
	   ssdwSelect.append(new Option($("#relatedName").val(),$("#relatedId").val()));
	}
</script>
</head>
<body>
	<form:form commandName="employee" id="employeeAddForm" name="employeeAddForm">
       	<div class="ui-widget">
       		<div id="employee_toolbar" class="ui-widget-header ui-state-default"> 
				<button id="employee_SaveBtn" type="button">保存</button>
				<button id="employee_CloseBtn"  type="button" onclick="closePop();">关闭</button>
	       	</div>
			<div id="employeeAddErrorMsg" class="errorCls"></div>
	   		<form:hidden path="employeeId"/>
			<fieldset>
				<legend>人员信息</legend>
			    <table cellspacing="5" cellpadding="5" border="0">
			    	<tr>
			    		<td nowrap>人员编号</td>
			    		<td>
			    			<form:input path="employeeBh" readonly="true"/>
			    		</td>
			    		<td nowrap>人员名称</td>
			    		<td>
			    			<form:input path="employeeMc"/><font color="red">*</font>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td nowrap>组织部门</td>
			    		<td>
			    			<form:input path="employeeZzbmMc" readonly="true" style="width:159px;" />
	                        <form:hidden path="employeeZzbmId" id="relatedId" />
	                        <form:hidden path="employeeZzbm" id="relatedName" />
							<font color="red">*</font>
			    		</td>
			    		<td nowrap>职位</td>
			    		<td>
			    			<form:input path="employeeZw"/>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td nowrap>性别</td>
			    		<td>
			    			<form:select path="employeeXb">
			    				<form:option value="">=请选择=</form:option>
			    				<form:options items="${xbList}" itemValue="code" itemLabel="codeDesc"/>
			    			</form:select>
			    		</td>
			    		<td nowrap>手机号</td>
			    		<td>
			    			<form:input path="employeeSjh"/>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td nowrap>备注</td>
			    		<td colspan="3">
			    			<form:textarea path="employeeBz" rows="4" cssStyle="width: 100%"/>
			    		</td>
			    	</tr>
			    </table>
			</fieldset>
		</div>
	</form:form>
	 <div id="orgDialog" style="display: none;">
            <jsp:include page="../organization/orgTree.jsp" ></jsp:include>
     </div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page import="com.ocellus.platform.model.User" %>
<%@ page import="com.ocellus.platform.utils.Constants" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript">
    var xbList = jQuery.parseJSON('${xbList}');   
    $(document).ready(function(){
        	//查询
        	$("#renYuanWeiHu_SearchBtn").button({
                icons : {primary:"ui-icon-search"}
            }).click(function(){
            	 var renYuanWeiHuMc = encodeURI($.trim($("#renYuanWeiHuMc").val()));
                $("#renYuanWeiHuGridList").jqGrid('setGridParam',{
                	postData:{
                		'employeeMc': renYuanWeiHuMc
                	}, 
                    mytype: 'POST',
                    page:1
                }).trigger("reloadGrid");//重新载入
            });
        	
        	//新增
        	$("#renYuanWeiHu_AddBtn").button({
                icons : {primary:" ui-icon-plusthick"}
            }).click(function(){
            	var _url = "${ctx}/employee/addEmployee.do";
                openDialog("renYuanWeiHuDialog", _url,"人员维护->添加");
            });
        	//编辑
        	$("#renYuanWeiHu_EditBtn").button({
                icons : {primary:"ui-icon-wrench"}
            }).click(function(){
            	var rowData = $('#renYuanWeiHuGridList').jqGrid('getGridParam','selarrrow');
        		if(rowData.length != 1){
        			alert("请选择一条记录");
        			return;
        		}
        		id= $('#renYuanWeiHuGridList').jqGrid('getCell',rowData[0],'employeeId');
        		var _url = "${ctx}/employee/editEmployee.do?employeeId=" + id;
        		var title = "人员维护->修改";
        		openDialog("renYuanWeiHuDialog", _url, title);
            });

        	//删除
           	$("#renYuanWeiHu_DeleBtn").button({
                   icons : {primary:"ui-icon-closethick"}
               }).click(function(){
    			var _url = "${ctx}/employee/deleteEmployee.do";
		     		var params = {"_time_":new Date().getTime()};
		     		deleteById("renYuanWeiHuGridList","employeeId",_url,params,function(data){
		     			var result = $.parseJSON(data);
		     	 			alert(result.message);
		     	 		if(result.status=="1"){
		         	 		reloadGridById("renYuanWeiHuGridList");
		         	 	}	
		     		});
             });
        	
        	$("#renYuanWeiHuGridList").jqGrid({//查询记录
                url: "${ctx}/employee/getEmployeeList.do",
                datatype: "json",
                mytype: 'POST',
                colNames:["employeeId","人员编号", "人员名称","组织部门","职位","性别","手机号"],
                colModel:[
                    {name: "employeeId",hidden:true},
                    {name: "employeeBh",align: 'center'},
                    {name: "employeeMc",align: 'center'},
                    {name: "employeeZzbmMc",align: 'center'},
                    {name: "employeeZw",align: 'center'},
                    {name: "employeeXb",align: 'center',formatter:function(cellValue){return formatterDesc(xbList,cellValue)}, 
                        unformat:function(cellValue){return unformatCode(xbList,cellValue)}},
                    {name: "employeeSjh",align: 'center'}
                ],
                rowNum: 10,
                page: 1,
                total: 15,
                rowList: [10, 20, 30],
                pager: "#renYuanWeiHuGridPager",
                viewrecords: true,
                height: "auto",
                autowidth: true,
                gridview:true,
                autoencode: true,
                multiselect: true,
                multiboxonly: true
            }).navGrid("#renYuanWeiHuGridPager", {
                refresh: false,
                edit: false,
                add: false,
                del: false,
                search: false
            });
        	initDialog("renYuanWeiHuDialog","人员维护",true,600,350);//对话框初始化
        });
    </script>
</head>
<body>
<div id="renYuanWeiHu_DIV" class="ui-widget">
        <div id="renYuanWeiHu_toolBar" class="ui-widget-header ui-state-default">
             <div>
           	 	<button id="renYuanWeiHu_SearchBtn">查询</button>
           	 	<button id="renYuanWeiHu_AddBtn">新增</button>
           	 	<button id="renYuanWeiHu_EditBtn">修改</button>
           	 	<button id="renYuanWeiHu_DeleBtn">删除</button>
            </div> 
        </div>
        <div id="renYuanWeiHu_criterion" >
            <form id="renYuanWeiHuQueryForm">
                <table cellspacing="5" cellpadding="5" border="1">
	                <tr>
	                    <td colspan="3">
	                    	<div id="chuKuShenQingErrorMsg"></div>
	                    </td>
	                </tr>
                    <tr>
                        <td align="right"><label>人员名称：</label></td>
                        <td>
                        	<input type="text" id="renYuanWeiHuMc" name="renYuanWeiHuMc">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    <table id="renYuanWeiHuGridList"></table>
    <div id="renYuanWeiHuGridPager"></div>
     <div id="renYuanWeiHuDialog"></div>
</div>
</body>
</html>
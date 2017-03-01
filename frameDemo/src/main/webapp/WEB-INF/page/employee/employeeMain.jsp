<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:include page="/common/common.jsp" />
    <script type="text/javascript">
    var xbList = jQuery.parseJSON('${xbList}');
    $(function(){
        $("#rywhDialog").dialog({
            title:"人员维护",
            autoOpen: false,
            position: {my: "top", at: "top+50", of: window},
            modal:true,
            resizable: true,
            width:"650",
            height:350
        });
        //查询
        $("#renYuanWeiHu_SearchBtn").button({
            icons : {primary:"ui-icon-search"}
        }).click(function(){
            var renYuanWeiHuMc = encodeURI($.trim($("#renYuanWeiHuMc").val()));
            $("#renYuanWeiHuGridList").jqGrid('setGridParam',{
                postData:{
                    'employeeMcLike': renYuanWeiHuMc
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
            $.post(_url,"",function(data){
                $("#rywhDialog").dialog("open").html(data);
            }).fail(function(){
                alert("加载失败!");
            });
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
            var id= $('#renYuanWeiHuGridList').jqGrid('getCell',rowData[0],'employeeId');
            var _url = "${ctx}/employee/editEmployee.do?employeeId=" + id;

            $.post(_url,"",function(data){
                $("#rywhDialog").dialog("open").html(data);
            }).fail(function(){
                alert("加载失败!");
            });
        });

        //删除
        $("#renYuanWeiHu_DeleBtn").button({
               icons : {primary:"ui-icon-closethick"}
        }).click(function(){
            var _url = "${ctx}/employee/deleteEmployee.do";
            var params = {"_time_":new Date().getTime()};
            var rowData = $('#renYuanWeiHuGridList').jqGrid('getGridParam','selarrrow');
            if(rowData.length < 1) {
                alert("请至少选择一条数据进行删除");
                return;
            }
            var ids = [];
            for (var i = 0; i < rowData.length; i++) {
                ids.push($("#renYuanWeiHuGridList").jqGrid('getCell', rowData[i], "employeeId"));
            }
            params.ids = encodeURI(ids);
            if (confirm("请确认是否要删除数据?")) {
                $.post(_url,params,function(data){
                    var result = $.parseJSON(data);
                    alert(result.message);
                    if(result.status=="1"){
                        $("#renYuanWeiHuGridList").jqGrid().trigger("reloadGrid");
                    }
                });
            }
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
            caption:"人员维护",
            viewrecords: true,
            loadComplete: function() {
                var grid = $("#renYuanWeiHuGridList");
                var ids = grid.getDataIDs();
                for (var i = 0; i < ids.length; i++) {
                    grid.setRowData ( ids[i], false, {height: 35+i*2} );
                }
            },
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
    });
    </script>
</head>
<body id="loading">
<div class="ui-widget-content">
    <div id="renYuanWeiHu_toolBar" class="ui-widget-header ui-state-default">
         <div>
            <button id="renYuanWeiHu_AddBtn">新建</button>
            <button id="renYuanWeiHu_EditBtn">更新</button>
            <button id="renYuanWeiHu_DeleBtn">删除</button>
        </div>
    </div>
    <div id="renYuanWeiHu_criterion" >
        <form id="renYuanWeiHuQueryForm">
            <table  ><tr><td >
                    <div class="criteria_float_div">
                        <ul><li >
                            <label>人员名称：</label>
                            <input type="text" id="renYuanWeiHuMc" name="renYuanWeiHuMc">
                        </li>
                        <li class="queryButton">
                            <button id="renYuanWeiHu_SearchBtn">查询</button>
                        </li></ul>
                    </div>
                </td ></tr>
            </table>
        </form>
    </div>
    <table id="renYuanWeiHuGridList"></table>
    <div id="renYuanWeiHuGridPager"></div>
    <div id="rywhDialog"></div>
</div>
</body>
</html>
<%@ page import="com.ocellus.platform.utils.PageConstants" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:include page="/common/common.jsp" />
    <script type="text/javascript">
        $(function(){
            $("#editDialog_${modelName}").dialog({
                title:"Counter",
                autoOpen: false,
                position: {my: "top", at: "top+50", of: window},
                modal:true,
                resizable: true,
                width:"50%",
                height:300
            });

            //查询
            $("#searchBtn_${modelName}").button({
                icons : {primary:"ui-icon-search"}
            }).click(function(){
                $("#jqGrid_${modelName}").jqGrid('setGridParam',{
                    url: "${pageContext.request.contextPath}/${modelName}/getBaseList.do?"+encodeURI($("#queryForm_${modelName}").serialize()),
                    page:1
                }).trigger("reloadGrid");
            });

            $("#resetBtn_${modelName}").button({
                icons : {primary:"ui-icon-reset"}
            }).click(function(){
                $("#queryForm_${modelName} input[type='text']").val("");
                $("#queryForm_${modelName} select").val("");
            });

            //新增
            $("#addBtn_${modelName}").button({
                icons : {primary:" ui-icon-plusthick"}
            }).click(function(){
                var _url = "${pageContext.request.contextPath}/${modelName}/editBase.do";
                $.post(_url,"",function(data){
                    $("#editDialog_${modelName}").dialog("open").html(data);
                }).fail(function(){
                    alert("post failed");
                });
            });

            //编辑
            $("#editBtn_${modelName}").button({
                icons : {primary:"ui-icon-wrench"}
            }).click(function(){
                var rowData = $('#jqGrid_${modelName}').jqGrid('getGridParam','selarrrow');
                if(rowData.length != 1){
                    alert("Please select one record");
                    return;
                }
                var id= $('#jqGrid_${modelName}').jqGrid('getCell',rowData[0],'id');
                var _url = "${pageContext.request.contextPath}/${modelName}/editBase.do?id=" + id;
                var params = {
                    id:id
                };
                $.post(_url,params,function(data){
                    $("#editDialog_${modelName}").dialog("open").html(data);
                }).fail(function(){
                    alert("post failed");
                });
            });

            //删除
            $("#deleteBtn_${modelName}").button({
                icons : {primary:"ui-icon-closethick"}
            }).click(function(){
                var _url = "${pageContext.request.contextPath}/${modelName}/deleteBase.do";
                var params = {"_time_":new Date().getTime()};
                var rowData = $('#jqGrid_${modelName}').jqGrid('getGridParam','selarrrow');
                if(rowData.length < 1) {
                    alert("Please select records");
                    return;
                }
                var ids = [];
                for (var i = 0; i < rowData.length; i++) {
                    ids.push($("#jqGrid_${modelName}").jqGrid('getCell', rowData[i], "id"));
                }
                params.ids = encodeURI(ids);
                if (confirm("Confirm delete data?")) {
                    $.post(_url,params,function(data){
                        var result = $.parseJSON(data);
                        alert(result.message);
                        if(result.status=="1"){
                            $("#jqGrid_${modelName}").jqGrid('setGridParam',{
                                url: "${pageContext.request.contextPath}/${modelName}/getList.do?"+encodeURI($("#queryForm_${modelName}").serialize()),
                                page:1
                            }).trigger("reloadGrid");
                        }
                    });
                }
            });

            $("#jqGrid_${modelName}").jqGrid({
                url: "${pageContext.request.contextPath}/${modelName}/getBaseList.do?"+encodeURI($("#queryForm_${modelName}").serialize()),
                datatype: "json",
                mytype: 'POST',
                colNames:["id","Counter Name","Next Key Value","Value1","Value2","Last Modified","Last Modified By","Version"],
                colModel:[
                    {name: "id",hidden:true,hidedlg:true},
                    {name: "counterName",align: 'center'},
                    {name: "nextKeyValue",align: 'center'},
                    {name: "value1",align: 'center'},
                    {name: "value2", align: 'center'},
                    {name: "lastModified", align: 'center'},
                    {name: "lastModifiedBy", align: 'center',hidden:true},
                    {name: "version", align: 'center'}
                ],
                rowNum: 10,
                page: 1,
                total: 15,
                sortname:"lastModified desc, id",
                sortorder:"desc",
                rowList: [10, 30, 50],
                pager: "#jqGridPager_${modelName}",
                loadComplete: function() {
                    var grid = $("#jqGrid_${modelName}");
                    var ids = grid.getDataIDs();
                    for (var i = 0; i < ids.length; i++) {
                        grid.setRowData ( ids[i], false, {height: 35+i*2} );
                    }
                },
                height:'auto',
                caption:"Counter",
                rownumbers:true,
                autowidth:true,
                viewrecords: true,
                gridview:true,
                autoencode: true,
                multiselect: true,
                multiboxonly:true
            }).navGrid("#jqGridPager_${modelName}", {
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
<div class="ui-widget">
    <div id="toolbar" class="ui-widget-header ui-state-default">
        <div>
            <button id="addBtn_${modelName}"><%=PageConstants.BUTTON_NEW_TEXT%></button>
            <button id="editBtn_${modelName}"><%=PageConstants.BUTTON_UPDATE_TEXT%></button>
            <button id="deleteBtn_${modelName}"><%=PageConstants.BUTTON_DELETE_TEXT%></button>
        </div>
    </div>
    <div id="criterion" >
        <form id="queryForm_${modelName}">
            <table><tr><td>
                <div class="criteria_float_div">
                    <ul>
                        <li ><label >Counter Name：</label>
                            <input type="text" name="counterNameLike" />
                        </li>
                        <li class="queryButton" >
                            <button id="searchBtn_${modelName}" type="button"><%=PageConstants.BUTTON_SEARCH_TEXT%></button>
                            <button id="resetBtn_${modelName}" type="button"><%=PageConstants.BUTTON_RESET_TEXT%></button>
                        </li>
                    </ul>
                </div>
            </td></tr></table>
        </form>
    </div>
        <table id="jqGrid_${modelName}" ></table>
        <div id="jqGridPager_${modelName}"></div>
    <br>
    <div id="editDialog_${modelName}"></div>
</div>
</body>
<script>
</script>
</html>
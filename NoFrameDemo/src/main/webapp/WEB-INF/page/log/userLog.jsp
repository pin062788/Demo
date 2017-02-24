<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

    <style type="text/css">
        .ui-widget {
            font-size: 12px;
            font-weight: 500;
        }
    </style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript">
        $(document).ready(function () {
            $("#searchBtn").button({
                        icons: {primary: "ui-icon-search"}
                    })
                    .click(function () {
                        alert("开发中....");
                        /* var _number = encodeURI($("#organizationNumber").val());
                         var _name =encodeURI($("#organizationName").val());
                         var getTimestamp=new Date().getTime();
                         $("#list").jqGrid('setGridParam',{
                         url: "
                        ${pageContext.request.contextPath}/organization/getOrganization.do?organizationNumber=" + _number + "&organizationName=" + _name+"&_timer_="+getTimestamp
                         }).trigger("reloadGrid"); */
                    });
            $("#list").jqGrid({
                url: "${pageContext.request.contextPath}/log/userLogList.do",
                datatype: "json",
                mytype: 'GET',
                colNames: [
                    '日志类型',
                    '产生时间',
                    '用户名称',
                    '日志信息'],
                colModel: [
                    {name: "type"},
                    {name: "time"},
                    {name: "user.userName"},
                    {name: "message"}
                ],
                rowNum: 10,
                page: 1,
                total: 15,
                rowList: [10, 20, 30],
                pager: "#pager",
                viewrecords: true,
                height: "auto",
                autowidth: true,
                gridview: true,
                autoencode: true,
                subGrid: false,
                multiselect: true,
                onSelectRow: function () {

                }//,

            }).navGrid("#pager", {
                refresh: false,
                edit: false,
                add: false,
                del: false,
                search: false
            });
        });


    </script>
</head>
<body>
<div class="ui-widget">
    <div id="toolbar" class="ui-widget-header ui-state-default">
        <div id="btns">
            <button id="searchBtn">查询</button>
        </div>
    </div>
    <div>
        <div id="criterion">
            <table cellspacing="5" cellpadding="5" border="0">
                <tr>
                    <td colspan="6">
                        <div id="errormsg" class="errorCls" style="text-align: center;"></div>
                    </td>
                </tr>
                <tr>
                    <td>类型：</td>
                    <td><input type="text" id="organizationNumber"/></td>
                </tr>
            </table>
        </div>
    </div>
    <table id="list"></table>
    <div id="pager"></div>
</div>
</body>
</html>
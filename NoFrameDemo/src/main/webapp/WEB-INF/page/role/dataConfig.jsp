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
        var columns = [];
        $(function () {
            //添加
            $("#addRestrictBtn").button({
                        icons: {primary: "ui-icon-plus"}
                    })
                    .click(function (event) {
                        event.preventDefault();
                        var selected = $("#tableId option:selected");
                        if (selected.size() == 0) {
                            alert("请先选择表！");
                            return;
                        }

                        var rowId = 0;
                        var len = $("#restrict_tb tr").length;
                        if (len && len > 0) {
                            rowId = $("#restrict_tb tr:last").attr("id");
                            rowId = parseInt(rowId) + 1;
                        }
                        $("#restrict_tb").append(generateNewRow(rowId));
                    });
            //删除
            $("#delRestrictBtn").button({
                        icons: {primary: "ui-icon-minus"}
                    })
                    .click(function (event) {
                        event.preventDefault();
                        var ckbs = $("input[name='rowCkb']:checked");
                        if (ckbs.size() == 0) {
                            alert("要删除指定行，需选中要删除的行！");
                            return;
                        }
                        ckbs.each(function () {
                            $(this).parent().parent().remove();
                        });
                    });
            //保存
            $(".save_btn").button({
                icons: {primary: "ui-icon-disk"}
            }).click(function (event) {
                event.preventDefault();

                var hasEmpty = false;
                $.each($("input[name^='restrictValue']"), function (i, item) {
                    var val = $(item).val();
                    if (!val || val == null || val == '') {
                        hasEmpty = true;
                    }
                });

                if (hasEmpty) {
                    alert("请正确填写数据！");
                }
                else {
                    save("#dataConfigForm");
                }
            });

            //加载
            $("#tableId").change(function () {
                var tableId = $(this).val();
                var _url = "${ctx}/role/getTableColumns.do";
                var _param = {tableId: tableId, roleId: "${roleId}"};
                $.getAjax(_url, _param, null, function (data) {
                    //get columns
                    columns = data.columns;
                    //remove old data
                    $('#restrict_tb tr').remove();
                    //generate new date
                    if (data.restricts) {
                        var rowId = 0;
                        $.each(data.restricts, function (key, val) {
                            $("#restrict_tb").append(generateNewRow(rowId));
                            $("#connOpt_" + rowId).val(val.connOpt);
                            $("#columnName_" + rowId).val(val.columnName);
                            $("#optCode_" + rowId).val(val.optCode);
                            $("#restrictValue_" + rowId).val(val.restrictValue);
                            rowId++;
                        });
                    }

                });

            });

        });
        //生成一行
        function generateNewRow(row) {
            var tr = "<tr id=\"" + row + "\">";
            tr += getCheckboxTd(row);
            tr += getConnectOptTd(row);
            tr += getColumnTd(row);
            tr += getOptTd(row);
            tr += getValueTd(row);
            //tr += getBtnsTd(row);
            tr += "</tr>";
            return tr;
        }
        function getCheckboxTd(row) {
            var td = "";
            td += "<td width=\"10%\">";
            td += "<input type=\"checkbox\" name=\"rowCkb\">";
            td += "</td>";
            return td;
        }
        function getConnectOptTd(row) {
            var td = "";
            td += "<td width=\"10%\">";
            td += "<select id=\"connOpt_" + row + "\" name=\"connOpt_" + row + "\">";
            td += "<option>AND</option>";
            td += "<option>OR</option>";
            td += "</select>";
            td += "</td>";
            return td;
        }

        function getColumnTd(row) {
            var td = "";
            td += "<td width=\"20%\">";
            td += "<select id=\"columnName_" + row + "\" name=\"columnName_" + row + "\">";
            $.each(columns, function (i, col) {
                td += "<option value=\"" + col.columnName + "\">" + col.columnDesc + "</option>";
            });
            td += "</select>";
            td += "</td>";
            return td;
        }

        function getOptTd(row) {
            var td = "";
            td += "<td width=\"10%\">";
            td += "<select id=\"optCode_" + row + "\" name=\"optCode_" + row + "\">";
            td += "<option value=\"eq\">等于</option>";
            td += "<option value=\"gt\">大于</option>";
            td += "<option value=\"lt\">小于</option>";
            td += "<option value=\"ge\">大于等于</option>";
            td += "<option value=\"le\">小于等于</option>";
            td += "<option value=\"ne\">不等于</option>";
            td += "<option value=\"like\">LIKE</option>";
            td += "</select>";
            td += "</td>";
            return td;
        }
        function getValueTd(row) {
            var td = "";
            td += "<td width=\"20%\">";
            td += "<input id=\"restrictValue_" + row + "\" name=\"restrictValue_" + row + "\" type=\"text\">";
            td += "</td>";
            return td;
        }
        function getBtnsTd(row) {
            var td = "";
            td += "<td width=\"20%\">";
            td += "<div class=\"restrict_toolbar\">";
            td += "<button id=\"add_restrict_btn_" + row + "\" class=\"add_restrict_btn\" onclick=\"addRestrict(this)\"></button>";
            td += "<button id=\"add_restrict_btn_" + row + "\" class=\"remove_restrict_btn\" onclick=\"removeRestrict(this)\"></button>";
            td += "</div>";
            td += "</td>";
            return td;
        }


    </script>
    <style type="text/css">
        .add_restrict_btn, .remove_restrict_btn {
            height: 20px;
            width: 20px;
        }
    </style>
</head>
<body>
<div id="dataConfigDiv">
    <form id="dataConfigForm" action="${ctx}/role/saveDataConfig.do" method="post">
        <input type="hidden" id="roleId" name="roleId" value="${roleId}">
        <table cellspacing="5" cellpadding="5" border="0" style="height:450px;width:100%">
            <tr>
                <td width="25%" valign="top">
                    <div id="data_table_div" class="ui-widget" style="height:100%">
                        <div class="ui-widget-header ui-state-default">
                            <span>可选表</span>
                        </div>
                        <div class="ui-widget-content">
                            <select id="tableId" name="tableId" size="25" style="width:100%;">
                                <c:forEach var="t" items="${tables }">
                                    <option value="${t.tableId}">${t.tableDesc}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </td>
                <td valign="top">
                    <div id="column_table_div" class="ui-widget">
                        <div class="ui-widget-header ui-state-default">
                            <span>数据限制</span>
                        </div>
                        <div class="ui-widget-content">
                            <table id="restrict_tb" cellspacing="5" cellpadding="5" border="0"
                                   style="height:100%;width:100%">

                            </table>
                            <br>

                            <div class="restrict_toolbar">
                                <button id="addRestrictBtn">添加</button>
                                <button id="delRestrictBtn">删除</button>
                            </div>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <div id="dataConfigBtns" style="float:right;">
                        <button id="saveDataConfigBtn" class="save_btn">保存</button>
                    </div>
                </td>
            </tr>

        </table>
    </form>
</div>
</body>
</html>
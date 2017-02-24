<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>主页配置</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/uuid.js"></script>
    <script type="text/javascript">
        var resourceArrayTxt = new Array();
        var resourceArrayVal = new Array();
        $(document).ready(function () {
            $("#saveBtn").button({
                        icons: {primary: "ui-icon-disk"}
                    })
                    .next()
                    .button({
                        icons: {primary: "ui-icon-close"}
                    });
            resourceArrayCreate();
            configureJson();
        });
        //创建
        function resourceArrayCreate() {
            $("#configureResource option").each(function () { //遍历全部option
                var val = $(this).val();
                var txt = $(this).text(); //获取option的内容
                resourceArrayVal.push(val);
                resourceArrayTxt.push(txt); //添加到数组中
            });
        }

        function addMoudleArr(col, row) {
            var selecthtml = "模块名称：<select id='row" + col + "Resource" + row + "'>";
            for (var i = 0; i < resourceArrayVal.length; i++) {
                selecthtml += "<option value=" + resourceArrayVal[i] + ">" + resourceArrayTxt[i] + "</option>";
            }
            selecthtml += "</select>";
            //模块大小定义
            /* selecthtml+="模块大小：<select id='"+col+"MoudleSize"+id+"'><option value='25%'>25%</option>"+
             "<option value='33%'>33%</option>"+
             "<option value='34%'>34%</option>"+
             "<option value='50%'>50%</option>"+
             "<option value='75%'>75%</option>"+
             "<option value='100%'>100%</option></select>"; */
            selecthtml += "</div>";
            return selecthtml;
        }
        function configureJson() {
            var json = $.parseJSON('${_sess_user_key.configure}');
            var html = '';
            var rowNum = json.type.split("*")[0];
            var colNum = json.type.split("*")[1];
            for (var i = 0; i < rowNum; i++) {
                html += "<tr>";
                for (var j = 0; j < colNum; j++) {
                    html += "<td>" + addMoudleArr(i, j) + "</td>";
                }
                html += "</tr>";
            }
            $("#configureBody").append(html);
            var _num = 0;
            for (var value in json) {
                if (value != "type") {
                    for (var j = 0; j < colNum; j++) {
                        $("#row" + _num + "Resource" + j).find("option[value='" + json[value][j].value + "']").attr("selected", true);
                    }
                    _num += 1;
                }
            }
            $("#configureType").find("option[value='" + json.type + "']").attr("selected", true);
        }

        //生成配置文件
        var width = "50%";//定义宽度
        var height = "400";//定义高度
        function creatJson() {
            var configureType = $("#configureType").val();
            var rowNum = configureType.split("*")[0];
            var colNum = configureType.split("*")[1];
            var json = '{"type":"' + configureType + '",';
            for (var i = 0; i < rowNum; i++) {
                json += '"Col' + i + '": [';
                for (var j = 0; j < colNum; j++) {
                    var value = $("#row" + i + "Resource" + j).val();
                    json += '{"number":"' + j + '",' + '"width":"'
                            + width + '","height":"' + height
                            + '","value":"' + value + '"},';
                }
                json = json.substring(0, json.length - 1);
                json += '],';
            }
            json = json.substring(0, json.length - 1);
            json += "}";
            indexConfigure(json);
            //----------------------------------------------
            /* if(oneCols.length!=0 && oneCols.length!=0){
             var json = '{"type":"'+configureType+'"';
             json += '"oneCols": [';
             for(var i=0;i<oneCols.length;i=i+2){
             var value = $(oneCols[i]).val();
             var width = $(oneCols[i+1]).val();
             json += '{"number":"'+i+'",' + '"width":"' + width +'","height":"' + "" + '","value":"' + value + '"},';
             }
             json = json.substring(0,json.length-1);
             json += '],';
             json += '"twoCols": [';
             for(var i=0;i<twoCols.length;i=i+2){
             var value = $(twoCols[i]).val();
             var width = $(twoCols[i+1]).val();
             json += '{"number":"'+i+'",' + '"width":"' + width +'","height":"' + "" +  '","value":"' + value + '"},';
             }
             json = json.substring(0,json.length-1);
             json += ']';
             json += "}";
             indexConfigure(json);
             }else{
             alert("保存的数据不能为空!")
             } */
        }
        function indexConfigure(configure) {
            $
                    .ajax({
                        type: "post",
                        url: "${pageContext.request.contextPath}/indexConfigure/edit.do",
                        dataType: "json",
                        data: {
                            "configure": configure
                        },
                        success: function (result) {
                            if (result == 1) {
                                alert("保存成功！");
                                closedBtn();
                            } else {
                                alert("保存失败！");
                            }
                        }
                    });
        }
        function configureType() {
            $("#configureBody tr").remove();
            var str = $("#configureType").val();
            var configureCols = str.substring(0, str.indexOf('*')); // 取子字符串。行
            var configureRow = str.substring(str.indexOf('*') + 1,
                    str.length); // 取子字符串。列
            var tableHtml = '';
            for (var i = 0; i < configureCols; i++) {
                tableHtml += "<tr>";
                for (var j = 0; j < configureRow; j++) {
                    tableHtml += "<td>" + addMoudleArr(i, j)
                            + "</td>";
                }
                tableHtml += "</tr>";
            }
            $("#configureBody").append(tableHtml);
        }
        function closedBtn() {
            $("#detailPopup").dialog("close");
        }
    </script>
</head>
<body style="margin: 0 auto;">
<div>
    <button id="saveBtn" onclick="creatJson()">保存</button>
    <button id="closedBtn" onclick="closedBtn()">取消</button>
</div>
<br>

<div>
    模板样式：
    <select id="configureType" onchange="configureType()">
        <option value="2*2">2*2</option>
        <option value="3*2">3*2</option>
    </select>
</div>
<div style="display:none;">
    <select id="configureResource" class="selcls">
        <c:forEach var="re" items="${resource}">
            <option value="<c:out value='${re.configureResourceUrl}' />">
                <c:out value='${re.configureResourceName}'/>
            </option>
        </c:forEach>
    </select>
</div>
<!-- 动态生成表格 -->
<table id="configureBody" border="1px" width="100%"></table>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>主页布局</title>
    <script type="text/javascript">
        $(function () {
            writeJson();
        });
        $(window).resize(function () {
            $('#oneDiv').css('height', ($("#content_div").height() / 2));
            $('#twoDiv').css('height', ($("#content_div").height() / 2));
        });
        //    	function backgroundColor(){
        //    		return '#'+('00000'+(Math.random()*0x1000000<<0).toString(16)).slice(-6);
        //    	}
        function writeJson() {
            <%--if('${_sess_user_key.configure}'){--%>
            <%--var json = $.parseJSON( '${_sess_user_key.configure}' );--%>
            var json = $.parseJSON('{"type":"2*1",' +
                    '"Col0": [{"number":"0","width":"50%","height":"330","value":"/dbrw/mainDbrw.do"},' +
                    '{"number":"0","width":"50%","height":"300","value":"/main/indexPage.do"}],' +
                    '"Col1": [{"number":"1","width":"50%","height":"330","value":"/xtgg/mainXtgg.do"},' +
                    '{"number":"1","width":"50%","height":"300","value":"/main/indexPage.do"}]}');
            var configureType = json.type;
            //var rowNum = configureType.split("*")[0];
            var colNum = configureType.split("*")[1];
            var bodyHtml = "";
            var i = 0;
            for (var value in json) {
                if (value != "type") {
                    for (var j = 0; j < colNum; j++) {
                        //var color = backgroundColor();
                        bodyHtml += '<div id="divrow' + i + 'Resource' + j + '" style="width:' + json[value][j].width + ';height:' + json[value][j].height + 'px;float:left;">' + '' + '</div>';
                        /* bodyHtml+='<div id="divrow'+i+'Resource'+j+'" style="width:'+ json[value][j].width +';height:'+json[value][j].height+'px;float:left;background-color: ' + color + ';">' + '' + '</div>'; */
                    }
                    i += 1;
                }
            }
            $("#userIndexBody").html(bodyHtml);

            i = 0;
            var divHeight = 0;
            for (var value in json) {
                if (value != "type") {
                    for (var j = 0; j < colNum; j++) {
                        //$("#divrow"+i+"Resource"+j).load("${pageContext.request.contextPath}"+json[value][j].value);
                        if (j == 0)divHeight += Number(json[value][j].height);
                    }
                    i += 1;
                }
            }
            setIndexDivHeight("菜单首页", (divHeight + 50));
//    		}

        }
    </script>
</head>
<body style="margin: 0 auto;">
<div id="userIndexBody" style="width:100%;height:100%;"></div>
</body>
</html>
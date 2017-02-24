<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/chat/css/chat.css"/>

    <script type="text/javascript">
        var flags = false;
        $(function () {
            $("#sendMsgBtn").button().click(function () {
                sendMessage();
            });
        });
        function sendMessage() {
            if (!checkIsNull('<%=request.getSession().getAttribute("userName")%>')) {
                if (checkIsNull(userArr) || userArr.length < 1) {//发送消息
                    alert("请选择用户！");
                    return;
                }
                if (!checkIsNull($("#txts").val()) && $.trim($("#txts").val()).length > 0) {
                    //$("#txts").val($("#txts").val().replace(/\n/g,"<br/>"));
                    $.ajax({
                        type: "post",
                        url: "${pageContext.request.contextPath}/chat/askServ",
                        dataType: "text",
                        data: {
                            "content": encodeURI($.trim($("#txts").val().replace(/\n/g, "<br/>"))),
                            "toUser": encodeURI(userArr)
                        },
                        global: false,
                        success: function (data) {
                            if (!checkIsNull(data)) {
                                var sd = $(".chat01_content").html() + "<br>" + decodeURI(data) + "<br>";
                                $(".chat01_content").html(sd);
                                scrollBottom("chat_content");
                                $("#txts").val("");
                                $("#txts").focus();

                            }
                        },
                        error: function (data) {
                            //alert(data);
                        }
                    });
                } else {
                    alert("发送内容不能为空!");
                }
            } else {
                alert("当前登陆已超时，请重新登陆系统");
            }
        }
        function getMsgLists() {//读取客户消息
            if (!checkIsNull('<%=request.getSession().getAttribute("userName")%>'))
                if (!checkIsNull(userArr) && userArr.length > 0) {
                    $.ajax({
                        type: "post",
                        url: "${pageContext.request.contextPath}/chat/askServ",
                        dataType: "text",
                        data: {
                            "msgFlag": 1,
                            "toUser": encodeURI(userArr)
                        },
                        global: false,
                        success: function (data) {
                            if (!checkIsNull(data)) {
                                var sd = $(".chat01_content").html() + "<br>" + decodeURI(data).replace(/\n/g, "<br/>") + "<br>";
                                $(".chat01_content").html(sd);
                                scrollBottom("chat_content");
                                $("#txts").focus();
                            }
                        },
                        error: function (data) {
                            //alert("error:"+data);
                        }
                    });
                }
        }

        var userArr = [];
        function getUserLists() {//读取用户列表
            if (!checkIsNull('<%=request.getSession().getAttribute("userName")%>'))
                $.ajax({
                    type: "post",
                    url: "${pageContext.request.contextPath}/chat/getUserLists.do",
                    dataType: "json",
                    data: {
                        "servMsgFlag": 1,
                        "timer": new Date()
                    },
                    global: false,
                    success: function (data) {

                        if (!checkIsNull(data)) {
                            //	alert(data+"--size"+data.length+"--"+data[0].userMsg.length);
                            var tmpUser = userArr;
                            var selected = "";
                            var count = "";
                            var msgStyle = "";
                            var liCss = "";
                            var tmpUsers = "";
                            $("#toUser > option").remove();
                            $("#userList").html("");
                            for (var i = 0; i < data.length; i++) {
                                if (!checkIsNull(data[i].userName))
                                    if (data[i].userName == '<%=request.getSession().getAttribute("userName")%>') continue;
                                for (var j = 0; j < data[i].userMsg.length; j++) {
                                    if (data[i].userMsg[j].toUser == "<%=request.getSession().getAttribute("userName")%>") {
                                        count = data[i].userMsg[j].sendMsg.length;
                                        break;
                                    }
                                }
                                if (count == 0 || checkIsNull(count)) {
                                    count = "";
                                } else {
                                    count = "(<font color=\'red\'>" + count + "</font>)";
                                }
                                if (!checkIsNull(userArr) && userArr.length > 0) {
                                    for (var a = 0; a < userArr.length; a++) {
                                        if (data[i].userName == userArr[a]) {
                                            selected = "selected=\\\"selected\\\"";
                                            liCss = "class=\"choosed\"";
                                            break;
                                        } else {
                                            selected = "";
                                            liCss = "";
                                        }
                                    }
                                }
                                var li = "<li id=\"" + data[i].userName + "\" onclick=\"setLiCss(\'" + data[i].userName + "\')\" " + liCss + "><label class=\"online\"></label> <a href=\"javascript:;\"><img src=\"${pageContext.request.contextPath}/chat/img/20141030091107.png\"></a>" +
                                        "<a href=\"javascript:;\" class=\"chat03_name\">" + data[i].userName + count + "</a></li>";
                                $('#userList').append(li);
                                count = "";
                                msgStyle = "";
                            }
                        } else {
                            alert("没有查询到用户!");
                        }
                    },
                    error: function (data) {
                        //alert(data);
                    }
                });
        }
        function sendMsg() {
            if (event.keyCode == 13) {
                $("#sendMsgBtn").click();
            }
        }
        function setLiCss(liId) {
            // alert("添加clss"+$("#"+liId).attr("class"));
            if (!checkIsNull($("#" + liId).attr("class"))) {
                if ($("#" + liId).attr("class") == "choosed") {
                    $("#" + liId).removeClass('choosed');
                    if (userArr != null && userArr.length > 0) {
                        for (var i = 0; i < userArr.length; i++) {
                            if (userArr[i] == liId) {
                                userArr.splice(i, 1);
                                break;
                            }
                        }
                    }
                } else {
                    $("#" + liId).addClass('choosed');
                    userArr.push(liId);
                }
            } else {
                $("#" + liId).addClass('choosed');
                userArr.push(liId);
            }
            $("#recever").html("");
            for (var j = 0; j < userArr.length; j++) {
                $("#recever").html($("#recever").html() + userArr[j]);
                if (j != userArr.length - 1)  $("#recever").append(",");
            }
        }

        function closeWin() {
            $(".chatBox").hide();
            stopInterval();
        }
        var getUserInterval = "";
        var getMsgInterfal = "";
        function stopInterval() {
            if (getUserInterval != "") {
                window.clearInterval(getUserInterval);
                getUserInterval = "";
            }
            if (getMsgInterfal != "") {
                window.clearInterval(getMsgInterfal);
                getMsgInterfal = "";
            }
            timer = setInterval("setMessageCount()", 3000);
        }

        getUserInterval = window.setInterval(getUserLists, 2000);
        getMsgInterfal = window.setInterval(getMsgLists, 1000);
    </script>

    <title>在线即时消息</title>
</head>
<body>
<center>
    <!-- -------------------------------------聊天窗口------------------------------------------------- -->
    <div class="content">
        <div class="chatBox">
            <div class="chatLeft">
                <div class="chat01">
                    <div class="chat01_title">
                        <ul class="talkTo">
                            <li><a href="javascript:;" id="recever">消息接收人</a></li>
                        </ul>
                        <a class="close_btn" href="javascript:;" onclick="closeWin()"></a>
                    </div>
                    <div class="chat01_content" id="chat_content">
                        <div class="message_box mes1">
                        </div>
                        <div class="message_box mes2">
                        </div>
                        <div class="message_box mes3" style="display: block;">
                        </div>
                        <div class="message_box mes4">
                        </div>
                        <div class="message_box mes5">
                        </div>
                        <div class="message_box mes6">
                        </div>
                        <div class="message_box mes7">
                        </div>
                        <div class="message_box mes8">
                        </div>
                        <div class="message_box mes9">
                        </div>
                        <div class="message_box mes10">
                        </div>
                    </div>
                </div>
                <div class="chat02">
                    <div class="chat02_title">
                        <a class="chat02_title_btn ctb01" href="javascript:;" style="display:none"></a><a
                            class="chat02_title_btn ctb02"
                            href="javascript:;" title="选择文件" style="display:none">
                        <embed width="15" height="16"
                               flashvars="swfid=2556975203&amp;maxSumSize=50&amp;maxFileSize=50&amp;maxFileNum=1&amp;multiSelect=0&amp;uploadAPI=http%3A%2F%2Fupload.api.weibo.com%2F2%2Fmss%2Fupload.json%3Fsource%3D209678993%26tuid%3D1887188824&amp;initFun=STK.webim.ui.chatWindow.msgToolBar.upload.initFun&amp;sucFun=STK.webim.ui.chatWindow.msgToolBar.upload.sucFun&amp;errFun=STK.webim.ui.chatWindow.msgToolBar.upload.errFun&amp;beginFun=STK.webim.ui.chatWindow.msgToolBar.upload.beginFun&amp;showTipFun=STK.webim.ui.chatWindow.msgToolBar.upload.showTipFun&amp;hiddenTipFun=STK.webim.ui.chatWindow.msgToolBar.upload.hiddenTipFun&amp;areaInfo=0-16|12-16&amp;fExt=*.jpg;*.gif;*.jpeg;*.png|*&amp;fExtDec=选择图片|选择文件"
                               data="upload.swf" wmode="transparent" bgcolor="" allowscriptaccess="always"
                               allowfullscreen="true"
                               scale="noScale" menu="false" type="application/x-shockwave-flash"
                               src="http://service.weibo.com/staticjs/tools/upload.swf?v=36c9997f1313d1c4"
                               id="swf_3140">
                    </a><a class="chat02_title_btn ctb03" href="javascript:;" title="选择附件" style="display:none">
                        <embed width="15" height="16"
                               flashvars="swfid=2556975203&amp;maxSumSize=50&amp;maxFileSize=50&amp;maxFileNum=1&amp;multiSelect=0&amp;uploadAPI=http%3A%2F%2Fupload.api.weibo.com%2F2%2Fmss%2Fupload.json%3Fsource%3D209678993%26tuid%3D1887188824&amp;initFun=STK.webim.ui.chatWindow.msgToolBar.upload.initFun&amp;sucFun=STK.webim.ui.chatWindow.msgToolBar.upload.sucFun&amp;errFun=STK.webim.ui.chatWindow.msgToolBar.upload.errFun&amp;beginFun=STK.webim.ui.chatWindow.msgToolBar.upload.beginFun&amp;showTipFun=STK.webim.ui.chatWindow.msgToolBar.upload.showTipFun&amp;hiddenTipFun=STK.webim.ui.chatWindow.msgToolBar.upload.hiddenTipFun&amp;areaInfo=0-16|12-16&amp;fExt=*.jpg;*.gif;*.jpeg;*.png|*&amp;fExtDec=选择图片|选择文件"
                               data="upload.swf" wmode="transparent" bgcolor="" allowscriptaccess="always"
                               allowfullscreen="true"
                               scale="noScale" menu="false" type="application/x-shockwave-flash"
                               src="http://service.weibo.com/staticjs/tools/upload.swf?v=36c9997f1313d1c4"
                               id="swf_3140">
                    </a>
                        <label class="chat02_title_t">
                            <a href="javascript:alert('正在建设中。。');" style="display:none">聊天记录</a>
                            当前用户：${userName }
                        </label>

                        <div class="wl_faces_box">
                            <div class="wl_faces_content">
                                <div class="title">
                                    <ul>
                                        <li class="title_name">常用表情</li>
                                        <li class="wl_faces_close"><span>&nbsp;</span></li>
                                    </ul>
                                </div>
                                <div class="wl_faces_main">
                                    <ul>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_01.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_02.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_03.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_04.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_05.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_06.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_07.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_08.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_09.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_10.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_11.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_12.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_13.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_14.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_15.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_16.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_17.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_18.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_19.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_20.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_21.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_22.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_23.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_24.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_25.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_26.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_27.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_28.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_29.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_30.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_31.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_32.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_33.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_34.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_35.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_36.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_37.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_38.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_39.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_40.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_41.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_42.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_43.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_44.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_45.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_46.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_47.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_48.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_49.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_50.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_51.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_52.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_53.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_54.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_55.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_56.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_57.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_58.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_59.gif"/></a></li>
                                        <li><a href="javascript:;">
                                            <img src="${pageContext.request.contextPath}/chat/img/emo_60.gif"/></a></li>
                                    </ul>
                                </div>
                            </div>
                            <div class="wlf_icon">
                            </div>
                        </div>
                    </div>
                    <div class="chat02_content">
                        <textarea id="txts" onkeypress="sendMsg()"></textarea>
                    </div>
                    <div class="chat02_bar">
                        <ul>

                            <li style="right: 5px; top: 5px;"><a href="javascript:;">
                                <button id="sendMsgBtn">发送</button>
                                <!--
                                <img src="${pageContext.request.contextPath}/chat/img/send_btn.jpg" id="send" onclick="sendMessage()"></a></li>
                             -->
                        </ul>
                    </div>
                </div>
            </div>
            <div class="chatRight">
                <div class="chat03">
                    <div class="chat03_title">
                        <label class="chat03_title_t">
                            成员列表</label>
                    </div>
                    <div class="chat03_content">
                        <ul id="userList">
                            <li>

                                &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
                                <img src="${pageContext.request.contextPath }/theme/system/default/images/loading.gif"><br>&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp; 正在加载用户列表
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div style="clear: both;">
            </div>
        </div>
    </div>
    <p></p>

    <p></p>
    </div>
</center>
</body>
</html>
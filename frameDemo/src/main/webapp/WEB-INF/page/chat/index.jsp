<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <jsp:include page="/common/common.jsp" />
    <script type="text/javascript">
        $(function () {
            $("#chatDialog").dialog({
                title:"在线即时消息系统",
                autoOpen: false,
                position: {my: "top", at: "top+50", of: window},
                modal:true,
                resizable: true,
                width:"800",
                height:500
            });

            var _url = "${pageContext.request.contextPath}/chat/openInstantMessaging.do";
            $.loadDiv("chatDialog", _url, {});
            $("#chatDialog").dialog("open");

            $("#chatDialog").on("dialogclose", function (event, ui) {
                $("#chatDialog").html("");
                stopInterval();
            });

        });
    </script>
    <title>在线即时消息</title>
</head>
<body >
<div id="chatDialog"></div>
</body>
</html>
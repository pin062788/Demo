<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("decorator", "none");
    response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
    response.setHeader("Pragma", "no-cache"); //HTTP 1.0
    response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <script type="text/javascript">
        $(function () {
            $("#flowImages").attr("src", "${pageContext.request.contextPath}/images/6.jpg");
        });
    </script>
</head>
<body style="background:#ebeced;">
<div id="menuss" style=" text-align:center; padding-top:30px; background:#ebeced;">
    <img id="flowImages" src="${pageContext.request.contextPath}/images/6.jpg" alt=""/>
</div>

</body>
</html>
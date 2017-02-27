<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>警务系统</title>

    <link rel="stylesheet" class="system-theme" type="text/css" href="${pageContext.request.contextPath}/css/login.css"/>

    <script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery-1.11.1.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {

            $(".login_info_select").click(function (index) {
                $(".login_info_select").removeClass("login_info_select_active");
                $(this).toggleClass("login_info_select_active");
                $("#sys_full_name").html("警务系统" + $(this).attr("property"));
            });

            if (document.getElementById("content_div")) {
                window.top.location = "${pageContext.request.contextPath}/login.jsp";
            }
            if (window.opener != null) {
                window.opener.top.location = "${pageContext.request.contextPath}/login.jsp";
                window.close();
            }

            var messages = {
                userName: {required: "登录名称不能为空!"},
                password: {required: "登录密码不能为空!"}
            };
            //需要验证的字段名称和验证规则
            var rules = {
                userName: {required: true},
                password: {required: true}
            };
            //封装成MAP类型的对象
            var validateMap = {
                "formId": "frmLogin",
                "showErrorId": "error",
                "rules": rules,
                "messages": messages
            };
            //开启验证规则
            validateForm(validateMap);
        });
        function verify() {
            var userName = $("#userName").val();
            var password = $("#password").val();
            if (userName=="" || password=="") {
                $("#error").html("用户名或密码错误，请重新输入!");
                return false;
            }
            return true;
        }
    </script>

</head>
<body class="login_bg" style="overflow:-Scroll;overflow-x:hidden">
<div class="login_top">
    <div class="login_logo"></div>
</div>
<div class="login_main">
    <div class="login_left">
        <form id="frmLogin" name="frmLogin" action="${pageContext.request.contextPath}/main/loginAuth.do" target="_top"
              method="post">
            <table class="login_left_table1">
                <tr class="login_left_tr">
                    <td class="login_info_select login_info_select_active" property="">
                        <img alt="" src="${pageContext.request.contextPath}/images/login/login_default.png">
                    </td>
                    <td class="login_info_area">
                        <p style="vertical-align: top;" class="login_info">
                            <b id="sys_full_name">登录信息</b>
                        </p>
                        <p style="vertical-align: top;" class="error">
                            <div id="error"></div>
                        </p>
                        <p class="login_info">
                            <input type="text" id="userName" name="userName" class="login_user_bg" maxlength="20"
                                   value="">
                        </p>
                        <p class="login_info">
                            <input type="password" id="password" name="password" class="login_pwd_bg" maxlength="20"
                                   value=""></p>
                        <p class="login_info">
                            <button type="submit" value=" " class="login_btn" onClick="return verify()">登录</button>
                            &nbsp;
                            <button type="reset" value=" " class="login_btn">重置</button>
                        </p>
                        <table width="100%" >
                        <tr>
                        <td></td>
                        <td align="left">
                        </td>
                        </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div class="login_right">
        <img alt="" src="${pageContext.request.contextPath}/images/login/login_right.png">
    </div>
</div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery.showLoading.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery.validate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/common.js"></script>
</html>
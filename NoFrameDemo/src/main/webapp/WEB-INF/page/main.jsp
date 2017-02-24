<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page import="com.ocellus.platform.utils.DateUtil" %>
<%@ page import="com.ocellus.platform.utils.WebUtil" %>
<%@ page import="com.ocellus.platform.utils.Constants" %>
<%@ page import="com.ocellus.platform.utils.TipMsgUtil" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>调查问卷系统</title>
    <script>
        var ctx = "${ctx}";
    </script>
    <link rel="stylesheet" class="ui-theme" type="text/css"
          href="${ctx}/theme/jqueryui/start/jquery-ui.min.css"/>
    <link rel="stylesheet" type="text/css"
          href="${ctx}/theme/layout/default/layout-default-latest.css"/>
    <link rel="stylesheet" type="text/css"
          href="${ctx}/theme/zTreeStyle/zTreeStyle.css"/>
    <link rel="stylesheet" type="text/css"
          href="${ctx}/theme/grid/ui.jqgrid.css"/>
    <link rel="stylesheet" type="text/css"
          href="${ctx}/theme/system/blue/css/showLoading.css"/>
    <link rel="stylesheet" type="text/css"
          href="${ctx}/theme/system/blue/css/style.css"/>
    <link rel="stylesheet" type="text/css"
          href="${ctx}/theme/system/blue/css/style2.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/theme/common.css"/>

    <link type="image/x-icon" rel="shortcut icon"
          href="${ctx}/images/favicon.ico"/>
    <link type="image/x-icon" rel="icon" href="${ctx}/images/favicon.ico"/>
    <link type="image/x-icon" rel="bookmark"
          href="${ctx}/images/favicon.ico"/>

    <link rel="stylesheet"
          href="${ctx}/theme/system/blue/css/jquery.webui-popover.min.css"/>
    <script type="text/javascript" src="${ctx }/scripts/jquery-1.11.1.js"></script>
    <script type="text/javascript"
            src="${ctx }/scripts/jquery.contextmenu.r2.js"></script>
    <script type="text/javascript" src="${ctx }/scripts/jquery-ui.min.js"></script>
    <script type="text/javascript"
            src="${ctx }/scripts/jquery.layout-latest.js"></script>
    <script type="text/javascript"
            src="${ctx }/scripts/jquery.ztree.all-3.5.min.js"></script>
    <script type="text/javascript" src="${ctx }/scripts/grid.locale-cn.js"></script>
    <script type="text/javascript"
            src="${ctx }/scripts/jquery.jqGrid.min.js"></script>
    <script type="text/javascript" src="${ctx }/scripts/grid.setcolumns.js"></script>
    <script type="text/javascript"
            src="${ctx }/scripts/jquery.showLoading.min.js"></script>
    <script type="text/javascript" src="${ctx }/scripts/common.js"></script>

    <jsp:include page="common/customDatepicker.js.jsp"></jsp:include>
    <jsp:include page="common/common.js.jsp"></jsp:include>

    <script type="text/javascript">
        var timer;
        var messageDivId = "msg" + new Date().getTime();
        var ctx = "${ctx}";
        /*
         *jqgrid显示、隐藏表头功能配置,  不需要控制的列 colModel行中加属性{，hidedlg:true}
         *配置参数,参照http://www.coding123.net/article/20130705/jqGrid-show-hide-columns.aspx
         */
        var jqGridCloseColOptions = {
            jqModal: true,
            top: 125,
            left: 200,
            dataheight: 300,
            closeOnEscape: true,
            ShrinkToFit: false,
            colnameview: false,
            updateAfterCheck: true
        };

        function resetWidth() {
            var tags = document.getElementsByTagName("table");
            for (var k in tags) {
                var tag = tags[k];
                if (tag.className == "ui-jqgrid-btable") {
                    $('#' + tag.id).setGridWidth($('#tabs').width() - 35);
                }
            }
        }
        $(document).ready(
                function () {
                    $("#popTopTip").hide();

                    $(document).bind("contextmenu", function (e) {
                        return false;
                    });
                    $(document).click(function () {
                        $(".context_menu").hide();
                    });
                    $(document).ajaxStart(function () {
                        $("#loading").showLoading();
                    });
                    $(document).ajaxComplete(function (event, request, settings) {
                        //custom process for ajax request e.g. session time out, etc.
                    });
                    $(document).ajaxStop(function () {
                        $("#loading").hideLoading();
                    });

                    $('#container').layout({
                        defaults: {
                            spacing_open: 0
                        },
                        north: {
                            size: 113
                        },
                        west: {
                            spacing_open: 2,
                            size: 202
                        },
                        south: {
                            size: 30
                        },
                        center: {
                            onresize_end: resetWidth
                        }
                    });

                    //Theme switch
                    $("#themeList").menu().position({
                        my: "left top",
                        at: "left bottom",
                        of: "#themeSelector"
                    }).hide();

                    $("#themeList").children().each(function () {
                        $(this).click(function () {
                            $("#themeList").hide();
                            var _themeName = $(this).attr('theme-name');
                            setTheme(_themeName);
                            setCookie("_client_theme", _themeName, 30);
                        });
                    });

                    $("#themeSelector").button({
                        icons: {
                            secondary: "ui-icon-triangle-1-s"
                        }
                    }).click(function () {
                        $("#themeList").toggle();
                    });
                    var _client_theme = getCookie("_client_theme");
                    if (!_client_theme) {
                        _client_theme = "start";
                    }
                    setTheme(_client_theme);
                    function setTheme(_themeName) {
                        var themeHref = "${ctx}/theme/jqueryui/" + _themeName
                                + "/jquery-ui.min.css";
                        var systemThemeHref = "${ctx}/theme/system/" + _themeName
                                + "/css/style2.css";
                        $("link.ui-theme").attr("href", themeHref);
                        $("link.system-theme").attr("href", systemThemeHref);
                    }

                    //search
                    $("#siteSearchBtn").button({
                        icons: {
                            primary: "ui-icon-search"
                        }
                    }).click(
                            function () {
                                var tags = $.trim($("#tags").val());
                                if (tags) {
                                    //var url = "${ctx}/main/showSearchList.do?criterion="+encodeURI(tags);
                                    //$("#content_div").load(url);
                                    //	locationTabUrl(url);
                                    $.getAjax("${ctx }/main/searchLeftTree.do", {
                                        criterion: encodeURI(tags)
                                    }, null, function (data) {
                                        $.fn.zTree.init($("#ntree"),
                                                menuTreeSetting, data);
                                        showSearchMenu("ntree");
                                    });
                                    //searchNodeOpen(url);
                                }
                            });
                    $(".ui-layout-pane-west").css({
                        "top": "132px"
                    });
                    $(".ui-layout-center").css({
                        "top": "132px"
                    });//151
                    $(".ui-layout-resizer-west-open").css({
                        "top": "132px"
                    });
                    $(".ui-layout-pane-north").css({
                        "height": "131px"
                    });

                    //var message = new messagePopup(messageDivId, 300, 150, "短消息提示：", "<a href='javascript:openMessage();' >您有新消息未读...</a>");
                    //message.show();
                    //timer = window.setInterval("setMessageCount()",3000);
                    //$("#content_div").load("${ctx}/userIndex.jsp");
                    //locationTabUrl("${ctx}/userIndex.jsp");
                    var lr_systembtn = $("#lr_systembtn");
                    var lr_menu = $("#lr_menu");
                    lr_systembtn.mouseenter(function () {
                        if (lr_menu.find("a").length > 0) {
                            if (lr_menu.find("dl").length > 15)
                                lr_menu.css("height", "400px");
                            else
                                lr_menu.css("height", '');
                            t_delay = setTimeout(function () {
                                lr_menu.fadeIn("slow");
                            }, 200);
                        }
                    });
                    lr_menu.mouseleave(function () {
                        clearTimeout(t_delay);
                        lr_menu.fadeOut("slow");
                    });


                });

        //回车后搜索
        function toSearch() {
            var lKeyCode = (navigator.appname == "Netscape") ? event.which
                    : window.event.keyCode; //event.keyCode按的建的代码，13表示回车
            if (lKeyCode == 13) {
                $("#siteSearchBtn").click();
            }
        }
        function openMessage() {
            //$("#content_div").load("${ctx }//messageCenter/messageList.do");
            openDialogDiv("chatPopup", "${ctx }/messageCenter/messageList.do",
                    "短消息", true);
            $("#" + messageDivId).hide();
        }
        function openChat() {
            //$("#content_div").load("${ctx }/chat/toInstantMessaging.do");
            openDialogDiv("chatPopup", "${ctx }/chat/toInstantMessaging.do", "即时通信");
            $("#messageCount").html("");
            window.clearInterval(timer);
        }
        function setMessageCount() {
            $.ajax({
                type: "post",
                url: "${ctx }/chat/getMessageCount.do",
                dataType: "json",
                global: false,
                success: function (result) {
                    if (result.data == '0')
                        $("#messageCount").html("");
                    else
                        $("#messageCount").html(" (" + result.data + ")");
                }
            });
        }
        /*
         * 设置模块大小信息
         */
        function configureBtn() {
            $("#detailPopup").dialog({
                width: 700,
                title: "模块大小 "
            });
            var _url = "${pageContext.request.contextPath}/indexConfigure/show.do";
            $("#detailPopup").load(_url);
        }
    </script>
    <style type="text/css">
        body, dl, dt, dd {
            padding: 0px;
            margin: 0px;
        }

        .lr_systembtn {
            width: 20px;
            height: 35px;
            line-height: 33px;
            position: absolute;
            right: 0px;
            z-index: 100004;
        }

        .lr_menu {
            width: 184px;
            padding: 8px 6px 8px 6px;
            background-color: #ffffff;
            border: #ACACAC solid 2px;
            filter: alpha(opacity=90);
            opacity: 0.90;
            position: absolute;
            top: 35px;
            right: 0px;
            z-index: 100005;
            display: none;
        }

        .lr_menu dl {
            width: 100%;
            display: block;
            overflow: hidden;
        }

        .lr_menu a {
            width: 100%;
            display: block;
            color: #666666;
            border-bottom: #ACACAC dashed 1px;
            height: 30px;
            line-height: 30px;
            font-size: 14px;
            background-repeat: no-repeat;
            background-position: 6px center;
        }

        .lr_menu a:hover {
            background-color: #E2E2E2;
            color: #333333;
            text-decoration: none;
        }

        .lr_menu dt a {
            text-indent: 11px;
            text-decoration: none;
        }

        .ui-button-icon-only .ui-button-text, .ui-button-icons-only .ui-button-text {
            padding: 0;
        }
    </style>
</head>

<body>
<div id="loading" style="height: 100%;">
    <div id="container" style="height: 100%; width: 100%;">
        <!-- 正文 -->
        <div id="content_div" class="pane ui-layout-center">
            <div id="tabs">
                <ul id="tabsUl">
                    <li id="tabsUlIndex_li"
                        onclick="locationTabAccord('/main/mainPage.do','菜单首页','tabsUlIndex')"><a
                            href="javascript:void(0);">菜单首页</a></li>
                    <!--  <span class="ui-icon ui-icon-circle-triangle-e"  onclick="tabsgo(1)" style="float:right;margin: 16px 4px;"></span>
            <span class="ui-icon ui-icon-circle-triangle-w" onclick="tabsgo(0)" style="float:right;margin: 16px 4px;"></span> -->
                    <span id="lr_systembtn" class="ui-icon ui-icon-circle-triangle-e"
                          style="float: right; margin: 6px 4px;"></span>

                    <div id="lr_menu" class="lr_menu" style="overflow: auto; border-color: black; width: 130px">
                    </div>
                </ul>
            </div>
        </div>
        <!-- header -->
        <div class="pane ui-layout-north">
            <div class="bannerbg">
                <%--<div style="position: relative; top: 30px;font-size: 48px;
                font-weight: bold;font-style: italic ;color: transparent;background-color : cornsilk;
                text-shadow : rgba(255,255,255,0.5) 0 5px 6px, rgba(255,255,255,0.2) 1px 3px 3px;
                -webkit-background-clip : text;text-align:left;letter-spacing: inherit;">
                    调 查 问 卷 系 统
                </div>--%>
                <div style="position: relative; left: -330px; top: 25px">
                    <img alt="" src="${ctx}/theme/system/default/images/login/login_logo.png">
                </div>
                <div style="position: absolute; top: 123px; float: left">
                    <ul id="kjcd" style="padding-left: 5px">
                        <li style="color: black; height: 30px; background-color: #EBF1FB">快捷菜单栏</li>
                        <c:forEach items="${shortcutMenu}" var="menus">
                            <li
                                    onclick="toAction('${menus.resourceUrl }','${menus.resourceName }',this)"><a
                                    href="#">${menus.resourceName }</a></li>
                        </c:forEach>
                    </ul>
                </div>
                <!-- <div style="background-image:url('/IMEP/images/17.png');width: 100%;height: 4px;position: absolute;top: 149px;left: 88px;display:none"></div> -->
            </div>
            <!-- header 右边 导航 -->
            <div class="ui-widget-header" style="text-align: right; width: 100%; height: 100px">
                <div class="ui-state-default ui-accordion-header-active ui-state-active ui-corner-top ui-accordion-icons">
                    <table border="0" width="100%">
                        <tbody>
                        <tr>
                            <td colspan="16">
                                <div class="div_navigation">
                                    <shiro:authenticated>
                                        <span class="span_navigation">
                                            <span>
                                                <div></div>
                                            </span>
                                        </span>
                                        <span class="span_navigation_customer">
                                            <a href="${pageContext.request.contextPath}/main/logout.do"
                                               target="_parent" onClick="return self.confirm('确定需要注销吗?')">
                                                <span style="color: #FFF; text-decoration: underline;">注销</span>
                                            </a>
                                            <font style="color: #FFF; ">
                                                欢迎您：<%=WebUtil.getLoginUserName()%><%--<shiro:principal />--%>
                                                &nbsp;&nbsp;
                                                组织单元：<%=WebUtil.getLoginUser().getGroups()%>&nbsp;&nbsp;
                                                操作日期：<%=DateUtil.date2Str(DateUtil.getDate(), DateUtil.DATE_PATTERN)%>
                                            </font>
                                        </span>
                                    </shiro:authenticated>
                                    <button id="themeSelector"
                                            style="width: 100px; display: none">主题
                                    </button>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="16">
                                <div style="margin-top: -100px; margin-left: 800px">
                                    <button id="configureBtn" onclick="configureBtn()"
                                            style="display: none">设置模块大小
                                    </button>
                                    <img alt="即时通信"
                                         style="vertical-align: middle; height: 20px; display: none"
                                         src="${ctx}/images/content.png" onclick="openChat()"/><span
                                        style="padding-right: 10px; color: red;"
                                        id="messageCount">${messageCount}</span>
                                    <input type="text" id='tags'
                                           style="width: 213px; display: none" onkeypress="toSearch();"
                                           style="display:none">
                                    <button id="siteSearchBtn" style="display: none">搜索</button>
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <a href="javascript:openModifyPassword();">
                                        <shiro:authenticated>
                                        <font style="color: white; display: none">
                                            欢迎您：<%=WebUtil.getLoginUserName()%><%--<shiro:principal />--%>&nbsp;&nbsp;
                                            组织单元：<%=WebUtil.getLoginUser().getGroups()%>&nbsp;&nbsp;
                                            操作日期：<%=DateUtil.date2Str(DateUtil.getDate(), DateUtil.DATE_PATTERN)%>
                                        </font>
                                    </a>&nbsp;&nbsp;
                                    <a href="${pageContext.request.contextPath}/main/logout.do" target="_parent"
                                       onClick="return self.confirm('确定需要注销吗?')">
                                        <span style="color: #FFF; text-decoration: underline; display: none">注销</span>
                                    </a>&nbsp;
                                    </shiro:authenticated>
                                    <button id="themeSelectors" style="width: 100px; display: none">主题</button>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <!-- 左侧导航 -->
        <div id="menu_div" class="pane ui-layout-west" style="top: 1150px">
            <div style="margin: 5px; height: 95%">
                <ul id="menuTree" class="ztree" style="display: none"></ul>
                <div id="accordion"></div>
            </div>
        </div>
    </div>
    <div class="pane ui-layout-south " style="overflow: hidden;">
        <div class="ui-widget-header" style="height: 30px;"></div>
    </div>

    <ul id="themeList" style="width: 100px;">
        <c:forEach var="theme" items="${themes }">
            <li theme-name="${theme.themeName }">
                <a>
                    <img alt="" src="../images/${theme.themeIcon }">
                    <span>${theme.themeDesc }</span>
                </a>
            </li>
        </c:forEach>
    </ul>
</div>
</div>
<div id="detailPopup"></div>
<div id="chatPopup"></div>
<div id="popTopTip" class="webui-popover top in"
     style="width: 215px; top: 50px; left: 550px; display: none">
    <div class="webui-popover-inner">
        <div class="webui-popover-content" align="center">
            <br> 窗口开多了会影响页面打开速度！
        </div>
    </div>
    <div class="contextMenu" id="myMenu" style="background-color: #fff">
        <ul>
            <li id="contextMenu_colse">关闭当前</li>
            <li id="contextMenu_colse_other">关闭其它</li>
            <li id="contextMenu_colse_all">关闭所有</li>
        </ul>
    </div>
</div>
</body>
<script type="text/javascript" src="${ctx }/scripts/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx }/scripts/validate.expand.js"></script>
<script type="text/javascript" src="${ctx }/scripts/jquery.form.js"></script>
<script type="text/javascript" src="${ctx }/scripts/messageWindow.js"></script>
<script type="text/javascript" src="${ctx }/scripts/menu.js"></script>
<script type="text/javascript" src="${ctx }/scripts/grid.button.js"></script>
<script type="text/javascript"
        src="${ctx }/scripts/jquery.ui.datepicker-zh-CN.js"></script>
<script type="text/javascript" src="${ctx }/scripts/linq.min.js"></script>
</html>

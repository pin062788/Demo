<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <jsp:include page="/common/common.jsp" />
    <script type="text/javascript" src="${ctx}/script/main.js"></script>
    <script type="text/javascript">
        /***设置JS全局的站点路径***/
        var ctx = "${ctx}";
        $(function(){
            clockon();
        });
        
    </script>
</head>
<body style="height:100%;">
<div id="loading" style="height:100%;">
    <div id="container" class="container">
        <div class="pane ui-layout-north">
            <div class="bannerbg">
                <div style="position:relative;top:15px;left:25px;text-align: left;">
                    <img alt="" src="${ctx}/css/images/banner_logo.gif">
                </div>
                <div style="position:relative;top:5px;right:125px;float:right;text-align: left; color: #000000; font-size: 16px;">
                	<span>${loginUserName} <a href="${ctx}/main/logout.do" target="_parent" onclick="return self.confirm('Are you sure to Logout?')">Logout</a></span>
                </div>
                <div style="position:relative;top:5px;right:200px;float:right;text-align: left; color:black;">
                    <span id="bgclock"></span>
                </div>
            </div>
        </div>
        <div class="pane ui-layout-center" style="height:100%; width:100%;padding:0px 2px 0px 2px;">
            <div id="content_div" style="height:100%;width:100%;">
                <div id="mainTabs">
                    <ul id="mainTabsUl" class="tabsUl">
                        <div style="float:right; margin: 10px 4px 0px 0px;">
                            <span id="mainTabsGoLeft" class="ui-icon ui-icon-circle-triangle-w" onclick="mainTabsGo('left')" style="float:left; cursor: pointer;" title="选项卡向左移动"></span>
                            <span id="mainTabsGoRight" class="ui-icon ui-icon-circle-triangle-e" onclick="mainTabsGo('right')" style="float:left; cursor: pointer;" title="选项卡向右移动"></span>
                        </div>
                    </ul>
                </div>
            </div>
        </div>

        <div id="main_pane_ui_layout_west_tree_accordion" class="pane ui-layout-west">
            <div style="padding:0px 2px 0px 2px;height:100%; overflow: hidden;">
                <ul id="mainMenuTree" class="ztree" style="display:none;"></ul>
                <!-- 手风琴 -->
                <div id="mainAccordion" style="height: 100%"></div>
            </div>
        </div>
    </div>

    <div id="mainTabTooMuchTip" class="webui-popover top in" style="width: 215px; top: 50px; left: 550px;display: none;">
        <div class="webui-popover-inner">
            <div class="webui-popover-content" align="center" style="padding-top: 20px;"></div>
        </div>
    </div>
    <div id="popTopTip" class="webui-popover top in"
         style="width: 215px; top: 50px; left: 550px; display: none">
        <div class="contextMenu" id="mainTabsMenu" >
            <ul>
                <li id="contextMenu_colse">关闭当前</li>
                <li id="contextMenu_colse_other">关闭其它</li>
                <li id="contextMenu_colse_all">关闭所有</li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>

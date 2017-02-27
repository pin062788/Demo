var mainMenuTreeObj = null; //左侧隐藏树形菜单对象
var mainAccordionObj = null; //左侧手风琴对象
var mainTabsObj = null; //主页Tab选项卡对象
//Main Menus
var mainMenuTreeSetting = {
	data : {
		key : {name :"resourceName", children:"children" }
	},
	simpleData: {enable: true, idKey: "resourceCode", pIdKey: "parentResourceCode", rootPId:null}
};
//初始化左侧手风琴下面的树形菜单
var subTreeSetting = {
	data : {
		key : {name : "resourceName", children : "children"}
	},
	simpleData : {enable : true, idKey : "resourceCode", pIdKey : "parentResourceCode", rootPId : null},
	view : {showLine : true,showIcon: true },
	callback : {
		beforeClick: function(treeId, treeNode){
			return (treeNode && !treeNode.isParent);
		},
		onClick: function(event, treeId, treeNode) {
			if(!checkIsNull(treeNode.resourceUrl)){
				openTabByUrl(treeNode.resourceUrl);
			}
		}
	}
};
$(document).ready(function () {
	/***设置主页布局***/
	$('#container').layout({
		defaults:{spacing_open:0},
		north:{size:50},
		west:{spacing_open:5, size: 242},
		south:{size: 30},
		center:{onresize_end: resetMainAccordionGridSize}
	});
	/***设置主页左侧树形菜单***/
	setMenu();
	/***设置Tab***/
	setHomeTab();
});
function setMenu(){
	$.getJSON(ctx + "/main/getMenu.do?timer="+new Date().getTime(), function(data){
		var menuData = data;//$.parseJSON(data);
		$.fn.zTree.init($("#mainMenuTree"), mainMenuTreeSetting, menuData);
		//初始化左侧手风琴
		initMainAccordion();
		$(".mainMenuTree_subTree").each(function(){
			var resourceId = $(this).data("resourceid");
			var treeNode = mainMenuTreeObj.getNodesByParam("resourceId", resourceId)[0];
			setICONForParentMenu(treeNode);
			$.fn.zTree.init($(this), subTreeSetting, treeNode.children);
		});
	});
}

//递归设置父节点菜单图标
function setICONForParentMenu(treeNode){
	if(treeNode.isParent){
		/*if(treeNode.resourceImg == null || treeNode.resourceImg ==""){
		 treeNode.icon = ctx + "/" +"images/213148246.gif";
		 }else{
		 treeNode.icon = ctx + "/" +treeNode.resourceImg;
		 }*/
		for(var i =0; i<treeNode.children.length;i++){
			var childrenNode = treeNode.children[i];
			setICONForParentMenu(childrenNode);
		}
	}
}
/**
 * 初始化左侧手风琴
 */
function initMainAccordion(){
	//初始化左侧隐藏树菜单
	mainMenuTreeObj = $.fn.zTree.getZTreeObj("mainMenuTree");
	var treearr = mainMenuTreeObj.getNodes();
	for (var i = 0; i < treearr.length; i++) {
		if (treearr[i].isParent) {
			var accordionItemText = treearr[i].resourceName.replace(/=/gm, "");
			/*var headerItemHtml = "<h3 style=\"white-space: nowrap; font-size:15px;align:center\" ><img src='" + ctx + treearr[i].resourceImg + "' " + "style='margin-bottom:-4px;margin-left:-5px'>&nbsp;&nbsp;&nbsp;" + accordionItemText + "</h3>" +
					"<div style='overflow: auto;'>" +
					"<ul id='" + treearr[i].resourceId + "_subtree' data-accordionindex='" + i + "' data-resourceid='" + treearr[i].resourceId + "' class='mainMenuTree_subTree ztree'></ul>" +
					"</div>";*/
			var headerItemHtml = "<h3 style=\"white-space: nowrap; font-size:15px;align:center\" >&nbsp;&nbsp;" + accordionItemText + "</h3>" +
					"<div style='overflow: auto;'>" +
					"<ul id='" + treearr[i].resourceId + "_subtree' data-accordionindex='" + i + "' data-resourceid='" + treearr[i].resourceId + "' class='mainMenuTree_subTree ztree'></ul>" +
					"</div>";
			$("#mainAccordion").append(headerItemHtml);
		}
	}
	mainAccordionObj = $("#mainAccordion").accordion({heightStyle : "fill", active: 0});
}
function setHomeTab(){
	//初始化第一个Tab选项卡
	$.get(ctx + "/main/mainPage.do", function(result){
		var rawId = new Date().getTime();
		$("#mainTabs #mainTabsUl").append("<li data-resourceurl='/main/mainPage.do'><a href='#mainTabsItem_" + rawId + "'>主页</a></li>");
		$("#mainTabs").append("<div id='mainTabsItem_" + rawId + "'>" + result + "</div>");
		mainTabsObj = $("#mainTabs").tabs({
			heightStyle: "fill",
			activate: function(event, ui){
				var resourceUrl = ui.newTab.data("resourceurl");
				$(".mainMenuTree_subTree").each(function(){
					var id = $(this).prop("id");
					var subTree = $.fn.zTree.getZTreeObj(id);
					var treeNode = subTree.getNodesByParam("resourceUrl", resourceUrl)[0];
					if(!checkIsNull(treeNode)){
						var accordionIndex = $(this).data("accordionindex"); //左侧手风琴index
						mainAccordionObj.accordion("option", "active", accordionIndex);
						subTree.selectNode(treeNode);
						return false;
					}
				});
			}
		});
		//Tab选项卡可以随意拖动效果
		mainTabsObj.find(".ui-tabs-nav").sortable({
			axis: "x",
			stop: function(){
				mainTabsObj.tabs("refresh");
			}
		});
		//Tabs右键
		mainTabsObj.mousedown(function(e){
			//鼠标右键
			if(3 == e.which){
				showContextMenu();
			}
		});
	});
}
/**
 * 根据传入的url打开Tab
 */
function openTabByUrl(url,params){
	var tabExist = false;
	$("#mainTabs #mainTabsUl li").each(function(index){
		if($(this).data("resourceurl") == url){
			mainTabsObj.tabs("option", "active", index);
			tabExist = true;
			//如果Tab存在，刷新当前Tab的内容
			var panelId = $(this).attr("aria-controls");
			if(checkIsNull(params)){
				url = ctx +  url
			}else{
				url = ctx +  url+"?"+params;
			}
			$("#" + panelId).html('<iframe name="target'+rawId+'" scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>');
		}
	});
	if(!tabExist){ //如果Tab不存在添加一个新Tab
		var tabCount = $("#mainTabs #mainTabsUl li").length;
		//如果页面超过5个Tab，提醒
		if(tabCount >= 5){
			if($("#mainTabTooMuchTip").css("display") == "none"){
				$("#mainTabTooMuchTip div div").html("窗口开多了会影响页面打开速度！");
				$("#mainTabTooMuchTip").fadeIn(1000);
				setTimeout(hideMainTabTooMuchTip, 5000);
			}
		}
		var treeNode = mainMenuTreeObj.getNodesByParam("resourceUrl", url)[0];
		var rawId = new Date().getTime();
		var tabLi = "<li data-resourceurl='" + url + "'><a href='#mainTabsItem_" + rawId + "'>" + treeNode.resourceName + "</a><span style='float: right;' class='ui-icon ui-icon-close' role='presentation' title='Close'>关闭</span></li>";
		mainTabsObj.find(".ui-tabs-nav").append(tabLi);
		mainTabsObj.append("<div id='mainTabsItem_" + rawId + "'></div>");
		mainTabsObj.tabs("refresh");
		mainTabsObj.tabs("option", "active", tabCount);
		if(checkIsNull(params)){
			url = ctx +  url
		}else{
			url = ctx +  url+"?"+params;
		}
		mainTabsObj.find("#mainTabsItem_" + rawId).append('<iframe name="target'+rawId+'" scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>');

		//close icon: removing the tab on click
		mainTabsObj.delegate("span.ui-icon.ui-icon-close", "click", function(){
			var panelId = $(this).closest("li").remove().attr("aria-controls");
			var tabPanel = $("#" + panelId);
			if(tabPanel.length > 0){
				//删除jqGird自动生成的jqGridDialog
				$(".ui-jqgrid.ui-widget.ui-widget-content.ui-corner-all", tabPanel).each(function(){
					var gboxId = $(this).attr("id");
					if(!checkIsNull(gboxId)){
						var jqGridDialogId = gboxId.replace("gbox", "alertmod");
						var jqGridDialog = $("#" + jqGridDialogId);
						if(jqGridDialog.hasClass("ui-jqdialog") && jqGridDialog.hasClass("ui-corner-all") && jqGridDialog.hasClass("ui-widget-content") && jqGridDialog.hasClass("ui-widget"))
							jqGridDialog.remove();
					}
				});
				tabPanel.remove();
				mainTabsObj.tabs("refresh");
				//关闭选项卡之后，所有选项卡自动向左移动
				var nextTab = mainTabsObj.find("#mainTabsUl li:visible:last").next();
				if(nextTab.css("display") == "none"){
					nextTab.show();
					//如果有换行
					while(mainTabsObj.find("#mainTabsUl li:visible:last").offset().top > mainTabsObj.find("#mainTabsUl li:eq(0)").offset().top){
						mainTabsObj.find("#mainTabsUl li:visible:eq(1)").hide();
					}
				}
			}
		});
	}
	//重新排列选项卡
	var firstTabTop = mainTabsObj.find("#mainTabsUl li:eq(0)").offset().top;
	mainTabsObj.find("#mainTabsUl li").show();
	//如果Tabs选项卡出现换行的情况，自动根据当前选项卡所处的位置，从左往右，从右往左隐藏Tab选项卡
	while(mainTabsObj.find("#mainTabsUl li:visible:last").offset().top > firstTabTop){
		//如果当前激活的选项卡处于第二行，从左往后隐藏Tab选项卡
		if(mainTabsObj.find(".ui-tabs-nav .ui-tabs-active").offset().top > firstTabTop){
			mainTabsObj.find("#mainTabsUl li:visible:eq(1)").hide(); //第一个选项卡mainTabsObj.find("#mainTabsUl li:visible:eq(0)")永远显示
		}else{//如果当前选项卡处于第一行，从右往左隐藏Tab选项卡
			mainTabsObj.find("#mainTabsUl li:visible:last").hide();
		}
	}
}
/**
 * 隐藏 提示框
 * @returns
 */
function hideMainTabTooMuchTip(){
	$("#mainTabTooMuchTip").fadeOut(1000);
}

/**
 * tab页右键弹出菜单
 */
function showContextMenu(){
	//第一个Tab不需要右键功能
	mainTabsObj.find("#mainTabsUl li:gt(0)").contextMenu('mainTabsMenu',{
		bindings:{
			"contextMenu_colse": function(t) { //关闭当前选项
				var panelId = $(t).closest("li").remove().attr("aria-controls");
				var tabPanel = $("#" + panelId);
				if(tabPanel.length > 0){
					//删除jqGird自动生成的jqGridDialog
					$(".ui-jqgrid.ui-widget.ui-widget-content.ui-corner-all", tabPanel).each(function(){
						var gboxId = $(this).attr("id");
						if(!checkIsNull(gboxId)){
							var jqGridDialogId = gboxId.replace("gbox", "alertmod");
							var jqGridDialog = $("#" + jqGridDialogId);
							if(jqGridDialog.hasClass("ui-jqdialog") && jqGridDialog.hasClass("ui-corner-all") && jqGridDialog.hasClass("ui-widget-content") && jqGridDialog.hasClass("ui-widget"))
								jqGridDialog.remove();
						}
					});
					tabPanel.remove();
					mainTabsObj.tabs("refresh");
					//关闭选项卡之后，所有选项卡自动向左移动
					var nextTab = mainTabsObj.find("#mainTabsUl li:visible:last").next();
					if(nextTab.css("display") == "none"){
						nextTab.show();
						//如果有换行
						while(mainTabsObj.find("#mainTabsUl li:visible:last").offset().top > mainTabsObj.find("#mainTabsUl li:eq(0)").offset().top){
							mainTabsObj.find("#mainTabsUl li:visible:eq(1)").hide();
						}
					}
				}
			},
			"contextMenu_colse_other": function(t) {//关闭其它选项
				mainTabsObj.find("#mainTabsUl li:gt(0)").each(function(){
					if($(this).data("resourceurl") != $(t).data("resourceurl")){
						var panelId = $(this).closest("li").remove().attr("aria-controls");
						var tabPanel = $("#" + panelId);
						if(tabPanel.length > 0){
							//删除jqGird自动生成的jqGridDialog
							$(".ui-jqgrid.ui-widget.ui-widget-content.ui-corner-all", tabPanel).each(function(){
								var gboxId = $(this).attr("id");
								if(!checkIsNull(gboxId)){
									var jqGridDialogId = gboxId.replace("gbox", "alertmod");
									var jqGridDialog = $("#" + jqGridDialogId);
									if(jqGridDialog.hasClass("ui-jqdialog") && jqGridDialog.hasClass("ui-corner-all") && jqGridDialog.hasClass("ui-widget-content") && jqGridDialog.hasClass("ui-widget"))
										jqGridDialog.remove();
								}
							});
							tabPanel.remove();
						}
					}
				});
				mainTabsObj.tabs("refresh");
			},
			"contextMenu_colse_all": function(t) {
				mainTabsObj.find("#mainTabsUl li:gt(0)").each(function(){
					var panelId = $(this).closest("li").remove().attr("aria-controls");
					var tabPanel = $("#" + panelId);
					if(tabPanel.length > 0){
						//删除jqGird自动生成的jqGridDialog
						$(".ui-jqgrid.ui-widget.ui-widget-content.ui-corner-all", tabPanel).each(function(){
							var gboxId = $(this).attr("id");
							if(!checkIsNull(gboxId)){
								var jqGridDialogId = gboxId.replace("gbox", "alertmod");
								var jqGridDialog = $("#" + jqGridDialogId);
								if(jqGridDialog.hasClass("ui-jqdialog") && jqGridDialog.hasClass("ui-corner-all") && jqGridDialog.hasClass("ui-widget-content") && jqGridDialog.hasClass("ui-widget"))
									jqGridDialog.remove();
							}
						});
						tabPanel.remove();
					}
				});
				mainTabsObj.tabs("refresh");
			}
		}
	});
}

/**
 * Tab选项卡左右切换
 */
function mainTabsGo(direction){
	if(direction == 'left'){
		var prevTab = mainTabsObj.find("#mainTabsUl li:visible:eq(1)").prev();
		if(prevTab.css("display") == "none"){
			prevTab.show();
			//如果有换行
			while(mainTabsObj.find("#mainTabsUl li:visible:last").offset().top > mainTabsObj.find("#mainTabsUl li:eq(0)").offset().top){
				mainTabsObj.find("#mainTabsUl li:visible:last").hide();
			}
		}else{
			$("#mainTabTooMuchTip div div").html("已经是第一个选项页.");
			$("#mainTabTooMuchTip").fadeIn(1000);
			setTimeout(hideMainTabTooMuchTip, 5000);
		}
	}else{
		var nextTab = mainTabsObj.find("#mainTabsUl li:visible:last").next();
		if(nextTab.css("display") == "none"){
			nextTab.show();
			//如果有换行
			while(mainTabsObj.find("#mainTabsUl li:visible:last").offset().top > mainTabsObj.find("#mainTabsUl li:eq(0)").offset().top){
				mainTabsObj.find("#mainTabsUl li:visible:eq(1)").hide();
			}
		}else{
			$("#mainTabTooMuchTip div div").html("已经是最后一个选项页.");
			$("#mainTabTooMuchTip").fadeIn(1000);
			setTimeout(hideMainTabTooMuchTip, 5000);
		}
	}
}
/*
 * 窗口大小化
 * 改变高度
 * 改变Grid宽度
 */
function resetMainAccordionGridSize(){
	var viewH = $("#main_pane_ui_layout_west_tree_accordion").height();
	$("#main_pane_ui_layout_west_tree_accordion #mainAccordion div").each(function(){
		$(this).height(viewH - 110);
	});
	$(".pane.ui-layout-center #mainTabs").height(viewH - 10);
}

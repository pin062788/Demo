function menuNodeDefalutChick(event, treeId, treeNode){
	if(treeNode.resourceUrl)
	{
		if(!treeNode.isParent)
			setLeveStyleAccrod(this,treeNode.resourceUrl,treeNode.resourceName,treeNode.resourceCode);//手风琴
	}
	if(treeNode.isParent){
		var treeObj = $.fn.zTree.getZTreeObj(treeNode.tId.split("_")[0]+"_ntree");
		var nodes = treeObj.getSelectedNodes();
		if (nodes.length>0) {
			if(nodes[0].open)   
				treeObj.expandNode(nodes[0], false, true, true);
			else
				treeObj.expandNode(nodes[0], true, true, true); 
		}
	 }  
}
			
function getMenuTreeSetting(onClickFunction){
	 var menuTreeSetting = {
			data : {
				key : {
					name :"resourceName",
					children:"children"
				}
			},
			simpleData: {
				enable: true,
				idKey: "resourceCode",
				pIdKey: "parentResourceCode",
				rootPId:null
			},
			view:{
				showLine: true
			},
			callback :{				
				onClick : $.isFunction(onClickFunction)? onClickFunction:menuNodeDefalutChick
			}
	};
	 return menuTreeSetting;
}
$(function(){
	$.getAjax(ctx+"/main/getMenu.do",null,null,function(data){
		$.fn.zTree.init($("#menuTree"), getMenuTreeSetting(), data);
		showMenuAccordionIndex();//手风琴
	});
	$("#tabs").tabs({
		ajaxOptions:{async:false},
		event: "mousedown",
		beforeLoad: function( event, ui ) {
			ui.jqXHR.error(function() {ui.panel.html("");});
	    }
	});
	$("#tabs").mousedown(function(e){ 
	    if(3 == e.which){
	    	showContextMenu();
	    }
	});
	$("#tabsleft" ).tabs().addClass( "ui-tabs-vertical ui-helper-clearfix" );		
});
	
function showMenuIndex(){//添加左侧内层导航菜单		
	$("#tabsleft ul").html("");
	$("#ntree").html("");
	$("#tabsUl").html("");
	$("#tabsUl").append( "<li onclick=\"locationTab(\'userIndex.jsp\')\"><a href=\"javascript:void(0)\">菜单首页</a></li>" );
    $("#tabs").tabs( "refresh" );
	var treeObj =  $.fn.zTree.getZTreeObj("menuTree");		
	var treearr =  treeObj.getNodes();		
	var li ="";		
	var showText = "";		
	for(var i=0;i<treearr.length;i++){
		if(treearr[i].isParent){
			showText = treearr[i].text.replace(/=/gm,"");
			showText = formatText(showText);	
			li = "<li onclick=\"showMenuLevle(this,'"+treearr[i].tId+"');\" id=\"tabsleft_"+treearr[i].tId+"\" class=\"ui-state-default ui-corner-left\" role=\"tab\" tabindex=\"-1\" aria-controls=\"ui-id-"+(i+1)+"\" aria-labelledby=\"ui-id-"+i+"\" aria-selected=\"false\" aria-expanded=\"false\"><a href=\"javascript:void(0)\" class=\"ui-tabs-anchor\" role=\"presentation\" tabindex=\"-1\" id=\"ui-id-"+i+"\">"+showText+"</a></li>";	
			$("#tabsleft ul").first().append(li);
			$( "#tabsleft li" ).removeClass( "ui-corner-top" ).addClass("ui-corner-left");
			showText = "";
		}else{
			var li  =  "<li  class=\"level2\"  onclick=\"setLeveStyle(this,\'"+treearr[i].resourceUrl+"\');\" id=\"ntree_"+treearr[i].tId+"\">"+
						"<a class=\"level2\"  href=\"javascript:void(0)\"  title=\""+treearr[i].text+"\">"+
						"<span class=\"button ico_docu\"></span>"+
						"<span>"+treearr[i].text+"</span>"+
						"</a>"+
						"</li>";
			$("#ntree").append(li);
		}			
	}
	li = "<li onclick=\"showMenuIndex();\" class=\"ui-state-default ui-corner-left\" role=\"tab\" tabindex=\"-1\" aria-controls=\"ui-id-"+(i+1)+"\" aria-labelledby=\"ui-id-"+i+"\" aria-selected=\"false\" aria-expanded=\"false\"><a href=\"javascript:void(0)\" class=\"ui-tabs-anchor\" role=\"presentation\" tabindex=\"-1\" id=\"ui-id-"+i+"\">返<br/>回<br/>菜<br/>单<br/>首<br/>页</a></li>";
	$("#tabsleft ul").first().append(li);
	$( "#tabsleft li" ).removeClass( "ui-corner-top" ).addClass("ui-corner-left");
	$("#tabsleft").css({"border":"0"});
	$("#tabsleft ul").first().removeClass("ui-widget-header");
	//$("#tabsleft ul .ui-state-default.ui-corner-left.ui-state-active").first().css({"background":"#6eac2c"});
	$("#tabsUl li").click();
	$("#tabsleft").css({"display":"block"});
}
/**
 * 手风琴样式
 */
function showMenuAccordionIndex(){//添加左侧内层导航菜单		 
	var treeObj =  $.fn.zTree.getZTreeObj("menuTree");		
	var treearr =  treeObj.getNodes();		
	var showText = "";		
	for(var i=0;i<treearr.length;i++){
		if(treearr[i].isParent){
			showText = treearr[i].text.replace(/=/gm,"");
			/***************************************手风琴******************************************************/
			$("#accordion").append("<H3 id=\"accordH3"+i+"\" style=\"white-space: nowrap\" onclick=\"getSubMenuNodes(\'"+treearr[i].resourceId+"\',\'accordDiv"+(i+1)+"\',\'"+treearr[i].tId+"\',\'accordH3"+i+"\',\'"+treearr[i].resourceImg+"\')\">"+"<label id=\""+treearr[i].resourceImg+"\" style=\"width:16px;height:16px;\" class=\"menu-icon menu-icon-"+treearr[i].resourceImg+"\">&nbsp;</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+showText+"</H3>");					
			$("#accordion").append("<div id=\"accordDiv"+(i+1)+"\" style=\"height:300px\"></div>");
			/*********************************************************************************************/
			showText = "";
		}			
	}
	$("#accordion").accordion();
	$("#accordH30").click();
	$("#tabsUl li:eq(0)").click();
	$(".ui-widget").css("fontSize","11px");		
	setMainWidth();		
}
function setMainWidth(){
	if(!checkIsNull($(".pane.ui-layout-center").css("width"))){
		var mainWidth = $(".pane.ui-layout-center").css("width").substring(0,$(".pane.ui-layout-center").css("width").indexOf("px"));
		mainWidth = Number(mainWidth)+17;
		if(mainWidth>1000)
			$(".pane.ui-layout-center").css("width",mainWidth+"px");
		else
			setTimeout(setMainWidth,3000);	
	}else{
		setTimeout(setMainWidth,3000);
	}
}

function getSubMenuNodes(resourceId,showDivId,tId,accordH3Id,img){
	$(".ui-accordion-header").css("color","#000");
	$("#"+accordH3Id).css("color","#fff");
	$("#"+accordH3Id).css("background-color","#31649D");	
	$("h3").each(function(index){
		if($(this).attr("id")!=accordH3Id){
			$(this).css("background-color","#EBF1FB");
		}
	});

	//渲染手风琴
	$("h3 label").each(function(){
		var lid=$(this).attr("id");
		if(lid==img){
			$(this).toggleClass("menu-icon-"+img+"-active");
		}else{
			$(this).removeClass("menu-icon-"+lid+"-active");
		}
	});
	$("#"+showDivId).css({"height":"300px","overflow": "auto"});
	$("#"+showDivId).html("");
    $("#"+showDivId).hide();
	$("#"+showDivId).append("<ul id=\""+showDivId+"_ntree\" class=\"ztree\"></ul>");
	var treeObj =  $.fn.zTree.getZTreeObj("menuTree");
	var treeNode =  treeObj.getNodeByParam("tId",tId);
	if(treeNode ){
		$.fn.zTree.init($("#"+showDivId+"_ntree"), getMenuTreeSetting(), treeNode.children);
	}
	$("#"+showDivId).css({"height":"300px","overflow": "auto"});
    var isShow = $("#"+showDivId).attr("aria-hidden");
    //debugger;
    if(isShow == false || isShow == "false"){
        $("#"+showDivId).removeClass("ui-accordion-content-active");
        $("#"+showDivId).removeAttr("aria-hidden");

        $("#"+showDivId).attr("tabindex",-1);
        $("#"+accordH3Id).attr("tabindex",-1);

        $("h3 label").removeClass("menu-icon-"+img+"-active");

        $("#"+accordH3Id+" span").removeClass("ui-icon ui-icon-triangle-1-s");
        $("#"+accordH3Id+" span").addClass("ui-icon ui-icon-triangle-1-e");

        $("#"+accordH3Id).css("color","#000");
        $("#"+accordH3Id).css("background-color","#EBF1FB");
        $("#"+showDivId).hide(300);        
    }else{
        $("#"+showDivId).addClass("ui-accordion-content-active");
        $("#"+showDivId).attr("aria-hidden",false);

        $("#"+showDivId).attr("tabindex",0);
        $("#"+accordH3Id).attr("tabindex",0);

        $("#"+accordH3Id+" span").removeClass("ui-icon ui-icon-triangle-1-e");
        $("#"+accordH3Id+" span").addClass("ui-icon ui-icon-triangle-1-s");

        $("#"+showDivId).show();
    }

	setDivHeight(showDivId);
}

function showSearchMenu(treeId){//添加左侧内层导航菜单		
	$("#tabsleft ul").html("");
	$("#tabsUl").html("");
	$("#tabsUl").append("<li><a href=\"javascript:void(0)\">菜单首页</a></li>" );
	$("#tabs").tabs( "refresh" );
	var treeObj =  $.fn.zTree.getZTreeObj(treeId);		
	var treearr =  treeObj.getNodes();		
	var li ="";		
	var showText = "";		
	$("#ntree").html("");
	li = "<li  class=\"ui-state-default ui-corner-left ui-state-active\" role=\"tab\" tabindex=\"-1\" aria-controls=\"ui-id-searchResult\" aria-labelledby=\"ui-id-searchResult\" aria-selected=\"false\" aria-expanded=\"false\" id=\"ui-id-searchResultLi\"><a href=\"javascript:void(0)\" class=\"ui-tabs-anchor\" role=\"presentation\" tabindex=\"-1\" id=\"ui-id-searchResult\">搜<br>索<br>结<br>果</a></li>";	
	$("#tabsleft ul").first().append(li);
	$( "#tabsleft li" ).removeClass( "ui-corner-top" ).addClass("ui-corner-left");
	for(var i=0;i<treearr.length;i++){
		if(treearr[i].isParent){
			showText = treearr[i].text.replace(/=/gm,"");
			showText = formatText(showText);	
			li = "<li onclick=\"showMenuLevle(this,'"+treearr[i].tId+"');\" id=\"tabsleft_"+treearr[i].tId+"\" class=\"ui-state-default ui-corner-left\" role=\"tab\" tabindex=\"-1\" aria-controls=\"ui-id-"+(i+1)+"\" aria-labelledby=\"ui-id-"+i+"\" aria-selected=\"false\" aria-expanded=\"false\"><a href=\"javascript:void(0)\" class=\"ui-tabs-anchor\" role=\"presentation\" tabindex=\"-1\" id=\"ui-id-"+i+"\">"+showText+"</a></li>";	
			$("#tabsleft ul").first().append(li);
			$( "#tabsleft li" ).removeClass( "ui-corner-top" ).addClass("ui-corner-left");
			showText = "";
		}else{
		     li = "<li  class=\"level2\" onclick=\"setLeveStyle(this,\'"+treearr[i].resourceUrl+"\');\" id=\"ntree_"+treearr[i].tId+"\">"+
                  "<a class=\"level2\"  href=\"javascript:void(0)\"  title=\""+treearr[i].text+"\">"+
                  "<span class=\"button ico_docu\"></span>"+
                  "<span>"+treearr[i].text+"</span>"+
                  "</a>"+
                  "</li>";
			$("#ntree").append(li);
		}			
	}
	li = "<li onclick=\"showMenuIndex();\" class=\"ui-state-default ui-corner-left\" role=\"tab\" tabindex=\"-1\" aria-controls=\"ui-id-"+(i+1)+"\" aria-labelledby=\"ui-id-"+i+"\" aria-selected=\"false\" aria-expanded=\"false\"><a href=\"javascript:void(0)\" class=\"ui-tabs-anchor\" role=\"presentation\" tabindex=\"-1\" id=\"ui-id-"+i+"\">返<br/>回<br/>菜<br/>单<br/>首<br/>页</a></li>";
	$("#tabsleft ul").first().append(li);
	$( "#tabsleft li" ).removeClass( "ui-corner-top" ).addClass("ui-corner-left");
	$("#tabsleft").css({"border":"0"});
	$("#tabsleft ul").first().removeClass("ui-widget-header");
	$("#tabsUl li").click();
	$("#tabsleft").css({"display":"block"});
}
function formatText(menutext){
	var txt = "";
	if(!checkIsNull(menutext))
	for(var i=0;i<menutext.length;i++){
		txt += menutext[i];
		if(i!==menutext.length-1) txt+="<br/>";
	}
	return txt;
}
function addTabList(objMap){//添加TAB页
	$("#tabsUl").html("");
	 for(var i=0;i<objMap.nodes.length;i++){
		$("#tabsUl").append( "<li onclick=\"setLeftMenuStatus(\'"+objMap.nodes[i].tId+"\');\"><a href=\"javascript:void(0)\">"+objMap.nodes[i].text+"</a></li>" );
		if (objMap.nodes.length==1) if(i==0) $("#tabsUl li").click();
	}
	    $("#tabs").tabs( "refresh" );
	    addMenuLeft(objMap);    
}
function addMenuLeft(objMap){// 添加左侧菜单，
	if(objMap.subNodes.length>0){
		for(var i=0;i<objMap.subNodes.length;i++){
			var li  =  "<li  class=\"level2\" onclick=\"setLeveStyle(this,\'"+objMap.subNodes[i].resourceUrl+"\');\" id=\"ntree_"+objMap.subNodes[i].tId+"\">"+
		               "<a class=\"level2\"  href=\"javascript:void(0)\"  title=\""+objMap.subNodes[i].text+"\">"+
		               "<span class=\"button ico_docu\"></span>"+
		               "<span>"+objMap.subNodes[i].text+"</span>"+
		               "</a>"+
		               "</li>";
			$("#ntree").append(li);
		}
	}
}
function showMenuLevle(obj,tId){//添加标签页
    debugger;
	if($("#tabsleft  .ui-state-active").length>0)
		$("#tabsleft .ui-state-active").removeClass("ui-state-active");
	$(obj).toggleClass("ui-state-active");	
	$("#ntree").html("");
	$("#tabsUl").html("");
	var objMap = checkLeve(tId);		
	if(!checkIsNull(objMap)){
		if(objMap.isParent){
			for(var i=0;i<objMap.nodes.length;i++){
				$("#tabsUl").append( "<li id=\"tabs_"+objMap.nodes[i].tId+"\" onclick=\"setLeftMenuStatus(\'"+objMap.nodes[i].tId+"\');\"><a href=\"javascript:void(0)\">"+objMap.nodes[i].text+"</a></li>" );
			    $("#tabs").tabs( "refresh" );
			    if(i==0) $("#tabsUl li").click();
			}
		}else{
			$("#tabsUl").html("");
			$("#tabsUl").append( "<li onclick=\"setLeftMenuStatus(\'\');\"><a href=\"javascript:void(0)\">"+objMap.menuName+"</a></li>" );
		    $("#tabs").tabs("refresh");
		    $("#tabsUl li").click();
		}
		addMenuLeft(objMap);
	}
}	

function setLeftMenuStatus(tId){//添加左侧外层菜单
	var objMap = null;
	if(!checkIsNull(tId))
	objMap = checkLeve(tId);	
	if(!checkIsNull(objMap)){
		if(!objMap.isParent){
			$("#ntree").html("");
			var li  =  "";
			for(var i=0;i<objMap.subNodes.length;i++){
				 li  =  "<li  class=\"level2\" onclick=\"setLeveStyle(this,\'"+objMap.subNodes[i].resourceUrl+"\');\" id=\"ntree_"+objMap.subNodes[i].tId+"\">"+
	               "<a class=\"level2\"  href=\"javascript:void(0)\"  title=\""+objMap.subNodes[i].text+"\">"+
	               "<span class=\"button ico_docu\"></span>"+
	               "<span>"+objMap.subNodes[i].text+"</span>"+
	               "</a>"+
              	   "</li>";
				$("#ntree").append(li);
			}
		}else{
			var nTreeSetting = getMenuTreeSetting( function(event, treeId, treeNode){
				if(treeNode.resourceUrl){
					if(!treeNode.isParent){
						setLeveStyle(this,treeNode.resourceUrl);
					}
				}else{
					locationTab(null);
				}
			});
			$.fn.zTree.init($("#ntree"), nTreeSetting, objMap.allNodes);
		}
		locationTab(objMap.menuUrl);
	}else{
		locationTab(null);
	}
}

function setLeveStyle(obj,url){
	if(typeof obj.id !="undefined"){
		if($("#ntree  li .curSelectedNode").length>0)
			$("#ntree li .curSelectedNode").removeClass("curSelectedNode");
		$("#"+obj.id+" a").toggleClass("curSelectedNode");		
	}
	$('#tabs-1').html("");
	locationTab(url);
}
function setLeveStyleAccrod(obj,url,treeNode,resourceCode){
	if(typeof obj.id !="undefined"){
		if($("#ntree  li .curSelectedNode").length>0)
			$("#ntree li .curSelectedNode").removeClass("curSelectedNode");
		$("#"+obj.id+" a").toggleClass("curSelectedNode");		
	}
	locationTabAccord(url,treeNode,resourceCode);
}
function locationTab(url){//标签页链接URL		
	 $(".ui-tabs-panel.ui-widget-content.ui-corner-bottom").each(function(){
		 if($(this).css("display")!="none"){
			 if(checkIsNull(url)){
				url = "/main/mainPage.do";//showConstruction.jsp
			 }
			 $(this).html("");
			 $(this).load(buildUrl(url),1,function (response,status,xhr){
				if(status!="success"){
					$(this).html("加载页面失败，请检查URL是否正确！");
				}
				hideLoading();
			}); 
		}
	});
}

var tabShowFlag = true;
function locationTabAccord(url,title,resourceCode){//标签页链接URL
	$(".toActionColor").each(function(index){
		$(this).removeClass("toActionColor");
	});
	if(!tabShowFlag) return;
	if(tabShowFlag) tabShowFlag = false;
	var tabLiId = resourceCode+"_li";
	var tab = $("#"+tabLiId)[0];
	if(!tab){
		if($("#tabsUl li").length>=5){
			if($("#popTopTip").css("display")=="none"&&!closePop)
				$("#popTopTip").fadeIn(3000); 
			tabShowFlag = true;
		}
		addTab(url,title,resourceCode);
	}
	//设置当前tab的样式；
	setCurrentLiCss(tabLiId);
	
	//设置最右侧下来菜单中隐藏tab的字体
	$("#tabsUl li").each(function (index){
		var liId = $(this).attr("id");
		var lr_menu_a = $("#"+liId.split("_")[0]+"_lr_menu_a");
		if($(this).offset().top > $("#tabsUlIndex_li").offset().top || $(this).offset().top <= 0){
			lr_menu_a.css("color","#000");
			lr_menu_a.css("font-family","Arial,Helvetica,sans-serif");
			lr_menu_a.css("font-weight","bold");
		}else{
			$("#lr_menu a").each(function(index){
				lr_menu_a.css("color","#000"); 
				lr_menu_a.css("font-family","Arial,Helvetica,sans-serif");
				lr_menu_a.css("font-weight","100");
			});
		} 
	}); 

	var showDivId = $("#"+tabLiId).attr("aria-controls");
	if(!checkIsNull(showDivId) && !checkIsNull($("#"+showDivId).text())){
		tabShowFlag = true;	
		return true; 
	}
	$("#tabs .ui-tabs-panel.ui-widget-content.ui-corner-bottom").each(function(index){
		if($(this).attr("id")==showDivId && $(this).css("display")!="none")
		{
			if(checkIsNull(url)){
				url = "/main/mainPage.do";//showConstruction.jsp
			}
			$(this).html("");
			$(this).load(buildUrl(url),1,function (response,status,xhr){
				if(status!="success"){
					$(this).html("加载页面失败，请检查URL是否正确！");
				}
				tabShowFlag = true;
				if($("#popTopTip").css("display")!="none"&&!closePop) setTimeout(hidePopTip,500);  
			}); 
		}
	});
}
function locationTabUrl(url,params){//标签页链接URL
	$(".ui-tabs-panel.ui-widget-content.ui-corner-bottom").each(function(){
		if($(this).css("display")!="none")
		{
			showLoading();
			if(checkIsNull(url)){
				url = "/main/mainPage.do";//showConstruction.jsp
			}
			$(this).load(url,params,function (response,status,xhr){
				if(status!="success"){
					$(this).html("加载页面失败，请检查URL是否正确！");
				}
				hideLoading();
			}); 
		}
	});
}
function openDialogDiv(id,url,title,flag){
	if(flag) $("#"+id).dialog({width:700,height:500,title:title});
	$("#"+id).load(url);
}
function checkLeve(tId){
	var treeObj =  $.fn.zTree.getZTreeObj("menuTree");		
	var nodeArr =[];
	var nodeAllArr =[];
	var subNodeArr =[];

	var treeNode = treeObj.getNodeByParam("tId",tId);
	if(treeNode.children!=null && treeNode.children.length>0){
		objMap.menuUrl=treeNode.resourceUrl;
		var child = treeNode.children;
		objMap.menuName=treeNode.text;
		for(var j=0;j<child.length;j++){
			nodeAllArr.push(child[j]);
			if(child[j].isParent){ 
				objMap.isParent = true;
				nodeArr.push(child[j]);
			}else{
				subNodeArr.push(child[j]);
			}
		}					
	}
	var objMap = {
			isParent:false,
			menuName:"菜单首页",
			menuUrl:"",
			nodes:[],
			subNodes:[],
			allNodes:[]
	};
	objMap.nodes = nodeArr;
	objMap.subNodes = subNodeArr;
	objMap.allNodes = nodeAllArr;
	return objMap;
}
function getParentNodeByUrl(url){
	var treearr = [];
	var treeObj =  $.fn.zTree.getZTreeObj("menuTree");		
	treearr =  treeObj.transformToArray(treeObj.getNodes());		
	var objMap = null;		
	for(var i=0;i<treearr.length;i++){	
		if(treearr[i].resourceUrl==url){
			objMap = {};
			objMap = getParentNodeByNode(treearr[i],objMap);
			objMap.selectId = treearr[i].tId;
			objMap.selectNode = treearr[i];
			break;
		}
    }
	return objMap;
}
function getParentNodeByNode(node,objMap){
	var parentNode = node.getParentNode();
	if(parentNode==null){
		objMap.tId = node.tId;
	}else if(parentNode.level!=1){
		getParentNodeByNode(parentNode,objMap);
	}else if(parentNode.level==1){
		objMap.secondtId = parentNode.tId;
		getParentNodeByNode(parentNode,objMap);
	}else{
		getParentNodeByNode(parentNode,objMap);
	}
	return objMap;
}
function checkSecondNode(node){
	var treeObj =  $.fn.zTree.getZTreeObj("ntree");	
	var parentNode = node.getParentNode();
	if(parentNode!=null && parentNode.level>=0 && parentNode.isParent){
		treeObj.expandNode(parentNode,true,false,true);
		treeObj.selectNode(parentNode);
		checkSecondNode(parentNode);
	}else{
		treeObj.expandNode(node,true,false,true);
		treeObj.selectNode(node);
	}
}
function searchNodeOpen(url){
	var objMap = getParentNodeByUrl(url);
	if(objMap!=null){
		$("#tabsleft_"+objMap.tId+" a").first().click();
		$("#tabs_"+objMap.secondtId+" a").first().click();
		if(typeof objMap.secondtId != "undefined"){//有二级菜单
			var treeObj =  $.fn.zTree.getZTreeObj("ntree");		
			treearr =  treeObj.transformToArray(treeObj.getNodes());		
			for(var i=0;i<treearr.length;i++){	
				if(treearr[i].resourceUrl==url){
					objMap.selectId = treearr[i].tId;
					objMap.selectNode = treearr[i];
					break;
				}
		    }
			checkSecondNode(objMap.selectNode);
			$("#"+objMap.selectId+" a").first().click();
		}
		$("#ntree_"+objMap.selectId+" a").first().click();
	}
}
/**
 * 快捷菜单拦打开
 * @param url
 * @param title
 * @param obj
 */
function toAction(url,title,obj){
	var showShorCutBkcolor = true;
	openParentAccord(title);
	if(showShorCutBkcolor){			
		$(".toActionColor").each(function(index){
			$(this).removeClass("toActionColor");
		});
		$(obj).toggleClass("toActionColor");
	}
}
function openParentAccord(title){	
	var treeObj =  $.fn.zTree.getZTreeObj("menuTree");		
	var treearr =  treeObj.transformToArray(treeObj.getNodes());	
	var showText = "";		
	var levelArr = [];
	for(var i=0;i<treearr.length;i++){
		showText = treearr[i].text.replace(/=/gm,"");
		if(showText==title){
			if(!checkIsNull(treearr[i].parentTId)){
				var parentTId = treearr[i].parentTId;
				levelArr = addLevel(parentTId,levelArr);//
			}
		}
	}
	levelArr.push(title);
	for(var i=0;i<levelArr.length;i++){
		$("H3").each(function(index){
			if($.trim($(this).text())==levelArr[i]){
				setDivHeight($(this).attr("aria-controls"));
				$(this).click();
				levelArr.splice($.inArray(levelArr[i],levelArr),1);
				openSubMenu(levelArr,title);
			}
		});
	}
	
	return levelArr;
}
function setDivHeight(divId){
	if(typeof divId !="undefined" && !checkIsNull(divId) && $("#"+divId).length>0){
        if(Number($("#"+divId).css("height").substring(0,$("#"+divId).css("height").indexOf("px")))<300){
			$("#"+divId).css({"height":"300px","overflow": "auto"});
			setTimeout("setDivHeight('"+divId+"')",3000);
		}
	}
}
/*打开级联菜单*/
function openSubMenu(levelArr,title){
	for(var key=0;key<levelArr.length;key++){	
		$(".ui-accordion-content-active ul li").each(function(index){
			var txt = $(this).text();
			if(txt==levelArr[key]){				
					if(txt!=title) {
						$(this).find("span:eq(0)").click();
					}else{
						$(this).find("a:eq(0)").click();
					}
				levelArr.splice($.inArray(levelArr[key],levelArr),1);
				openSubMenu(levelArr,title);
				return false; 
			}
		});
	}
}
//添加每级菜单到数组
function addLevel(parentTId,levelArr){
	var treeObj =  $.fn.zTree.getZTreeObj("menuTree");		
	var treearr =  treeObj.transformToArray(treeObj.getNodes());	
	var showText = "";		
	for(var j=0;j<treearr.length;j++){
		showText = treearr[j].text.replace(/=/gm,"");
		if(treearr[j].tId==parentTId){
			if(treearr[j].level!=0){
				addLevel(treearr[j].parentTId,levelArr);
				levelArr.push(showText);
			}else{
				levelArr.push(showText);				
			}
		}
	} 
	return levelArr;
}
/**
 * 设置菜单首页加载完成后的高度
 * @param title
 * @param height
 */
function setIndexDivHeight(title,height){
	var showDivId = ""; 
	$("#tabsUl li").each(function (index){		    	
		if($(this).text()==title){
			showDivId = $(this).attr("aria-controls");	
			$("#tabs .ui-tabs-panel.ui-widget-content.ui-corner-bottom").each(function(){
				if($(this).attr("id")==showDivId)
					$("#"+showDivId).css("height",height+"px");
			});
			return true;
		}
	});
}
/**
 * 隐藏提示框
 */
function hidePopTip(){ 
	hidpopTipMi(6);
}
var closePop = false;
function hidpopTipMi(mis){
	closePop = true;
	mis-=1;
	if(mis!=0)
		setTimeout(function(){hidpopTipMi(mis);},1000); 
	else{
		$("#popTopTip").fadeOut(1000);
		closePop = false;
	}		
}
function buildUrl(url){
	return ctx+"/"+url+(url.indexOf('?') >0 ? '&' : '?')+"t="+new Date().getTime();
}
function showContextMenu(indexs){
	$("#tabsUl li").each(function(index) {
		if (index == indexs) {
			$('#tabs').tabs('option', 'active', indexs);
			$("#tabs").tabs("refresh");
		}
     });
	$("#tabsUl li").contextMenu('myMenu',{
		bindings:{  
            'contextMenu_colse': function(call) {
            	closeTab($('#tabs').find(".ui-tabs-active"));
            },
            'contextMenu_colse_other': function(call) {
            	closeTab($('#tabs').find(".ui-tabs-active"),1);
            },
            'contextMenu_colse_all': function(call) {
            	closeTab(null,2);
            }
      }  
    });
	$("#jqContextMenu").css("background-color","#fff");
}
function delMenuByTitle(delTitle,exclude){
	$("#lr_menu a").each(function(index){
		if($(this).text()==delTitle&&(checkIsNull(exclude))){
			$("#lr_menu dl:eq("+index+")").remove();
		}else if(exclude&&$(this).text()!=delTitle){
			delMenuByTitle($(this).text());
		}
	});
}
var tabCounter = 0; 
function addTab(url,title,resourceCode){
	var tabLiId = resourceCode+"_li";
	var label = title || "Tab " + tabCounter,
	id = "tabs-" + tabCounter++,
	tabTemplate = "<li><a href='#{href}' url='#{url}' onclick=\"locationTabAccord('#{url}','#{label}','#{tabLiId}');\"><span>#{label}</span></a><span class='ui-icon ui-icon-close' role='presentation'></span></li>",
	li = $(tabTemplate.replace( /#\{href\}/g, "#" + id ).replace( /#\{label\}/g, label ).replace(/#\{url\}/g, url).replace(/#\{tabLiId\}/g, resourceCode) );
	li.attr("id",tabLiId);
	li.find("a").attr("id", resourceCode+"_a");
	li.find("span").attr("id", resourceCode+"_span");
	$('#tabs').children(".ui-tabs-nav").append( li );
	$('#tabs').append("<div id='" + id + "'></div>"); 
	$('#tabs').tabs("refresh");
	$("#"+tabLiId).css((Number($("#"+tabLiId).css("width").substring(0,$("#"+tabLiId).css("width").indexOf("px")))+Number(200))+"px");
	if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){ 
		var widthLi =$("#"+tabLiId).css("width");
		widthLi = widthLi.substring(0,widthLi.length-2);
		$("#"+tabLiId).css("width",(Number(widthLi)+Number(20))+"px");
   }
	var lr_dl = $("<dl><dt><a href=\"#\" onclick=\"locationTabAccord('"+url+"','"+title+"','"+resourceCode+"')\">"+title+"</a></dt></dl>");
	lr_dl.attr("id",resourceCode+"_lr_menu_dl");
	lr_dl.find("a").attr("id", resourceCode+"_lr_menu_a");
	$("#lr_menu").append(lr_dl);
	$("#tabs").delegate( "span.ui-icon-close", "click", function() {
		$(".toActionColor").each(function(index){
			$(this).removeClass("toActionColor");
		});
		closeTab($(this).closest("li"));
	});
}
function removeTab(tab){
	var resourceCode = tab.attr("id").split("_")[0];
	var panelId = tab.attr( "aria-controls" );
	$("#" + panelId).remove();
	tab.remove();
	$("#"+resourceCode+"_lr_menu_dl").remove(); 
}
/**
 * 
 * @param currentLi
 * @param type type=1关闭其他，type=2关闭所有，type=0关闭当前
 */
function closeTab(currentLi,type){
	if(type){
		 $("#tabsUl li").each(function(index) {
			 if(index !=0 && ((type == 1 && $(this).attr( "aria-controls" ) != currentLi.attr( "aria-controls" )) || type == 2)){
				 removeTab($(this));
			 }
          });
	}else{
		if ($(currentLi).length > 0){ 
			removeTab(currentLi);
		}		
	}
	tabsgo (0);
	$("#tabs").tabs( "refresh" );			
	var lasLi = $("#tabsUl li").last();
    $('#tabs').tabs('option', 'active', lasLi.attr('index'));
    $('#tabs').append("<div id='"+ lasLi.attr("aria-controls")+ "'></div>");
    tabShowFlag = true;
    locationTabAccord(lasLi.find("a").attr("url"), lasLi.text(),lasLi.attr("id").split("_")[0]);
}
function tabsgo (lr) {
	if (lr){
		if($("#tabsUl li:visible:last").offset().top>$("#tabsUlIndex_li").offset().top){//防止全部被隐藏
			$("#tabsUl li:visible:eq(1)").hide();//eq(0)为所有分类，永不隐藏
		}
	}else{
		$("#tabsUl li:hidden:last").show();
	}
}	
/**
 * 设置tab标签页样式
 * @param liId
 */
function setCurrentLiCss(liId){
	$("#tabsUl li").each(function (index){
		if($(this).attr("id")==liId){
			$('#tabs').tabs('option', 'active', index);
			$(this).css("background-color","#fff"); 
			$(this).find("span").css("background-color","#fff"); 
			$(this).find("a").css("background-color","#fff");
			$(this).find("a").css("color","#000");
			while($(this).offset().top>$("#tabsUlIndex_li").offset().top)
				tabsgo (1);
			while($(this).offset().top<=0)
				tabsgo (0);
			$("#tabs").tabs( "refresh" ); 
		}else{
			$(this).css("background-color","#EBF1FB");
			$(this).find("span").css("background-color","#EBF1FB"); 
			$(this).find("a").css("background-color","#EBF1FB");
			$(this).find("a").css("color","#000");
		}
	});
}

function setActiveTab(tabid){
	var index = $("#tabsUl li").index($(tabid));
	tabsgo (0);
	$("#tabs").tabs( "refresh" );		
	$('#tabs').tabs('option', 'active', index);
    $('#tabs').append("<div id='"+ $(tabid).attr("aria-controls")+ "'></div>");
    tabShowFlag = true;
    locationTabAccord($(tabid).find("a").attr("url"), $(tabid).text(),$(tabid).attr("id").split("_")[0]);
}
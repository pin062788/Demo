/**
 * 格式化日期
 * @param date
 * @returns {String}
 */
function formatDateToString(date){
	if(checkIsNull(date)){
		date = new Date();
	}
	var year = date.getFullYear();
	var month = date.getMonth()+1;
	var day = date.getDate();
	if(month<10) month = "0"+month;
	if(day<10) day = "0"+day;
	return year+month+day;
}
function formateDateAndTimeToString(date)
{
	var hours = date.getHours();
	var mins = date.getMinutes();
	var secs = date.getSeconds();
	var msecs = date.getMilliseconds();
	if(hours<10) hours = "0"+hours;
	if(mins<10) mins = "0"+mins;
	if(secs<10) secs = "0"+secs;
	if(msecs<10) secs = "0"+msecs;
	return formatDateToString(date)+hours+mins+secs+msecs;
}
/**
 * 如果数字字段为空值，则返回0
 * @param cellValue
 * @returns {Number}
 */
function formateNumber(cellValue){
	return cellValue || 0;
}

/**
 * 适用于"移拨预留"模块的关联功能:对第二个Grid以及第三个Grid的数量比较
 */
function checkDetailIsRight(gridId,yzybylBh,wlBm,sccj,cksl){
	var rowData = getSelectRowDataAll(gridId);
	var bh = [];
	for(var i = 0; i<rowData.length; i++){
		if(i==0){
			bh.push(rowData[i][yzybylBh]);
		}else{
			var fl = false;
			for(var j=0;j<bh.length;j++){
				if(bh[j]==rowData[i][yzybylBh]){
					fl = true;
				}
			}
			if(!fl) bh.push(rowData[i][yzybylBh]);
		}
	}
	var countArr = [];
	for(var i=0;i<bh.length;i++){//总数
		var bhtmp = bh[i];
		var count = Number(0);
		var obj = {};
		obj.bh=bhtmp;
		for(var j=0;j<rowData.length;j++){
			if(rowData[j][yzybylBh]==bhtmp){
				count+=Number(rowData[j][cksl]);
			}
		}
		obj.count = count;
		countArr.push(obj);
	}
	return countArr;
}

/**
 * 统计所选择记录的某行总和（columnName列的值必须是数字）
 */
function sumSelected(gridId,columnName){
	var rowData = $('#'+gridId).jqGrid('getGridParam','selarrrow');;
	var count = '';
	if(rowData.length < 1){
		return 0;
	}else{
		for(var i=0; i<rowData.length; i++){
			value= $('#'+gridId).jqGrid('getCell',rowData[i],columnName);
			if(!checkIsNull(value)){
				count = Number(count);
				count+=Number(value);
			}
		}
	}
	if(checkIsNull(count)){
		return 0;
	}else{
		return count;
	}
}

/**
 * 适用于删除数据对查寻条件的静态刷新
 */
function refreshSelectData(url,selectBh,selectMc,objBh,objMc){
	$.get(url,function(json){
	dateJson = $.parseJSON(json);            	 			
	var bh='<option value="">=请选择=</option>';
	var mc='<option value="">=请选择=</option>';
		for(var i=0;i<dateJson.length;i++){
			bh+='<option value="'+dateJson[i][objBh]+'">'+dateJson[i][objBh]+ '</option>';
			mc+='<option value="'+dateJson[i][objMc]+'">'+dateJson[i][objMc]+ '</option>';
		}
		$("#"+selectBh).html(bh);
		$("#"+selectMc).html(mc);
 		});
}

/**
 * back weekday
 */
function getWeekDay(day){
			  var statusArr = ["","星期一","星期二","星期三","星期四","星期五","星期六","星期日"];
			  if(day<0 || day>7 || day==undefined || day==null || day=="")  day=0;
			  return statusArr[day];
}
/** 
 * close dialog and reload jqgrid
 */
function closeAndReload(dialog,list)
{
	$("#"+dialog).dialog("close");
	$("#"+dialog).html("");
	$("#"+list).jqGrid().trigger("reloadGrid");
}
/**
 * check all or invert or cancel
 * checkboxName  checkboxs is name
 * type=all select all
 * type=invert select invert
 * type=cancel cancel checked
 *
 */
function checkedAll(checkboxName,type){
	if(type=='all')
	$("input[name='"+checkboxName+"']").each(function(){this.checked=true;});
	else if(type=='invert')
	$("input[name='"+checkboxName+"']").each(function(){this.checked=!this.checked;});
	else if(type=='cancel')
	$("input[name='"+checkboxName+"']").each(function(){this.checked=false;});
		
}
/**
 * check is null
 * @param value is null return true,is not null return false
 * @returns {Boolean}
 */
function checkIsNull(value){
//	if((value!=null&&value!=""&&value!=''&&value!="null"&&value!="NULL"&&value!=undefined)||value==0)
	if(typeof value=="undefined") return true;
	if(value == 0 && (value+"").length>0) return false;
	if (value != null && value != "" && value != "null" && value != "NULL" && value != "\"\"" && value != undefined){  
	 return false;
	}else{
	 return true;
	}	
}
/**
 * show mask
 * */
function showLoading(){
	$("#loading").showLoading();
    //setTimeout("hideLoading()",30000);
}
/**
 * close mask
 * */

function hideLoading(){
	$("#loading").hideLoading();
}
/**
 * jquery validate
 * 
 * @param validateMap
 */
function validateForm(validateMap){
$("#"+validateMap.formId).validate({ 
	  onsubmit:true, 
	  onfocusout:false,
	  onkeyup :false,
	  rules: validateMap.rules, 
	  messages:validateMap.messages, 
	  submitHandler: validateMap.submitMethod,
	  invalidHandler: function(form, validator) {  
	      return false; 
	  },
	  showErrors: function(errorMap, errorList) {
		  $("#"+validateMap.showErrorId).text("");
		  if(errorList.length > 0){
			  for(var i=0;i<errorList.length;i++){
				  $("#"+validateMap.showErrorId).append("* "+errorList[i].message);
				  $("#"+validateMap.showErrorId).append("<br>");
			  }
			  return false;
          }else{
          	return true;
          }
	  }
	 });
}
/**
 * 公用ajax调用方法
 * @param params
 */
function submitAjax(params){
	$.ajax({
		type : checkIsNull(params.dataType)?"POST":params.type,
		url : params.url,							
		dataType : checkIsNull(params.dataType)?"json":params.dataType,
		data:params.data,
		success : params.success,
		error:checkIsNull(params.failedMethod)?failedMethod:params.failedMethod
	}).fail(function() {
		alert("加载失败");
	});
}
/**
 * 公用的ajax错误提示方法
 */
function failedMethod(data){
	alert("请求失败，请检查提交参数是否有误！");
}
/**
 * 日期比较
 * @param date
 */
function dateCompare(dates,sdate){
	var date = new Date();
	if(!checkIsNull(sdate)) date=sdate;
	dates = dates.replace(/-/g,"/");
	var start = new Date(dates);
	if(start.getFullYear()==date.getFullYear()){
		if(start.getMonth()==date.getMonth()){
			return 0;
		}
	}else if(start.getFullYear()>date.getFullYear()){
		return 1;
	}else {
		return -1;
	}
	
}
function selectRows(_gridId,allowMulti,allowZero,alterDataName){
	var rowIds = $("#"+_gridId).jqGrid('getGridParam','selarrrow');
	var count = rowIds.length;
	if(count == 0 ){
		if(!allowZero){
			alert(alterDataName?"请最少选择一条"+alterDataName:"请最少选择一条数据");
		}
		return;
	}
	else if(count >1 && !allowMulti)
	{
		alert(alterDataName?"最多只能选择一条"+alterDataName:"最多只能选择一条数据");
		return;
	}
	var rowDatas = new Array(rowIds.length);
	for(var i=0 ;i<rowIds.length;i++ ){
		rowDatas[i] = $("#"+_gridId).getRowData(rowIds[i]);
	}
	return rowDatas;
	
}

/**
 * Select widget : left <-> right
 * @param _right Select list on the right.
 * @param _left Select list on the left.
 * @param _btnR
 * @param _btnL
 * @param _btnAR
 * @param _btnAL
 */
function initLeftRight(_right,_left,_btnR,_btnL,_btnAR,_btnAL)
{
	$('#'+_right).dblclick(function(){
	       $("option:selected",this).appendTo('#'+_left);
	    });
	$('#'+_left).dblclick(function(){
	       $("option:selected",this).appendTo('#'+_right);
	    });
	
	
	$('#'+_btnR).click(function(event){
		event.preventDefault();
		$('#'+_left+' option:selected').appendTo('#'+_right);
	});
	$('#'+_btnAR).click(function(event){
		event.preventDefault();
		$('#'+_left+' option').appendTo('#'+_right);
	});
	
	$('#'+_btnL).click(function(event){
		event.preventDefault();
		$('#'+_right+' option:selected').appendTo('#'+_left);
	});
	$('#'+_btnAL).click(function(event){
		event.preventDefault();
		$('#'+_right+' option').appendTo('#'+_left);
	});
	
	$("#"+_btnR).button({
		text:false,
		icons : {primary: "ui-icon-arrowthick-1-e"}
	});
	$("#"+_btnL).button({
		text:false,
		icons : {primary: "ui-icon-arrowthick-1-w"}
	});
	$("#"+_btnAR).button({
		text:false,
		icons : {primary: "ui-icon-arrowthickstop-1-e"}
	});
	$("#"+_btnAL).button({
		text:false,
		icons : {primary: "ui-icon-arrowthickstop-1-w"}
	});
}

function initOrgWidget(_departFieldName,_orgDialogName)
{
	$("#"+_departFieldName).click(function(){
        $("#"+_orgDialogName).addClass("orgTreeWidget")
        			   .toggle("show")
        			   .position({my:"left top", at : "left bottom", of:this });
    });
}

//trim方法
function trim(str){  
    return str.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '');  
}
/**
 * 检查是否只有数字/字母/下划线，是返回true，不是返回false
 */
function checkOnlyEngAndNum(str){
	str = decodeURI(str);
	var reg = /^[0-9a-zA-Z_]+$/;
	if(reg.test(str)){
		return true;
	}
	return false;
}

/**
 * 检查是否存在特殊字符，存在返回true，不存在返回false
 */
function checkSpecialCharacter(str){
	str = decodeURI(str);
	var myreg=/^(?!.*?[\~\`\·\\\ \！\!@\#\￥\$%\……\^&\*\(\)\（\）\_\-\——\+\=\【\】\[\]\{\}\|\、：\:\;\；\"\”\“\’\'\'\<\>\《\》\,\，\。\.\?\？\/]).*$/;
	if (myreg.test(str)){
		return true;
	}
	return false;
}

/**
 * 检查是否存在特殊字符，存在返回true，不存在返回false
 */
function haveSpecialCharacter(str){
	str = decodeURI(str);
	var badword='?？%';  //------特殊字符--------
	for(var i=0;i<str.length;i++){
		char=str.charAt(i);
    	if (badword.indexOf(char)>=0){
			return true;
		}
	}
	return false;
}

/**
 * 检查数据状态，存在返回true，不存在返回false
 * currentValue:所选记录的状态
 * notContain：不允许处理的状态
 */
function statusCheck(currentValue,notContain){
	str = decodeURI(currentValue);
	for(var i=0;i<str.length;i++){
		char=str.charAt(i);
		if (notContain.indexOf(char)>=0){
			return true;
		}
	}
	return false;
}
/**
 * 格式化日期
 * @param date
 * @returns {String}
 */
function formatDate(date){
	if(checkIsNull(date)){
		date = new Date();
	}
	var year = date.getFullYear();
	var month = date.getMonth()+1;
	var day = date.getDate();
	if(month<10) month = "0"+month;
	if(day<10) day = "0"+day;
	return year+"-"+month+"-"+day;
}
function formateDateAndTime(date)
{
	var hours = date.getHours();
	var mins = date.getMinutes();
	var secs = date.getSeconds();
	if(hours<10) hours = "0"+hours;
	if(mins<10) mins = "0"+mins;
	if(secs<10) secs = "0"+secs;
	return formatDate(date)+" "+hours+":"+mins+":"+secs;
}
/**
 * 根据count返回时间列表
 * @param count
 * @param flag 等于true时只取年份，等于false时取年月日   都取上下count年
 */
function getYears(count,flag){	
	var dateList = new Array();
	var year = Number(formatDate().split("-")[0])-Number(count)-1;
	var date = "";
	for(var i=0;i<=count*2;i++){
		if(!flag){
			date = (Number(year)+i+1)+formatDate().substring(4);
			dateList.push(date);
		}else{
			dateList.push((Number(year)+i+1));
		}
	}
	return dateList;
}
/**
 * 返回所有月份列表 也可根据count返回几个月的列表
 * @param count
 */
function getMonths(count,start){
	if(checkIsNull(count)) count = 11;
	if(checkIsNull(start)) start = 0;
	if(count<0 || count>11) count=11;
	var dateList = new Array();
	for(var i=start;i<=count;i++){
		dateList.push(i+1);
	}
	return dateList;
}
/**
 * 格式化小数 zs
 * @param val 值
 * @param digit 小数位数
 * @param flag 是否去除小数后面多余的0
 * @returns
 */
function formatDecimals(val,digit,flag){
	 var tval = 0; 
	 if(checkIsNull(val)){
	   tval = 0;
	 }else{
	  if(!isNaN(val)){
	   if((""+val).indexOf(".")!=-1){
	     var decimalVal = (""+val).split(".")[1];
	     var end = decimalVal.length;
	     for(var i=decimalVal.length-1;i>=0;i--){
	    	 if(decimalVal.charAt(i)=="0"){
	    		end = i; 
	    	 }else{
	    		 break;
	    	 }
	     }
	     tval = (""+val).split(".")[0]+"."+decimalVal.substring(0,end);
	   }else{
		   tval = val;
	   }    
	  }else{
		  tval = 0;
	  } 
	}
	 if(!checkIsNull(digit) && digit>0){
		// if(Number(tval)==0 && flag){
		//	 tval = "0.0";
		// }else{
			 if((""+val).indexOf(".")!=-1){
				 tval = Number(tval).toFixed(digit);
			 }else{
				 var wz = "";
				 for(var i=0;i<digit;i++){
					 wz+="0";
				 }
				 tval = tval+"."+wz;
			 }
		 //}
	 }
	 if((""+tval).indexOf(".")!=-1 && flag){
	  decimalVal = (""+tval).split(".")[1];
      end = decimalVal.length;
     for(var i=decimalVal.length-1;i>=0;i--){
    	 if(decimalVal.charAt(i)=="0"){
    		end = i; 
    	 }else{
    		 break;
    	 }
     }
     if(end!=0)
    	 tval = (""+tval).split(".")[0]+"."+decimalVal.substring(0,end);
     else
    	 tval = (""+tval).split(".")[0];
     }
	 return tval;
}

/**
 * 日期比较精确到日 zs
 * @param date
 */
function dateCompareForDay(start,end){
	end =  end.replace(/-/g,"/");
	start = start.replace(/-/g,"/");
	start = new Date(start);
	 var date = new Date(end);
	if(start.getFullYear()==date.getFullYear()){
		if(start.getMonth()>date.getMonth()){
			return 1;
		}else if(start.getMonth()<date.getMonth()){
			return -1;
		}else{
			if(start.getDate()>date.getDate()){
				return 1;
			}else if(start.getDate()<date.getDate()){
				return -1;
			}else{
				return 0;
			}
		}
	}else if(start.getFullYear()>date.getFullYear()){
		return 1;
	}else {
		return -1;
	}
}
/**
 * 检验是否为数字
 * @param value
 * @returns
 */
function checkNumber(value) {
	return /^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test( value );
}
/**
 * 参数编码 zs
 * @param paramId
 * @returns
 */
function encodeParams(paramId){
	if(checkIsNull($("#"+paramId).val())) $("#"+paramId).val("");
	return encodeURI($.trim($("#"+paramId).val()));
}
/**
 * 根所数组返回参数KEY和VALUE的数组 zs
 * @param _arr
 * @returns {___anonymous9634_9635}
 */
function encodeParamsArr(_arr,queryArr){
	var _paramsJson = {};
	var _paramsArr = [];
	var val = "";
	if(!checkIsNull(_arr)){
		for(var i=0;i<_arr.length;i++){
			if(typeof _arr[i]=="object" && !checkIsNull(_arr[i]["queryName"])){
				val = encodeParams(_arr[i]["sourceName"]);
				_paramsJson[_arr[i]["sourceName"]] = val;
				if(!checkIsNull(_arr[i]["queryName"])) 
					_paramsJson[_arr[i]["queryName"]] = val;
			}else{
				val = encodeParams(_arr[i]);
				_paramsJson[_arr[i]] = val;
			}
			_paramsArr.push(val);
		}
		_paramsJson.paramsVal = _paramsArr;
	}
	if(!checkIsNull(queryArr)){
		setParamsName(queryArr, _paramsJson);
	}
	return _paramsJson;
}
/**
 * 判断是否有特殊字符 zs
 * @param _arr
 */
function checkIsSpecialCharacter(obj){
	if(obj && typeof obj==='object' && Array == obj.constructor){
		for(var i=0;i<obj.length;i++){
			if(haveSpecialCharacter(obj[i]))
				return true;
		}
	}else if(typeof obj ==="object"){
		for(var i in obj){
			if(haveSpecialCharacter(obj[i]))
				return true;
		}
	}
	return false;
}
/**
 * 判断数组值是否有空值 zs
 * @param _arr
 */
function checkIsNull_arr(_arr){
	if(!checkIsNull(_arr)&&_arr.length>0){
		for(var i=0;i<_arr.length;i++){
			if(checkIsNull(_arr[i]))
				return true;
		}
	}
	return false;
}
/**
 * 按参数重新加载 grid zs
 * @param gridId
 * @param url
 * @param paramsJson
 * @param callback
 */
function reloadJqGrid(gridId,url,paramsJson,callback){
	//alert(paramsJson.paramsVal);  	 
	 paramsJson.paramsVal = undefined;    	            	 
	// alert(paramsJson.paramsVal);  	 
	 $("#"+gridId).jqGrid('setGridParam',{
	 url: url,
	 //beforeRequest: callback,
	 postData:paramsJson
	}).trigger("reloadGrid");
}
/**
 * 创建按扭事件 zs
 * @param butId
 * @param ico
 * @param showTxt
 * @param callback
 */
function createButtonClick(butId,ico,showTxt,callback){
	$("#"+butId).button({
		icons : {primary : ico},
		label:showTxt
	}).click(callback);
}
/**
 * 初始化日期控件 zs
 * @param id
 */
function initDate(id){
	$("#"+id).datepicker();
}
/**
 * 关闭弹出窗口 zs
 * @param diogId
 */
function closeDiogById(diogId){
	if($("#"+diogId).length>0){ 
		$("#"+diogId).html("");
		$("#"+diogId).dialog("close");
	}
}
/**
 * 返回选中行ID zs
 * @param gridId
 * @returns
 */
function getSelectRowData(gridId){
	return  $('#'+gridId).jqGrid('getGridParam','selarrrow');
}
/**
 * 返回所有选中行的对象
 * @param gridId
 * @returns {Array}
 */
function getSelectRowDataAll(gridId){
	var rowData = [];	
	var rowIds =  getSelectRowData(gridId);
	for(var i=0;i<rowIds.length;i++){
		var data = $("#"+gridId).jqGrid("getRowData",rowIds[i]);
		rowData.push(data);
	}
	rowIds = null;
	return rowData;
}
/**
 * 返回所有行的对象
 * @param gridId
 * @returns {Array}
 */
function getRowDataAll(gridId){
	return  $('#'+gridId).jqGrid('getRowData');;
}

/**
 * 返回选中行对象根据rowid zs
 * @param gridId
 * @returns
 */
function getSelectRowDataById(gridId,rowId){
	return  $('#'+gridId).jqGrid('getRowData',rowId);
}
/**
 *  动态添加grid行记录,grid不在当前页面时调用 zs
 * @param addGrid
 * @param sourceGrid
 * @param columnNameArr
 * @param rowId
 * @param isAllFlag
 * @param interTime
 */
function setAddGridData(addGrid,sourceGrid,columnNameArr,rowId,isAllFlag,interTime){
	if(typeof interTime!="undefined") {
		if($("#"+addGrid).length>0){
    		setRowDataToOther(addGrid,sourceGrid,columnNameArr,rowId,isAllFlag);
			window.clearInterval(interTime);
        }
	}else {
		interTime = window.setInterval(function(){
			setAddGridData(addGrid,sourceGrid,columnNameArr,rowId,isAllFlag,interTime);
		},1300);
	}
}
/** 
 * 动态添加grid行记录 zs
 */
function setRowDataToOther(addGridId,sourceGridId,columnNameArr,rowId,isAllFlag,callBack){
	if($("#"+addGridId).length<=0) {
		$("table[id^='"+addGridId+"']").each(function (){
			addGridId = $(this).attr("id");
		});
	}
	if(checkGridRowIsNull(addGridId,columnNameArr)){
		alert("新增加行未保存且数据不能为空，保存信息后再继续添加！");
		return;
	}
	if(checkIsNull(sourceGridId)){
		if(typeof addFlag !="undefined") addFlag = false;
		//var rowIds = $("#"+addGridId).jqGrid('getDataIDs'); 
        var rowDataId = new Date().getTime();  
        //var rowDataId = (rowIds.length ==0 ? 1: Math.max.apply(Math,rowIds)+1);  
		var myData = {};
		for(var j=0;j<columnNameArr.length;j++){				
				myData[columnNameArr[j]["addName"]]="";  
				if(typeof columnNameArr[j]["value"] !="undefined"){ 
					myData[columnNameArr[j]["addName"]]=columnNameArr[j]["value"];					
				}
		}
		myData["addFlag"]=true;
		$("#"+addGridId).addRowData(rowDataId, myData, "first");
	}else{
		if(typeof addFlag !="undefined") addFlag = true;
		var rowData = getSelectRowData(sourceGridId);
		if(isAllFlag) rowData = $("#"+sourceGridId).jqGrid('getDataIDs');		
		var val = "";
		var rowDataId ="";
		for(var i=0;i<rowData.length;i++){
			if(typeof rowId !="undefined" && !checkIsNull(rowId)){
				if(rowId!=rowData[i]) continue;//选中一行后显示明细，取消后明细删除
			}
		/*	var rowIds = $("#"+addGridId).jqGrid('getDataIDs'); 
	        var rowDataId = (rowIds.length ==0 ? 1: Math.max.apply(Math,rowIds)+1);*/
			rowDataId = new Date().getTime(); 
			//var rowDataId = $("#"+addGridId).getDataIDs().length + 1;
			var myData = {};
			for(var j=0;j<columnNameArr.length;j++){
				val = $('#'+sourceGridId).jqGrid('getCell', rowData[i], columnNameArr[j]["sourceName"]);
				if(typeof columnNameArr[j].pkId !="undefined" && !checkIsNull(columnNameArr[j].pkId) && columnNameArr[j].pkId){
					if(!checkGridRowIdExist(addGridId,columnNameArr[j]["addName"],val)){
						alert("已添加过当前记录请重新选择！");
						return;
					}
				}
				if(typeof columnNameArr[j]["sourceName"] !="undefined"){
				if(!checkIsNull(columnNameArr[j]["sourceName"]))
					myData[columnNameArr[j]["addName"]]=val;
				else
					myData[columnNameArr[j]["addName"]]=rowDataId;
				}else if(typeof columnNameArr[j]["value"] !="undefined"){ 
					myData[columnNameArr[j]["addName"]]=columnNameArr[j]["value"];					
				}else{
					myData[columnNameArr[j]["addName"]] = "";
				}
			}
			//myData["addFlag"]=false;
			addFlag = false;
			$("#"+addGridId).addRowData(rowDataId, myData, "first");
		}
		if(!checkIsNull(callBack)) callBack();
		return rowDataId;		
	}
	
}
/**
 * 编辑行 zs
 * @param grid
 * @param rowId
 * @param lastsel
 * @param columnNameArr
 */
function editGridRow(grid,rowId,lastsel,columnNameArr){
	if(rowId && rowId!==lastsel){		
		if(typeof lastsel !="undefined"){
			if(!checkIsNull(columnNameArr) && columnNameArr.length>0)
			for(var i=0;i<columnNameArr.length;i++){
				eval("var _"+columnNameArr[i]+" = $(\"#"+lastsel+"_"+columnNameArr[i]+"\").val()");
			}
			if($('#'+grid).jqGrid('getCell', lastsel, "addFlag")=="true"|| checkIsNull(columnNameArr) || columnNameArr.length==0){
				$("#"+grid).jqGrid('saveRow',lastsel);
				var rowData = $("#"+grid).jqGrid('getRowData',lastsel);
			}
			if(!checkIsNull(columnNameArr) && columnNameArr.length>0 && eval("_"+columnNameArr[0])!="undefined" && eval("_"+columnNameArr[0])!=undefined){	  
				$('#'+grid).jqGrid('restoreRow',lastsel); 
				for(var i=0;i<columnNameArr.length;i++){
					$('#'+grid).setCell(lastsel,columnNameArr[i], eval("_"+columnNameArr[i]));
				}
			}else if(checkIsNull(columnNameArr) || columnNameArr.length==0){
				$('#'+grid).jqGrid('restoreRow',lastsel); 
			}
		}
		this.lastsel=rowId;
	}
		$('#'+grid).jqGrid('editRow',rowId,true);
}
/**
 * 删除行 zs
 * @param grid
 */
function removeGridRow(grid,delName,val,rowId){
	if(typeof delName !="undefined" && !checkIsNull(delName)){
		var rowData = $("#"+grid).jqGrid('getDataIDs');
		for(var i=0;i<rowData.length;i++){
			if($('#'+grid).jqGrid('getCell', rowData[i], delName)==val){		
				$('#'+grid).jqGrid('delRowData',rowData[i],true);
				i--;
			}
		}
	}else if(!checkIsNull(rowId)){
		$('#'+grid).jqGrid('delRowData',rowId,true);	
	}else{
		var rowData = getSelectRowData(grid);
		if(rowData.length>0){
			if(confirm("确认要删除当前选中的记录吗？"))
				for(var i=0;i<rowData.length;i++){
					$('#'+grid).jqGrid('delRowData',rowData[i],true);
					i--;
				}
		}else{
			alert("请选择至少一条记录!");
		}
	}
}
/**
 * 是否存在相同记录 zs
 * @param gridId
 * @param pkId 按哪个字段来检查是否重复
 * @param val
 * @returns {Boolean}
 */
function checkGridRowIdExist(gridId,pkId,val,columnArr,rowId){ 
	if($("#"+gridId).length<=0) {
		$("table[id^='"+gridId+"']").each(function (){
			gridId = $(this).attr("id");
		});
	}
	var flag = true;
	var rowData = $("#"+gridId).jqGrid('getDataIDs');
	if(!checkIsNull(pkId))
	for(var i=0;i<rowData.length;i++){
		if($('#'+gridId).jqGrid('getCell', rowData[i], pkId)==val){			
			return false;
		}
	}else if(!checkIsNull(columnArr)){ 
		var tmpIsExs = "";
		for(var i=0;i<rowData.length;i++){
			if(rowData[i]==rowId) continue;  
			tmpIsExs = "if(";
			for(var j=0;j<columnArr.length;j++){ 
				if(j!=0) tmpIsExs+="&&";
				tmpIsExs+="$('#"+gridId+"').jqGrid('getCell', rowData["+i+"], columnArr["+j+"].name)==columnArr["+j+"].value";
			}
			tmpIsExs += ") {flag =  false;i=rowData.length;}"; 
			eval(tmpIsExs);
		}
	}
	return flag;
}
/**
 * 检查是否已有空行 zs
 * @param gridId
 * @param _colArr
 * @returns {Boolean}
 */
function checkGridRowIsNull(gridId,_colArr){
	var rowData = $("#"+gridId).jqGrid('getDataIDs');
	var flag = false;
	var count = 0;
	for(var i=0;i<rowData.length;i++){
		flag = true;
		if(count>=1){
			alert("添加数据有空行记录，请检查！");
			return true;
		}
		var ckcolumnval = "if(";
		var infoTipMsg = "";
		var infoCount = 0;
		for(var j=0;j<_colArr.length;j++){
			if(!checkIsNull($('#'+gridId).jqGrid('getCell', rowData[i], _colArr[j]["addName"]))){
				if($.trim($('#'+gridId).jqGrid('getCell', rowData[i], _colArr[j]["addName"])).indexOf("input ")!=-1 || $.trim($('#'+gridId).jqGrid('getCell', rowData[i], _colArr[j]["addName"])).indexOf("INPUT ")!=-1){
	        		//removeKeypress([rowData[i]+"_sqsl"]);
//					if($('#'+gridId).jqGrid('getCell', rowData[i], _colArr[j].addName).indexOf("<input type")!=-1){					
				/*	$('#'+rowData[i]+'_'+_colArr[j]["addName"]).focus();
					var e = jQuery.Event("keydown"); 
					e.keyCode = 13;
					$('#'+rowData[i]+'_'+_colArr[j]["addName"]).trigger(e);//*/
					alert("您有未保存的数据，请先保存数据再提交");
					return true;
					if($.trim($('#'+gridId).jqGrid('getCell', rowData[i], _colArr[j]["addName"])).indexOf("input type")!=-1) 
					{
						j++;
					//	alert("请在编辑行中按回车键保存当前输入的数据!");
						return false;
					}else{
						j--;
					}
					continue;
				}
				flag = false;
			}else if(typeof _colArr[j]["infoName"] !="undefined" && typeof _colArr[j]["group"]!="undefined"){ 
				if(ckcolumnval.length>3) ckcolumnval+="&&";
					ckcolumnval+="checkIsNull($(\'#"+gridId+"\').jqGrid(\'getCell\', rowData["+i+"], _colArr["+j+"][\"addName\"]))";
					if(!checkIsNull(infoTipMsg)) infoTipMsg+=",";
					infoTipMsg+=_colArr[j]["infoName"];
					infoCount++;
				//alert( _colArr[j]["infoName"]+"为必填项，请点击编辑按扭进行填写！");
			//	return true;
			}else if(typeof _colArr[j]["infoName"] !="undefined" ){
				alert( _colArr[j]["infoName"]+"为必填项，请点击编辑按扭进行填写！");
				return true;
			}
		}
		if(ckcolumnval.length>3&&infoCount>1){
			ckcolumnval+="){i=rowData.length;flag = true;}";
			eval(ckcolumnval);
			if(flag){
				alert(infoTipMsg+"至少填写一项！");
				return true;
			}
		}
		if(flag) count++;		
	}
	if(flag) alert("添加数据有空行记录，请检查！");
	return flag;
}
/**
 * 检查是否处于行编辑状态
 * @param gridId
 * @param _colArr
 * @returns {Boolean}
 */
function checkGridRowIsEdit(gridId,_colArr){
	var rowData = $("#"+gridId).jqGrid('getDataIDs');
	for(var i=0;i<rowData.length;i++){
		for(var j=0;j<_colArr.length;j++){
			if($.trim($('#'+gridId).jqGrid('getCell', rowData[i], _colArr[j]["addName"])).indexOf("input")!=-1){
				alert("您有未保存的数据，请先保存数据再提交");
				return true;
			}
		}
	}
	return false;
}
/**
 * 数据组转字符串 zs
 */
function arrToString(_arr,name){
	var result = "";
	for(var i=0;i<_arr.length;i++){
		result+=_arr[i][name];
		if(i!=_arr.length-1) result+=",";
	}
	return result;	
}
/**
 * 重新加载grid zs
 * @param gridId
 */
function reloadGridById(gridId){
	$("#"+gridId).jqGrid().trigger("reloadGrid");
}
/**
 * 返回选中ID数组 zs
 * @param gridId
 */
function getSelectRowsIds(gridId,idName){
	var rowData = getSelectRowData(gridId);
	var ids = [];
	if(rowData.length > 0){
		for (var i = 0; i < rowData.length; i++) {
			ids.push($('#'+gridId).jqGrid('getCell', rowData[i], idName));
		}
	}
	return ids;
}

/**
 * 根据选择行按ID删除 zs
 * @param grid
 * @param idName
 * @param url
 * @param params
 * @param callback
 */        	
function deleteById(grid,idName,url,params,callback){
	var rowData = getSelectRowData(grid);
    if(rowData.length < 1) {
    	alert("请选择记录");
		return;
    }
  var  ids = getSelectRowsIds(grid,idName);
    params.ids = encodeURI(ids);
    if (confirm("确认删除数据吗？")) {
    	submitAjaxPost(grid,url,params,callback);
    }
}
/**
 * 根据不同的URL和参数，执行ajax post请求 zs
 * @param url
 * @param params
 * @param callback
 */
function submitAjaxPost(grid,url,params,callback){
	$.postAjax(url,params,typeof jqGridBeforeRequest!="undefined"?jqGridBeforeRequest:"",function(data){
		if(!checkIsNull(callback) && typeof callback != "undefined") 
			callback(data);
		else{
			data = $.parseJSON(data);
			alert(data);
			reloadGridById(grid);
		}
	}); 
}
/**
 * 验证参数 zs
 * @param _arr
 * @param validateMap
 */
function rulesByArr(_arr,validateMap){
	var rules = {};
	var messages = {};
	if(!checkIsNull(_arr)&&$.isArray(_arr)&&_arr.length>0){
		for(var i=0;i<_arr.length;i++){
			var ruleTmp = {};
			var msgTmp = {};
			for(var j=0;j<_arr[i].rules.length;j++){
				ruleTmp[_arr[i].rules[j].name]=_arr[i].rules[j].value;
				msgTmp[_arr[i].rules[j].name]=_arr[i].rules[j].msg;
			}
			rules[_arr[i].name] = ruleTmp;
			messages[_arr[i].name] = msgTmp;
		}
		validateMap.rules=rules;
		validateMap.messages=messages;
		validateForm(validateMap);
	}else{
		alert("参数格式有误！");
	}
}
/*点了加号后加载详细
 */ 
 function imepLoad(id, url){
		$.get(url,function(data){
			$("#" + id).html(data);
		}).fail(function(){
			alert("加载失败");
		});
}
/**
 * 按参数查询 zs
 * @param params
 * @param url
 * @param errorId
 * @param reloadGridId
 * @param msg
 */
function queryByParams(params,url,errorId,reloadGridId,msg,queryName){
	var paramsJson = {};
	paramsJson = encodeParamsArr(params,queryName);
	 if(checkIsSpecialCharacter(paramsJson.paramsVal)){
			$("#"+errorId).show();
         	$("#"+errorId).html(checkIsNull(msg)?"不能输入特殊字符，请重新输入！":msg);
         }else{
         	 $("#"+errorId).hide();
			 paramsJson.timer = new Date().getTime();
	         reloadJqGrid(reloadGridId,url,paramsJson,null);
         }
}
/**
 * 保存方法 zs
 * @param params
 * @param url
 * @param grid
 * @param errorDivId
 * @param detailAllFlag
 * @param updateId
 * @param callBack
 * @param columnNameArrs
 * @returns {Boolean}
 */
function saveOrUpdateSharing(params,url,grid,errorDivId,detailAllFlag,updateId,callBack,columnNameArrs,updateIdName,queryName){
	var paramsJson = {};
	paramsJson = encodeParamsArr(params,queryName);
	if(checkIsSpecialCharacter(paramsJson.paramsVal)){
			$("#"+errorDivId).show();
         	$("#"+errorDivId).html("不能输入非法字符百分号或问号，请重新输入！");
    }else{
   		 paramsJson.paramsVal = undefined;    	            	 
    	 $("#"+errorDivId).hide();
    	 var getTimestamp=new Date().getTime();
    	 paramsJson.timer = getTimestamp;
    	 var obj = [];
    	 var message = "";
    	 if(detailAllFlag){//是否为选中行还是所有行
    	 	 obj = $("#"+grid).jqGrid("getRowData");
    	 	 message = "明细不能为空，请添加明细";
    	 }else{
    		 obj = getSelectRowDataAll(grid);
    		 message = "请至少选一条记录";
    	 }
    	 if(obj.length>0){
    		 if(checkGridRowIsNull(grid,columnNameArrs)){
    			 alert(message);
    			 return false;
    		 }
    	 }else{
    		 alert(message);
    		 return false;
    	 }
    		 if(detailAllFlag){//是否为选中行还是所有行true选中所有行
        	 	 obj = $("#"+grid).jqGrid("getRowData");
        	 }else{
        		 obj = getSelectRowDataAll(grid);
        	 }
    		 for(var i=0;i<obj.length;i++){
    			 if(typeof obj[i]["option"]!="undefined") delete obj[i]["option"];
    		 }
    		 paramsJson.detail = obj;
    		 paramsJson.detailJson = JSON.stringify(obj);
    		 paramsJson.detailSize = obj.length;  
//    		 alert(JSON.stringify(obj));
    		 if(!checkIsNull(updateId)){
    			 paramsJson.updateId = updateId;
    			if(!checkIsNull(updateIdName)) paramsJson[updateIdName] = updateId;
    		 }
    	 	submitAjaxPost(grid,url,paramsJson,!checkIsNull(callBack)?callBack:function(data){
    	 		var result = $.parseJSON(data);
    	 		alert(result.message);
    	 		if(result.status=="1"){
    	 			if(typeof addFlag!="undefined") addFlag = true;
    	 			reloadGridById(grid);
    	 		}
    	 	});
    }
}
/**
 * 设置查询的名称
 * @param _arr
 * @param paramJson
 */
function setParamsName(_arr,paramJson){
	for(var i=0;i<_arr.length;i++){
		var param = _arr[i];
		if(!checkIsNull(param["queryName"]))
			paramJson[param["queryName"]] = paramJson[param["sourceName"]];		
	}
}
/**
 * 编辑行
 * @param rowid
 * @param grid
 */
function editRowData(rowid,grid){
	$("#"+grid).jqGrid('editRow',rowid);
	$("#"+rowid+"_save").css({"display":"block"});
	$("#"+rowid+"_cancle").css({"display":"block"});
	$("#"+rowid+"_edit").css({"display":"none"});
	$("#"+rowid+"_del").css({"display":"none"});
}

/**
 * grid 添加按钮
 * @param cellValue
 * @param rowObject
 * @returns {String}
 */
function createButton(text,id,rowObject,onclick){
	var btn = "<button type=\"button\" id='"+rowObject.rowId+id+"' onclick='"+onclick+"("+rowObject.rowId+")'>"+text+"</button>";
	return btn;
}
/**
 * 创建 编辑/保存/取消 按扭
 * @param cellValue
 * @param rowObject
 * @returns {String}
 */
function createEditSaveButton(cellValue,rowObject,grid,delFlag,cancleFlag){
	var editBut = "<ul class=\"editButtonCss\"><li><button type=\"button\" id=\'"+rowObject.rowId+"_edit\' onclick=\'editRowData(\""+rowObject.rowId+"\",\""+grid+"\")\' >编辑</button></li>";
	var saveBut = "<li><button type=\"button\" id=\'"+rowObject.rowId+"_save\' onclick=\'saveRowData(\""+rowObject.rowId+"\",\""+grid+"\")\' style=\'display:none\'>保存</button></li>";
	var cancleBut = "<li><button type=\"button\" id=\'"+rowObject.rowId+"_cancle\' onclick=\'cancleRowData(\""+rowObject.rowId+"\",\""+grid+"\")\' style=\'display:none\'>取消</button></li>";
	var delBut = "<li><button type=\"button\" id=\'"+rowObject.rowId+"_del\'  onclick=\'removeGridRow(\""+grid+"\",\"\",\"\",\""+rowObject.rowId+"\")\'>删除</button></li>";
	var showBut = editBut+saveBut;	
	if(cancleFlag) showBut+=cancleBut;
	if(delFlag) showBut+=delBut;
	showBut += "</ul>";
	return showBut;
}
/**
 * 取消编辑
 * @param rowid
 * @param grid
 */
function cancleRowData(rowid,grid){
$('#'+grid).restoreRow(rowid);
$("#"+rowid+"_save").css({"display":"none"});
$("#"+rowid+"_cancle").css({"display":"none"});
$("#"+rowid+"_edit").css({"display":"block"});
$("#"+rowid+"_del").css({"display":"block"});
}


/**
 * 新增和修改共用
 * @param type
 * @param titles
 * @param grid
 * @param idName
 * @param url
 * @param dialogDiv
 * @param params
 */
function addOrUpdate_index(type,titles,grid,idName,url,dialogDiv){
	var id="";
	var title = "新增"+titles;
	if(!checkIsNull(type)){
		title = "修改"+titles;
		var rowData = getSelectRowData(grid);
	    if(rowData.length != 1) {
	    	alert("请选择一条记录");
	    //	reloadGridById("feed_searchList");
			return;
	    }
	    id = getSelectRowsIds(grid,idName);
	}
	openDialogPost(dialogDiv, url,title,{"_time_":new Date().getTime(),"id":encodeURI(id)});
}
/**
 * 返回描述
 * @param codeList
 * @param cellvalue
 * @returns
 */
function formatterDesc(codeList,cellvalue){
	if(cellvalue){
		cellvalue = trim(cellvalue);
	}
    return getReferenceDesc(codeList,cellvalue);
}
/**
 * 返回code
 * @param codeList
 * @param cellvalue
 * @returns
 */
function unformatCode(codeList,cellvalue){
    return getTargetColumnValue(codeList,cellvalue,'codeDesc','code');
}
/**
 * 设置列值
 * @param url
 * @param gridId 
 * @param paramsValue
 * @param rowId
 * @param columnNameArr
 */
function setKcxxBhValue(url,gridId,paramsValue,rowId,columnNameArr){ 
	var params = {
				url:url,
				data:paramsValue,
				success:function(data){
					if(typeof data.data !="undefined" && data.data.length==1){
						var result = data.data[0];
						for(var i=0;i<columnNameArr.length;i++){
							$('#'+gridId).setCell(rowId,columnNameArr[i], result[columnNameArr[i]]);
						}
					}else{
						alert("未查到库信息，请重新选择!");
					}
				}
			}; 
	submitAjax(params);
}
/**
 * 验证是否为日期
 * @param ckdate
 * @returns {Boolean}
 */
function checkIsDate(ckdate){ 
	return (new Date(ckdate).getDate()==ckdate.substring(ckdate.length-2));   
}
/**
 * 返回某一列的值
 * @param grid
 * @param rowid
 * @param cellName
 */
function getJqgridCellVal(grid,rowid,cellName){
	return $('#'+grid).jqGrid('getCell', rowid, cellName);
}
function scrollBottom(contentId){
	var scrollTop = $("#"+contentId)[0].scrollHeight;  
	$("#"+contentId).scrollTop(scrollTop);  
}
function setCookie(name,value,days) //一个是cookie的名字，一个是值
{
    var exp  = new Date(); 
    exp.setTime(exp.getTime() + days*24*60*60*1000);
    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString()+";path=/";
}

function getCookie(name)       
{
    var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
    if (arr != null) 
    	return unescape(arr[2]); 
    return null;

}
function removeDotProperty(o)
{
	if(typeof o == "string" || o == null) {
		return o;
	}else{
		if(!o.sort){
   			var newObj={};  
       	    for(var i in o){
	       	    if(i.indexOf(".")<0){
	       	    	newObj[i]=removeDotProperty(o[i]);
	       	    }
       	    }
       	 	return newObj;
		}else{
				var a = new Array();
				for(var i =0;i<o.length;i++){
					a.push(removeDotProperty(o[i]));
				}
				return a;
		}
    }
}
function Map(){
	this.container = new Object();
}
Map.prototype.put = function(key, value){
	this.container[key] = value;
};
Map.prototype.get = function(key){
	return this.container[key];
};
Map.prototype.keySet = function() {
	var keyset = new Array();
	var count = 0;
	for (var key in this.container) {
	// 跳过object的extend函数
	if (key == 'extend') {
		continue;
	}
	keyset[count] = key;
	count++;
	}
	return keyset;
};
Map.prototype.size = function() {
	var count = 0;
	for (var key in this.container) {
		// 跳过object的extend函数
		if (key == 'extend'){
			continue;
		}
		count++;
	}
	return count;
};
Map.prototype.remove = function(key) {
	delete this.container[key];
};
Map.prototype.toString = function(){
	var str = "";
	for (var i = 0, keys = this.keySet(), len = keys.length; i < len; i++) {
		str = str + keys[i] + "=" + this.container[keys[i]] + ";\n";
	}
	return str;
};
Array.prototype.remove = function(val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};

function getColumnValueArray(gridId,columnName,excluedValue,selected){
	var rowData;
	if(selected){
		rowData =selectRows(gridId,true,true);
	}else{
		rowData = $('#'+gridId).jqGrid('getRowData');
	}
   	var valueArray = new Array();
    if(rowData.length > 0){
       	for(var i=0;i<rowData.length;i++){
       		value= $('#'+gridId).jqGrid('getCell',rowData[i],columnName);
       		if(excluedValue){
       			if(rowData[i][columnName] != excluedValue){
       				valueArray.push(rowData[i][columnName]);
       			}
       		}else{
       			valueArray.push(rowData[i][columnName]);
       		}
            
        }
   	}
    return valueArray;
}
function getDataColumnValueArray(rowData,columnName){
  	if(rowData){
  		var valueArray = new Array();
  	    if(rowData.length > 0){
  	       	for(var i=0;i<rowData.length;i++){
  	            valueArray.push(rowData[i][columnName]);
  	        }
  	   	}
  	  return valueArray;
  	}else{
  		return;
  	}
}
function clearGridData(gridId){
	$("#"+gridId).jqGrid('clearGridData');
}

/**
 * wuyuan 
 * @param gridName
 * @param cellName
 * @param method
 * @param preName 前置条件
 * @param ralateName 同步设置字段
 */
//gird合并单元格公共调用方法
function Merger(gridName, cellName ,method ,preName ,ralateName) {
	var gridId="#"+gridName;
	var cellId="#"+gridName+"_"+cellName.replace('.','');
    //得到显示到界面的id集合
    var mya = $(gridId).getDataIDs();
    //当前显示多少条
    var length = mya.length;
    for (var i = 0; i < length; i++) {
 	   //从上到下获取一条信息
       var before = $(gridId).jqGrid('getRowData', mya[i]);
 	   if(method=='rowspan'){    		   	           
           //定义合并行数
           var rowSpanCount = 1;
           for (j = i + 1; j <= length; j++) {
               //和上边的信息对比 如果值一样就合并行数+1 然后设置rowspan 让当前单元格隐藏
               var end = $(gridId).jqGrid('getRowData', mya[j]);
               if (!preName || before[preName] == end[preName]){
	               if (before[cellName] == end[cellName]) {
	            	   rowSpanCount++;
	                   $(cellId+ mya[j]).attr("style","display:none");
	                   if(ralateName){
	                	   $("#" +gridName+"_"+ ralateName.replace('.','') + mya[j]).attr("style","display:none");
	                   }
	               } else {
	                   break;
	               }
               }
           }
           if(rowSpanCount>1){
               $(cellId+ mya[i]).attr("rowspan", rowSpanCount);
               if(ralateName){
            	   $("#"+gridName+"_" + ralateName.replace('.','') + mya[i]).attr("rowspan", rowSpanCount);
               }
           }
	   }else{
		   //检查模板横向合并用，（同行不同列合并）
		   if(before.divideFlag!=1){
			   var colspanCount=$("#indexColspan"+mya[i]).attr("colspanValue");
			   $("#indexColspan"+mya[i]).attr("colspan",colspanCount);
			   $(".colspanField"+mya[i]).attr("style","display:none");
		   }
	   }
	}
}

/**
 * 通过reference JSON data 获取 jqgrid column select formatter edit options参数值 
 * @param refJsonData
 * @returns {String}
 */
function getEditOptionsByRefJsonData(refJsonData) {
	var value = ":";
	if(refJsonData && refJsonData.length && refJsonData.length > 0) {
		for(var i=0; i<refJsonData.length; i++) { 
			value += (";"+refJsonData[i].code+":"+refJsonData[i].codeDesc); 
		}
	}
	return value;
}
Date.prototype.format = function(format) {  
    /* 
     * eg:format="yyyy-MM-dd hh:mm:ss"; 
     */  
    var o = {  
        "M+" : this.getMonth() + 1, // month  
        "d+" : this.getDate(), // day  
        "h+" : this.getHours(), // hour  
        "m+" : this.getMinutes(), // minute  
        "s+" : this.getSeconds(), // second  
        "q+" : Math.floor((this.getMonth() + 3) / 3), // quarter  
        "S" : this.getMilliseconds() // millisecond  
    };
  
    if (/(y+)/.test(format)) {  
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4  
                        - RegExp.$1.length));  
    }  
  
    for (var k in o) {  
        if (new RegExp("(" + k + ")").test(format)) {  
            format = format.replace(RegExp.$1, RegExp.$1.length == 1  
                            ? o[k]  
                            : ("00" + o[k]).substr(("" + o[k]).length));  
        }  
    }  
    return format;  
};

function checkGridIsEdit(gridId,rowId){
	if(rowId){
		var row = $('#'+gridId).jqGrid('getRowData', rowId);
		for(var k in row){
			if(row[k].indexOf("input")!=-1){
				return true;
			}
		}
	}else{
		var rows = $('#'+gridId).jqGrid('getRowData');
		for(var j=0;j<rows.length;j++){
			for(var k in rows[j]){
				if(rows[j][k].indexOf("input")!=-1){
					return true;
				}
			}
		}
	}
}

function editAndSaveRow(gridId,rowId){
	if(checkGridIsEdit(gridId,rowId)){
		$('#'+gridId).jqGrid('saveRow',rowId,false, 'clientArray');
	}else{
		$('#'+gridId).jqGrid('editRow',rowId,true); 
	}
}
function getEditOptionsByJsonData(jsonData, keyName, valueName) {
	var value = ":";
	if(jsonData && jsonData.length && jsonData.length > 0) {
		for(var i=0; i< jsonData.length; i++) { 
			value += (";"+jsonData[i][keyName]+":"+jsonData[i][valueName]); 
		}
	}
	return value;
}
/**
 * 判断grid中是否有还在行编辑状态的行，可用于验证保存是否成功
 * @param gridId
 * @returns {Boolean}  true: grid正处于编辑状态； false: grid中没有正在编辑的行
 */
function isGridEditing(gridId) {
	var grid = $('#'+gridId);
	if(grid){
		if($("#"+gridId+" tr[role=\"row\"][editable=\"1\"]").length == 0){
			return false;
		}
		else{
			return true;
		}
	}
	else{
		return true;
	}
}
function log(msg)
{
	if(typeof(console) != 'undefined' && console.log)
	{
		console.log(msg);
	}
}
/**处理运算精度丢失问题**/
Number.prototype.add = function(num){
	var str1 = this.toString();
	var arr1 = str1.split('.');
	var len1 = arr1[1]? arr1[1].length:0;
	if(num)
	{
		var str2 = num.toString();
		var arr2 = str2.split('.');
		var len2 = arr2[1]? arr2[1].length:0;
		
		
		var baseNum = Math.pow(10,Math.max(len1,len2));
		return (this*baseNum+num*baseNum)/baseNum;
	}
	return this;
}
Number.prototype.sub = function(num){
	var str1 = this.toString();
	var arr1 = str1.split('.');
	var len1 = arr1[1]? arr1[1].length:0;
	if(num)
	{
		var str2 = num.toString();
		var arr2 = str2.split('.');
		var len2 = arr2[1]? arr2[1].length:0;
		
		
		var baseNum = Math.pow(10,Math.max(len1,len2));
		return (this*baseNum-num*baseNum)/baseNum;;
	}
	return this;
}

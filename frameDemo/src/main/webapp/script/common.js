var needShowLoading = true; //设置是否显示蒙版效果
/**设置JS右键菜单隐藏**/
$(document).click(function () {
    $(".context_menu").hide();
});
/***设置过场动画开始***/
$(document).ajaxStart(function () {
    if (needShowLoading)
        $("#loading").showLoading();
    else
        $("#loading").hideLoading();
});
$(document).ajaxComplete(function (event, request, settings) {
    //custom process for ajax request e.g. session time out, etc.
});
$(document).ajaxStop(function () {
    $("#loading").hideLoading();
});

$(window).resize(function () {
    $(".ui-jqgrid.ui-widget.ui-widget-content.ui-corner-all .ui-jqgrid-btable").not(".ui-dialog-content.ui-widget-content .ui-jqgrid-btable").each(function () {
        var viewW = $(window).width();
        $(this).setGridWidth(viewW - 4);
    });
});

jQuery.extend(jQuery.jgrid.defaults, {
    rowList: [10, 30, 50],
    height: "auto",
    rownumbers: true,
    viewrecords: true,
    gridview: true,
    autoencode: true,
    autowidth: true,
    shrinkToFit: true,
    multiboxonly: true,
    multiselect: true
});

$.fn.serializeJson = function (encodeValue, checkSpecialChar) {
    var serializeObj = {};
    $(this.serializeArray()).each(function () {
        if (encodeValue) {
            serializeObj[this.name] = encodeURI($.trim(this.value));
        } else {
            serializeObj[this.name] = $.trim(this.value);
        }
    });
    return serializeObj;
};

/***设置过场动画结束***/

function addParentTab(menuAUrl, params) {
    window.parent.window.openTabByUrl(menuAUrl, params);
}

var cache = {};
var term;
function clearCache() {
    cache = {};
}
function initAutocomplete(elementJqueryObj, ajaxUrl, label, filterName, selectFunction, ajaxData) {
    return elementJqueryObj.autocomplete({
        autoFocus: false,
        delay: 500,
        minLength: 2,
        source: function (request, response) {
            term = request.term;
            var key = label + term;
            if (key in cache) {
                response(cache[key]);
                return;
            }
            var postData = {label: label};
            postData[filterName] = term;
            if (ajaxData) {
                postData = $.extend(postData, ajaxData);
            }

            $.ajax({
                global: false,
                type: "post",
                async: false,
                url: ajaxUrl,
                data: postData,
                dataType: 'json',
                success: function (data) {
                    cache[key] = data;
                    response(data);
                }
            });

        },
        focus: selectFunction,
        select: selectFunction
    });
}

function validateForm(validateMap) {
    $("#" + validateMap.formId).validate({
        onsubmit: true,
        onfocusout: false,
        onkeyup: false,
        rules: validateMap.rules,
        messages: validateMap.messages,
        submitHandler: validateMap.submitMethod,
        invalidHandler: function (form, validator) {
            return false;
        },
        showErrors: function (errorMap, errorList) {
            $("#" + validateMap.showErrorId).text("");
            if (errorList.length > 0) {
                for (var i = 0; i < errorList.length; i++) {
                    $("#" + validateMap.showErrorId).append("* " + errorList[i].message);
                    $("#" + validateMap.showErrorId).append("<br>");
                }
                return false;
            } else {
                return true;
            }
        }
    });
}

function checkIsNull(value) {
//	if((value!=null&&value!=""&&value!=''&&value!="null"&&value!="NULL"&&value!=undefined)||value==0)
    if (typeof value == "undefined") return true;
    if (value == 0 && (value + "").length > 0) return false;
    if (value != null && value != "" && value != "null" && value != "NULL" && value != "\"\"" && value != undefined) {
        return false;
    } else {
        return true;
    }
}

//加载时钟
function clockon() {
    var now = new Date();
    var year = now.getFullYear();
    var month = now.getMonth();
    var date = now.getDate();
    var day = now.getDay();
    var hour = now.getHours();
    var minu = now.getMinutes();
    var sec = now.getSeconds();
    var week;
    month = month + 1;
    if (month < 10) month = "0" + month;
    if (date < 10) date = "0" + date;
    if (hour < 10) hour = "0" + hour;
    if (minu < 10) minu = "0" + minu;
    if (sec < 10) sec = "0" + sec;
    var arr_week_en = new Array("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
    var arr_week_cn = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
    week = arr_week_cn[day];
    var time = "";
    time = year + "-" + month + "-" + date + " " + hour + ":" + minu + ":" + sec + " " + week;

    $("#bgclock").html(time);

    var timer = setTimeout("clockon()", 200);
}

/**XML format Start */
String.prototype.removeLineEnd = function () {
    return this.replace(/(<.+?\s+?)(?:\n\s*?(.+?=".*?"))/g, '$1 $2')
}
function formatXml(text) {
    //去掉多余的空格
    text = '\n' + text.replace(/(<\w+)(\s.*?>)/g, function ($0, name, props) {
            return name + ' ' + props.replace(/\s+(\w+=)/g, " $1");
        }).replace(/>\s*?</g, ">\n<");
    //把注释编码
    text = text.replace(/\n/g, '\r').replace(/<!--(.+?)-->/g,
        function ($0, text) {
            var ret = '<!--' + escape(text) + '-->';
            //alert(ret);
            return ret;
        }).replace(/\r/g, '\n');
    //调整格式
    var rgx = /\n(<(([^\?]).+?)(?:\s|\s*?>|\s*?(\/)>)(?:.*?(?:(?:(\/)>)|(?:<(\/)\2>)))?)/mg;
    var nodeStack = [];
    var output = text.replace(rgx, function ($0, all, name, isBegin,
                                             isCloseFull1, isCloseFull2, isFull1, isFull2) {
        var isClosed = (isCloseFull1 == '/') || (isCloseFull2 == '/')
            || (isFull1 == '/') || (isFull2 == '/');
        //alert([all,isClosed].join('='));
        var prefix = '';
        if (isBegin == '!') {
            prefix = getPrefix(nodeStack.length);
        } else {
            if (isBegin != '/') {
                prefix = getPrefix(nodeStack.length);
                if (!isClosed) {
                    nodeStack.push(name);
                }
            } else {
                nodeStack.pop();
                prefix = getPrefix(nodeStack.length);
            }
        }
        var ret = '\n' + prefix + all;
        return ret;
    });

    var prefixSpace = -1;
    var outputText = output.substring(1);
    //alert(outputText);

    //把注释还原并解码，调格式
    outputText = outputText.replace(/\n/g, '\r').replace(
        /(\s*)<!--(.+?)-->/g,
        function ($0, prefix, text) {
            //alert(['[',prefix,']=',prefix.length].join(''));
            if (prefix.charAt(0) == '\r')
                prefix = prefix.substring(1);
            text = unescape(text).replace(/\r/g, '\n');
            var ret = '\n' + prefix + '<!--'
                + text.replace(/^\s*/mg, prefix) + '-->';
            //alert(ret);
            return ret;
        });
    return outputText.replace(/\s+$/g, '').replace(/\r/g, '\r\n');
}
function getPrefix(prefixIndex) {
    var span = '    ';
    var output = [];
    for (var i = 0; i < prefixIndex; ++i) {
        output.push(span);
    }
    return output.join('');
}
/**XML format End */
/**
 * 检查是否存在特殊字符，存在返回true，不存在返回false
 */
function checkSpecialCharacter(str) {
    str = decodeURI(str);
    var myreg = /^(?!.*?[\~\`\·\\\ \！\!@\#\￥\$%\……\^&\*\(\)\（\）\_\-\——\+\=\【\】\[\]\{\}\|\、：\:\;\；\"\”\“\’\'\'\<\>\《\》\,\，\。\.\?\？\/]).*$/;
    if (myreg.test(str)) {
        return true;
    }
    return false;
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
function initLeftRight(_right, _left, _btnR, _btnL, _btnAR, _btnAL) {
    $('#' + _right).dblclick(function () {
        $("option:selected", this).appendTo('#' + _left);
    });
    $('#' + _left).dblclick(function () {
        $("option:selected", this).appendTo('#' + _right);
    });

    $('#' + _btnR).click(function (event) {
        event.preventDefault();
        $('#' + _left + ' option:selected').appendTo('#' + _right);
    });
    $('#' + _btnAR).click(function (event) {
        event.preventDefault();
        $('#' + _left + ' option').appendTo('#' + _right);
    });

    $('#' + _btnL).click(function (event) {
        event.preventDefault();
        $('#' + _right + ' option:selected').appendTo('#' + _left);
    });
    $('#' + _btnAL).click(function (event) {
        event.preventDefault();
        $('#' + _right + ' option').appendTo('#' + _left);
    });

    $("#" + _btnR).button({
        text: false,
        icons: {primary: "ui-icon-arrowthick-1-e"}
    });
    $("#" + _btnL).button({
        text: false,
        icons: {primary: "ui-icon-arrowthick-1-w"}
    });
    $("#" + _btnAR).button({
        text: false,
        icons: {primary: "ui-icon-arrowthickstop-1-e"}
    });
    $("#" + _btnAL).button({
        text: false,
        icons: {primary: "ui-icon-arrowthickstop-1-w"}
    });
}

/**
 * 返回描述
 * @param codeList
 * @param cellvalue
 * @returns
 */
function formatterDesc(codeList, cellvalue) {
    if (cellvalue) {
        cellvalue = $.trim(cellvalue);
    }
    return getReferenceDesc(codeList, cellvalue);
}
/**
 * 返回code
 * @param codeList
 * @param cellvalue
 * @returns
 */
function unformatCode(codeList, cellvalue) {
    return getTargetColumnValue(codeList, cellvalue, 'codeDesc', 'code');
}
function getReferenceDesc(dataSouce, sourceColumnValue) {
    return getTargetColumnValue(dataSouce, sourceColumnValue, 'code', 'codeDesc');
}
function getTargetColumnValue(dataSouce, sourceColumnValue, sourceColumn, targetColumn) {
    var desc = sourceColumnValue ? sourceColumnValue : "";
    sourceColumnValue = sourceColumnValue ? $.trim(sourceColumnValue) : sourceColumnValue;
    if (checkIsNull(dataSouce))
        return desc;
    if (typeof dataSouce == 'string') {
        dataSouce = jQuery.parseJSON(dataSouce);
    }
    if (sourceColumn && targetColumn) {
        var object = Enumerable.From(dataSouce).Where("p => p." + sourceColumn + " == '" + sourceColumnValue + "'").FirstOrDefault();
        if (object && object[targetColumn]) {
            desc = object[targetColumn];
        }
    }
    return desc;
}

function initOrgWidget(_departFieldName,_orgDialogName){
    $("#"+_departFieldName).click(function(){
        $("#"+_orgDialogName).addClass("orgTreeWidget")
            .toggle("show")
            .position({my:"left top", at : "left bottom", of:this });
    });
}
function selectRows(_gridId,allowMulti,allowZero,alterDataName){
    var rowIds = $("#"+_gridId).jqGrid('getGridParam','selarrrow');
    var count = rowIds.length;
    if(count == 0 ){
        if(!allowZero){
            alert(alterDataName?"请最少选择一条"+alterDataName:"请最少选择一条数据");
        }
        return;
    }else if(count >1 && !allowMulti){
        alert(alterDataName?"最多只能选择一条"+alterDataName:"最多只能选择一条数据");
        return;
    }
    var rowDatas = new Array(rowIds.length);
    for(var i=0 ;i<rowIds.length;i++ ){
        rowDatas[i] = $("#"+_gridId).getRowData(rowIds[i]);
    }
    return rowDatas;
}
function closeAndReload(dialog,list){
    $("#"+dialog).dialog("close");
    $("#"+dialog).html("");
    $("#"+list).jqGrid().trigger("reloadGrid");
}

function save(_form,_addParams,_succesCallBack){
    var form = $(_form);
    $.ajax({
        type 	: 	"POST",
        url		:	 form[0].action,
        data	:	 $.extend(form.serializeJson(),_addParams),
        dataType:	 "json"
    }).done(function(data){
        if(data.status==1){
            if($.isFunction(_succesCallBack)){
                _succesCallBack(data.data);
            }
        }else{
            alert(data.message);
        }
    }).fail(function(data){
        if(data.responseText){
            form[0].innerHTML=data.responseText;
        }else{
            alert('保存失败');
        }
    });
}
$.getAjax = function (_url, _param, _beforeGet, _callback) {
    if ($.isFunction(_beforeGet)) {
        _beforeGet();
    }
    $.getJSON(_url, _param, _callback);
};
$.postAjax = function (_url, _param, _beforePost, _callback) {
    if ($.isFunction(_beforePost)) {
        _beforePost();
    }
    $.post(_url, _param, _callback);
};
$.loadDiv = function (_divId, _url, _param, _type, _succesCallBack) {
    var type = "post";
    if (_type) {
        type = _type;
    }
    $.ajax({
        type: type,
        url: _url,
        data: _param,
        dataType: "html",
        success: function (data) {
            $("#" + _divId).html(data);
            if ($.isFunction(_succesCallBack)) {
                _succesCallBack(data.data);
            }
        },
        error: function () {
            alert("加载失败！");
        }
    });
};
$.initDropDown = function (groupName, _divId, async, callback) {
    $.ajax({
        type: "POST",
        async: async,
        url: "${pageContext.request.contextPath}/reference/findGroupList.do?groupName=" + groupName,
        contentType: "application/json",
        dataType: 'json',
        success: function (data) {
            $("#" + _divId).html("<option value=''></option>");
            $.each(data, function (i, item) {
                var option = "<option value='" + item.code + "'>" + item.codeDesc + "</option>";
                $("#" + _divId).append(option);
            });
            callback && callback();
        },
        error: function (error) {
            console.info("request ${pageContext.request.contextPath}/reference/findGroupList.do?groupName=" + groupName + " is failed!");
        }
    });
};
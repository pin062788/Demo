<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.ocellus.platform.utils.Constants" %>
<script type="text/javascript">
    /**id: 通过URL加载的内容会显示在id="id"这个div中, 如果SESSION超时自动跳到登录页面**/
    function lmepLoad(id, url) {
        showLoading();
        $.get(url, function (data) {
            if (data.indexOf("{\"status\":") != -1) {
                data = $.parseJSON(data);
                //Session超时
                if (data.status == <%=Constants.SESSION_TIME_OUT%>)
                    window.location = "${pageContext.request.contextPath }/login.jsp";
                else
                    $("#" + id).html(data);
            } else
                $("#" + id).html(data);
            hideLoading();
        }).fail(function () {
            alert("加载失败");
            hideLoading();
        });
    }

    $.ajaxSetup ({
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false
    });
    jQuery.extend(jQuery.jgrid.defaults, {
        beforeSelectRow: beforeGridRowSelect
    });

    function isLogout() {
        var getTimestamp = new Date().getTime();  //JS时间戳
        var status = false;
        $.ajax({
            type: "GET",
            global: false,
            async: false,
            url: "${pageContext.request.contextPath}/main/getUserLogin.do?_timer_" + getTimestamp,
            dataType: "json",
            beforeSend: function () {
            },
            success: function (data) {
                if (data == "null") {
                    status = false;
                    window.location = "${pageContext.request.contextPath }/login.jsp";
                } else
                    status = true;
            }
        });
        return status;
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
    $.loadObject = function (_formId, obj, method) {
        var key, value, tagName, type, arr;
        for (x in obj) {
            key = x;
            value = obj[x];
            if (method == 'view') {
                var target = '#' + _formId + ' [name=' + key + ']';
                $(target).html(value);
            } else {
                $("#" + _formId + ' [name="' + key + '"]').each(function () {
                    tagName = $(this)[0].tagName;
                    type = $(this).attr('type');
                    if (tagName == 'INPUT') {
                        if (type == 'radio') {
                            $(this).attr('checked', $(this).val() == value);
                        } else if (type == 'checkbox') {
                            arr = value.split(',');
                            for (var i = 0; i < arr.length; i++) {
                                if ($(this).val() == arr[i]) {
                                    $(this).attr('checked', true);
                                    break;
                                }
                            }
                        } else {
                            $(this).val(value);
                        }
                    } else if (tagName == 'SELECT' || tagName == 'TEXTAREA') {
                        $(this).val(value);
                    }
                });
            }
        }

    };
    $.fn.serializeJson = function (encodeValue, checkSpecialChar) {
        var serializeObj = {};
        var serializeName = new Array();
        $(this.serializeArray()).each(function () {
            if(serializeName.indexOf(this.name)<0){
                if (encodeValue) {
                    serializeObj[this.name] = encodeURI($.trim(this.value));
                } else {
                    serializeObj[this.name] = $.trim(this.value);
                }
                serializeName.push(this.name);
            }else{
                if (encodeValue) {
                    serializeObj[this.name] = serializeObj[this.name]+','+encodeURI($.trim(this.value));
                } else {
                    serializeObj[this.name] = serializeObj[this.name]+','+$.trim(this.value);
                }
            }
        });
        if (checkSpecialChar) {
            var error = this.find("#error");
            if (!error[0]) {
                error = $('<div id="error"  class="errorCls" align="center"></div>');
                this.prepend(error);
            }
            if (checkIsSpecialCharacter(serializeObj)) {
                error.show();
                error.html("不能输入非法字符百分号或问号，请重新输入！");
                return;
            } else {
                error.hide();
            }
        }
        return serializeObj;
    };
    function serializeComplexJsonArray(jsonArrayData) {
        var newArray = new Array();
        if (!jsonArrayData)
            return jsonArrayData;
        $.each(jsonArrayData, function (idx, jsonObj) {
            var objectData = serializeComplexJson(jsonObj);
            newArray.push(objectData);
        });
        return newArray;
    }
    ;

    function serializeComplexJson(jsonData) {
        var objectData = {};
        if (typeof (jsonData) == "object") {
            $.each(jsonData, function (tagName, tagValue) {
                var value;
                if (tagValue != null) {
                    value = tagValue;
                } else {
                    value = '';
                }
                if (tagName.indexOf('.') != -1) {
                    var attrs = tagName.split('.');
                    var tempArr = new Array();
                    var tx;
                    for (var i = attrs.length - 1; i >= 0; i--) {
                        tx = {};
                        if (i == attrs.length - 1) {
                            tx = value;
                        }
                        else {
                            var pre = objectData;
                            for (var j = 0; j <= i; j++) {
                                if (pre[attrs[j]] == undefined) {
                                    break;
                                }
                                if (j == i) {
                                    tx = pre[attrs[j]];
                                }
                                pre = pre[attrs[j]];
                            }
                            tx[attrs[i + 1]] = tempArr[attrs[i + 1]];
                        }
                        tempArr[attrs[i]] = tx;
                    }
                    objectData[attrs[0]] = tempArr[attrs[0]];
                } else {
                    if (objectData[tagName] != null) {
                        if (!objectData[tagName].push) {
                            objectData[tagName] = [objectData[tagName]];
                        }
                        objectData[tagName].push(value);
                    } else {
                        objectData[tagName] = value;
                    }
                }
            });
        }
        else {
            objectData = jsonData;
        }
        return objectData;
    }

    function saveJson(url, jsonData, _succesCallBack) {
        var jsonString;
        if (typeof (jsonData) == "object") {
            jsonString = JSON.stringify(jsonData);
        } else {
            jsonString = jsonData;
        }

        $.ajax({
                    type: "POST",
                    url: url,
                    contentType: "application/json",
                    data: jsonString,
                    dataType: "json"
                })
                .done(function (data) {
                    if (data.status != 0) {
                        if ($.isFunction(_succesCallBack)) {
                            _succesCallBack(data);
                        }
                    } else {
                        alert(data.message);
                    }
                })
                .fail(function (data) {
                    if (data.message) {
                        alert(data.message);
                    } else {
                        alert('保存失败');
                    }
                });
    }

    function save(_form, _addParams, _succesCallBack) {

        var form = $(_form);
        $.ajax({
                    type: "POST",
                    url: form[0].action,
                    data: $.extend(form.serializeJson(), _addParams),
                    dataType: "json"
                })
                .done(function (data) {
                    if (data.status == 1) {
                        if ($.isFunction(_succesCallBack)) {
                            _succesCallBack(data.data);
                        }
                    } else {
                        alert(data.message);
                    }
                })
                .fail(function (data) {
                    if (data.responseText) {
                        form[0].innerHTML = data.responseText;
                    } else {
                        alert('保存失败');
                    }

                });
    }

    /**初始化POP UP 窗口**/
    function initDialog(_dialogId, _title, _modal, _width, _height, _resizable) {
        if (!_resizable) {
            _resizable = false;
        }
        var contentHeight = $("#content_div").height();
        $("#" + _dialogId).dialog({
            autoOpen: false,
            position: {my: "top", at: "top+100", of: window},
            resizable: _resizable,
            minHeight: contentHeight * 0.2,
            maxHeight: contentHeight,
            close: function () {
                $("#" + _dialogId).html("");
            }
        });
        if (_modal == true)
            $("#" + _dialogId).dialog("option", "modal", _modal);
        if (_width)
            $("#" + _dialogId).dialog("option", "width", _width);
        if (_height)
            $("#" + _dialogId).dialog("option", "height", _height);
        if (_title)
            $("#" + _dialogId).dialog("option", "title", _title);
    }
    /**打开POP UP窗口，如果超时自动跳转到登录页面**/
    function openDialog(_dialogId, _url, _title) {
        $("#" + _dialogId).dialog("option", "minHeight", $("#content_div").height() * 0.2);
        $("#" + _dialogId).dialog("option", "maxHeight", $("#content_div").height());
        $.get(_url, function (data) {
            if (!checkIsNull(_title)) {
                $("#" + _dialogId).dialog("option", "title", _title);
            }
            $("#" + _dialogId).dialog("open").html(data);
        }).fail(function () {
            alert("加载失败");
        });
    }

    /**打开POP UP窗口，post方式 zs**/
    function openDialogPost(_dialogId, _url, _title, params, callBack) {
        $("#" + _dialogId).dialog("option", "minHeight", $("#content_div").height() * 0.2);
        $("#" + _dialogId).dialog("option", "maxHeight", $("#content_div").height());
        $.post(_url, params, function (data) {
            if (!checkIsNull(_title)) {
                $("#" + _dialogId).dialog("open").dialog("option", "title", _title);
            }
            $("#" + _dialogId).dialog("open").html(data);
            if ($.isFunction(callBack)) {
                callBack();
            }
        }).fail(function () {
            alert("加载失败");
        });
    }

    /**初始化jqGrid**/
    function initGrid(_gridId, _pagerId, _url, _colNames, _colModel, _caption, _multiselect) {
        if (_gridId) {
            $("#" + _gridId).jqGrid({
                url: _url,
                datatype: "json",
                mytype: 'GET',
                colNames: _colNames,
                colModel: _colModel,
                rowNum: 10,
                rowList: [10, 20, 30],
                pager: "#" + _pagerId,
                viewrecords: true,
                height: "auto",
                autowidth: true,
                gridview: true,
                autoencode: true,
                subGrid: false,
                multiselect: _multiselect && _multiselect == true ? true : false
            }).navGrid("#" + _pagerId, {
                refresh: false,
                edit: false,
                add: false,
                del: false,
                search: false
            });
        }
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


    function copyFormToGrid(_formId, gridId, rowid) {
        var formJson = $("#" + _formId).serializeJson();
        if (rowid) {
            for (var i in formJson) {
                $('#' + gridId).jqGrid('setCell', rowid, i, formJson[i]);
            }
        } else {
            var rownumber = $('#' + gridId).getGridParam('reccount');
            $('#' + gridId).jqGrid('addRowData', rownumber + 1, formJson);
        }
    }
    function copyGridToForm(gridId, rowid, _formId) {
        var formJson = $("#" + _formId).serializeJson();
        if (rowid) {
            for (var i in formJson) {
                var cell = $('#' + gridId).jqGrid('getCell', rowid, i);
                if (cell) {
                    $("#" + _formId).find("[name='" + i + "']").val(cell);
                }
            }
        }
    }

    /**
     * 获得grid中选中行 对应列的值，返回值为数组
     */
    function getColumnValueInSelectedRow(gridId, columnName, useSimpleItem) {
        if (!gridId || !columnName)
            return null;
        var saveDataAry = new Array();
        var rowData = jQuery("#" + gridId).jqGrid('getGridParam', 'selarrrow');
        if (rowData.length > 0) {
            for (var i = 0; i < rowData.length; i++) {
                var ids;
                if (!useSimpleItem) {
                    ids = {};
                    ids[columnName] = $("#" + gridId).jqGrid('getCell', rowData[i], columnName);
                }
                else {
                    ids = $("#" + gridId).jqGrid('getCell', rowData[i], columnName);
                }
                saveDataAry.push(ids);
            }
        }
        return saveDataAry;
    }
    /**
     * 获得grid中选中行 对应列的值，返回值为分隔符分割的字符串
     */
    function getColumnValueStrInSelectedRow(gridId, columnName, delimiter) {
        if (!gridId || !columnName)
            return null;
        var saveDataStr = "";
        if (!delimiter) {
            delimiter = ",";
        }
        var rowData = jQuery("#" + gridId).jqGrid('getGridParam', 'selarrrow');
        if (rowData && rowData.length > 0) {
            for (var i = 0; i < rowData.length; i++) {
                var columnValue = $("#" + gridId).jqGrid('getCell', rowData[i], columnName);
                if (columnValue && columnValue.length > 0) {
                    if (saveDataStr.length > 0) {
                        saveDataStr = saveDataStr + delimiter;
                    }
                    saveDataStr = saveDataStr + columnValue;
                }
            }
        }
        return saveDataStr;
    }

    function getReferenceDesc(dataSouce, sourceColumnValue) {
        return getTargetColumnValue(dataSouce, sourceColumnValue, 'code', 'codeDesc');
    }

    function filterDataSouce(dataSouce, filterFunction) {
        return Enumerable.From(dataSouce).Where(filterFunction).ToArray();
    }
    function appendOptions(dropDownId, dataSouce, valueField, textField) {
        for (var i = 0; i < dataSouce.length; i++) {
            $("#" + dropDownId).append("<option value='" + dataSouce[i][valueField] + "'>" + dataSouce[i][textField] + "</option>");
        }
    }
    function getTargetColumnValue(dataSouce, sourceColumnValue, sourceColumn, targetColumn) {
        var desc = sourceColumnValue ? sourceColumnValue : "";
        sourceColumnValue = sourceColumnValue ? trim(sourceColumnValue) : sourceColumnValue;
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
    function findObjectByFiled(dataSouce, sourceColumnValue, sourceColumn) {
        sourceColumnValue = sourceColumnValue ? trim(sourceColumnValue) : sourceColumnValue;
        if (checkIsNull(dataSouce))
            return "";
        if (typeof dataSouce == 'string') {
            dataSouce = jQuery.parseJSON(dataSouce);
        }
        if (sourceColumn) {
            var object = Enumerable.From(dataSouce).Where("p => p." + sourceColumn + " == '" + sourceColumnValue + "'").FirstOrDefault();
            if (object) {
                return object;
            }
        } else {
            return dataSouce;
        }
    }
    /**window改变大小时用来重新设置jqgrid宽度的方法**/
    function resizeJqGrid(gridId, widthExpr) {
        if (!widthExpr) {
            widthExpr = "$('#content_div').width() - 40";
        }
        setTimeout("$('#" + gridId + "').setGridWidth(" + widthExpr + ");", 200);
    }
    /**window改变大小时用来重新设置dialog宽度和高度的方法**/
    function resizeDialogGrid(_dialogId, _width, _height) {
        var expression = "";
        if (_width)
            expression = expression + "$('#" + _dialogId + "').dialog('option', 'width', " + _width + ");";
        if (_height)
            expression = expression + "$('#" + _dialogId + "').dialog('option', 'height', " + _height + ");";

        if (expression && expression.length > 0) {
            setTimeout(expression, 200);
        }
    }

    function beforeGridRowSelect(rowid, e) {
        var grid = $(e.target).closest('table')[0].id;
        var onloadMx = $("#" + grid).jqGrid('getGridParam', 'onloadMx');
        if ($.isFunction(onloadMx)) {
            var i = $.jgrid.getCellIndex($(e.target).closest('td')[0]);
            var cm = $("#" + grid).jqGrid('getGridParam', 'colModel');
            var clickCheckBox = (cm[i].name === 'cb');//$(e.target)[0].type == "checkbox";
            var tr = "#" + grid + " tr[id=" + rowid + "]";
            var selectRow = clickCheckBox || $(tr).attr('class').indexOf('ui-state-highlight') < 0;
            if (clickCheckBox == false) {
                var perDisplayMxRowId = $("#" + grid).jqGrid('getGridParam', 'displayMxRowId');
                if (perDisplayMxRowId != null && perDisplayMxRowId == rowid) {
                    $("#" + grid).jqGrid("setGridParam", {displayMxRowIdChanged: false});
                } else {
                    $("#" + grid).jqGrid("setGridParam", {displayMxRowIdChanged: true});
                    $("#" + grid).jqGrid("setGridParam", {displayMxRowId: rowid});
                    $("#" + grid + " tr[id=" + perDisplayMxRowId + "]").removeClass("ui-displaymx-row");
                    $(tr).addClass("ui-displaymx-row");
                }

            } else {
                if ($(tr).attr('class').indexOf('ui-displaymx-row') >= 0) {
                    $("#" + grid).jqGrid("setGridParam", {displayMxRowIdChanged: true});
                    $("#" + grid).jqGrid("setGridParam", {displayMxRowId: -1});
                    $(tr).removeClass("ui-displaymx-row");
                } else {
                    $("#" + grid).jqGrid("setGridParam", {displayMxRowIdChanged: false});
                }
            }
            if ($("#" + grid).jqGrid('getGridParam', 'displayMxRowIdChanged')) {
                if ($("#" + grid).jqGrid('getGridParam', 'displayMxRowId') == -1) {
                    var mxGridId = $("#" + grid).jqGrid('getGridParam', 'mxGridId');
                    if (mxGridId) {
                        var mxclass = $('#' + mxGridId).attr('class');
                        if (mxclass && mxclass.indexOf('ui-jqgrid-btable') >= 0) {
                            $("#" + mxGridId).clearGridData();
                        } else {
                            $("#" + mxGridId).find(".ui-jqgrid-btable").clearGridData();
                        }
                    }
                } else {
                    onloadMx(rowid);
                }
            }
            return selectRow;
        } else {
            return true;
        }
    }


    /**
     * 将对象中的数据显示到正在打开的dialog中的grid内，可调节开始行数
     **/
    function moveObjectsToGridInDialog(gridId, objects, startRowId, times, interval) {
        var _times = times || 100; //100次
        var _interval = interval || 100; //100毫秒每次
        var _iIntervalID; //定时器id
        var _grid = $("#" + gridId);
        if (_grid.length && _grid.length > 0) {
            moveObjectsToGrid(gridId, objects, startRowId);
        }
        else {
            _iIntervalID = setInterval(function () {
                if (!_times) { //是0就退出
                    clearInterval(_iIntervalID);
                }
                _times <= 0 || _times--; //如果是正数就 --

                _grid = $("#" + gridId); //再次选择
                if (_grid.length && _grid.length > 0) { //判断是否取到
                    moveObjectsToGrid(gridId, objects, startRowId);
                    clearInterval(_iIntervalID);
                }
            }, _interval);
        }
    }

    /**将对象中的数据显示到grid中，可调节开始行数 **/
    function moveObjectsToGrid(gridId, objects, startRowId) {
        var array;
        if (objects instanceof Array) {
            array = objects;
        }
        else {
            array = new Array();
            array.push(objects);
        }
        var rowId;
        if (startRowId && startRowId * 1 > 0) {
            rowId = startRowId;
        }
        else {
            var previousRowData = jQuery('#' + gridId).jqGrid('getRowData');
            if (previousRowData && previousRowData.length) {
                rowId = previousRowData.length + 1;
            }
            else {
                rowId = 1;
            }
        }
        for (var i = 0; i < array.length; i++) {
            jQuery('#' + gridId).jqGrid('addRowData', rowId, array[i]);
            rowId++;
        }
    }
    function formatSF(value) {
        if (value == "1") {
            return "是";
        } else {
            return "否";
        }
    }
    function unformatSF(value) {
        return value == "否" ? "0" : "1";
    }
    function getDetailNo(prefixNo) {
        var detailNo = "";
        $.ajax({
            type: "GET",
            async: false,
            url: "${pageContext.request.contextPath}/serial/getDetailNo.do?prefixNo=" + prefixNo,
            contentType: "application/json",
            dataType: 'json',
            success: function (data) {
                detailNo = data;
            },
            error: function (error) {
                console.info("${pageContext.request.contextPath}/serial/getDetailNo.do?prefixNo=" + prefixNo + " is failed!");
            }
        });
        return detailNo;
    }
    ;

</script>
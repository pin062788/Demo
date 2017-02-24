<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script type="text/javascript">

    function reInitYear(elemlent, year) {
        $(elemlent).find("option").remove();
        if (typeof year == 'string') {
            year = parseInt(year);
        }
        $(elemlent).append("<option value=''></option>");
        for (var i = -10; i < 11; i++) {
            $(elemlent).append("<option value='" + (year + i) + "'" + (i == 0 ? "selected='selected'" : "") + ">" + (year + i) + "</option>");
        }
    }

    function DatepickerObj(jObject, model) {

        this.model = model;
        this.jObject = jObject;
        if (model) {
            this.yearElement =
                    $("<select id='" + jObject.attr("id") + "_yearSelected' style='width:60px' onchange='reInitYear(this,this.options[this.options.selectedIndex].value)'></select>");
            var currentYear = new Date().getFullYear();
            //this.yearElement.append("<option value=''></option>");
            for (var i = -10; i < 11; i++) {
                this.yearElement.append("<option value='" + (currentYear + i) + "'>" + (currentYear + i) + "</option>");
            }
            ;

            this.month = $("<select id='" + jObject.attr("id") + "_monthSelected' style='width:45px'></select>");
            //this.month.append("<option value=''></option>");
            for (var i = 1; i <= 12; i++) {
                this.month.append("<option value='" + i + "'>" + i + "</option>");
            }
            this.quarter = $("<select id='" + jObject.attr("id") + "_quarterSelected' style='width:60px'></select>");
            //this.quarter.append("<option value=''></option>");
            for (var i = 1; i <= 4; i++) {
                this.quarter.append("<option value='" + i + "'>Q" + i + "</option>");
            }

            jObject.append(this.yearElement);
            if (model == "month") {
                jObject.append(this.month);
            } else if (model == "quarter") {
                jObject.append(this.quarter);
            }

        }

    }

    DatepickerObj.prototype.changeModel = function (model) {
        this.model = model;
        if (model) {
            this.jObject.html("");
            this.jObject.append(this.yearElement);
            this.yearElement.val(new Date().getFullYear());
            if (model == "month") {
                this.month.val(new Date().getMonth());
                this.jObject.append(this.month);
            } else if (model == "quarter") {
                this.month.val(new Date().getMonth() / 3 + 1);
                this.jObject.append(this.quarter);
            }
        }
        return this;
    };
    DatepickerObj.prototype.getYearValue = function () {
        var year = $("#" + this.jObject.attr("id") + "_yearSelected").val();
        return year = parseInt(year);
    };
    DatepickerObj.prototype.getMonthValue = function (lastMonth) {
        var month = 0;
        if (this.model == "year") {
            if (lastMonth) {
                month = 11;
            } else {
                month = 0;
            }
        } else if (this.model == "month") {
            month = $("#" + this.jObject.attr("id") + "_monthSelected").val() - 1;
        } else {
            var quarter = $("#" + this.jObject.attr("id") + "_quarterSelected").val();
            if (quarter) {
                switch (quarter) {
                    case "1":
                        if (lastMonth) {
                            month = 2;
                        } else {
                            month = 0;
                        }
                        break;
                    case "2":
                        if (lastMonth) {
                            month = 5;
                        } else {
                            month = 3;
                        }
                        break;
                    case "3":
                        if (lastMonth) {
                            month = 8;
                        } else {
                            month = 6;
                        }
                        break;
                    case "4":
                        if (lastMonth) {
                            month = 11;
                        } else {
                            month = 9;
                        }
                        break;
                    default:
                        alert("error");
                }
            }
        }
        return month;
    };
    DatepickerObj.prototype.getStartDate = function () {
        var year = this.getYearValue();
        var month = this.getMonthValue();
        return new Date(year, month, 1);
    };
    DatepickerObj.prototype.getEndDate = function () {
        var year = this.getYearValue();
        var month = this.getMonthValue(true);
        return new Date(new Date(year, month + 1, 1).getTime() - 1);
    };
    DatepickerObj.prototype.defaultYear = function (year) {
        $("#" + this.jObject.attr("id") + "_yearSelected").val(year);
        return this;
    };
    DatepickerObj.prototype.defaultMonth = function (month) {
        $("#" + this.jObject.attr("id") + "_monthSelected").val(month);
        return this;
    };
    DatepickerObj.prototype.defaultQuarter = function (quarter) {
        $("#" + this.jObject.attr("id") + "_quarterSelected").val(quarter);
        return this;
    };
    var dateMap = new Map();
    $.fn.costomerDatepicker = function (model) {
        if (model) {
            dateMap.put(this.attr("id"), new DatepickerObj(this, model));
        }
        return dateMap.get(this.attr("id"));

    };
</script>
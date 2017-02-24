<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript">
        $(document).ready(function () {
            var criterion = "${criterion}";
            $("#searchList").jqGrid({
                url: "${pageContext.request.contextPath}/main/search.do?criterion=" + encodeURI(criterion),
                datatype: "json",
                mytype: 'GET',
                multiselect: false,
                colNames: [""],
                colModel: [
                    {name: "content", formatter: searchFormatter}
                ],
                rowNum: 10,
                page: 1,
                total: 15,
                rowList: [10, 20, 30],
                pager: "#searchPager",
                viewrecords: true,
                height: "auto",
                //width: "auto",
                autowidth: true,
                gridview: true,
                autoencode: true,
                multiselect: true
            }).navGrid("#searchPager", {
                refresh: false,
                edit: false,
                add: false,
                del: false,
                search: false
            });

            function searchFormatter(cellvalue, options, rowObject) {

                return "<a href=\"#\" onclick=\"openUrl('" + rowObject.resourceUrl + "')\">" + rowObject.resourceName + "</a>";
            }

        });
        function openUrl(url) {
            if (url && url != null && url != "null") {
                $("#content_div").load("${ctx}" + url);
            }
        }
    </script>
</head>
<body>
<div id="query" class="ui-widget">
    <table id="searchList"></table>
    <div id="searchPager"></div>
</div>
</body>
</html>
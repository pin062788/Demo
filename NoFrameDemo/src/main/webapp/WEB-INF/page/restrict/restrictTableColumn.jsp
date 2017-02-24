<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript">
        $(document).ready(function () {
            $("#tableDIV").load("${pageContext.request.contextPath}/restrictTable/list.do");
        });
        $(document).ready(function () {
            $("#columnDIV").load("${pageContext.request.contextPath}/restrictColumn/list.do?tableId=0");
        });
    </script>
</head>
<body>
<table cellspacing="5" cellpadding="5" width="100%" border="0">
    <tr>
        <td width="40%">
            <div class="ui-widget-content">
                <div id="tableDIV" style="margin:5px 0;overflow:auto;height:550px;width:100%;background:#fff;position:">

                </div>
        </td>
        <td width="60%">
            <div class="ui-widget-content">
                <div id="columnDIV"
                     style="margin:5px 0;overflow:auto;height:550px;width:100%;background:#fff;position:">
                </div>
        </td>
    </tr>
</table>

</body>
</html>
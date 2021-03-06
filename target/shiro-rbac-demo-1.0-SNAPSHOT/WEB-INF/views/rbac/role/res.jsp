<%--
  Created by IntelliJ IDEA.
  User: haol
  Date: 2016/9/10
  Time: 19:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="/common/meta.jsp" %>
    <title>user/res</title>
</head>
<body>
<%@ include file="/common/top.jsp" %>
<div class="container">
    <div class="row" style="margin-bottom: 10px;">
        <div class="col-md-12" style="text-align: center">
            <h3><span class="label label-warning">${role.name}的权限资源</span></h3>
        </div>
    </div>

    <table class="table table-bordered">
        <tr>
            <td>编号</td>
            <td>资源名称</td>
            <td>资源url</td>
            <td>资源权限字符串</td>
            <td>操作</td>
        </tr>
        <c:forEach items="${resList}" var="res" varStatus="status">
            <tr>
                <td>${status.count}</td>
                <td>${res.name}</td>
                <td>${res.url}</td>
                <td>${res.permission}</td>
                <td>
                    <a  class="btn btn-info" href="${ctx}/role/res/${res.id}">查看资源管理</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>

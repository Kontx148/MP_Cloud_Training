<%--
  Created by IntelliJ IDEA.
  User: Norbi
  Date: 2026. 01. 24.
  Time: 11:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="gyak.model.Car"%>
<%@ page import="java.util.List"%>
<html>
<head>
    <title>Cars page</title>
</head>
<body>
<div>Car list incoming</div>
<%
    List<Car> cars = (List<Car>) request.getAttribute("cars");
    for(Car c : cars) {
%>
<li> <%= c.getId() %> - <%= c.getMake() %> - <%= c.getModel() %> - <%= c.getYear() %> </li>
<% } %>

</body>
</html>

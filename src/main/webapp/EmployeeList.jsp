<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import = "java.util.List"%>
<@page language="java" contentType="text/html; charset=IS0-8859-1"
pageEncoding="ISO-8859-1"%>

<%
    // get data from request:
    String message = (String) request.getAttribute("message");
%>

<html>
<body>
<h1><%= "Employee List" %>
</h1>
<br/>

<c:set var="income" scope="page" value="${12*456}"></c:set>
<h1><c:out value="${income}"


</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: asadn
  Date: 5/6/2022
  Time: 10:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<title>Login</title>
<h2>Employee Login</h2>

<form action="EmployeeServlet" method="GET">
    <label for="email">Email: </label><br>
    <input type="text" id="email" name="email" placeholder="asad@domain.com"><br>
    <label for="password">Password: </label><br>
    <input type="password" id="password" name="password" placeholder="12345"><br><br>
    <input type="submit" value="Submit">
</form>

<p>Click the "Submit" button to continue to ticket Page</p>
</body>
</html>

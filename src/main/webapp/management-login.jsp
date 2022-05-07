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
    <title>Manager Login</title>
</head>
<body>
<h2>Manager Login</h2>

<form action="ManagerServlet" method="GET">
    <label for="manager_email">Email: </label><br>
    <input type="manager_email" id="manager_email" name="manager_email" placeholder="asad@domain.com"><br>
    <label for="manager_password">Password: </label><br>
    <input type="manager_password" id="manager_password" name="manager_password" placeholder="12345"><br><br>
    <input type="submit" value="Submit">
</form>

<p>Click the "Submit" button to continue</p>
</body>
</html>

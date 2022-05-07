<%--
  Created by IntelliJ IDEA.
  User: asadn
  Date: 5/6/2022
  Time: 9:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Employee Registration</title>
</head>
<body>
<h2> Employee Registration</h2>


<form action="EmployeeServlet" method="POST">
    <label for="employee_name">Name:</label><br>
    <input type="text" id="employee_name" name="employee_name" placeholder="John"><br><br>
    <label for="employee_email">Email: </label><br>
    <input type="text" id="employee_email" name="employee_email" placeholder="john@domain.com"><br><br>
    <label for="employee_password">Select a secure password: </label><br>
    <input type="text" id="employee_password" name="employee_password" placeholder="password123"><br><br>
    <input type="submit" value="Submit">
</form>

<p>If you click the "Submit" button. You will be directed to the Main Menu where you can use your credentials to login</p>


</body>
</html>

<%
    // get data from request:
    String message = (String) request.getAttribute("message");
%>

<html>
<body>
<h1><%= "Expense Tracker Project" %>
</h1>
<br/>
<a href="new-user-registration.jsp">New Employee Registration</a><br><br>
<a href="employee-login.jsp">Employee Login</a><br><br>
<a href="management-login.jsp">Management Login</a><br><br>
<a href="EmployeeServlet">Click here to view all the Employees</a><br><br>
<a href="ticket-form.jsp">Click here Post a new Expense Ticket</a><br><br>
</body>
</html>

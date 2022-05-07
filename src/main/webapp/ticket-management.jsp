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
    <title>Management Login</title>
</head>
<body>
<h2>Ticket Management</h2>

<form action="TicketServlet" method="POST">
   <label for="ticket_id">Enter the Ticket Number</label><br>
    <input type="ticket_id" id="ticket_id" name="ticket_id"><br><br><br>
     <label for="ticket_status">Select the status: </label> <br>
              <select id="ticket_status" name="ticket_status">
              <option value="accept">Accept</option>
              <option value="deny">Deny</option>
              </select><br><br>
     <input type="submit" value="Submit">
</form>

<p>Click the "Submit" button to continue</p>
</body>
</html>

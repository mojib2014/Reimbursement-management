<%--
  Created by IntelliJ IDEA.
  User: asadn
  Date: 5/6/2022
  Time: 10:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // get data from request:
    String message = (String) request.getAttribute("");
%>

<html>
<body>
<h1><%= "Ticket View Form" %>
</h1>

<p>Choose the order:</p>


<form action="TicketServlet" method="GET">
 <label for="employee_id">Employee_id:  </label><br>
            <input type="number" id="employee_id" name="employee_id"
                   min="0"><br><br>
 <label for="ticket_order">Choose a category:</label>
            <select id="ticket_order" name="ticket_order">
            <option value="DESC">New Tickets first</option>
            <option value="ASC">Old Tickets First</option>
            </select><br><br>
  <input type="submit" value="Submit">

</form>

<p>Click the "Submit" button to continue to ticket Page</p>


</body>
</html>

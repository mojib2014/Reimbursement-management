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
    <title>Ticket Form</title>
</head>
<body>
<h2>Ticket Form</h2>

<form action="TicketServlet" method="POST">
    <label for="employee_id">Employee_id:  </label><br>
            <input type="number" id="employee_id" name="employee_id"
                   min="0" max="1000000" placeholder="500"><br><br>
     <label for="amount">Amount:  </label><br>
        <input type="number" id="ticket_amount" name="ticket_amount"
               min="0" max="1000000" placeholder="500"><br><br>
        <label for="ticket_category">Choose a category:</label>
            <select id="ticket_category" name="ticket_category">
            <option value="living">Living/Accommodation</option>
            <option value="traveling">Traveling</option>
            <option value="food">Food</option>
            <option value="misc">Misc</option>
            </select><br><br>
        <label for="ticket_description">Description: </label><br>
        <input type="text" id="ticket_description" name="ticket_description" placeholder="Stay at X hotel or Dinner with client etc."><br><br>
    <input type="submit" value="Submit">

</form>

<p>Click the "Submit" button to continue to ticket Page</p>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<body>
    current session info: ${user.name}
    <header>
        <a href="/reimbursement-management/">Reimbursement Management</a>
        <nav>
            <ul>
                <li><a href="/reimbursement-management/tickets.jsp">Tickets</a>
                <li><a href="logout">Logout</a>
            </ul>
        </nav>
    </header>
    <h1>Welcome to Employee home page</h1>
    <p>This is the home page of the web app, Here you can create a ticket</p>
    <h2>Your pending tickets</h2>
   <table border="1" cellpadding="2" cellspacing="2" border-collaps="collaps">
    <thead>
        <tr>
            <th>Amount</th>
            <th>Description</th>
            <th>Created At</th>
            <th>Update At</th>
            <th>Status</th>
            <th>Category</th>
        </tr>
    </thead>
        <tbody>
            <c:forEach var="ticket" items="${tickets}">
                <tr>
                    <td>${ticket.getAmount()}</td>
                    <td>${ticket.getDescription()}</td>
                    <td>${ticket.getCreated_at()}</td>
                    <td>${ticket.getUpdated_at()}</td>
                    <td>${ticket.getStatus()}</td>
                    <td>${ticket.getCategory()}</td>
                </tr>
            </c:forEach>
        </tbody>
   </table>
</body>
</html>

package com.revature.servlets;

import com.revature.dao.DaoFactory;
import com.revature.dao.EmployeeDao;
import com.revature.dao.TicketDao;
import com.revature.dao.UDArray;
import com.revature.entity.Employee;
import com.revature.entity.Ticket;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

// http://localhost:8080/ExpenseTracker/EmployeeServlet?email=reiffe%40domain.com&password=12345

public class TicketServlet extends HttpServlet {
    EmployeeDao eDao = DaoFactory.getEmployeDao();
    TicketDao ticketDao = DaoFactory.getTicketDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        String referrer = req.getHeader("Referer");
        if (referrer.contains("ticket-view-form")) {
            int employee_id = Integer.parseInt(req.getParameter("employee_id"));
            String order = req.getParameter("ticket_order");
            UDArray<Ticket> tickets = ticketDao.getTickets(employee_id, order);
            System.out.println(employee_id);
            System.out.println(order);
            System.out.println(tickets.getSize());
            for (int i = 0; i < tickets.getSize(); i++) {
                out.print(String.format("<H3>%s</H3>", tickets.get(i).toString()));
                System.out.println(tickets.get(i).toString());
            }
        }
        else if (referrer.contains("ManagerServlet")) {
            UDArray<Ticket> tickets = ticketDao.getTickets();
           System.out.println(tickets.getSize());
            for (int i = 0; i < tickets.getSize(); i++) {
                out.print(String.format("<H3>%s</H3>", tickets.get(i).toString()));
                System.out.println(tickets.get(i).toString());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("At the post");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        String referrer = req.getHeader("Referer");
        if (referrer.contains("ticket-form")) {
            processTicketInsertion(req, resp);
        }
        if (referrer.contains("ticket-management")) {
            doPut(req, resp);
        }
    }

    protected void processTicketInsertion(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("at the ticket process");
        int employee_id = Integer.parseInt(req.getParameter("employee_id"));
        Double amount = Double.valueOf(req.getParameter("ticket_amount"));
        String desc = req.getParameter("ticket_description");
        String ticket_cat = req.getParameter("ticket_category");
        Ticket ticket = new Ticket(amount, desc, ticket_cat);
        boolean isSuccess = ticketDao.insert(ticket,employee_id);
        if (isSuccess) {
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("Ticket successfully posted");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("At the put");
        int ticket_id = Integer.parseInt(req.getParameter("ticket_id"));
        String newStatus = req.getParameter("ticket_status");
        boolean isSuccessFull = ticketDao.updateTicketStatus(ticket_id, newStatus);
        if (isSuccessFull) {
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<h3>Status of the ticket change</h3>");
            UDArray<Ticket> tickets = ticketDao.getTickets();
            for (int i = 0; i < tickets.getSize(); i++) {
                out.print(String.format("<H3>%s</H3>", tickets.get(i).toString()));
                System.out.println(tickets.get(i).toString());
            }
            out.println("<h3><a href=ticket-form>Go to ticket management</a></h3>");
        }
    }
}

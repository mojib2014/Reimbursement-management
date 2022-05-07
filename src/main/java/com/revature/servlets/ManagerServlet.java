package com.revature.servlets;

import com.revature.dao.DaoFactory;
import com.revature.dao.ManagerDao;
import com.revature.dao.TicketDao;
import com.revature.dao.UDArray;
import com.revature.entity.Ticket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ManagerServlet extends HttpServlet {

    ManagerDao managerDao = DaoFactory.getManagerDao();
    TicketDao ticketDao = DaoFactory.getTicketDao();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("At the doGet manager");
        String referrer = req.getHeader("Referer");
        if (referrer.contains("management-login.jsp")) {
            processManagerLogin(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("At the doPost manager");

    }

    protected void processManagerLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("At the process login");
        String manager_email = req.getParameter("manager_email");
        String manager_password = req.getParameter("manager_password");
        boolean isLoginSuccess = managerDao.managementLogin(manager_email, manager_password);
        if (isLoginSuccess) {
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            UDArray<Ticket> tickets = ticketDao.getTickets();
            System.out.println(tickets.getSize());
            for (int i = 0; i < tickets.getSize(); i++) {
                out.print(String.format("<H3>%s</H3>", tickets.get(i).toString()));
                System.out.println(tickets.get(i).toString());
            }

            String ticket_servlet = "<h3><a href=ticket-management.jsp> Click Here to deal / view tickets </a> </h3>";
            out.println(ticket_servlet);
        }





    }
}

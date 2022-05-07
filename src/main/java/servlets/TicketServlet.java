package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import daos.Dao;
import daos.DaoFactory;
import datastructure.UDArray;
import entities.Ticket;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TicketServlet extends HttpServlet {
    private Dao<Ticket> ticketDao = DaoFactory.getTicketDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        int id = 0;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
           String tickets = getAllTickets();
           out.print(tickets);
           return;
        }
        String ticket = getTicket(id);
        out.print(ticket);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        Ticket jsonTicket = getInputFromReq(req, res);
        String daoRes = ticketDao.insert(jsonTicket);
        if(daoRes.equals("Success")) {
            res.setStatus(202);
            out.print("Ticket successfully created!");
        }else {
            res.setStatus(500);
            out.print("Something went wrong processing your request");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Ticket ticket = getInputFromReq(req, res);
        boolean daoRes = ticketDao.update(ticket);
        if(daoRes) {
            res.setStatus(200);
            res.getWriter().print("Successfully updated!");
        }else {
            res.setStatus(500);
            res.getWriter().print("Something failed");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        int id  = 0;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        }catch (NumberFormatException ex) {
            res.setStatus(500);
            out.print("Please enter a valid integer id");
        }
        boolean daoRes = ticketDao.delete(id);
        if(daoRes) {
            res.setStatus(200);
            out.print("Successfully deleted!");
        }
    }

    private String getTicket(int id) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(ticketDao.getById(id));
    }

    private String getAllTickets() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            UDArray<Ticket> tickets = ticketDao.getAll();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tickets);
        }catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return null;
    }

    private Ticket getInputFromReq(HttpServletRequest req, HttpServletResponse res) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Ticket ticket = mapper.readValue(req.getInputStream(), Ticket.class);
            return ticket;
        }catch (IOException ex) {
            try {
                res.setStatus(500);
                res.getWriter().print(ex.getLocalizedMessage());
            }catch (IOException exe) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
        return null;
    }
}

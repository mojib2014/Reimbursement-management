package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import daos.DaoFactory;
import daos.TicketDao;
import datastructure.UDArray;
import entities.Ticket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TicketServlet extends HttpServlet {
    private TicketDao ticketDao = DaoFactory.getTicketDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        int id = 0;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            String orderBy = req.getParameter("orderBy");
            String status = req.getParameter("status");
            List<String> options = new ArrayList<>();
            if(status != null) options.add(status);
            if(orderBy != null) options.add(orderBy);
           String tickets = getAllTickets(options);
           out.print(tickets);
           return;
        }
        Ticket ticket = getTicket(id);
        String jsonTicket = new Gson().toJson(ticket);
        out.print(jsonTicket);
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
        System.out.println("doPut:" + ticket);
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

    private Ticket getTicket(int id) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return ticketDao.getById(id);
    }

    private String getAllTickets(List<String> options) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            UDArray<Ticket> tickets = ticketDao.getAll(options);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tickets.getContainer());
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

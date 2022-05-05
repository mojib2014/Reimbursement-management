package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import daos.Dao;
import daos.DaoFactory;
import entities.Ticket;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class TicketServlet extends HttpServlet {
    private Dao<Ticket> ticketDao = DaoFactory.getTicketDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String context = req.getServletPath();
            switch (context) {
                case "/tickets":
                    getAllTickets(req, res);
                    break;
                case "/tickets/ticket":
                    getTicket(req, res);
                    break;
            }
        }catch (IOException ex) {
            res.setStatus(500);
            res.getWriter().print("Something went wrong get the resource!");
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            Ticket jsonTicket = getInputFromReq(req, res);
//            res.addCookie(Cookie "user_id=1");
            if(jsonTicket.getAmount() <= 0) {
                res.setStatus(400);
                res.getWriter().print("amount can't be zero");
            }

            String daoRes = ticketDao.insert(jsonTicket);

            if(daoRes.equals("Success")) {
                res.setStatus(202);
                res.getWriter().print("Ticket successfully created!");
            }
        }catch (IOException ex) {
            res.setStatus(500);
            res.getWriter().print(ex.getLocalizedMessage());
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        super.doDelete(req, res);
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
        super.doDelete(req, res);
        int ticket_id = getIdFromReq(req);
        boolean daoRes = ticketDao.delete(ticket_id);
        if(daoRes) {
            res.setStatus(200);
            res.getWriter().print("Successfully deleted!");
        }else {
            res.setStatus(500);
            res.getWriter().print("Something went  wrong while deleting the ticket!");
        }
    }

    private void getTicket(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        int ticket_id = getIdFromReq(req);
        Ticket ticket = ticketDao.getById(ticket_id);
        String tickets = mapper.writeValueAsString(ticket);
        res.setStatus(200);
        res.getWriter().print(tickets);
    }

    private void getAllTickets(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Ticket> ticketList = ticketDao.getAll();
        String tickets = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ticketList);
        res.setStatus(200);
        res.getWriter().print(tickets);
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

    private int getIdFromReq(HttpServletRequest req) {
        String queryStr = req.getQueryString();
        int ticket_id = Integer.parseInt(queryStr.split("=")[1]);
        return ticket_id;
    }
}

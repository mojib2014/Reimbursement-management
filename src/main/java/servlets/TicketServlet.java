package servlets;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import daos.Dao;
import daos.DaoFactory;
import entities.Ticket;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name="tickets", value = "/tickets", urlPatterns = "tickets/ticket_id")
public class TicketServlet extends HttpServlet {
    private Dao<Ticket> ticketDao = DaoFactory.getTicketDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String context = req.getServletPath();
            System.out.println(context);
            switch (context) {
                case "/tickets":
                    getAllTickets(req, res);
                    break;
                case "/tickets/ticket_id":
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Ticket jsonTicket = mapper.readValue(req.getInputStream(), Ticket.class);
            resp.setStatus(400);
            if(jsonTicket.getAmount() <= 0) resp.getWriter().print("amount can't be zero");
            boolean res = ticketDao.insert(jsonTicket);
            if(res) {
                resp.setStatus(202);
                resp.getWriter().print("Ticket successfully created!");
            }
        }catch (IOException ex) {
            resp.setStatus(500);
            resp.getWriter().print(ex.getLocalizedMessage());
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Ticket ticket = getInputFromReq(req, resp);

        ticketDao.update(ticket);
        resp.setStatus(200);
        resp.getWriter().print("Successfully updated!");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    private void getTicket(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String queryStr = req.getQueryString();
        int ticket_id = Integer.parseInt(queryStr.split("=")[1]);
        Ticket ticket = ticketDao.get(ticket_id);
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
}

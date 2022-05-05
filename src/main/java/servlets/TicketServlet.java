package servlets;

import com.fasterxml.jackson.core.JsonParser;
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

@WebServlet(name="tickets", value = "/")
public class TicketServlet extends HttpServlet {
    private Dao<Ticket> ticketDao = DaoFactory.getTicketDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Ticket tickets = mapper.readValue((JsonParser) ticketDao.getAll(), Ticket.class);
            // TODO: make this get method return a list for jsp file
            req.setAttribute("tickets", tickets); // giving jsp file access to the tickets list
            req.getRequestDispatcher("index.jspx").forward(req,resp);
//            System.out.println(tickets);
//            resp.setStatus(200);
//            resp.getWriter().print(tickets);
        }catch (IOException ex) {
            resp.setStatus(500);
            resp.getWriter().print("Something went wrong get the resource!");
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
        Ticket ticket = getTicketFromReq(req, resp);

        ticketDao.update(ticket);
        resp.setStatus(200);
        resp.getWriter().print("Successfully updated!");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    private Ticket getTicketFromReq(HttpServletRequest req, HttpServletResponse res) {
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

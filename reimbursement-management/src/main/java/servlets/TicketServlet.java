package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import daos.Dao;
import daos.DaoFactory;
import entities.Ticket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TicketServlet extends HttpServlet {
    private Dao<Ticket> ticketDao = DaoFactory.getTicketDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String ticket_id = req.getQueryString();
            System.out.println(ticket_id.split("=")[1]);
            Ticket ticket = ticketDao.get(Integer.parseInt(ticket_id.split("=")[1]));

            System.out.println(ticket);
            String jsonTicket = mapper.writeValueAsString(ticket);
            resp.setStatus(200);
            resp.getWriter().print(jsonTicket);
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
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}

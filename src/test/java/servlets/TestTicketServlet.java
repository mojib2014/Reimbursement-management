package servlets;

import daos.Dao;
import daos.DaoFactory;
import entities.Ticket;
import entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.Assert.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

public class TestTicketServlet {
    private static Dao<Ticket> ticketDao;
    private static Dao<User> userDao;

    @BeforeEach
    public void setup() {
        ticketDao = DaoFactory.getTicketDao();
        userDao = DaoFactory.getUserDao();
        userDao.initTables();
        userDao.fillTables();
        ticketDao.initTables();
        ticketDao.fillTables();
    }

    @Test
    public void shouldGetTicketById() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(res.getWriter()).thenReturn(printWriter);

        String ticket1 = ticketDao.getById(1).toString();
        String ticket2 = ticketDao.getById(2).toString();
        String ticket3 = ticketDao.getById(3).toString();
        String ticket4 = ticketDao.getById(4).toString();
        new TicketServlet().doGet(req, res);

        printWriter.flush();
        System.out.println("String writer: " + stringWriter.toString());
        assertTrue(stringWriter.toString().contains(ticket1));
        assertTrue(stringWriter.toString().contains(ticket2));
        assertTrue(stringWriter.toString().contains(ticket3));
        assertTrue(stringWriter.toString().contains(ticket4));
    }
}

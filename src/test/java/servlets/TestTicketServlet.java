package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import daos.DaoFactory;
import daos.TicketDao;
import daos.UserDao;
import entities.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.Assert.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;

import static org.mockito.Mockito.*;

public class TestTicketServlet {
    private static TicketDao ticketDao;
    private static UserDao userDao;

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

        ObjectMapper mapper = new ObjectMapper();

        String ticket1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ticketDao.getById(1));
        String ticket2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ticketDao.getById(2));
        String ticket3 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ticketDao.getById(3));
        String ticket4 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ticketDao.getById(4));

        new TicketServlet().doGet(req, res);

        printWriter.flush();

        assertTrue(stringWriter.toString().contains(ticket1));
        assertTrue(stringWriter.toString().contains(ticket2));
        assertTrue(stringWriter.toString().contains(ticket3));
        assertTrue(stringWriter.toString().contains(ticket4));
    }

    @Test
    public void shouldGetATicket() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        when(req.getParameter("id")).thenReturn("1");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(printWriter);

        new TicketServlet().doGet(req, res);

        // verify the parameter:
        verify(req, atLeast(1)).getParameter("id");

        printWriter.flush();

        ObjectMapper mapper = new ObjectMapper();
        String ticket1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ticketDao.getById(1));

        assertTrue(stringWriter.toString().contains(ticket1));
    }

    @Test
    public void shouldPostATicket() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

//        Ticket ticket = new Ticket(10.5, "new description", "new category", 2);
        FileReader fr = new FileReader("src/test/java/servlets/ticket.txt");
        BufferedReader bf = new BufferedReader(fr);
        when(req.getReader()).thenReturn(bf);

        // set up the print writer:
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(writer);

        new TicketServlet().doPost(req, res);

        writer.flush();
        Ticket ticket1 = ticketDao.getById(5);
        assertEquals(5, ticket1.getTicket_id());
        assertEquals(10.5, ticket1.getAmount(), 4);
    }

    @Test
    public void shouldUpdateATicket() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        when(req.getParameter("id")).thenReturn("1");

        FileReader fr = new FileReader("src/test/java/servlets/ticket_id.txt");
        BufferedReader bf = new BufferedReader(fr);
        when(req.getReader()).thenReturn(bf);

        // set up the print writer:
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(res.getWriter()).thenReturn(writer);

        new TicketServlet().doPut(req, res);

        writer.flush();
        Ticket ticket1 = ticketDao.getById(5);
        assertEquals(5, ticket1.getTicket_id());
        assertEquals(20.10, ticket1.getAmount(), 4);
    }
}

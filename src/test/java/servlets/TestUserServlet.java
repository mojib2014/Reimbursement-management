package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import daos.DaoFactory;
import daos.UserDao;
import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class TestUserServlet {
    private static UserDao userDao;

    @BeforeEach
    public void setup() {
        userDao = DaoFactory.getUserDao();
        userDao.initTables();
        userDao.fillTables();
    }

    @Test
    public void shouldGetUsers() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(res.getWriter()).thenReturn(printWriter);

        ObjectMapper mapper = new ObjectMapper();

        String user1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDao.getById(1));
        String user2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDao.getById(2));
        String user3 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDao.getById(3));
        String user4 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDao.getById(4));
        Cookie cookie = new Cookie("user_id", "1");
        res.addCookie(cookie);
        new UserServlet().doPost(req, res);
        new UserServlet().doGet(req, res);

        printWriter.flush();

        System.out.println(user1);
        System.out.println(stringWriter.toString());
        assertTrue(stringWriter.toString().contains(user1));
        assertTrue(stringWriter.toString().contains(user2));
        assertTrue(stringWriter.toString().contains(user3));
        assertTrue(stringWriter.toString().contains(user4));
    }

    @Test
    public void shouldGetAUser() throws IOException, ServletException {
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
        String ticket1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDao.getById(1));

        assertTrue(stringWriter.toString().contains(ticket1));
    }

    @Test
    public void shouldRegisterAUser() throws IOException, ServletException {
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
        User user1 = userDao.getById(5);
        assertEquals(5, user1.getUser_id());
        assertEquals("", user1.getName());
    }

    @Test
    public void shouldLoginAUser() throws IOException, ServletException {
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
        User user = userDao.getById(5);
        assertEquals(5, user.getUser_id());
        assertEquals("", user.getName(), 4);
    }
}

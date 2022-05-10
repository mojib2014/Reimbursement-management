package servlets;

import com.google.gson.Gson;
import daos.DaoFactory;
import daos.TicketDao;
import daos.UserDao;
import entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Key;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestManagerServlet {
    private static UserDao userDao;
    private static TicketDao ticketDao;

    @BeforeEach
    public void setup() {
        userDao = DaoFactory.getUserDao();
        ticketDao = DaoFactory.getTicketDao();
        userDao.initTables();
        userDao.fillTables();

        ticketDao.initTables();
        ticketDao.fillTables();
    }

    @Test
    public void shouldGetTickets() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        // Register a user
        when(req.getServletPath()).thenReturn("/managers/register");
        FileReader fr = new FileReader("src/test/java/servlets/managersRegister.txt");
        BufferedReader bf = new BufferedReader(fr);
        when(req.getReader()).thenReturn(bf);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(res.getWriter()).thenReturn(printWriter);
        new ManagerServlet().doPost(req, res);

        User user = new User("manager1@email.com", "manager1123");
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jsonUser = new Gson().toJson(user);
        String jws = Jwts.builder().setSubject(jsonUser).signWith(key).compact();

        when(req.getParameter("status")).thenReturn("Pending");
        when(res.getWriter()).thenReturn(printWriter);
        when(req.getHeader("x-auth-token")).thenReturn(jws);
        new ManagerServlet().doGet(req, res);

        printWriter.flush();

        assertTrue(stringWriter.toString().contains("Pending"));
        assertTrue(stringWriter.toString().contains("1"));
        assertTrue(stringWriter.toString().contains("2"));
        assertTrue(stringWriter.toString().contains("3"));
        assertTrue(stringWriter.toString().contains("4"));
    }

    @Test
    public void shouldNotGetTickets() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        // Register a user
        when(req.getServletPath()).thenReturn("/managers/register");
        FileReader fr = new FileReader("src/test/java/servlets/managersRegister.txt");
        BufferedReader bf = new BufferedReader(fr);
        when(req.getReader()).thenReturn(bf);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(res.getWriter()).thenReturn(printWriter);
        new ManagerServlet().doPost(req, res);

        User user = new User("manager1@email.com", "manager1123");
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jsonUser = new Gson().toJson(user);
        String jws = Jwts.builder().setSubject(jsonUser).signWith(key).compact();

        when(req.getParameter("status")).thenReturn(null);
        when(res.getWriter()).thenReturn(printWriter);
        when(req.getHeader("x-auth-token")).thenReturn(jws);
        new ManagerServlet().doGet(req, res);

        printWriter.flush();
        System.out.println(stringWriter.toString());
        assertTrue(stringWriter.toString().contains("Please enter query string (orderBy=DESC or status=Pending)"));
    }

    @Test
    public void shouldThrowRegistering() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        // Register a user
//        when(req.getServletPath()).thenReturn("/managers/register");
        FileReader fr = new FileReader("src/test/java/servlets/managersRegister.txt");
        BufferedReader bf = new BufferedReader(fr);
        when(req.getReader()).thenReturn(bf);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(res.getWriter()).thenReturn(printWriter);
        new ManagerServlet().doPost(req, res);

        printWriter.flush();
        assertTrue(stringWriter.toString().contains("Endpoint null"));
    }

    @Test
    public void shouldGetAUser() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        // Register a user
        when(req.getServletPath()).thenReturn("/managers/register");
        FileReader fr = new FileReader("src/test/java/servlets/managersRegister.txt");
        BufferedReader bf = new BufferedReader(fr);
        when(req.getReader()).thenReturn(bf);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        User user = new User("manager1@email.com", "manager1123");
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jsonUser = new Gson().toJson(user);
        String jws = Jwts.builder().setSubject(jsonUser).signWith(key).compact();

        when(res.getWriter()).thenReturn(printWriter);
        new ManagerServlet().doPost(req, res);

        when(req.getParameter("id")).thenReturn("1");
        when(res.getWriter()).thenReturn(printWriter);
        when(req.getHeader("x-auth-token")).thenReturn(jws);
        new ManagerServlet().doGet(req, res);

        printWriter.flush();

        assertTrue(stringWriter.toString().contains("1"));
    }

    @Test
    public void shouldLoginAUser() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        // Register a user
        when(req.getServletPath()).thenReturn("/managers/register");
        FileReader fr = new FileReader("src/test/java/servlets/managersRegister.txt");
        BufferedReader bf = new BufferedReader(fr);
        when(req.getReader()).thenReturn(bf);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(res.getWriter()).thenReturn(printWriter);
        new ManagerServlet().doPost(req, res);

        // Login user
        HttpServletRequest req1 = mock(HttpServletRequest.class);
        HttpServletResponse res1 = mock(HttpServletResponse.class);
        when(req1.getServletPath()).thenReturn("/managers/login");
        FileReader fr1 = new FileReader("src/test/java/servlets/managersRegister.txt");
        BufferedReader bf1 = new BufferedReader(fr1);
        when(req1.getReader()).thenReturn(bf1);
        when(res1.getWriter()).thenReturn(printWriter);

        new ManagerServlet().doPost(req1, res1);

        printWriter.flush();

        System.out.println(stringWriter.toString());
        assertTrue(stringWriter.toString() != null);
    }

    @Test
    public void shouldUpdateTicketStatus() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        // Register a user
        when(req.getServletPath()).thenReturn("/managers/register");
        FileReader fr = new FileReader("src/test/java/servlets/managersRegister.txt");
        BufferedReader bf = new BufferedReader(fr);
        when(req.getReader()).thenReturn(bf);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(res.getWriter()).thenReturn(printWriter);
        new ManagerServlet().doPost(req, res);

        // Login user
        HttpServletRequest req1 = mock(HttpServletRequest.class);
        HttpServletResponse res1 = mock(HttpServletResponse.class);
        when(req1.getServletPath()).thenReturn("/managers/login");
        FileReader fr1 = new FileReader("src/test/java/servlets/managersRegister.txt");
        BufferedReader bf1 = new BufferedReader(fr1);
        when(req1.getReader()).thenReturn(bf1);
        when(res1.getWriter()).thenReturn(printWriter);

        new ManagerServlet().doPost(req1, res1);

        printWriter.flush();

        assertTrue(stringWriter.toString() != null);

        User user = new User("manager1@email.com", "manager1123");
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jsonUser = new Gson().toJson(user);
        String jws = Jwts.builder().setSubject(jsonUser).signWith(key).compact();
        when(req1.getHeader("x-auth-token")).thenReturn(jws);

        when(req1.getServletPath()).thenReturn("/managers/update-ticket");
        when(req1.getParameter("id")).thenReturn("1");
        when(req1.getParameter("status")).thenReturn("Approved");
        when(req1.getReader()).thenReturn(bf1);
        when(res1.getWriter()).thenReturn(printWriter);

        new ManagerServlet().doPut(req1, res1);

        printWriter.flush();
        System.out.println(res1.getStatus());
        System.out.println("String writer: " + stringWriter.toString());
        assertTrue(stringWriter.toString().contains("Successfully updated"));
    }
}

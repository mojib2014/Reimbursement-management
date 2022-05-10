package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import daos.DaoFactory;
import daos.UserDao;
import entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpCookie;
import java.security.Key;

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

        // Register a user
        when(req.getServletPath()).thenReturn("/users/register");
        FileReader fr = new FileReader("src/test/java/servlets/registerUser.txt");
        BufferedReader bf = new BufferedReader(fr);
        when(req.getReader()).thenReturn(bf);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        User user = new User("user1@email.com", "user1123");
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jsonUser = new Gson().toJson(user);
        String jws = Jwts.builder().setSubject(jsonUser).signWith(key).compact();

        when(res.getWriter()).thenReturn(printWriter);
        new UserServlet().doPost(req, res);

        when(res.getWriter()).thenReturn(printWriter);
        when(req.getHeader("x-auth-token")).thenReturn(jws);
        new UserServlet().doGet(req, res);

        printWriter.flush();

        assertTrue(stringWriter.toString().contains("user1@email.com"));
    }

    @Test
    public void shouldGetAUser() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        // Register a user
        when(req.getServletPath()).thenReturn("/users/register");
        FileReader fr = new FileReader("src/test/java/servlets/registerUser.txt");
        BufferedReader bf = new BufferedReader(fr);
        when(req.getReader()).thenReturn(bf);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        User user = new User("user1@email.com", "user1123");
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jsonUser = new Gson().toJson(user);
        String jws = Jwts.builder().setSubject(jsonUser).signWith(key).compact();

        when(res.getWriter()).thenReturn(printWriter);
        new UserServlet().doPost(req, res);

        when(req.getParameter("id")).thenReturn("1");
        when(res.getWriter()).thenReturn(printWriter);
        when(req.getHeader("x-auth-token")).thenReturn(jws);
        new UserServlet().doGet(req, res);

        printWriter.flush();

        assertTrue(stringWriter.toString().contains("user1@email.com"));
    }

    @Test
    public void shouldLoginAUser() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        // Register a user
        when(req.getServletPath()).thenReturn("/users/register");
        FileReader fr = new FileReader("src/test/java/servlets/registerUser.txt");
        BufferedReader bf = new BufferedReader(fr);
        when(req.getReader()).thenReturn(bf);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(res.getWriter()).thenReturn(printWriter);
        new UserServlet().doPost(req, res);

        // Login user
        HttpServletRequest req1 = mock(HttpServletRequest.class);
        HttpServletResponse res1 = mock(HttpServletResponse.class);
        when(req1.getServletPath()).thenReturn("/users/login");
        FileReader fr1 = new FileReader("src/test/java/servlets/registerUser.txt");
        BufferedReader bf1 = new BufferedReader(fr1);
        when(req1.getReader()).thenReturn(bf1);
        when(res1.getWriter()).thenReturn(printWriter);

        new UserServlet().doPost(req1, res1);

        printWriter.flush();

        assertTrue(stringWriter.toString() != null);
    }

    @Test
    public void shouldUpdateAUser() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        // Register a user
        when(req.getServletPath()).thenReturn("/users/register");
        FileReader fr = new FileReader("src/test/java/servlets/registerUser.txt");
        BufferedReader bf = new BufferedReader(fr);
        when(req.getReader()).thenReturn(bf);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(res.getWriter()).thenReturn(printWriter);
        new UserServlet().doPost(req, res);

        // Login user
        HttpServletRequest req1 = mock(HttpServletRequest.class);
        HttpServletResponse res1 = mock(HttpServletResponse.class);
        when(req1.getServletPath()).thenReturn("/users/login");
        FileReader fr1 = new FileReader("src/test/java/servlets/registerUser.txt");
        BufferedReader bf1 = new BufferedReader(fr1);
        when(req1.getReader()).thenReturn(bf1);
        when(res1.getWriter()).thenReturn(printWriter);

        new UserServlet().doPost(req1, res1);

        printWriter.flush();

        User user = new User("user1@email.com", "user1123");
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jsonUser = new Gson().toJson(user);
        String jws = Jwts.builder().setSubject(jsonUser).signWith(key).compact();

        FileReader fr2 = new FileReader("src/test/java/servlets/registerUser.txt");
        BufferedReader bf2 = new BufferedReader(fr2);
        when(req1.getReader()).thenReturn(bf2);
        when(req1.getReader()).thenReturn(bf2);
        when(req1.getHeader("x-auth-token")).thenReturn(jws);
        when(res1.getWriter()).thenReturn(printWriter);

        new UserServlet().doPut(req1, res1);

        printWriter.flush();

        System.out.println(stringWriter.toString());
        assertTrue(stringWriter.toString() != null);
    }
}

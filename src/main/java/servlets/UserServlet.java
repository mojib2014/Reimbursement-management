package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import daos.DaoFactory;
import daos.UserDao;
import datastructure.UDArray;
import entities.Ticket;
import entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserServlet extends HttpServlet {

    private UserDao userDao = DaoFactory.getUserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        boolean authRes = checkAuthenticated(req, res);
        if(authRes) {
            int id = 0;
            try {
                id = Integer.parseInt(req.getParameter("id"));
            } catch (NumberFormatException e) {
                String users = getAllUsers(req, res);
                res.setStatus(200);
                out.print(users);
                return;
            }
            try {
                ObjectMapper mapper = new ObjectMapper();
                User user = userDao.getById(id);
                String jsonUser = mapper.writeValueAsString(user);
                res.setStatus(200);
                out.print(jsonUser);
            } catch (IOException ex) {
                res.setStatus(500);
                out.print("Something went wrong processing your request!");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
            try {
                String endpoint = req.getServletPath();

                switch (endpoint) {
                    case "/users/register":
                        registerUser(req, res);
                        break;
                    case "/users/login":
                        loginUser(req, res);
                        break;
                    default:
                        res.getWriter().print("We don't support that endpoint at the moment!");
                }

            } catch (IOException ex) {
                res.setStatus(500);
                res.getWriter().print(ex.getLocalizedMessage());
                System.out.println(ex.getLocalizedMessage());
            }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        boolean authRes = checkAuthenticated(req, res);
        if(authRes) {
            PrintWriter out = null;
            try {
                out = res.getWriter();
                User user = getInputFromReq(req, res);
                User updatedUser = userDao.update(user);
                res.setStatus(204);
                out.print(updatedUser);
            } catch (IOException ex) {
                res.setStatus(500);
                out.print(ex.getLocalizedMessage());
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO: implement this if time (not in the requirement)
    }

    /**
     * Helper methods
     */

    /**
     * Register a user and hashes the password user Bcrypt
     * @param req
     * @param res
     */
    private void registerUser(HttpServletRequest req, HttpServletResponse res) {
        PrintWriter out = null;
        try {
            out = res.getWriter();
            User user = getInputFromReq(req, res);
            user.setUser_type("Employee");
            User dbUser = userDao.register(user);
            if(dbUser != null) {
                String jsonUser = new Gson().toJson(user);
                res.setStatus(200);
                Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
                String jws = Jwts.builder().setSubject(jsonUser).signWith(key).compact();
                res.setHeader("x-auth-token", jws);
                out.print(jws);
            }else {
                res.setStatus(500);
                out.print("Something wen wrong, please try again!");
            }
        }catch (IOException ex) {
            res.setStatus(500);
            out.print(ex.getLocalizedMessage());
        }
    }

    /**
     * Login a user and match the password with the hashed version
     * @param req
     * @param res
     */
    private void loginUser(HttpServletRequest req, HttpServletResponse res) {
        PrintWriter out = null;
        try {
            out = res.getWriter();
            User userInput = getInputFromReq(req, res);
            User user = userDao.login(userInput.getEmail(), userInput.getPassword());
            if(user != null) {
                String jsonUser = new Gson().toJson(user);
                res.setStatus(200);
                Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
                String jws = Jwts.builder().setSubject(jsonUser).signWith(key).compact();
                res.setHeader("x-auth-token", jws);
                out.print(jws);
            }
        }catch (IOException ex) {
            res.setStatus(500);
            out.print(ex.getLocalizedMessage());
        }
    }

//    private void getUser(HttpServletRequest req, HttpServletResponse res) throws IOException {
//        PrintWriter out = null;
//        try {
//            int user_id = getUserIdFromCookies(req);
//            out = res.getWriter();
//            if(user_id != 0) {
//                ObjectMapper mapper = new ObjectMapper();
//                User user = userDao.getById();
//                String jsonUser = mapper.writeValueAsString(user);
//                res.setStatus(200);
//                out.print(jsonUser);
//            } else {
//                res.setStatus(403);
//                out.print("Access denied, Please login or register!");
//            }
//        }catch (IOException ex) {
//            res.setStatus(500);
//            out.print("Something went wrong processing your request!");
//        }
//    }

    private String getAllUsers(HttpServletRequest req, HttpServletResponse res) throws IOException {
        PrintWriter out = null;
        try {
            out = res.getWriter();
            out = res.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            UDArray<User> userList = userDao.getAll();
            String users = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userList.getContainer());
            return users;
        }catch (IOException ex) {
            res.setStatus(500);
            out.print(ex.getLocalizedMessage());
        }
        return null;
    }

    private User getInputFromReq(HttpServletRequest req, HttpServletResponse res) {
        PrintWriter out = null;
        try {
            out = res.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.readValue(req.getReader(), User.class);
            return user;
        }catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
            out.print(ex.getLocalizedMessage());
        }
        return null;
    }

    private boolean checkAuthenticated(HttpServletRequest req, HttpServletResponse res) {
        PrintWriter out = null;
        try {
            out = res.getWriter();
            if(req.getHeader("x-auth-token") == null) {
                res.setStatus(403);
                out.print("Access denied, Please login or register");
                return false;
            }
        }catch (IOException ex) {
            res.setStatus(500);
            out.print(ex.getLocalizedMessage());
        }
        return true;
    }
}

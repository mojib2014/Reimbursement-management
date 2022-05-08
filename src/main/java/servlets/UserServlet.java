package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import daos.DaoFactory;
import daos.UserDao;
import datastructure.UDArray;
import entities.Ticket;
import entities.User;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserServlet extends HttpServlet {

    private UserDao userDao = DaoFactory.getUserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
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
            int user_id = getUserIdFromCookies(req);
            if(user_id == 0) {
                res.setStatus(403);
                out.print("Access denied, Please login or register!");
            } else {
                ObjectMapper mapper = new ObjectMapper();
                User user = userDao.getById(id);
                String jsonUser = mapper.writeValueAsString(user);
                res.setStatus(200);
                out.print(jsonUser);
            }
        }catch (IOException ex) {
            res.setStatus(500);
            out.print("Something went wrong processing your request!");
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

        }catch (IOException ex) {
            res.setStatus(500);
            res.getWriter().print(ex.getLocalizedMessage());
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = null;
        int user_id = getUserIdFromCookies(req);
        try {
            out = res.getWriter();
            if(user_id != 0) {
                User user = getInputFromReq(req, res);
                User updatedUser = userDao.update(user);
                res.setStatus(204);
                out.print(updatedUser);
            }else {
                res.setStatus(403);
                out.print("Access denied, Please login or register!");
            }
        }catch (IOException ex) {
            res.setStatus(500);
            out.print(ex.getLocalizedMessage());
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
            if(dbUser == null) {
                res.setStatus(500);
                out.print("Something wen wrong, please try again!");
            }else {
                res.setStatus(200);
                out.print("User successfully registered!");
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
            ObjectMapper mapper = new ObjectMapper();
            User userInput = mapper.readValue(req.getInputStream(), User.class);
            User user = userDao.login(userInput.getEmail(), userInput.getPassword());
            if(user != null) {
                String jsonUser = new Gson().toJson(user);
                res.setStatus(200);
                Cookie cookie = new Cookie("user_id", URLEncoder.encode( String.valueOf(user.getUser_id()), "UTF-8" ));
                cookie.setMaxAge(10000);
                res.addCookie(cookie);
                res.setHeader("isLoggedIn", "true");
                out.print("User successfully logged in!");
                out.print(jsonUser);
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
            int user_id = getUserIdFromCookies(req);
            if(user_id != 0) {
                out = res.getWriter();
                ObjectMapper mapper = new ObjectMapper();
                UDArray<User> userList = userDao.getAll();
                String users = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userList.getContainer());
                return users;
            }else {
                res.setStatus(403);
                out.print("Access denied, Please login or register!");
            }
        }catch (IOException ex) {
            res.setStatus(500);
            out.print(ex.getLocalizedMessage());
        }
        return null;
    }

    private User getInputFromReq(HttpServletRequest req, HttpServletResponse res) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.readValue(req.getInputStream(), User.class);
            return user;
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

    private int getIdFromReq(HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = 0;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        }catch (NumberFormatException ex) {
            res.setStatus(500);
            res.getWriter().print(ex.getLocalizedMessage());
        }
        return id;
    }

    private int getUserIdFromCookies(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        String cookieName = "user_id";
        String user_id = "";
        if(cookies == null) {
            return 0;
        }
        for ( int i=0; i<cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookieName.equals(cookie.getName()))
                user_id += cookie.getValue();
        }
        if(user_id == "") {
            return 0;
        }
        return Integer.parseInt(user_id);
    }
}

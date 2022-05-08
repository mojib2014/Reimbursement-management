package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import daos.ManagerDaoImpl;
import datastructure.UDArray;
import entities.Ticket;
import entities.User;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ManagerServlet extends HttpServlet {
    private ManagerDaoImpl managerDao = new ManagerDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        try {
            out = res.getWriter();
            int id = 0;
            try {
                id = Integer.parseInt(req.getParameter("id"));
            } catch (NumberFormatException e) {
                String status = req.getParameter("status");
                String orderBy = req.getParameter("orderBy");
                System.out.println(status);
                if(orderBy == null && status == null) {
                    res.setStatus(400);
                    out.print("Please enter query string (orderBy=DESC or status=Pending)");
                    return;
                }
                List<String> options = new ArrayList<>();
                if(status != null) options.add(status);
                if(orderBy != null) options.add(orderBy);
                UDArray<Ticket> tickets = managerDao.getAllTickets(options);
                String jsonTickets = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tickets.getContainer());
                res.setStatus(200);
                out.print(jsonTickets);
                return;
            }
            Ticket ticket = managerDao.getATicket(id);
            String jsonTicket = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ticket);
            res.setStatus(200);
            out.print(jsonTicket);
        }catch (IOException ex) {
            res.setStatus(500);
            out.print(ex.getLocalizedMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String endpoint = req.getServletPath();

            switch (endpoint) {
                case "/managers/register":
                    registerUser(req, res);
                    break;
                case "/managers/login":
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
        res.setContentType("application/json");
        PrintWriter out = null;
        int user_id = getUserIdFromCookies(req, res);
        int ticket_id = Integer.parseInt(req.getParameter("id"));
        String status = req.getParameter("status");
        try {
            out = res.getWriter();
            if(user_id != 0) {
                boolean response = managerDao.updateTicketStatus(ticket_id, status);
                if(response) {
                    res.setStatus(202);
                    return;
                }
            }else {
                res.setStatus(403);
                out.print("Access denied, Please login or register!");
                return;
            }
        }catch (IOException ex) {
            res.setStatus(500);
            out.print(ex.getLocalizedMessage());
        }
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
            user.setUser_type("Manager");
            managerDao.register(user);
            res.setStatus(200);
            Cookie cookie = new Cookie("user", user.getName());
            res.addCookie(cookie);
            out.print("User successfully registered!");
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
            User user = managerDao.login(userInput.getEmail(), userInput.getPassword());
            if(user != null) {
                String jsonUser = new Gson().toJson(user);
                String name = user.getName();
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

    private void updateTicket(HttpServletRequest req, HttpServletResponse res) {

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

    private int getUserIdFromCookies(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Cookie[] cookies = req.getCookies();
        String cookieName = "user_id";
        String user_id = "";
        if(cookies == null) {
            return 0;
        }

        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName()))
                user_id += cookie.getValue();
        }
        return Integer.parseInt(user_id);
    }
}

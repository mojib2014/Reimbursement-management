package servlets;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import daos.Dao;
import daos.DaoFactory;
import datastructure.UDArray;
import entities.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserServlet extends HttpServlet {

    private Dao<User> userDao = DaoFactory.getUserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String endpoint = req.getServletPath();

            switch (endpoint) {
                case "/users":
                    getAllUsers(req, res);
                    break;
                case "/users/user":
                    getUser(req, res);
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User jsonUser = getInputFromReq(req, resp);
            String res = userDao.insert(jsonUser);
            if(res.trim().equals("Success")) {
                resp.setStatus(200);
                resp.getWriter().print("User successfully created!");
            }else {
                resp.setStatus(500);
                resp.getWriter().print(res);
            }
        }catch (IOException ex) {
            resp.setStatus(500);
            resp.getWriter().print(ex.getLocalizedMessage());
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    // Helper methods
    private void getUser(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        int user_id = getIdFromReq(req);
        System.out.println(user_id);
        User user = userDao.getById(user_id);
        String users = mapper.writeValueAsString(user);
        res.setStatus(200);
        res.getWriter().print(users);
    }

    private void getAllUsers(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        UDArray<User> userList = userDao.getAll();
        String users = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userList);
        res.setStatus(200);
        res.getWriter().print(users);
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

    private int getIdFromReq(HttpServletRequest req) {
        String queryStr = req.getQueryString();
        int user_id = Integer.parseInt(queryStr.split("=")[1]);
        return user_id;
    }
}

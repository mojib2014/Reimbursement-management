package servlets;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import daos.Dao;
import daos.DaoFactory;
import daos.UserdaoImpl;
import entities.Ticket;
import entities.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends HttpServlet {

    private Dao<User> userdao = DaoFactory.getUserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        try {
            ObjectMapper mapper = new ObjectMapper();
            User jsonUser = mapper.readValue((JsonParser) userdao.getAll(), User.class);
        } catch (IOException ex) {
            resp.setStatus(500);
            resp.getWriter().print(ex.getLocalizedMessage());
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        try {
            ObjectMapper mapper = new ObjectMapper();
            User jsonUser = mapper.readValue(req.getInputStream(), User.class);
            resp.setStatus(400);
            boolean res = userdao.insert(jsonUser);
            if(res) {
                resp.setStatus(202);
                resp.getWriter().print("User successfully created!");
            }
        }catch (IOException ex) {
            resp.setStatus(500);
            resp.getWriter().print(ex.getLocalizedMessage());
            System.out.println(ex.getLocalizedMessage());
        }
    }
}

package servlets.users;

import daos.DaoFactory;
import daos.UserDao;
import entities.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class UserCheckLoginServlet extends HttpServlet {
    private UserDao userDao = DaoFactory.getUserDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String referer = (String) req.getAttribute("origin");
        req.setAttribute("origin", referer);
        req.getRequestDispatcher("LoginServlet").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String key = req.getParameter("name");
        String pass = req.getParameter("password");

        User user = userDao.login(key, pass);
        if (user == null) {
            req.setAttribute("error", "invalid login");
            req.getRequestDispatcher("LoginServlet").forward(req, res);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("user", user);

        res.sendRedirect(req.getParameter("origin"));
    }
}

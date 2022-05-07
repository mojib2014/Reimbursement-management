package com.revature.servlets;




import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        // dispatch this request to a jsp file (this will be in the webapp folder:
        RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
        // set the attribute of this request to be an arbitrary value ("bananas")
        req.setAttribute("message", "banana");
        // include the request and response when we dispatch
        dispatcher.include(req,resp);
        // write an additional message:
        out.write("Hello from servlets!");

    }

}

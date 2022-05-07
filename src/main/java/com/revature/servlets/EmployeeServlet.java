package com.revature.servlets;

import com.revature.dao.DaoFactory;
import com.revature.dao.EmployeeDao;
import com.revature.entity.Employee;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EmployeeServlet extends HttpServlet {
    EmployeeDao eDao = DaoFactory.getEmployeDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setStatus(200);
        String referrer = req.getHeader("Referer");
        if (referrer.contains("new-user-registration")) {
            processRegistrationRequest(req, resp);
        } else {
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("Cannot process the new employee registration");
        }

    }

    protected void processRegistrationRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setStatus(200);
        String employee_name = request.getParameter("employee_name");
        String employee_email = request.getParameter("employee_email");
        String employee_password =request.getParameter("employee_password");
        // int employee_id, String employee_name, String employee_email, String employee_password
        Employee employee = new Employee(employee_name, employee_email, employee_password);
        System.out.println(employee.toString());
        boolean isSuccessful = eDao.insert(employee);
        Employee insertedEmployee = eDao.getEmployee(employee_email, employee_password);
        if (isSuccessful) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println(String.format("<h3>You can now post expense tickets. Your id is %s</h3>",
                    insertedEmployee.getEmployee_id()));
            out.println("<h3><a href=ticket-form.jsp>Click here Post a new Expense Ticket</a><br><br></h3>");
        } else {
            request.getRequestDispatcher("FailServlet").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setStatus(200);
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        String referrer = req.getHeader("Referer");
        if (referrer.contains("employee-login")) {
            processEmployeeLoginRequest(req, resp);
        } else if (referrer.contains("ticket-form")) {

        }

//        resp.setContentType("text/html");
//        List<Employee> employeeList = new ArrayList<>();
//        employeeList = eDao.getAllEmployees();
//        for (Employee e : employeeList) {
//            System.out.println(e.toString());
//        }
//        out.print(employeeList.size());
//        for (Employee e : employeeList) {
//            String print = String.format("<h2> %s </h2>", e.toString());;
//            out.println(print);
//        }
    }

    protected void processEmployeeLoginRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(200);
        String employee_email = req.getParameter("email");
        String employee_password = req.getParameter("password");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        boolean isLoginSuccess = eDao.loginSuccess(employee_email, employee_password);
        if (isLoginSuccess) {
            out.println("<h3><a href=ticket-form.jsp>Click here Post a new Expense Ticket</a><br><br></h3>");
            out.println("<h3><a href=ticket-view-form.jsp>Click here to view past tickets</a><br><br></h3>");

        } else {
            req.getRequestDispatcher("FailServlet").forward(req, resp);
        }
    }


}

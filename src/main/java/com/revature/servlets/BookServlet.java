//package com.revature.servlets;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.revature.service.BookService2;
//
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.awt.print.Book;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.List;
//
///*
//This is the entry point to our project from Postman (or any client). This servlet class is mapped the end point "/books" in the pom.xml.
//In Postman, we would enter an endpoint that looks something like "localhost:8080/book/book" and change which request
//we trigger by selecting POST, GET, PUT, or DELETE from the dropdown on Postman.
//
//The reason that we have /book/book in our url is that the url to our project is "localhost:8080/book". We add the second "/book"
//because this is what we mapped this http servlet class to.
// */
//public class BookServlet extends HttpServlet {
//    /*
//    For this method, depending on whether or not we pass in a parameter, we return all books or just a specific book:
//    Example: "localhost:8080/book/book" would return all books
//    "http://localhost:8080/book/book?id=10" would return the book corresponding to that id
//     */
//    @Override
//    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
//        res.setContentType("application/json");
//        // set up writer:
//        PrintWriter out = res.getWriter();
//        // this will store the id that we pass in through postman:
//        int idToGet;
//        try {
//            // try to parse the id from parameters. If it fails, that means we didn't pass one in:
//            idToGet = Integer.parseInt(req.getParameter("id"));
//        } catch (NumberFormatException e) {
//            // if we didn't pass in an id, we want all books:
//            List<Book> books = BookService2.getAllBooks();
//            out.print(books);
//            return;
//        }
//
//        // if the catch block didn't trigger, that means we did pass in an id so we can use that to get a specific book:
//        Book book = BookService2.getBookById(idToGet);
//        out.print(book);
//
//    }
//
//    @Override
//    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
//        // here, we are mapping the json request (What we put into Postman) to a Java object (entity.Book).
//        ObjectMapper mapper = new ObjectMapper();
//        Book book = mapper.readValue(req.getReader(), Book.class);
//        // call the service and return the book:
//        Book result = BookService2.add(book);
//        PrintWriter out = res.getWriter();
//        out.write(result.toString());
//    }
//
//    @Override
//    public void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
//        // here, we are mapping the json request (What we put into Postman) to a Java object (entity.Book).
//        ObjectMapper mapper = new ObjectMapper();
//        Book book = mapper.readValue(req.getReader(), Book.class);
//        // call the service and return the book:
//        Book result = BookService2.update(book);
//        PrintWriter out = res.getWriter();
//        out.write(result.toString());
//    }
//
//    @Override
//    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
//        PrintWriter out = res.getWriter();
//
//        // get the id from the request parameter:
//        int idToDelete = Integer.parseInt(req.getParameter("id"));
//        // call the service:
//       boolean success = BookService2.delete(idToDelete);
//       // check if deletion was successful and send the appropriate response:
//       if(success) {
//           out.write("Deletion success!");
//       }
//       else {
//           out.write("Deletion failed!");
//       }
//
//
//    }
//}
